package controle.features;

import java.util.LinkedList;

/**
 *
 * @author Wesllen Sousa
 */
public class FrequencyFeatures {

    public static float SpectralCentroid(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float sCentroid = 0f, sumN = 0f, sumD = 0f;
        int n = N.size();
        for (Float k : N) {
            sumN += k * n;
            sumD += k;
        }
        sCentroid = sumN / sumD;

        //Long end = System.currentTimeMillis();
        //System.out.println("SpectralCentroid: " + DateUtil.timeInterval(init, end));
        return sCentroid;
    }

    public static float SpectralEnergy(LinkedList<Float> N) {
        //System.out.println("SpectralEnergy -> ");
        return TimeFeature.Energy(N);
    }

    public static float SpectralEnergyNormalized(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float sEnergyNorm = 0, sum = 0;
        int n = N.size();
        for (Float k : N) {
            sum += k;
        }
        for (Float k : N) {
            sEnergyNorm += Math.pow(k / sum, 2);
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("SpectralEnergyNormalized: " + DateUtil.timeInterval(init, end));
        return sEnergyNorm;
    }

    public static float SpectralPower(LinkedList<Float> N) {
        //System.out.println("SpectralPower -> ");
        return SpectralEnergy(N) / N.size();
    }

    public static float SpectralEntropy(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float sum = 0, fn = 0, log = 0;
        float sumEntropy = 0, sEntropy = 0;
        for (Float k : N) {
            sum += k;
        }
        for (Float k : N) {
            fn = (float) (k / sum);
            log = (float) Math.log(fn);
            sumEntropy += fn * log;
        }
        sEntropy = sumEntropy * -1;

        //Long end = System.currentTimeMillis();
        //System.out.println("SpectralEntropy: " + DateUtil.timeInterval(init, end));
        return sEntropy;
    }

    public static float CoefficientsSum(LinkedList<Float> N) {
        //System.out.println("CoefficientsSum -> ");
        //int cont = (N.size() * 20) / 100;
        int cont = 10;

        LinkedList<Float> Nnew = new LinkedList<>();
        for (int i = 0; i < cont; i++) {
            Nnew.add(N.get(i));
        }

        return TimeFeature.Sum(Nnew);
    }

    public static float DComponents(LinkedList<Float> N) {
        //System.out.println("DComponents -> ");
        //return TimeFeature.Mean(N);
        return N.getFirst();
    }

    public static float DominantFrequency(LinkedList<Float> N) {
        return 0f;
    }

    /**
     * generates time between peaks for one axis over a 10 second tuple
     *
     * @param N
     * @return average time between peaks
     */
    public static float PeakTime(LinkedList<Float> N) {
        long[] t = new long[N.size()];
        for (int i = 0; i < N.size(); i++) {
            t[i] = i;
        }

        /**
         * the maximum number of peak values is half of the total values plus 1
         * because every other value could be a peak
         */
        int maxPeaks = (N.size() / 2) + 1;

        double[] allPeaks = new double[maxPeaks];
        long[] peakTimes = new long[maxPeaks], highTimes = new long[maxPeaks];

        double tmp1 = N.get(0), tmp2 = N.get(1), tmp3 = N.get(2);
        int highPeakCount = 0;
        float favr = 0;
        double highest = 0, threshold = 0.9, avr = 0;

        // runs through array and grabs peaks
        for (int i = 3, j = 0; i < (N.size() - 2); i++) {

            if (tmp2 > tmp1 && tmp2 > tmp3) {
                allPeaks[j] = tmp2;
                peakTimes[j] = t[i];
                j++;
                if (tmp2 > highest) {// remember the highest peak
                    highest = tmp2;
                }
            }

            tmp1 = tmp2;
            tmp2 = tmp3;
            tmp3 = N.get(i + 1);
        }

        // count peaks above threshold and store their timestamps
        for (int i = 0; i < allPeaks.length; i++) {
            if (allPeaks[i] > threshold * highest) {
                highTimes[highPeakCount] = peakTimes[i];
                highPeakCount++;
            }
        }

        // if not enough peaks are found, the loop executes
        while (highPeakCount < 3 && threshold > 0) {
            // lower the threshold incrementally until enough peaks are found
            threshold -= .05;
            highPeakCount = 0; // reset to avoid a double count

            for (int i = 0; i < allPeaks.length; i++) {
                if (allPeaks[i] > threshold * highest) {
                    // if the loop executes, it will write over the old values
                    highTimes[highPeakCount] = peakTimes[i];
                    highPeakCount++;
                }
            }
        }

        // calcs the actual average time between given peaks
        if (highPeakCount < 3) {
            avr = 0;
        } else {
            for (int i = 0; i < (highPeakCount - 1); i++) {
                // for now avr is the sum of each difference
                avr += (highTimes[i + 1] - highTimes[i]);
            }
            // avr becomes the average of those differences
            avr = avr / (highPeakCount - 1);
        }
        favr = (float) avr;
        return favr;
    }

}
