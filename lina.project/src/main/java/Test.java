
import algorithms.SAX.Params;
import algorithms.SAX.SAXVSM;
import controle.constants.ConstDataset;
import controle.constants.Parameters;
import java.util.List;
import java.util.Map;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;
import net.seninp.util.UCRUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wesllen Sousa
 */
public class Test {

    public static void main(String[] args) throws Exception {

        int window = (int) (Parameters.MIN_WINDOW_LENGTH);
        Params params = new Params(window, Parameters.MAX_WORD_LENGTH,
                Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.NORMALIZATION_THRESHOLD,
                NumerosityReductionStrategy.EXACT);

        String train = ConstDataset.DS_TRAIN + "uci_symbolic_all.csv";
        String test = ConstDataset.DS_TEST + "uci_symbolic.csv";
        System.out.println(train);

        Map<String, List<double[]>> trainData = UCRUtils.readUCRData(train);
        Map<String, List<double[]>> testData = UCRUtils.readUCRData(test);

        SAXVSM sax_vsm = new SAXVSM();
        sax_vsm.eval(trainData, testData, params);
    }
}
