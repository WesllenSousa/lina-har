/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.SFA.test;

import constants.ConstDataset;
import controle.SFA.classification.BOSSClassifier;
import controle.SFA.classification.Classifier;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;
import java.io.IOException;

/**
 *
 * @author Wesllen Sousa
 */
public class BOSSClassification {

    public static void main(String[] args) {

        // Load the train/test splits
        TimeSeries[] testSamples = TimeSeriesLoader.loadHorizontalData(ConstDataset.DS_TEMP + "SFAdatasets/CBF/CBF_TEST", " ", true);
        TimeSeries[] trainSamples = TimeSeriesLoader.loadHorizontalData(ConstDataset.DS_TEMP + "SFAdatasets/CBF/CBF_TRAIN", " ", true);

        // The BOSS ensemble classifier
        try {
            int window = 30;
            Classifier boss = new BOSSClassifier(trainSamples, testSamples, window);
            Classifier.Score scoreBOSS = boss.eval();
            System.out.println(scoreBOSS.toString());

        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

}
