package algorithms.SFA.sfa_test.classification;

import algorithms.SFA.classification.BOSSVSClassifier;
import algorithms.SFA.classification.Classifier;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;

public class BossVSClassifierTest {

    public static void main(String[] args) {

        //uci_symbolic, shoaib_symbolic
        String test = System.getProperty("user.home") + "\\Lina\\Datasets\\Test\\shoaib_symbolic_pca.csv";
        String train = System.getProperty("user.home") + "\\Lina\\Datasets\\Train\\shoaib_symbolic_pca.csv";

        // Load the train/test splits
        TimeSeries[] testSamples = TimeSeriesLoader.loadHorizontalData(test, ",", false);
        TimeSeries[] trainSamples = TimeSeriesLoader.loadHorizontalData(train, ",", false);

        // The BOSS VS classifier
        Classifier bossVS = new BOSSVSClassifier();
        Classifier.Score scoreBOSSVS = bossVS.eval(trainSamples, testSamples);
        System.out.println(bossVS.toString());
        System.out.println(scoreBOSSVS.toString());

        System.out.println("==============================");

//        //The BOSS ensemble classifier
//        Classifier boss = new BOSSEnsembleClassifier();
//        Classifier.Score scoreBOSS = boss.eval(trainSamples, testSamples);
//        System.out.println(boss.toString());
//        System.out.println(scoreBOSS.toString());
    }

}
