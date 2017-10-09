// Copyright (c) 2016 - Patrick Schäfer (patrick.schaefer@zib.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package controle.SFA.test;

import controle.SFA.classification.BOSSEnsembleClassifier;
import controle.SFA.classification.BOSSVSClassifier;
import controle.SFA.classification.Classifier;
import controle.SFA.classification.ParallelFor;
import controle.SFA.classification.ShotgunClassifier;
import controle.SFA.classification.ShotgunEnsembleClassifier;
import controle.SFA.classification.WEASELClassifier;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;
import java.io.File;
import java.io.IOException;

public class UCRClassification {

    // The datasets to use
//    public static String[] datasets = new String[]{
//        "Coffee", "ECG200", "FaceFour", "OliveOil",
//        "Gun_Point", "Beef",
//        "DiatomSizeReduction",
//        "CBF",
//        "ECGFiveDays",
//        "TwoLeadECG",
//        "SonyAIBORobot SurfaceII",
//        "MoteStrain",
//        "ItalyPowerDemand",
//        "SonyAIBORobot Surface",};
//    
    public static String[] datasets = new String[]{"CBF"};

    public static void main(String argv[]) throws IOException {
        try {
            // the relative path to the datasets
            File dir = new File("samples/SFAdatasets/");

            for (String s : datasets) {
                File d = new File(dir.getAbsolutePath() + "/" + s);
                if (d.exists() && d.isDirectory()) {
                    for (File f : d.listFiles()) {
                        if (f.getName().toUpperCase().endsWith("TRAIN")) {
                            File train = f;
                            File test = new File(f.getAbsolutePath().replaceFirst("TRAIN", "TEST"));

                            if (!test.exists()) {
                                System.err.println("File " + test.getName() + " does not exist");
                                test = null;
                            }

                            Classifier.DEBUG = false;

                            // Load the train/test splits
                            TimeSeries[] testSamples = TimeSeriesLoader.loadHorizontalData(test.getAbsolutePath(), " ", true);
                            TimeSeries[] trainSamples = TimeSeriesLoader.loadHorizontalData(train.getAbsolutePath(), " ", true);

                            // The W-classifier
                            Classifier w = new WEASELClassifier(trainSamples, testSamples);
                            Classifier.Score scoreW = w.eval();
                            System.out.println(s + ";" + scoreW.toString());

                            // The BOSS ensemble classifier
                            Classifier boss = new BOSSEnsembleClassifier(trainSamples, testSamples);
                            Classifier.Score scoreBOSS = boss.eval();
                            System.out.println(s + ";" + scoreBOSS.toString());

                            // The BOSS VS classifier
                            Classifier bossVS = new BOSSVSClassifier(trainSamples, testSamples);
                            Classifier.Score scoreBOSSVS = bossVS.eval();
                            System.out.println(s + ";" + scoreBOSSVS.toString());

                            // The Shotgun ensemble classifier
                            Classifier shotgunEnsemble = new ShotgunEnsembleClassifier(trainSamples, testSamples);
                            Classifier.Score scoreShotgunEnsemble = shotgunEnsemble.eval();
                            System.out.println(s + ";" + scoreShotgunEnsemble.toString());

                            // The Shotgun classifier
                            Classifier shotgun = new ShotgunClassifier(trainSamples, testSamples);
                            Classifier.Score scoreShotgun = shotgun.eval();
                            System.out.println(s + ";" + scoreShotgun.toString());
                        }
                    }
                } else {
                    System.err.println("Does not exist!" + d.getAbsolutePath());
                }
            }
        } finally {
            ParallelFor.shutdown();
        }
    }

}
