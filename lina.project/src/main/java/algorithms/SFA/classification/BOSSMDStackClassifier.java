// Copyright (c) 2016 - Patrick Schäfer (patrick.schaefer@zib.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package algorithms.SFA.classification;

import com.carrotsearch.hppc.LongFloatHashMap;
import com.carrotsearch.hppc.ObjectObjectHashMap;
import com.carrotsearch.hppc.cursors.IntIntCursor;
import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import algorithms.SFA.classification.ClassifierMD.BOSSMDType;
import algorithms.SFA.transformation.BOSS.BagOfPattern;
import algorithms.SFA.transformation.BOSSMDStack;
import datasets.timeseries.MultiVariateTimeSeries;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Bag-of-SFA-Symbols in Vector Space classifier as published in Schäfer,
 * P.: Scalable time series classification. DMKD (Preprint)
 *
 * @author bzcschae
 */
public class BOSSMDStackClassifier extends ClassifierMD {

    public BOSSMDType typeClassifier;
    public static double factor = 0.92;

    public static boolean normMagnitudes = false;

    public BOSSMDStackClassifier(MultiVariateTimeSeries[] train, MultiVariateTimeSeries[] test) {
        super(train, test);
        typeClassifier = BOSSMDType.STACK;
    }

    public static class BossMDScore<E> extends Score {

        public BossMDScore(boolean normed, int windowLength) {
            super("BOSS MD", 0, 0, normed, windowLength);
        }

        public ObjectObjectHashMap<Double, E> idf;
        public BOSSMDStack model;
        public int features;

        @Override
        public void clear() {
            super.clear();
            this.idf = null;
            this.model = null;
        }
    }

    @Override
    public Score eval() throws IOException {
        //threads variavel herdado de Classifier
        ExecutorService exec = Executors.newFixedThreadPool(threads);
        try {
            // BOSS Distance
            BossMDScore<LongFloatHashMap> totalBestScore = null;
            int bestCorrectTesting = 0;
            int bestCorrectTraining = 0;

            // generate test train/split for cross-validation
            generateIndices();

            for (boolean normMean : NORMALIZATION) {
                long startTime = System.currentTimeMillis();

                this.correctTraining = new AtomicInteger(0);

                List<BossMDScore<LongFloatHashMap>> scores = fitEnsemble(exec, normMean);

                // training score
                BossMDScore<LongFloatHashMap> bestScore = scores.get(0);
                if (DEBUG) {
                    System.out.println("BOSS " + typeClassifierToString() + " Training:\t S_" + maxSymbol + " F_" + bestScore.features + "\tw_" + bestScore.windowLength + "\tnormed: \t" + normMean);
                    outputResult(this.correctTraining.get(), startTime, this.trainSamples.length);
                }

                // determine labels based on the majority of predictions
                int correctTesting = predictEnsamble(exec, scores, this.testSamples, normMean);

                if (bestCorrectTraining <= this.correctTraining.get()) {
                    bestCorrectTesting = correctTesting;
                    bestCorrectTraining = this.correctTraining.get();
                    totalBestScore = bestScore;
                }
                if (DEBUG) {
                    System.out.println("");
                }
            }

            return new Score(
                    "BOSS " + typeClassifierToString(),
                    1 - formatError(bestCorrectTesting, this.testSamples.length),
                    1 - formatError(bestCorrectTraining, this.trainSamples.length),
                    totalBestScore.normed,
                    totalBestScore.windowLength);
        } finally {
            exec.shutdown();
        }

    }

    protected String typeClassifierToString() {
        if (typeClassifier == BOSSMDType.STACK) {
            return "MDStack";
        } else if (typeClassifier == BOSSMDType.MDWord) {
            return "MDwords";
        }
        return "EMPTY";
    }

    public List<BossMDScore<LongFloatHashMap>> fitEnsemble(ExecutorService exec, final boolean normMean) throws FileNotFoundException {
        int min = this.minWindowLength;
        int max = getMax(trainSamples, this.maxWindowLength);

        // equi-distance sampling of windows
        ArrayList<Integer> windows = new ArrayList<>();
        double count = Math.sqrt(max);
        double distance = ((max - min) / count);
        if (distance == 0) {
            distance = 1;
        }
        for (int c = min; c <= max; c += distance) {
            windows.add(c);
        }
        return fit(windows.toArray(new Integer[]{}), normMean, trainSamples, exec);
    }

    public List<BossMDScore<LongFloatHashMap>> fit(
            Integer[] allWindows,
            boolean normMean,
            MultiVariateTimeSeries[] samples,
            ExecutorService exec) {

        final List<BossMDScore<LongFloatHashMap>> results = new ArrayList<>(allWindows.length);

        ParallelFor.withIndex(exec, threads, new ParallelFor.Each() {
            HashSet<Double> uniqueLabels = uniqueClassLabels(samples);
            BossMDScore<LongFloatHashMap> bestScore = new BossMDScore<>(normMean, 0);

            @Override
            public void run(int id, AtomicInteger processed) {
                for (int i = 0; i < allWindows.length; i++) {
                    if (i % threads == id) {
                        BossMDScore<LongFloatHashMap> score = new BossMDScore<>(normMean, allWindows[i]);
                        try {
                            BOSSMDStack model = new BOSSMDStack(maxWordLength, maxSymbol, score.windowLength, score.normed);

                            long[][][] words = model.createWordsMDStack(trainSamples);

                            optimize:
                            for (int f = minWordLenth; f <= Math.min(score.windowLength, maxWordLength); f += 2) {
                                BagOfPattern[] bag = model.createBagOfPatternMDStack(words, trainSamples, f);

                                // cross validation using folds
                                int correct = 0;
                                for (int s = 0; s < folds; s++) {
                                    // calculate the tf-idf for each class
                                    ObjectObjectHashMap<Double, LongFloatHashMap> idf = model.createTfIdf(bag, trainIndices[s], uniqueLabels);

                                    correct += predict(testIndices[s], bag, idf).correct.get();
                                }
                                if (correct > score.training) {
                                    score.training = correct;
                                    score.testing = correct;
                                    score.features = f;

                                    if (correct == samples.length) {
                                        break optimize;
                                    }
                                }
                            }

                            // obtain the final matrix              
                            BagOfPattern[] bag = model.createBagOfPatternMDStack(words, trainSamples, score.features);

                            // calculate the tf-idf for each class
                            score.idf = model.createTfIdf(bag, uniqueLabels);
                            score.model = model;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (this.bestScore.compareTo(score) < 0) {
                            synchronized (this.bestScore) {
                                if (this.bestScore.compareTo(score) < 0) {
                                    BOSSMDStackClassifier.this.correctTraining.set((int) score.training);
                                    this.bestScore = score;
                                }
                            }
                        }

                        // add to ensemble
                        if (score.training >= BOSSMDStackClassifier.this.correctTraining.get() * factor) {
                            synchronized (results) {
                                results.add(score);
                            }
                        }
                    }
                }
            }
        });

        // cleanup unused scores
        for (BossMDScore<LongFloatHashMap> s : results) {
            if (s.model != null
                    && s.training < this.correctTraining.get() * factor) {
                s.clear();
            }
        }

        // sort descending
        Collections.sort(results, Collections.reverseOrder());
        return results;
    }

    public Predictions predict(
            final int[] indices,
            final BagOfPattern[] bagOfPatternsTestSamples,
            final ObjectObjectHashMap<Double, LongFloatHashMap> matrixTrain) {

        Predictions p = new Predictions(new Double[bagOfPatternsTestSamples.length], 0);

        ParallelFor.withIndex(BLOCKS, new ParallelFor.Each() {
            @Override
            public void run(int id, AtomicInteger processed) {
                // iterate each sample to classify
                for (int i : indices) {
                    if (i % BLOCKS == id) {
                        double bestDistance = 0.0;

                        // for each class
                        for (ObjectObjectCursor<Double, LongFloatHashMap> classEntry : matrixTrain) {

                            Double label = classEntry.key;
                            LongFloatHashMap stat = classEntry.value;

                            // determine cosine similarity
                            double distance = 0.0;
                            for (IntIntCursor wordFreq : bagOfPatternsTestSamples[i].bag) {
                                double wordInBagFreq = wordFreq.value;
                                double value = stat.get(wordFreq.key);
                                distance += wordInBagFreq * (value + 1.0);
                            }

                            // norm by magnitudes
                            if (normMagnitudes) {
                                distance /= magnitude(stat.values());
                            }

                            // update nearest neighbor
                            if (distance > bestDistance) {
                                bestDistance = distance;
                                p.labels[i] = label;
                            }
                        }

                        // check if the prediction is correct
                        if (bagOfPatternsTestSamples[i].label.equals(p.labels[i])) {
                            p.correct.incrementAndGet();
                        }
                    }
                }
            }
        });

        return p;
    }

    public int predictEnsamble(
            ExecutorService executor,
            final List<BossMDScore<LongFloatHashMap>> results,
            final MultiVariateTimeSeries[] testSamples,
            boolean normMean) {
        long startTime = System.currentTimeMillis();

        @SuppressWarnings("unchecked")
        final List<Pair<Double, Double>>[] testLabels = new List[testSamples.length];
        for (int i = 0; i < testLabels.length; i++) {
            testLabels[i] = new ArrayList<>();
        }

        final List<Integer> usedLengths = new ArrayList<>(results.size());
        final int[] indicesTest = createIndices(testSamples.length);

        // parallel execution
        ParallelFor.withIndex(executor, threads, new ParallelFor.Each() {
            @Override
            public void run(int id, AtomicInteger processed) {
                // iterate each sample to classify
                for (int i = 0; i < results.size(); i++) {
                    if (i % threads == id) {
                        final BossMDScore<LongFloatHashMap> score = results.get(i);
                        if (score.training >= BOSSMDStackClassifier.this.correctTraining.get() * factor) { // all with same score
                            usedLengths.add(score.windowLength);

                            BOSSMDStack model = (BOSSMDStack) score.model;

                            // create words and BOSS model for test samples
                            long[][][] wordsTest = model.createWordsMDStack(testSamples);
                            BagOfPattern[] bagTest = model.createBagOfPatternMDStack(wordsTest, testSamples, score.features);

                            Predictions p = predict(indicesTest, bagTest, score.idf);

                            for (int j = 0; j < p.labels.length; j++) {
                                synchronized (testLabels[j]) {
                                    testLabels[j].add(new Pair<>(p.labels[j], score.training));
                                }
                            }
                        } else {
                            score.clear();
                        }
                    }
                }
            }
        });

        return score("BOSS " + typeClassifierToString(), testSamples, startTime, testLabels, usedLengths);
    }

}
