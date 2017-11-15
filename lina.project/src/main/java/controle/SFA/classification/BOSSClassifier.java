/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.SFA.classification;

import com.carrotsearch.hppc.cursors.IntIntCursor;
import static controle.SFA.classification.BOSSEnsembleClassifier.factor;
import static controle.SFA.classification.Classifier.DEBUG;
import static controle.SFA.classification.Classifier.NORMALIZATION;
import static controle.SFA.classification.Classifier.formatError;
import static controle.SFA.classification.Classifier.outputResult;
import static controle.SFA.classification.Classifier.threads;
import controle.SFA.transformation.BOSSModel;
import datasets.timeseries.TimeSeries;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Wesllen Sousa
 */
public class BOSSClassifier extends Classifier {

    private ArrayList<Integer> windowsBoss = new ArrayList<>();

    public BOSSClassifier(TimeSeries[] train, TimeSeries[] test, int windowLength) throws IOException {
        super(train, test);
        windowsBoss.add(windowLength);
    }

    public BOSSClassifier(int windowLength) {
        super();
        windowsBoss.add(windowLength);
    }

    public BOSSClassifier(TimeSeries[] train, TimeSeries[] test) throws IOException {
        super(train, test);
    }

    public static class BossScore extends Score {

        public BossScore(boolean normed, int windowLength) {
            super("BOSS", 0, 0, normed, windowLength);
        }

        public BOSSModel.BagOfPattern[] bag;
        public BOSSModel model;
        public int features;

        @Override
        public void clear() {
            super.clear();
            this.model = null;
            this.bag = null;
        }
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

                List<BossScore> results = fit(windowsBoss.toArray(new Integer[]{}), norm, trainSamples, exec);

                @SuppressWarnings("unchecked")
                final List<Pair<String, Double>>[] testLabels = new List[testSamples.length];
                for (int i = 0; i < testLabels.length; i++) {
                    testLabels[i] = new ArrayList<>();
                }
                final List<Integer> usedLengths = new ArrayList<>(results.size());

                for (int i = 0; i < results.size(); i++) {
                    final BossScore score = results.get(i);
                    usedLengths.add(score.windowLength);

                    // training score
                    if (DEBUG) {
                        System.out.println("BOSS Training:\t" + score.windowLength + " " + score.features + "\tnormed: \t" + norm);
                        outputResult(this.correctTraining.get(), startTime, this.trainSamples.length);
                    }

                    BOSSModel model = score.model;

                    // create words and BOSS model for test samples
                    int[][] wordsTest = model.createWords(testSamples);
                    BOSSModel.BagOfPattern[] bagTest = model.createBagOfPattern(wordsTest, testSamples, score.features);

                    // determine labels based on the majority of predictions
                    Predictions p = predict(windowsBoss.get(0), bagTest, score.bag);
                    for (int j = 0; j < p.labels.length; j++) {
                        synchronized (testLabels[j]) {
                            testLabels[j].add(new Pair<>(p.labels[j], score.training));
                        }
                    }
                    int correctTesting = score("BOSS", testSamples, startTime, testLabels, usedLengths);

                    if (bestCorrectTraining < score.training) {
                        bestCorrectTesting = correctTesting;
                        bestCorrectTraining = (int) score.training;
                        totalBestScore = score;
                    }
                }
            }

            return new Score(
                    "BOSS",
                    1 - formatError(bestCorrectTesting, this.testSamples.length),
                    1 - formatError(bestCorrectTraining, this.trainSamples.length),
                    totalBestScore.normed,
                    totalBestScore.windowLength);
        } finally {
            exec.shutdown();
        }
    }

    public ArrayList<BossScore> fit(
            Integer[] allWindows,
            boolean normMean,
            TimeSeries[] samples,
            ExecutorService exec) {
        final ArrayList<BossScore> results = new ArrayList<>(allWindows.length);
        ParallelFor.withIndex(exec, threads, new ParallelFor.Each() {
            BossScore bestScore = new BossScore(normMean, 0);

            @Override
            public void run(int id, AtomicInteger processed) {
                for (int i = 0; i < allWindows.length; i++) {
                    if (i % threads == id) {
                        BossScore score = new BossScore(normMean, allWindows[i]);
                        try {
                            BOSSModel boss = new BOSSModel(maxWordLength, maxSymbol, allWindows[i], score.normed);
                            int[][] words = boss.createWords(samples);

                            optimize:
                            for (int wLength = minWordLenth; wLength <= maxWordLength; wLength += 2) {

                                BOSSModel.BagOfPattern[] bag = boss.createBagOfPattern(words, samples, wLength);

                                Predictions p = predict(score.windowLength, bag, bag);

                                if (p.correct.get() > score.training) {
                                    score.training = p.correct.get();
                                    score.testing = p.correct.get();
                                    score.features = wLength;
                                    score.model = boss;
                                    score.bag = bag;

                                    if (p.correct.get() == samples.length) {
                                        break optimize;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // keep best scores
                        if (this.bestScore.compareTo(score) < 0) {
                            synchronized (this.bestScore) {
                                if (this.bestScore.compareTo(score) < 0) {
                                    BOSSClassifier.this.correctTraining.set((int) score.training);
                                    this.bestScore = score;
                                }
                            }
                        }

                        // add to ensemble
                        if (score.training >= BOSSClassifier.this.correctTraining.get() * factor) { // all with same score
                            synchronized (results) {
                                results.add(score);
                            }
                        }
                    }
                }
            }
        });

        // cleanup unused scores
        for (BossScore s : results) {
            if (s.bag != null
                    && s.training < BOSSClassifier.this.correctTraining.get() * factor) {
                s.clear();
            }
        }

        // sort descending
        Collections.sort(results, Collections.reverseOrder());
        return results;
    }

    public Predictions predict(
            final int windowLength,
            final BOSSModel.BagOfPattern[] bagOfPatternsTestSamples,
            final BOSSModel.BagOfPattern[] bagOfPatternsTrainSamples) {

        Predictions p = new Predictions(new String[bagOfPatternsTestSamples.length], 0);

        ParallelFor.withIndex(BLOCKS, new ParallelFor.Each() {
            @Override
            public void run(int id, AtomicInteger processed) {
                // iterate each sample to classify
                for (int i = 0; i < bagOfPatternsTestSamples.length; i++) {
                    if (i % BLOCKS == id) {
                        int bestMatch = -1;
                        long minDistance = Integer.MAX_VALUE;

                        // Distance if there is no matching word
                        double noMatchDistance = 0.0;
                        for (IntIntCursor key : bagOfPatternsTestSamples[i].bag) {
                            noMatchDistance += key.value * key.value;
                        }

                        nnSearch:
                        for (int j = 0; j < bagOfPatternsTrainSamples.length; j++) {
                            if (bagOfPatternsTestSamples[i] != bagOfPatternsTrainSamples[j]) {
                                // determine distance
                                long distance = 0;
                                for (IntIntCursor key : bagOfPatternsTestSamples[i].bag) {
                                    long buf = key.value - bagOfPatternsTrainSamples[j].bag.get(key.key);
                                    distance += buf * buf;

                                    if (distance >= minDistance) {
                                        continue nnSearch;
                                    }
                                }

                                // update nearest neighbor
                                if (distance != noMatchDistance && distance < minDistance) {
                                    minDistance = distance;
                                    bestMatch = j;
                                }
                            }
                        }

                        // check if the prediction is correct
                        p.labels[i] = bestMatch > -1 ? bagOfPatternsTrainSamples[bestMatch].label : null;
                        if (bagOfPatternsTestSamples[i].label.equals(p.labels[i])) {
                            p.correct.incrementAndGet();
                        }
                    }
                }
            }
        });

        return p;
    }

    public Predictions predictStream(
            final BOSSModel.BagOfPattern bagOfPatternsTestSample,
            final BOSSModel.BagOfPattern[] bagOfPatternsTrainSamples) {

        Predictions p = new Predictions(new String[1], 0);

        int bestMatch = -1;
        long minDistance = Integer.MAX_VALUE;

        // Distance if there is no matching word
        double noMatchDistance = 0.0;
        for (IntIntCursor key : bagOfPatternsTestSample.bag) {
            noMatchDistance += key.value * key.value;
        }

        nnSearch:
        for (int j = 0; j < bagOfPatternsTrainSamples.length; j++) {
            if (bagOfPatternsTestSample != bagOfPatternsTrainSamples[j]) {
                // determine distance
                long distance = 0;
                for (IntIntCursor key : bagOfPatternsTestSample.bag) {
                    long buf = key.value - bagOfPatternsTrainSamples[j].bag.get(key.key);
                    distance += buf * buf;

                    if (distance >= minDistance) {
                        continue nnSearch;
                    }
                }

                // update nearest neighbor
                if (distance != noMatchDistance && distance < minDistance) {
                    minDistance = distance;
                    bestMatch = j;
                }
            }
        }

        // check if the prediction is correct
        p.labels[0] = bestMatch > -1 ? bagOfPatternsTrainSamples[bestMatch].label : null;
//        if (bagOfPatternsTestSample.label.equals(p.labels[0])) {
//            p.correct.incrementAndGet();
//        }

        return p;
    }

}
