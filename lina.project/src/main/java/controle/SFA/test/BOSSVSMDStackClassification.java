/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.SFA.test;

import static cern.clhep.Units.s;
import constants.ConstDataset;
import controle.SFA.multDimension.BOSSVSMDStackClassifier;
import controle.SFA.multDimension.ClassifierMD;
import datasets.timeseries.TimeSeriesLoader;
import datasets.timeseries.TimeSeriesMD;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wesllen Sousa
 */
public class BOSSVSMDStackClassification {
    
    public static void main(String[] args) {
        
        try {
            TimeSeriesMD[] trainSamples = TimeSeriesLoader.loadHorizontalDataMultiDimensional(
                    ConstDataset.DS_TRAIN + "train_shoaib_md.csv", ",", true, 3);
            TimeSeriesMD[] testSamples = TimeSeriesLoader.loadHorizontalDataMultiDimensional(
                    ConstDataset.DS_TEST + "test_shoaib_md.csv", ",", true, 3);
            
            ClassifierMD bossmd = new BOSSVSMDStackClassifier(trainSamples, testSamples);
            ClassifierMD.Score scoreMD = bossmd.eval();
            System.out.println(s + ";" + scoreMD.toString());
            
        } catch (IOException ex) {
            Logger.getLogger(BOSSVSMDStackClassification.class.getName()).log(Level.SEVERE, null, ex);
        }
                    
    }
    
}
