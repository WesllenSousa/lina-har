// Copyright (c) 2016 - Patrick Schäfer (patrick.schaefer@zib.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package controle.SFA.sfa_test;

import controle.SFA.classification.BOSSEnsembleClassifier;
import controle.SFA.classification.BOSSVSClassifier;
import controle.SFA.classification.Classifier;
import controle.SFA.classification.ShotgunClassifier;
import controle.SFA.classification.ShotgunEnsembleClassifier;
import controle.SFA.classification.WEASELClassifier;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader2;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;

@RunWith(JUnit4.class)
public class UCRClassificationTest {

    // The datasets to use
    public static String[] datasets = new String[]{
        //        "Coffee", "Beef",
        "CBF"
//            ,
//        "ECG200", "FaceFour", "OliveOil",
//        "Gun_Point",
//        "DiatomSizeReduction",
//        "ECGFiveDays",
//        "TwoLeadECG",
//        "SonyAIBORobot SurfaceII",
//        "MoteStrain",
//        "ItalyPowerDemand",
//        "SonyAIBORobot Surface",
    };

    public static void main(String[] args) {

        File dir = new File(System.getProperty("user.home") + "\\Lina\\Datasets\\Temp\\SFAdatasets");
        //File dir = new File("/Users/bzcschae/workspace/similarity/datasets/classification");

        for (String s : datasets) {
            File d = new File(dir.getAbsolutePath() + "/" + s);
            if (d.exists() && d.isDirectory()) {
                for (File train : d.listFiles()) {
                    if (train.getName().toUpperCase().endsWith("TRAIN")) {
                        File test = new File(train.getAbsolutePath().replaceFirst("TRAIN", "TEST"));

                        if (!test.exists()) {
                            System.err.println("File " + test.getName() + " does not exist");
                            test = null;
                        }

                        Classifier.DEBUG = false;

                        // Load the train/test splits
                        TimeSeries[] testSamples = TimeSeriesLoader2.loadDataset(test);
                        TimeSeries[] trainSamples = TimeSeriesLoader2.loadDataset(train);

                        // The WEASEL-classifier
                        Classifier w = new WEASELClassifier();
                        Classifier.Score scoreW = w.eval(trainSamples, testSamples);
                        System.out.println(s + ";" + scoreW.toString());

                        // The BOSS ensemble classifier
                        Classifier boss = new BOSSEnsembleClassifier();
                        Classifier.Score scoreBOSS = boss.eval(trainSamples, testSamples);
                        System.out.println(s + ";" + scoreBOSS.toString());

                        // The BOSS VS classifier
                        Classifier bossVS = new BOSSVSClassifier();
                        Classifier.Score scoreBOSSVS = bossVS.eval(trainSamples, testSamples);
                        System.out.println(s + ";" + scoreBOSSVS.toString());

                        // The Shotgun ensemble classifier
                        Classifier shotgunEnsemble = new ShotgunEnsembleClassifier();
                        Classifier.Score scoreShotgunEnsemble = shotgunEnsemble.eval(trainSamples, testSamples);
                        System.out.println(s + ";" + scoreShotgunEnsemble.toString());

                        // The Shotgun classifier
                        Classifier shotgun = new ShotgunClassifier();
                        Classifier.Score scoreShotgun = shotgun.eval(trainSamples, testSamples);
                        System.out.println(s + ";" + scoreShotgun.toString());
                    }
                }
            } else {
                // not really an error. just a hint:
                System.out.println("Dataset could not be found: " + d.getAbsolutePath() + ". "
                        + "Please download datasets from [http://www.cs.ucr.edu/~eamonn/time_series_data/].");
            }
        }

    }
}
