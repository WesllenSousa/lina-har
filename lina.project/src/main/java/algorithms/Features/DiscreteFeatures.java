package algorithms.Features;

import java.util.LinkedList;

/**
 *
 * @author Wesllen Sousa
 */
public class DiscreteFeatures {

    /**
     * Bins the data and then generates an array of percents: one representing
     * the percent of samples falling into each bin.
     *
     * @param N
     * @return an array of %'s
     */
    public static LinkedList<Double> BinnedDistribution(LinkedList<Float> N) {
        LinkedList<Double> binAvgs = new LinkedList<>();
        double binAvg = 0.0f;
        int[] binCounts = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        double[] bins = {-2.5, 0, 2.5, 5, 7.5, 10, 12.5, 15, 17.5, 20};

        // sorts samples into bins & counts them
        for (Float value : N) {
            if (value < bins[0]) { // anything smaller than the first level goes
                // in the first bin
                binCounts[0]++;
            } else if (value < bins[1]) {
                binCounts[1]++;
            } else if (value < bins[2]) {
                binCounts[2]++;
            } else if (value < bins[3]) {
                binCounts[3]++;
            } else if (value < bins[4]) {
                binCounts[4]++;
            } else if (value < bins[5]) {
                binCounts[5]++;
            } else if (value < bins[6]) {
                binCounts[6]++;
            } else if (value < bins[7]) {
                binCounts[7]++;
            } else if (value < bins[8]) {
                binCounts[8]++;
            } else { // anything larger than the 9th level goes in the 10th bin
                binCounts[9]++;
            }
        }
        // generates percent per bin statistic
        for (int i = 0; i < 10; i++) {
            binAvg = (double) binCounts[i] / (double) N.size();
            binAvgs.add(binAvg);
        }

        return binAvgs;
    }

}
