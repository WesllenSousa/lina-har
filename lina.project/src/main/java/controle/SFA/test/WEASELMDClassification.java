/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.SFA.test;

import static cern.clhep.Units.s;
import constants.ConstDataset;
import controle.SFA.multDimension.concatenateWords.classification.MDClassifier;
import controle.SFA.multDimension.concatenateWords.classification.WEASELMDClassifier;
import datasets.timeseries.TimeSeriesLoader;
import datasets.timeseries.TimeSeriesMD;

/**
 *
 * @author Wesllen Sousa
 */
public class WEASELMDClassification {

    public static void main(String[] args) {

        TimeSeriesMD[] trainSamples = TimeSeriesLoader.loadHorizontalDataMultiDimensional(
                ConstDataset.DS_TRAIN + "train_shoaib_md.csv", ",", true, 3);
        TimeSeriesMD[] testSamples = TimeSeriesLoader.loadHorizontalDataMultiDimensional(
                ConstDataset.DS_TEST + "test_shoaib_md.csv", ",", true, 3);

        MDClassifier bossmd = new WEASELMDClassifier(trainSamples, testSamples);
        MDClassifier.Score scoreMD = bossmd.eval();
        System.out.println(s + ";" + scoreMD.toString());

    }

}
