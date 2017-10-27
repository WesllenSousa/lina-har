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
public class SFAWordsWindowing {

    public static void main(String[] argv) throws IOException {

        int symbols = 4;
        int wordLength = 8;
        int windowLength = 64;
        boolean normMean = true;

        SFA sfa = new SFA(HistogramType.EQUI_DEPTH);

        // Load the train/test splits
        TimeSeries[] train = TimeSeriesLoader.loadHorizontalData(ConstDataset.DS_TEMP + "SFAdatasets/CBF/CBF_TRAIN", " ", true);
        TimeSeries[] test = TimeSeriesLoader.loadHorizontalData(ConstDataset.DS_TEMP + "SFAdatasets/CBF/CBF_TEST", " ", true);

        // train SFA representation
        sfa.fitWindowing(train, windowLength, wordLength, symbols, normMean, true);

        // bins
        sfa.printBins();

        // transform
        int q = 0;
        //for (int q = 0; q < test.length; q++) {
            short[][] wordsQuery = sfa.transformWindowing(test[q], wordLength);
            System.out.print("Time Series " + q + "\t");
            for (short[] word : wordsQuery) {
                System.out.print(toSfaWord(word) + ";");
            }
            System.out.println("");
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
