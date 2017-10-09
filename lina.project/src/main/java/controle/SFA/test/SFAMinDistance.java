// Copyright (c) 2016 - Patrick Schäfer (patrick.schaefer@zib.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package controle.SFA.test;

import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;
import controle.SFA.transformation.SFA;
import controle.SFA.transformation.SFA.HistogramType;
import controle.SFA.transformation.SFADistance;
import java.io.IOException;

/**
 * Performs a 1-NN search
 *
 */
public class SFAMinDistance {

    public static void main(String[] argv) throws IOException {

        int symbols = 8;
        int wordLength = 16;
        boolean normMean = true;

        SFA sfa = new SFA(HistogramType.EQUI_DEPTH);
        SFADistance sfaDistance = new SFADistance(sfa);

        // Load the train/test splits
        TimeSeries[] train = TimeSeriesLoader.loadHorizontalData("samples/SFAdatasets/CBF/CBF_TRAIN", " ", true);
        TimeSeries[] test = TimeSeriesLoader.loadHorizontalData("samples/SFAdatasets/CBF/CBF_TEST", " ", true);

        // train SFA representation
        short[][] wordsTrain = sfa.fitTransform(train, wordLength, symbols, normMean);

        double minDistance = Double.MAX_VALUE;
        double accuracy = 0.0;
        int best = 0;

        // all queries
        for (int q = 0; q < test.length; q++) {
            TimeSeries query = test[q];
            // approximation
            double[] dftQuery = sfa.transformation.transform(query, wordLength);

            // quantization
            short[] wordQuery = sfa.quantization(dftQuery);

            // perform 1-NN search using the lower bounding distance
            for (int t = 0; t < train.length; t++) {
                double distance = sfaDistance.getDistance(wordsTrain[t], wordQuery, dftQuery, normMean, minDistance);

                // check the real distance, if lower bounding distance is smaller than best-so-far
                if (distance < minDistance) {
                    double realDistance = getEuclideanDistance(train[t], query, minDistance);
                    if (realDistance < minDistance) {
                        minDistance = realDistance;
                        best = t;
                    }
                    // plausability check
                    if (realDistance < distance) {
                        System.err.println("Lower bounding violated:\tSFA: " + distance + "\tED: " + realDistance);
                    }
                }
            }

            if (test[q].getLabel().equals(train[best].getLabel())) {
                accuracy++;
            }
        }

        System.out.println("Accuracy: " + (Math.round(100.0 * (accuracy / test.length)) / 100.0));
    }

    public static double getEuclideanDistance(TimeSeries t1, TimeSeries t2, double minValue) {
        double distance = 0;
        double[] t1Values = t1.getData();
        double[] t2Values = t2.getData();

        for (int i = 0; i < Math.min(t1.getLength(), t2.getLength()); i++) {
            double value = t1Values[i] - t2Values[i];
            distance += value * value;

            // pruning
            if (distance > minValue) {
                return Double.POSITIVE_INFINITY;
            }
        }

        return distance;
    }
}
