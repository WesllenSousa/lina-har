package algorithms.Features;

import java.util.LinkedList;

public class TimeFeature {

    public static float Sum(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float sum = 0;
        for (Float k : N) {
            sum += k;
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("Sum: " + DateUtil.timeInterval(init, end));
        return sum;
    }

    public static float Energy(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float energy = 0;
        for (Float k : N) {
            energy += Math.pow(k, 2);
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("Energy: " + DateUtil.timeInterval(init, end));
        return energy;
    }

    public static float EnergyLog(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float energylog = 0;
        for (Float k : N) {
            energylog += Math.log10(Math.pow(k, 2));
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("EnergyLog: " + DateUtil.timeInterval(init, end));
        return energylog;
    }

    public static float Power(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float sum = 0;
        for (Float k : N) {
            sum += Math.pow(k, 2);
        }
        float power = sum / N.size();

        //Long end = System.currentTimeMillis();
        //System.out.println("Power: " + DateUtil.timeInterval(init, end));
        return power;
    }

    public static float AbsoluteSum(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float absSum = 0;
        for (Float k : N) {
            absSum += Math.abs(k);
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("AbsoluteSum: " + DateUtil.timeInterval(init, end));
        return absSum;
    }

    public static float Mean(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float sum = 0;
        for (Float k : N) {
            sum += k;
        }
        float mean = sum / N.size();

        //Long end = System.currentTimeMillis();
        //System.out.println("Mean: " + DateUtil.timeInterval(init, end));
        return mean;
    }

    public static float Mean(double[] N) {
        //Long init = System.currentTimeMillis();

        float sum = 0;
        for (int i = 0; i < N.length; i++) {
            sum += N[i];
        }
        float mean = sum / N.length;

        //Long end = System.currentTimeMillis();
        //System.out.println("Mean: " + DateUtil.timeInterval(init, end));
        return mean;
    }

    public static float AbsoluteMean(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float sum = 0;
        for (Float k : N) {
            sum += Math.abs(k);
        }
        float absolutemean = sum / N.size();

        //Long end = System.currentTimeMillis();
        //System.out.println("AbsoluteMean: " + DateUtil.timeInterval(init, end));
        return absolutemean;
    }

    public static float SumSquareError(LinkedList<Float> N, float meanN) {
        //Long init = System.currentTimeMillis();

        double sumSquareError = 0;
        for (Float k : N) {
            sumSquareError += Math.pow(k - meanN, 2);
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("SumSquareError: " + DateUtil.timeInterval(init, end));
        return (float) sumSquareError;
    }

    public static float AbsoluteMeanDeviation(LinkedList<Float> N, float meanN) {
        //Long init = System.currentTimeMillis();

        double sum = 0;
        for (Float k : N) {
            sum += Math.abs(k - meanN);
        }
        float AbsoluteMeanDeviation = (float) sum / N.size();

        //Long end = System.currentTimeMillis();
        //System.out.println("AbsoluteMeanDeviation: " + DateUtil.timeInterval(init, end));
        return AbsoluteMeanDeviation;
    }

    public static float Variance(LinkedList<Float> N, float meanN) {
        //Long init = System.currentTimeMillis();

        double sum = 0;
        for (Float k : N) {
            sum += Math.pow(k - meanN, 2);
        }
        float variance = (float) sum / N.size();

        //Long end = System.currentTimeMillis();
        //System.out.println("Variance: " + DateUtil.timeInterval(init, end));
        return variance;
    }

    public static float Variance(double[] N, float meanN) {
        //Long init = System.currentTimeMillis();

        double sum = 0;
        for (int i = 0; i < N.length; i++) {
            sum += Math.pow(N[i] - meanN, 2);
        }
        float variance = (float) sum / N.length;

        //Long end = System.currentTimeMillis();
        //System.out.println("Variance: " + DateUtil.timeInterval(init, end));
        return variance;
    }

    public static float StandardDeviation(LinkedList<Float> N, float meanN) {
        //Long init = System.currentTimeMillis();

        float StandardDeviation = (float) Math.sqrt(Variance(N, meanN));

        //Long end = System.currentTimeMillis();
        //System.out.println("StandardDeviation: " + DateUtil.timeInterval(init, end));
        return StandardDeviation;
    }

    public static float PearsonCoefficientVariation(LinkedList<Float> N, float meanN) {
        //Long init = System.currentTimeMillis();

        float PearsonCoefficientVariation = StandardDeviation(N, meanN) / meanN;

        //Long end = System.currentTimeMillis();
        //System.out.println("PearsonCoefficientVariation: " + DateUtil.timeInterval(init, end));
        return PearsonCoefficientVariation;
    }

    public static float Min(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float min = Float.MAX_VALUE;
        for (Float k : N) {
            if (k < min) {
                min = k;
            }
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("Min: " + DateUtil.timeInterval(init, end));
        return min;
    }

    public static float Max(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float max = Float.MIN_VALUE;
        for (Float k : N) {
            if (k > max) {
                max = k;
            }
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("Max: " + DateUtil.timeInterval(init, end));
        return max;
    }

    public static float Amplitude(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float Amplitude = Math.abs(Max(N) - Min(N));

        //Long end = System.currentTimeMillis();
        //System.out.println("Amplitude: " + DateUtil.timeInterval(init, end));
        return Amplitude;
    }

    public static float PeakAmplitude(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float PeakAmplitude = Max(N) - Min(N);

        //Long end = System.currentTimeMillis();
        //System.out.println("PeakAmplitude: " + DateUtil.timeInterval(init, end));
        return PeakAmplitude;
    }

    public static float EuclideanNorm(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float sum = 0;
        for (Float k : N) {
            sum += Math.pow(k, 2);
        }
        float RootMeanSquare = (float) Math.sqrt(sum);

        //Long end = System.currentTimeMillis();
        //System.out.println("EuclideanNorm: " + DateUtil.timeInterval(init, end));
        return RootMeanSquare;
    }

    public static float RootMeanSquare(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float sum = 0;
        for (Float k : N) {
            sum += Math.pow(k, 2);
        }
        float RootMeanSquare = (float) Math.sqrt(sum / N.size());

        //Long end = System.currentTimeMillis();
        //System.out.println("RootMeanSquare: " + DateUtil.timeInterval(init, end));
        return RootMeanSquare;
    }

    public static float Correlation(LinkedList<Float> X, LinkedList<Float> Y) {
        //Long init = System.currentTimeMillis();

        float corr = 0, sumXY = 0, sumX = 0, sumY = 0, numerador = 0, denominador = 0;
        int n = Y.size();
        for (int k = 0; k < n; k++) {
            sumXY += X.get(k) * Y.get(k);
            sumX += Math.pow(X.get(k), 2);
            sumY += Math.pow(Y.get(k), 2);
        }
        numerador = (n * sumXY) - (Sum(X) * Sum(Y));
        denominador = (float) (Math.sqrt((n * sumX) - Math.pow(Sum(X), 2)) + Math.sqrt((n * sumY) - Math.pow(Sum(Y), 2)));
        corr = numerador / denominador;

        //Long end = System.currentTimeMillis();
        //System.out.println("Correlation: " + DateUtil.timeInterval(init, end));
        return corr;
    }

    public static float CrossCorrelation(LinkedList<Float> X, LinkedList<Float> Y, float meanX, float meanY) {
        //Long init = System.currentTimeMillis();

        float xcorr = 0, sumXY = 0, sumX = 0, sumY = 0, denominador = 0;
        int n = Y.size();
        for (int k = 0; k < n; k++) {
            sumXY += ((X.get(k) - meanX) * (Y.get(k) - meanY));
            sumX += Math.pow(X.get(k) - meanX, 2);
            sumY += Math.pow(Y.get(k) - meanY, 2);
        }
        denominador = (float) Math.sqrt(sumX * sumY);
        xcorr = sumXY / denominador;

        //Long end = System.currentTimeMillis();
        //System.out.println("CrossCorrelation: " + DateUtil.timeInterval(init, end));
        return xcorr;
    }

    public static float AutoCorrelation(LinkedList<Float> N, float meanX) {
        //Long init = System.currentTimeMillis();

        float autocorr = 0, sumN = 0, sumD = 0;
        int n = N.size();
        for (Float k : N) {
            sumN += (k - meanX) * ((k + 1) - meanX);
            sumD += Math.pow(k - meanX, 2);
        }
        autocorr = sumN / sumD;

        //Long end = System.currentTimeMillis();
        //System.out.println("AutoCorrelation: " + DateUtil.timeInterval(init, end));
        return autocorr;
    }

    public static float ZeroCrossingRation(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float zcr = 0;
        float sum = 0;
        for (Float k : N) {
            sum += Math.abs(Math.signum(k) - Math.signum(k - 1));
        }
        zcr = sum / (2 * N.size());

        //Long end = System.currentTimeMillis();
        //System.out.println("ZeroCrossingRation: " + DateUtil.timeInterval(init, end));
        return zcr;
    }

    public static float Quartile25(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        ExtraAlgorithms.quickSort(N, 0, N.size() - 1);
        int position = Math.round((25 / 100) * (N.size() + 1));

        //Long end = System.currentTimeMillis();
        //System.out.println("Quartile: " + DateUtil.timeInterval(init, end));
        return N.get(position);
    }

    public static float Quartile50(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        ExtraAlgorithms.quickSort(N, 0, N.size() - 1);
        int position = Math.round((50 / 100) * (N.size() + 1));

        //Long end = System.currentTimeMillis();
        //System.out.println("Quartile: " + DateUtil.timeInterval(init, end));
        return N.get(position);
    }

    public static float Quartile75(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        ExtraAlgorithms.quickSort(N, 0, N.size() - 1);
        int position = Math.round((75 / 100) * (N.size() + 1));

        //Long end = System.currentTimeMillis();
        //System.out.println("Quartile: " + DateUtil.timeInterval(init, end));
        return N.get(position);
    }

    public static float InterQuartileRange(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float InterQuartileRange = Quartile75(N) - Quartile25(N);

        //Long end = System.currentTimeMillis();
        //System.out.println("InterQuartileRange: " + DateUtil.timeInterval(init, end));
        return InterQuartileRange;
    }

    public static float Area(LinkedList<LinkedList<Float>> vectors) {
        //Long init = System.currentTimeMillis();

        float sum = 0f;
        float acum = 0f;
        for (int i = 0; i < vectors.get(0).size() - 1; i++) {
            sum = 0;
            for (LinkedList<Float> line : vectors) {
                sum += line.get(i);
            }
            acum += sum;
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("Area: " + DateUtil.timeInterval(init, end));
        return acum;
    }

    public static float AbsoluteArea(LinkedList<LinkedList<Float>> vectors) {
        //Long init = System.currentTimeMillis();

        float sum = 0f;
        float acum = 0f;
        for (int i = 0; i < vectors.get(0).size() - 1; i++) {
            sum = 0;
            for (LinkedList<Float> line : vectors) {
                sum += Math.abs(line.get(i));
            }
            acum += sum;
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("AbsoluteArea: " + DateUtil.timeInterval(init, end));
        return acum;
    }

    public static float SignalMagnitudeMean(LinkedList<LinkedList<Float>> vectors) {
        //Long init = System.currentTimeMillis();

        float sum = 0;
        float acum = 0;
        for (int i = 0; i < vectors.get(0).size() - 1; i++) {
            sum = 0;
            for (LinkedList<Float> line : vectors) {
                sum += Math.pow(line.get(i), 2);
            }
            acum += Math.sqrt(sum);
        }
        float SignalMagnitudeMean = acum / vectors.get(0).size();

        //Long end = System.currentTimeMillis();
        //System.out.println("SignalMagnitudeMean: " + DateUtil.timeInterval(init, end));
        return SignalMagnitudeMean;
    }

    public static float SignalMagnitudeArea(LinkedList<LinkedList<Float>> vectors) {
        //Long init = System.currentTimeMillis();

        float SignalMagnitudeArea = AbsoluteArea(vectors) / vectors.get(0).size();

        //Long end = System.currentTimeMillis();
        //System.out.println("SignalMagnitudeArea" + DateUtil.timeInterval(init, end));
        return SignalMagnitudeArea;
    }

    public static float AverageMagnitudeDifferenceFunction(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        float amdf = 0f, sum = 0f;
        int n = N.size();
        for (Float k : N) {
            sum += Math.abs(k - (k + 1));
        }
        amdf = (sum / n) / Max(N);

        //Long end = System.currentTimeMillis();
        //System.out.println("AverageMagnitudeDifferenceFunction: " + DateUtil.timeInterval(init, end));
        return amdf;
    }

    public static float Skewness(LinkedList<Float> N, float meanX) {
        //Long init = System.currentTimeMillis();

        float skew = 0f, sumN = 0f, sumD = 0f;
        int n = N.size();
        for (Float k : N) {
            sumN += Math.pow(k - meanX, 3);
            sumD += Math.pow(Math.pow(k - meanX, 2), 3 / 2);
        }
        skew = (sumN / n) / (sumD / n);

        //Long end = System.currentTimeMillis();
        //System.out.println("Skewness: " + DateUtil.timeInterval(init, end));
        return skew;
    }

    public static float Kurtosis(LinkedList<Float> N, float meanX) {
        //Long init = System.currentTimeMillis();

        float kurtosis = 0f, sumN = 0f, sumD = 0f;
        int n = N.size();
        for (Float k : N) {
            sumN += Math.pow(k - meanX, 4);
            sumD += Math.pow(Math.pow(k - meanX, 2), 3);
        }
        kurtosis = ((sumN / n) / (sumD / n)) - 3;

        //Long end = System.currentTimeMillis();
        //System.out.println("Kurtosis: " + DateUtil.timeInterval(init, end));
        return kurtosis;
    }

}
