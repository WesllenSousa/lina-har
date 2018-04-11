// Copyright (c) 2016 - Patrick Schäfer (patrick.schaefer@zib.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package algorithms.SFA.sfa_test;

import algorithms.SFA.transformation.SFA;
import algorithms.SFA.transformation.SFA.HistogramType;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;
import datasets.timeseries.TimeSeriesLoader2;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests the SFA transformation
 *
 * This is more of a demonstration use case
 */
@RunWith(JUnit4.class)
public class SFAWordsVariableLengthTest {

  @Test
  public void testSFAWordsVarLength() throws IOException {

    int symbols = 8;
    int wordLength = 16;
    boolean normMean = true;

    SFA sfa = new SFA(HistogramType.EQUI_DEPTH);

    // Load the train/test splits
    ClassLoader classLoader = SFAWordsTest.class.getClassLoader();
    TimeSeries[] train = TimeSeriesLoader2.loadDataset(classLoader.getResource("datasets/univariate/CBF/CBF_TRAIN").getFile());
    TimeSeries[] test = TimeSeriesLoader2.loadDataset(classLoader.getResource("datasets/univariate/CBF/CBF_TEST").getFile());

    // train SFA representation using wordLength
    sfa.fitTransform(train, wordLength, symbols, normMean);

    // transform
    for (int q = 0; q < test.length; q++) {
      short[] wordQuery = sfa.transform(test[q]);

      // iterate variable lengths
      for (int length = 4; length <= wordLength; length*=2) {
        System.out.println("Time Series " + q + "\t" + length + "\t" + toSfaWord(Arrays.copyOf(wordQuery, length)));
      }
    }
  }

  public static String toSfaWord(short[] word) {
    StringBuilder sfaWord = new StringBuilder();
    for (short c : word) {
      sfaWord.append((char)(Character.valueOf('a') + c));
    }
    return sfaWord.toString();
  }
}
