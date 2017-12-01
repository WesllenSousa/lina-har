/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.SFA.test;

import static cern.clhep.Units.s;
import constants.ConstDataset;
import controle.SFA.classification.BOSSMDWordsClassifier;
import controle.SFA.classification.ClassifierMD;
import datasets.timeseries.TimeSeriesLoader;
import datasets.timeseries.TimeSeriesMD;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wesllen Sousa
 */
public class BOSSMDWordsClassification {

    public static void main(String[] args) {

        TimeSeriesMD[] trainSamples = TimeSeriesLoader.loadHorizontalDataMultiDimensional(
                ConstDataset.DS_TRAIN + "train_shoaib_md.csv", ",", true, 3);
        TimeSeriesMD[] testSamples = TimeSeriesLoader.loadHorizontalDataMultiDimensional(
                ConstDataset.DS_TEST + "test_shoaib_md.csv", ",", true, 3);

        try {
            ClassifierMD bossmd = new BOSSMDWordsClassifier(trainSamples, testSamples);
            ClassifierMD.Score scoreMD = bossmd.eval();
            System.out.println(s + ";" + scoreMD.toString());
        } catch (IOException ex) {
            Logger.getLogger(BOSSMDWordsClassification.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
