// Copyright (c) 2016 - Patrick Schäfer (patrick.schaefer@zib.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package controle.SFA.classification;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static controle.SFA.classification.Classifier.DEBUG;
import static controle.SFA.classification.Classifier.NORMALIZATION;
import controle.SFA.classification.Classifier.Pair;
import controle.SFA.classification.Classifier.Predictions;
import controle.SFA.classification.Classifier.Score;
import static controle.SFA.classification.Classifier.formatError;
import static controle.SFA.classification.Classifier.outputResult;
import static controle.SFA.classification.Classifier.threads;
import datasets.timeseries.TimeSeries;
import controle.SFA.transformation.BOSSModel;
import controle.SFA.transformation.BOSSModel.BagOfPattern;

/**
 *
 * The Bag-of-SFA-Symbols Ensemble Classifier as published in Schäfer, P.: The
 * boss is concerned with time series classification in the presence of noise.
 * DMKD 29(6) (2015) 1505–1530
 *
 * @author bzcschae
 *
 */
public class BOSSEnsembleClassifier extends BOSSClassifier {

    public static double factor = 0.92;

    public BOSSEnsembleClassifier(TimeSeries[] train, TimeSeries[] test) throws IOException {
        super(train, test);
    }

    @Override
    public Score eval() throws IOException {
        ExecutorService exec = Executors.newFixedThreadPool(threads);
        try {
            BossScore totalBestScore = null;
            int bestCorrectTesting = 0;
            int bestCorrectTraining = 0;

            for (boolean norm : NORMALIZATION) {
                long startTime = System.currentTimeMillis();

                this.correctTraining = new AtomicInteger(0);

                List<BossScore> scores = fitEnsemble(exec, norm);

                // training score
                BossScore bestScore = scores.get(0);
                if (DEBUG) {
                    System.out.println("BOSS Training:\t" + bestScore.windowLength + " " + bestScore.features + "\tnormed: \t" + norm);
                    outputResult(this.correctTraining.get(), startTime, this.trainSamples.length);
                }

                // determine labels based on the majority of predictions
                int correctTesting = predictEnsamble(exec, scores, this.testSamples, norm);

                if (bestCorrectTraining < bestScore.training) {
                    bestCorrectTesting = correctTesting;
                    bestCorrectTraining = (int) bestScore.training;
                    totalBestScore = bestScore;
                }
                if (DEBUG) {
                    System.out.println("");
                }
            }

            return new Score(
                    "BOSS ensemble",
                    1 - formatError(bestCorrectTesting, this.testSamples.length),
                    1 - formatError(bestCorrectTraining, this.trainSamples.length),
                    totalBestScore.normed,
                    totalBestScore.windowLength);
        } finally {
            exec.shutdown();
        }
    }

    public List<BossScore> fitEnsemble(
            ExecutorService exec,
            final boolean normMean) throws FileNotFoundException {
        int minWindowLength = this.minWindowLength;
        int maxWindowLength = getMax(trainSamples, this.maxWindowLength);
        for (TimeSeries ts : this.trainSamples) {
            maxWindowLength = Math.min(ts.getLength(), maxWindowLength);
        }
        ArrayList<Integer> windows = new ArrayList<Integer>();
        for (int windowLength = maxWindowLength; windowLength >= minWindowLength; windowLength--) {
            windows.add(windowLength);
        }
        return fit(windows.toArray(new Integer[]{}), normMean, trainSamples, exec);
    }

    public int predictEnsamble(
            final ExecutorService executor,
            final List<BossScore> results,
            final TimeSeries[] testSamples,
            boolean normMean) {
        long startTime = System.currentTimeMillis();

        @SuppressWarnings("unchecked")
        final List<Pair<String, Double>>[] testLabels = new List[testSamples.length];
        for (int i = 0; i < testLabels.length; i++) {
            testLabels[i] = new ArrayList<Pair<String, Double>>();
        }

        final List<Integer> usedLengths = new ArrayList<Integer>(results.size());

        // parallel execution
        ParallelFor.withIndex(executor, threads, new ParallelFor.Each() {
            @Override
            public void run(int id, AtomicInteger processed) {
                // iterate each sample to classify
                for (int i = 0; i < results.size(); i++) {
                    if (i % threads == id) {
                        final BossScore score = results.get(i);
                        if (score.training >= BOSSEnsembleClassifier.this.correctTraining.get() * factor) { // all with same score
                            usedLengths.add(score.windowLength);

                            BOSSModel model = score.model;

                            // create words and BOSS model for test samples
                            int[][] wordsTest = model.createWords(testSamples);
                            BagOfPattern[] bagTest = model.createBagOfPattern(wordsTest, testSamples, score.features);

                            Predictions p = predict(score.windowLength, bagTest, score.bag);

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

        return score("BOSS", testSamples, startTime, testLabels, usedLengths);
    }

}
