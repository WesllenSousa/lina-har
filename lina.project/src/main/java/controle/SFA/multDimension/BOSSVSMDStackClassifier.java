// Copyright (c) 2016 - Patrick Schäfer (patrick.schaefer@zib.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package controle.SFA.multDimension;

import com.carrotsearch.hppc.LongFloatHashMap;
import com.carrotsearch.hppc.ObjectObjectHashMap;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.carrotsearch.hppc.cursors.IntIntCursor;
import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import controle.SFA.classification.ParallelFor;
import controle.SFA.transformation.BOSSModel.BagOfPattern;
import datasets.timeseries.TimeSeriesMD;
import java.util.Iterator;

/**
 * The Bag-of-SFA-Symbols in Vector Space classifier as published in Schäfer,
 * P.: Scalable time series classification. DMKD (Preprint)
 *
 *
 * @author bzcschae
 *
 */
public class BOSSVSMDStackClassifier extends ClassifierMD {

    public static double factor = 0.92;

    public static boolean normMagnitudes = false;

    public BOSSVSMDStackClassifier(TimeSeriesMD[] train, TimeSeriesMD[] test) throws IOException {
        super(train, test);
    }

    public static class BossMDScore<E> extends Score {

        public BossMDScore(boolean normed, int windowLength) {
            super("BOSS MD", 0, 0, normed, windowLength);
        }

        public ObjectObjectHashMap<String, E> idf;
        public BOSSMDModel model;
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
        //threads variavel herdado de ClassifierMD
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
                    System.out.println("BOSS VS Training:\t w_" + bestScore.windowLength + " f_" + bestScore.features + "\tnormed: \t" + normMean);
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
                    "BOSS MD",
                    1 - formatError(bestCorrectTesting, this.testSamples.length),
                    1 - formatError(bestCorrectTraining, this.trainSamples.length),
                    totalBestScore.normed,
                    totalBestScore.windowLength);
        } finally {
            exec.shutdown();
        }

    }

    public List<BossMDScore<LongFloatHashMap>> fitEnsemble(ExecutorService exec, final boolean normMean) throws FileNotFoundException {
        int min = minWindowLength;
        int max = getMax(trainSamples, maxWindowLength);

        // equi-distance sampling of windows
        ArrayList<Integer> windows = new ArrayList<>();
        double count = Math.sqrt(max);
        double distance = ((max - min) / count);
        for (int c = min; c <= max; c += distance) {
            windows.add(c);
        }
        return fit(windows.toArray(new Integer[]{}), normMean, trainSamples, exec);
    }

    public List<BossMDScore<LongFloatHashMap>> fit(
            Integer[] allWindows,
            boolean normMean,
            TimeSeriesMD[] samples,
            ExecutorService exec) {
        final List<BossMDScore<LongFloatHashMap>> results = new ArrayList<>(allWindows.length);

        ParallelFor.withIndex(exec, threads, new ParallelFor.Each() {
            HashSet<String> uniqueLabels = uniqueClassLabels(samples);
            BossMDScore<LongFloatHashMap> bestScore = new BossMDScore<>(normMean, 0);

            @Override
            public void run(int id, AtomicInteger processed) {
                for (int i = 0; i < allWindows.length; i++) {
                    if (i % threads == id) {
                        BossMDScore<LongFloatHashMap> score = new BossMDScore<>(normMean, allWindows[i]);
                        try {
                            BOSSMDModel model = new BOSSMDModel(maxWindowLength, maxSymbol, score.windowLength, score.normed);
                            int[][][] words = model.createWordsMD(trainSamples);

                            optimize:
                            for (int f = minWindowLength; f <= Math.min(score.windowLength, maxWindowLength); f += 2) {
                                BagOfPattern[] bag = model.createBagOfPatternMD(words, trainSamples, f);

                                // cross validation using folds
                                int correct = 0;
                                for (int s = 0; s < folds; s++) {
                                    // calculate the tf-idf for each class
                                    ObjectObjectHashMap<String, LongFloatHashMap> idf = model.createTfIdf(bag, trainIndices[s], uniqueLabels);

                                    correct += predict(testIndices[s], bag, idf).correct.get();
                                }
                                if (correct > score.training) {
                                    score.training = correct;
                                    score.testing = correct;
                                    score.features = f;

                                    if (correct == samples.length) {
                                        break;
                                    }
                                }
                            }

                            // obtain the final matrix              
                            BagOfPattern[] bag = model.createBagOfPatternMD(words, trainSamples, score.features);

                            // calculate the tf-idf for each class
                            score.idf = model.createTfIdf(bag, uniqueLabels);
                            score.model = model;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (this.bestScore.compareTo(score) < 0) {
                            synchronized (this.bestScore) {
                                if (this.bestScore.compareTo(score) < 0) {
                                    BOSSVSMDStackClassifier.this.correctTraining.set((int) score.training);
                                    this.bestScore = score;
                                }
                            }
                        }

                        // add to ensemble
                        if (score.training >= BOSSVSMDStackClassifier.this.correctTraining.get() * factor) {
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
            final ObjectObjectHashMap<String, LongFloatHashMap> matrixTrain) {

        Predictions p = new Predictions(new String[bagOfPatternsTestSamples.length], 0);

        ParallelFor.withIndex(BLOCKS, new ParallelFor.Each() {
            @Override
            public void run(int id, AtomicInteger processed) {
                // iterate each sample to classify
                for (int i : indices) {
                    if (i % BLOCKS == id) {
                        double bestDistance = 0.0;

                        // for each class
                        for (ObjectObjectCursor<String, LongFloatHashMap> classEntry : matrixTrain) {

                            String label = classEntry.key;
                            LongFloatHashMap stat = classEntry.value;

                            // determine cosine similarity
                            double distance = 0.0;
                            for (Iterator<IntIntCursor> it = bagOfPatternsTestSamples[i].bag.iterator(); it.hasNext();) {
                                IntIntCursor wordFreq = it.next();
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
            final TimeSeriesMD[] testSamples,
            boolean normMean) {
        long startTime = System.currentTimeMillis();

        @SuppressWarnings("unchecked")
        final List<Pair<String, Double>>[] testLabels = new List[testSamples.length];
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
                        if (score.training >= BOSSVSMDStackClassifier.this.correctTraining.get() * factor) { // all with same score
                            usedLengths.add(score.windowLength);

                            BOSSMDModel model = score.model;

                            // create words and BOSS model for test samples
                            int[][][] wordsTest = model.createWordsMD(testSamples);
                            BagOfPattern[] bagTest = model.createBagOfPatternMD(wordsTest, testSamples, score.features);

                            Predictions p = predict(indicesTest, bagTest, score.idf);

                            for (int j = 0; j < p.labels.length; j++) {
                                synchronized (testLabels[j]) {
                                    testLabels[j].add(new Pair<String, Double>(p.labels[j], score.training));
                                }
                            }
                        } else {
                            score.clear();
                        }
                    }
                }
            }
        });
        return score("BOSS MD", testSamples, startTime, testLabels, usedLengths);
    }

    public static boolean setParameters(int maxWordLenght, int minWordLenght, int maxAlfabetSize, int numSources) {

        int maxLenght = 64;
        int num_bits = binlog(maxAlfabetSize);
        int num_bits_source = binlog(numSources);

        int total_bits = num_bits * maxWordLenght * numSources + num_bits_source;
        if (total_bits <= maxLenght) {
            maxWindowLength = maxWordLenght;
            minWindowLength = minWordLenght;
            maxSymbol = maxWordLenght;
            return true;
        } else {
            return false;
        }

    }

    private static int binlog(int number) // returns 0 for bits=0
    {
        int log = 0;
        int bits = number;
        if ((bits & 0xffff0000) != 0) {
            bits >>>= 16;
            log = 16;
        }
        if (bits >= 256) {
            bits >>>= 8;
            log += 8;
        }
        if (bits >= 16) {
            bits >>>= 4;
            log += 4;
        }
        if (bits >= 4) {
            bits >>>= 2;
            log += 2;
        }
        if (1 << log < number) {
            log++;
        }
        return log + (bits >>> 1);
    }
}
