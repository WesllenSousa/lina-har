// Copyright (c) 2016 - Patrick Schäfer (patrick.schaefer@zib.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package controle.SFA.test;

import constants.ConstDataset;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;
import controle.SFA.transformation.SFA;
import controle.SFA.transformation.SFA.HistogramType;
import java.io.IOException;

/**
 * Performs a 1-NN search
 *
 */
public class SFAWords {

    public static void main(String[] argv) throws IOException {

        // Load the train/test splits
        TimeSeries[] train = TimeSeriesLoader.loadHorizontalData(ConstDataset.DS_TEMP + "SFAdatasets/CBF/CBF_TRAIN", " ", true);
        TimeSeries[] test = TimeSeriesLoader.loadHorizontalData(ConstDataset.DS_TEMP + "SFAdatasets/CBF/CBF_TEST", " ", true);

        int symbols = 4;
        int wordLength = 8;
        boolean normMean = true;

        SFA sfa = new SFA(HistogramType.EQUI_DEPTH);

        // train SFA representation
        sfa.fitTransform(train, wordLength, symbols, normMean);

        // bins
        sfa.printBins();

        // transform
        int q = 0;
        //for (int q = 0; q < test.length; q++) {
        short[] wordQuery = sfa.transform(test[q]);
        System.out.println("Time Series " + q + "\t" + toSfaWord(wordQuery));
        //}
    }

    public static String toSfaWord(short[] word) {
        StringBuffer sfaWord = new StringBuffer();
        for (short c : word) {
            sfaWord.append((char) (Character.valueOf('a') + c));
        }
        return sfaWord.toString();
    }
}
