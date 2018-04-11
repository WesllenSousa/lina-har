// Copyright (c) 2017 - Patrick Schäfer (patrick.schaefer@hu-berlin.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package algorithms.SFA.sfa_test;

import controle.constants.ConstDataset;
import algorithms.SFA.classification.Classifier;
import algorithms.SFA.classification.MUSEClassifier;
import datasets.timeseries.MultiVariateTimeSeries;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;
import datasets.timeseries.TimeSeriesLoader2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;

@RunWith(JUnit4.class)
public class MTSClassificationTest {

    // The multivariate datasets to use
    public static String[] datasets = new String[]{
        "LP1",
        "LP2",
        "LP3",
        "LP4",
        "LP5",
        "PenDigits",
        "ShapesRandom",
        "DigitShapeRandom",
        "CMUsubject16",
        "ECG",
        "JapaneseVowels",
        "KickvsPunch",
        "Libras",
        "UWave",
        "Wafer",
        "WalkvsRun",
        "CharacterTrajectories",
        "ArabicDigits",
        "AUSLAN",
        "NetFlow",};

    @Test
    public void testMultiVariatelassification() throws IOException {
        try {
            // the relative path to the datasets
            ClassLoader classLoader = SFAWordsTest.class.getClassLoader();

            File dir = new File(classLoader.getResource("datasets/multivariate/").getFile());

            for (String s : datasets) {
                File d = new File(dir.getAbsolutePath() + "/" + s);
                if (d.exists() && d.isDirectory()) {
                    for (File train : d.listFiles()) {
                        if (train.getName().toUpperCase().endsWith("TRAIN3")) {
                            File test = new File(train.getAbsolutePath().replaceFirst("TRAIN3", "TEST3"));

                            if (!test.exists()) {
                                System.err.println("File " + test.getName() + " does not exist");
                                test = null;
                            }

                            Classifier.DEBUG = false;

                            boolean useDerivatives = true;
                            MultiVariateTimeSeries[] trainSamples = TimeSeriesLoader2.loadMultivariateDatset(train, ConstDataset.SEPARATOR, useDerivatives);
                            MultiVariateTimeSeries[] testSamples = TimeSeriesLoader2.loadMultivariateDatset(test, ConstDataset.SEPARATOR, useDerivatives);

                            MUSEClassifier muse = new MUSEClassifier();
                            MUSEClassifier.BIGRAMS = true;
                            MUSEClassifier.Score museScore = muse.eval(trainSamples, testSamples);
                            System.out.println(s + ";" + museScore.toString());
                        }
                    }
                } else {
                    // not really an error. just a hint:
                    System.out.println("Dataset could not be found: " + d.getAbsolutePath() + ".");
                }
            }
        } finally {
            TimeSeries.APPLY_Z_NORM = true; // FIXME static variable breaks some test cases!
        }
    }
}
