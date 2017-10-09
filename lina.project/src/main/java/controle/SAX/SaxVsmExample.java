/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.SAX;

import net.seninp.jmotif.sax.NumerosityReductionStrategy;

/**
 *
 * @author Wesllen Sousa
 */
public class SaxVsmExample {

    public static void main(String[] args) {

        int WINDOW_SIZE = 28;
        int SAX_PAA = 7;
        int SAX_ALPHABET = 4;
        Double NORMALIZATION_THRESHOLD = 0.05;
        Params params = new Params(WINDOW_SIZE, SAX_PAA, SAX_ALPHABET,
                NORMALIZATION_THRESHOLD, NumerosityReductionStrategy.NONE);

        String train = "samples/SFAdatasets/CBF/CBF_TRAIN";
        String test = "samples/SFAdatasets/CBF/CBF_TEST";

        SAX.SAX_VSM(train, test, params);

    }

}
