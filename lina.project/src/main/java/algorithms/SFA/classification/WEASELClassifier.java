// Copyright (c) 2017 - Patrick Schäfer (patrick.schaefer@hu-berlin.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package algorithms.SFA.classification;

import com.carrotsearch.hppc.cursors.IntIntCursor;
import de.bwaldvogel.liblinear.*;
import algorithms.SFA.transformation.WEASEL;
import algorithms.SFA.transformation.WEASEL.BagOfBigrams;
import algorithms.SFA.transformation.WEASEL.Dictionary;
import datasets.timeseries.TimeSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * The WEASEL (Word ExtrAction for time SEries cLassification) classifier as
 * published in
 * <p>
 * Schäfer, P., Leser, U.: Fast and Accurate Time Series Classification with
 * WEASEL." CIKM 2017
 */
public class WEASELClassifier extends Classifier {

    public static SolverType solverType = SolverType.L2R_LR_DUAL;

    public static double chi = 2;
    public static double bias = 1;
    public static double p = 0.1;
    public static int iterations = 1000;
    public static double c = 1;

    // the trained weasel
    WEASELModel model;

    public WEASELClassifier() {
        super();
        Linear.resetRandom();
    }

    public static class WEASELModel extends Model {

        public WEASELModel() {
        }

        public WEASELModel(
                boolean normed,
                int features,
                WEASEL model,
                de.bwaldvogel.liblinear.Model linearModel,
                int testing,
                int testSize,
                int training,
                int trainSize
        ) {
            super("WEASEL", testing, testSize, training, trainSize, normed, -1);
            this.features = features;
            this.weasel = model;
            this.linearModel = linearModel;
        }

        // the best number of Fourier values to be used
        public int features;

        // the trained WEASEL transformation
        public WEASEL weasel;

        // the trained liblinear classifier
        public de.bwaldvogel.liblinear.Model linearModel;
    }

    @Override
    public Score eval(
            final TimeSeries[] trainSamples, final TimeSeries[] testSamples) {
        long startTime = System.currentTimeMillis();

        Score score = fit(trainSamples);

        // training score
        if (DEBUG) {
            System.out.println(score.toString());
            outputResult(score.training, startTime, trainSamples.length);
        }

        // determine score
        int correctTesting = score(testSamples).correct.get();

        if (DEBUG) {
            System.out.println("WEASEL Testing:\t");
            outputResult(correctTesting, startTime, testSamples.length);
            System.out.println("");
        }

        return new Score(
                "WEASEL",
                correctTesting, testSamples.length,
                score.training, trainSamples.length,
                score.windowLength
        );
    }

    @Override
    public Score fit(final TimeSeries[] trainSamples) {

        // train the shotgun models for different window lengths
        this.model = fitWeasel(trainSamples);

        // return score
        return model.score;
    }

    @Override
    public Predictions score(final TimeSeries[] testSamples) {
        Double[] labels = predict(testSamples);
        return evalLabels(testSamples, labels);
    }

    @Override
    public Double[] predict(TimeSeries[] samples) {
        final int[][][] wordsTest = model.weasel.createWords(samples);
        BagOfBigrams[] bagTest = model.weasel.createBagOfPatterns(wordsTest, samples, model.features);

        // chi square changes key mappings => remap
        model.weasel.dict.remap(bagTest);

        FeatureNode[][] features = initLibLinear(bagTest, model.linearModel.getNrFeature());

        Double[] labels = new Double[samples.length];

        for (int ind = 0; ind < features.length; ind++) {
            double label = Linear.predict(model.linearModel, features[ind]);
            labels[ind] = label;
        }

        return labels;
    }

    protected WEASELModel fitWeasel(final TimeSeries[] samples) {
        try {
            int maxCorrect = -1;
            int bestF = -1;
            boolean bestNorm = false;

            int min = minWindowLength;
            int max = getMax(samples, maxWindowLength);
            int[] windowLengths = new int[max - min + 1];
            for (int w = min, a = 0; w <= max; w++, a++) {
                windowLengths[a] = w;
            }

            optimize:
            for (final boolean mean : NORMALIZATION) {
                
                long init = System.currentTimeMillis();

                WEASEL model = new WEASEL(maxWordLength, maxSymbol, windowLengths, mean, false);
                int[][][] words = model.createWords(samples);

                long end = System.currentTimeMillis();
                long time = end - init;
                System.out.println("Create words time: " + time);

                for (int f = minWordLenth; f <= maxWordLength; f += 2) {
                    init = System.currentTimeMillis();
                    
                    model.dict.reset();
                    BagOfBigrams[] bop = model.createBagOfPatterns(words, samples, f);
                    model.filterChiSquared(bop, chi);

                    // train liblinear
                    final Problem problem = initLibLinearProblem(bop, model.dict, bias);

                    int correct = trainLibLinear(problem, solverType, c, iterations, p, folds);

                    end = System.currentTimeMillis();
                    time = end - init;
                    System.out.println("Train time: " + time);

                    if (correct > maxCorrect) {
                        // System.out.println(correct + "\t" + f);
                        maxCorrect = correct;
                        bestF = f;
                        bestNorm = mean;
                    }
                    if (correct == samples.length) {
                        break optimize;
                    }
                }
            }

            // obtain the final matrix
            WEASEL model1 = new WEASEL(maxWordLength, maxSymbol, windowLengths, bestNorm, false);
            int[][][] words = model1.createWords(samples);
            BagOfBigrams[] bob = model1.createBagOfPatterns(words, samples, bestF);
            model1.filterChiSquared(bob, chi);

            // train liblinear
            Problem problem = initLibLinearProblem(bob, model1.dict, bias);
            de.bwaldvogel.liblinear.Model linearModel = Linear.train(problem, new Parameter(solverType, c, iterations, p));

            return new WEASELModel(
                    bestNorm,
                    bestF,
                    model1,
                    linearModel,
                    0, // testing
                    1,
                    maxCorrect, // training
                    samples.length
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Problem initLibLinearProblem(
            final BagOfBigrams[] bob,
            final Dictionary dict,
            final double bias) {
        Problem problem = new Problem();
        problem.bias = bias;
        problem.n = dict.size() + 1;
        problem.y = getLabels(bob);

        final FeatureNode[][] features = initLibLinear(bob, problem.n);

        problem.l = features.length;
        problem.x = features;
        return problem;
    }

    protected static FeatureNode[][] initLibLinear(final BagOfBigrams[] bob, int max_feature) {
        FeatureNode[][] featuresTrain = new FeatureNode[bob.length][];
        for (int j = 0; j < bob.length; j++) {
            BagOfBigrams bop = bob[j];
            ArrayList<FeatureNode> features = new ArrayList<>(bop.bob.size());
            for (IntIntCursor word : bop.bob) {
                if (word.value > 0 && word.key <= max_feature) {
                    features.add(new FeatureNode(word.key, (word.value)));
                }
            }
            FeatureNode[] featuresArray = features.toArray(new FeatureNode[]{});
            Arrays.parallelSort(featuresArray, new Comparator<FeatureNode>() {
                @Override
                public int compare(FeatureNode o1, FeatureNode o2) {
                    return Integer.compare(o1.index, o2.index);
                }
            });
            featuresTrain[j] = featuresArray;
        }
        return featuresTrain;
    }

    public static FeatureNode[][] initLibLinear(final BagOfBigrams bob, int max_feature) {
        FeatureNode[][] featuresTrain = new FeatureNode[1][];
        BagOfBigrams bop = bob;
        ArrayList<FeatureNode> features = new ArrayList<>(bop.bob.size());
        for (IntIntCursor word : bop.bob) {
            if (word.value > 0 && word.key <= max_feature) {
                features.add(new FeatureNode(word.key, ((double) word.value)));
            }
        }
        FeatureNode[] featuresArray = features.toArray(new FeatureNode[]{});
        Arrays.parallelSort(featuresArray, new Comparator<FeatureNode>() {
            @Override
            public int compare(FeatureNode o1, FeatureNode o2) {
                return Integer.compare(o1.index, o2.index);
            }
        });
        featuresTrain[0] = featuresArray;
        return featuresTrain;
    }

    protected static double[] getLabels(final BagOfBigrams[] bagOfPatternsTestSamples) {
        double[] labels = new double[bagOfPatternsTestSamples.length];
        for (int i = 0; i < bagOfPatternsTestSamples.length; i++) {
            labels[i] = bagOfPatternsTestSamples[i].label;
        }
        return labels;
    }

    @Override
    public String toString() {
        return "Name: WEASEL \n"
                //+ "Score - " + score.toString() + "\n"
                + "Word: " + model.weasel.maxF + "\n"
                + "Norm: " + model.weasel.normMean + "\n"
                + "Symbol: " + model.weasel.alphabetSize + "\n"
                + "Window: " + model.weasel.windowLengths[0] + "\n";
    }

}
