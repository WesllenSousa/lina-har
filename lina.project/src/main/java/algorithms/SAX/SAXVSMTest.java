/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.SAX;

import controle.constants.ConstDataset;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;
import net.seninp.util.UCRUtils;

/**
 *
 * @author Wesllen Sousa
 */
public class SAXVSMTest {

    public static void main(String[] args) throws IOException {
        int WINDOW_SIZE = 50;
        int SAX_PAA = 6;
        int SAX_ALPHABET = 4;
        Double NORMALIZATION_THRESHOLD = 0.05;
        Params params = new Params(WINDOW_SIZE, SAX_PAA, SAX_ALPHABET,
                NORMALIZATION_THRESHOLD, NumerosityReductionStrategy.EXACT);

        String train = ConstDataset.DS_TRAIN + "train_acc_total_magnitude.csv";
        String test = ConstDataset.DS_TEST + "test_acc_total_magnitude.csv";

        Map<String, List<double[]>> trainData = UCRUtils.readUCRData(train);
        Map<String, List<double[]>> testData = UCRUtils.readUCRData(test);

        SAXVSM sax_vsm = new SAXVSM();
        sax_vsm.eval(trainData, testData, params);

        String dir = "E:\\dataset.txt";
        sax_vsm.saveWordBags(trainData, params, dir);
    }

}
