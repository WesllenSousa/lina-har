// Copyright (c) 2016 - Patrick Schäfer (patrick.schaefer@hu-berlin.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package algorithms.SFA.classification;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import algorithms.SFA.transformation.BOSS.BagOfPattern;
import algorithms.SFA.transformation.BOSSVS;

import com.carrotsearch.hppc.IntFloatHashMap;
import com.carrotsearch.hppc.ObjectObjectHashMap;
import com.carrotsearch.hppc.cursors.IntIntCursor;
import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import datasets.timeseries.TimeSeries;

/**
 * The Bag-of-SFA-Symbols in Vector Space classifier as published in
 * <p>
 * Schäfer, P.: Scalable time series classification. DMKD (2016)
 */
public class BOSSVSClassifier extends Classifier {

    // default training parameters
    public static double factor = 0.95;

    public static boolean normMagnitudes = false;

    // the trained weasel
    public Ensemble<BossVSModel<IntFloatHashMap>> model;

    public BOSSVSClassifier() {
        super();
    }

    public static class BossVSModel<E> extends Model {

        public BossVSModel() {
        }

        public BossVSModel(
                boolean normed,
                int windowLength) {
            super("BOSS VS", -1, 1, -1, 1, normed, windowLength);
        }

        // The inverse document frequencies learned by training
        public ObjectObjectHashMap<Double, E> idf;

        // the trained BOSS VS transformation
        public BOSSVS bossvs;

        // the best number of Fourier values to be used
        public int features;
    }

    @Override
    public Score eval(
            final TimeSeries[] trainSamples, final TimeSeries[] testSamples) {
        long startTime = System.currentTimeMillis();

        Score scoreTrain = fit(trainSamples);

        if (DEBUG) {
            System.out.println("training");
            System.out.println(scoreTrain.toString());
            outputResult(scoreTrain.training, startTime, testSamples.length);
            System.out.println("");
        }

        // Classify: testing score
        Predictions preticted = score(testSamples);
        int correctTesting = preticted.correct.get();

        return new Score(
                "BOSS VS",
                correctTesting, testSamples.length,
                scoreTrain.training, trainSamples.length,
                scoreTrain.windowLength);
    }

    @Override
    public Score fit(final TimeSeries[] trainSamples) {
        // generate test train/split for cross-validation
        generateIndices(trainSamples);

        Score bestScore = null;
        int bestCorrectTraining = 0;

        int min = BOSSVSClassifier.minWindowLength;
        int max = getMax(trainSamples, BOSSVSClassifier.maxWindowLength);

        // equi-distance sampling of windows
        ArrayList<Integer> windows = new ArrayList<>();
        double count = Math.sqrt(max);
        int distance = (int) Math.round(((maxWindowLength - minWindowLength) / count));
        if (distance == 0) {
            distance = 1;
        }
        for (int c = min; c <= max; c += Math.round(distance)) {
            windows.add(c);
        }

        for (boolean normMean : NORMALIZATION) {
            // train the shotgun models for different window lengths
            Ensemble<BossVSModel<IntFloatHashMap>> m = fitEnsemble(
                    windows.toArray(new Integer[]{}), normMean, trainSamples);
            Double[] labels = predict(m, trainSamples);
            Predictions pred = evalLabels(trainSamples, labels);

            if (bestCorrectTraining <= pred.correct.get()) {
                bestCorrectTraining = pred.correct.get();
                bestScore = m.getHighestScoringModel().score;
                bestScore.training = pred.correct.get();
                this.model = m;
            }
        }

        // return score
        return bestScore;
    }

    @Override
    public Predictions score(final TimeSeries[] testSamples) {
        Double[] labels = predict(testSamples);
        return evalLabels(testSamples, labels);
    }

    @Override
    public Double[] predict(final TimeSeries[] testSamples) {
        return predict(this.model, testSamples);
    }

    protected Ensemble<BossVSModel<IntFloatHashMap>> fitEnsemble(Integer[] windows,
            boolean normMean,
            TimeSeries[] samples) {

        final List<BossVSModel<IntFloatHashMap>> results = new ArrayList<>(windows.length);
        final AtomicInteger correctTraining = new AtomicInteger(0);

        ParallelFor.withIndex(exec, threads, new ParallelFor.Each() {
            Double[] uniqueLabels = uniqueClassLabels(samples);

            @Override
            public void run(int id, AtomicInteger processed) {
                for (int i = 0; i < windows.length; i++) {
                    if (i % threads == id) {
                        BossVSModel<IntFloatHashMap> model = new BossVSModel<>(normMean, windows[i]);
                        try {
                            BOSSVS bossvs = new BOSSVS(maxWordLength, maxSymbol, windows[i], model.normed);
                            int[][] words = bossvs.createWords(samples);

                            for (int f = minWordLenth; f <= Math.min(model.windowLength, maxWordLength); f += 2) {
                                BagOfPattern[] bag = bossvs.createBagOfPattern(words, samples, f);

                                // cross validation using folds
                                int correct = 0;
                                for (int s = 0; s < folds; s++) {
                                    // calculate the tf-idf for each class
                                    ObjectObjectHashMap<Double, IntFloatHashMap> idf = bossvs.createTfIdf(bag,
                                            BOSSVSClassifier.this.trainIndices[s], this.uniqueLabels);

                                    correct += predict(BOSSVSClassifier.this.testIndices[s], bag, idf).correct.get();
                                }
                                if (correct > model.score.training) {
                                    model.score.training = correct;
                                    model.features = f;

                                    if (correct == samples.length) {
                                        break;
                                    }
                                }
                            }

                            // obtain the final matrix
                            BagOfPattern[] bag = bossvs.createBagOfPattern(words, samples, model.features);

                            // calculate the tf-idf for each class
                            model.idf = bossvs.createTfIdf(bag, this.uniqueLabels);
                            model.bossvs = bossvs;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // keep best scores
                        synchronized (correctTraining) {
                            if (model.score.training > correctTraining.get()) {
                                correctTraining.set(model.score.training);
                            }

                            // add to ensemble if train-score is within factor to the best score
                            if (model.score.training >= correctTraining.get() * factor) {
                                results.add(model);
                            }
                        }
                    }
                }
            }
        });

        // returns the ensemble based on the best window-lengths within factor
        return filterByFactor(results, correctTraining.get(), factor);
    }

    protected Predictions predict(
            final int[] indices,
            final BagOfPattern[] bagOfPatternsTestSamples,
            final ObjectObjectHashMap<Double, IntFloatHashMap> matrixTrain) {

        Predictions p = new Predictions(new Double[bagOfPatternsTestSamples.length], 0);

        ParallelFor.withIndex(BLOCKS, new ParallelFor.Each() {
            @Override
            public void run(int id, AtomicInteger processed) {
                long init = System.currentTimeMillis();

                // iterate each sample to classify
                for (int i : indices) {
                    if (i % BLOCKS == id) {
                        double bestDistance = 0.0;

                        // for each class
                        for (ObjectObjectCursor<Double, IntFloatHashMap> classEntry : matrixTrain) {

                            Double label = classEntry.key;
                            IntFloatHashMap stat = classEntry.value;

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
                        if (compareLabels(bagOfPatternsTestSamples[i].label, p.labels[i])) {
                            p.correct.incrementAndGet();
                        }
                    }
                }

                long end = System.currentTimeMillis();
                long time = end - init;
                System.out.println("Predict time: " + time);
            }
        });

        return p;
    }

    protected Double[] predict(final Ensemble<BossVSModel<IntFloatHashMap>> model, final TimeSeries[] testSamples) {
        @SuppressWarnings("unchecked")
        final List<Pair<Double, Integer>>[] testLabels = new List[testSamples.length];
        for (int i = 0; i < testLabels.length; i++) {
            testLabels[i] = new ArrayList<>();
        }

        final List<Integer> usedLengths = Collections.synchronizedList(new ArrayList<>(model.size()));
        final int[] indicesTest = createIndices(testSamples.length);

        // parallel execution
        ParallelFor.withIndex(exec, threads, new ParallelFor.Each() {
            @Override
            public void run(int id, AtomicInteger processed) {
                // iterate each sample to classify
                for (int i = 0; i < model.size(); i++) {
                    if (i % threads == id) {
                        final BossVSModel<IntFloatHashMap> score = model.get(i);
                        usedLengths.add(score.windowLength);

                        BOSSVS model = score.bossvs;

                        // create words and BOSS boss for test samples
                        int[][] wordsTest = model.createWords(testSamples);
                        BagOfPattern[] bagTest = model.createBagOfPattern(wordsTest, testSamples, score.features);

                        Predictions p = predict(indicesTest, bagTest, score.idf);

                        for (int j = 0; j < p.labels.length; j++) {
                            synchronized (testLabels[j]) {
                                testLabels[j].add(new Pair<>(p.labels[j], score.score.training));
                            }
                        }
                    }
                }
            }
        });

        return score("BOSS VS", testSamples, testLabels, usedLengths);
    }

    @Override
    public String toString() {
        BOSSVS bossvs = model.getHighestScoringModel().bossvs;

        return "Name: " + model.getHighestScoringModel().name + "\n"
                //+ "Score - " + score.toString() + "\n"
                + "Word: " + bossvs.maxF + "\n"
                + "Norm: " + bossvs.normMean + "\n"
                + "Symbol: " + bossvs.symbols + "\n"
                + "Window: " + bossvs.windowLength + "\n";
    }

}
