package controle.SFA.classification;

import com.carrotsearch.hppc.LongFloatHashMap;
import com.carrotsearch.hppc.ObjectObjectHashMap;
import com.carrotsearch.hppc.cursors.IntIntCursor;
import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import controle.SFA.transformation.BOSS.BagOfPattern;
import controle.SFA.transformation.BOSSMDWords;
import datasets.timeseries.MultiVariateTimeSeries;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class BOSSMDWordsClassifier extends BOSSMDStackClassifier {

    public BOSSMDWordsClassifier(MultiVariateTimeSeries[] train, MultiVariateTimeSeries[] test) {
        super(train, test);
        typeClassifier = BOSSMDType.MDWord;
    }

    @Override
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
                            BOSSMDWords model = new BOSSMDWords(maxWordLength, maxSymbol, score.windowLength, score.normed);

                            final long[][] words = model.createWordsMDWords(trainSamples);

                            optimize:
                            for (int f = minWordLenth; f <= Math.min(score.windowLength, maxWordLength); f += 2) {
                                BagOfPattern[] bag = model.createBagOfPatternMDWords(words, trainSamples);

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
                            BagOfPattern[] bag = model.createBagOfPatternMDWords(words, trainSamples);

                            // calculate the tf-idf for each class
                            score.idf = model.createTfIdf(bag, uniqueLabels);
                            score.model = model;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (this.bestScore.compareTo(score) < 0) {
                            synchronized (this.bestScore) {
                                if (this.bestScore.compareTo(score) < 0) {
                                    BOSSMDWordsClassifier.this.correctTraining.set((int) score.training);
                                    this.bestScore = score;
                                }
                            }
                        }

                        // add to ensemble
                        if (score.training >= BOSSMDWordsClassifier.this.correctTraining.get() * factor) {
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

    @Override
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

    @Override
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
                        if (score.training >= BOSSMDWordsClassifier.this.correctTraining.get() * factor) { // all with same score
                            usedLengths.add(score.windowLength);

                            BOSSMDWords model = (BOSSMDWords) score.model;

                            // create words and BOSS model for test samples
                            long[][] wordsTest = model.createWordsMDWords(testSamples);
                            BagOfPattern[] bagTest = model.createBagOfPatternMDWords(wordsTest, testSamples);

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
