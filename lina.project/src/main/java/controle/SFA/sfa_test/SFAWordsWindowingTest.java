// Copyright (c) 2016 - Patrick Schäfer (patrick.schaefer@zib.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package controle.SFA.sfa_test;

import controle.SFA.transformation.SFA;
import controle.SFA.transformation.SFA.HistogramType;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader2;
import java.io.File;

import org.junit.Assert;

/**
 * Extracts windows from a time series and transforms each window using SFA.
 *
 */
public class SFAWordsWindowingTest {

    public static void main(String[] args) {
        int symbols = 4;
        int wordLength = 4;
        int windowLength = 64;
        boolean normMean = true;

        SFA sfa = new SFA(HistogramType.EQUI_DEPTH);

        // Load the train/test splits
        TimeSeries[] train = TimeSeriesLoader2.loadDataset(new File(System.getProperty("user.home") + "\\Lina\\Datasets\\Temp\\SFAdatasets\\CBF\\CBF_TRAIN"));
        TimeSeries[] test = TimeSeriesLoader2.loadDataset(new File(System.getProperty("user.home") + "\\Lina\\Datasets\\Temp\\SFAdatasets\\CBF\\CBF_TEST"));

        // train SFA representation
        sfa.fitWindowing(train, windowLength, wordLength, symbols, normMean, true);

        // bins
        sfa.printBins();

        // transform
        for (int q = 0; q < test.length; q++) {
            short[][] wordsQuery = sfa.transformWindowing(test[q]);
            System.out.print(q + "-th time Series " + "\t");
            Assert.assertTrue("SFA word queryLength does not match actual queryLength.",
                    wordsQuery.length == test[q].getLength() - windowLength + 1);

            for (short[] word : wordsQuery) {
                Assert.assertTrue("SFA word queryLength does not match actual queryLength.", word.length == wordLength);
                System.out.print(toSfaWord(word, symbols) + ";");
            }

            System.out.println("");
        }
    }

    public static String toSfaWord(short[] word, int symbols) {
        StringBuilder sfaWord = new StringBuilder();

        for (short c : word) {
            sfaWord.append((char) (Character.valueOf('a') + c));
            Assert.assertTrue("Wrong symbols used ", c < symbols && c >= 0);
        }

        return sfaWord.toString();
    }
}
