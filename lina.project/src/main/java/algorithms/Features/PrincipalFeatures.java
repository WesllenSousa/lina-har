package algorithms.Features;

import datasets.timeseries.TimeSeries;
import java.util.LinkedList;

/**
 *
 * @author Wesllen Sousa
 */
public class PrincipalFeatures {

    public static LinkedList<Float> Magnitude(LinkedList<LinkedList<Float>> vectors) {
        //Long init = System.currentTimeMillis();

        LinkedList<Float> magnitude = new LinkedList<>();
        for (int i = 0; i < vectors.get(0).size() - 1; i++) {
            double sum = 0;
            for (LinkedList<Float> column : vectors) {
                sum += Math.pow(column.get(i), 2);
            }
            magnitude.add((float) Math.sqrt(sum));
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("Magnitude: " + DateUtil.timeInterval(init, end));
        return magnitude;
    }

    public static LinkedList<Float> VerticalAxis(LinkedList<LinkedList<Float>> vectors, LinkedList<Float> gravity) {
        //Long init = System.currentTimeMillis();

        LinkedList<Float> vertical = new LinkedList<>();
        for (int i = 0; i < vectors.get(0).size() - 1; i++) {
            LinkedList<Float> a = new LinkedList<>();
            for (LinkedList<Float> column : vectors) {
                a.add(column.get(i));
            }
            vertical.add(ExtraAlgorithms.vectorProductIntern(a, gravity));
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("VerticalAxis: " + DateUtil.timeInterval(init, end));
        return vertical;
    }

    public static TimeSeries VerticalAxis(TimeSeries[] data, LinkedList<Float> gravity) {
        //Long init = System.currentTimeMillis();

        double[] vertical = new double[data[0].getLength()];
        for (int i = 0; i < data[0].getLength(); i++) {
            LinkedList<Double> a = new LinkedList<>();
            for (TimeSeries column : data) {
                a.add(column.getData(i));
            }
            vertical[i] = ExtraAlgorithms.vectorProductInternDouble(a, gravity);
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("VerticalAxis: " + DateUtil.timeInterval(init, end));
        TimeSeries timeSeries = new TimeSeries(vertical);
        return timeSeries;
    }

    public static LinkedList<Float> HorizontalAxis(LinkedList<LinkedList<Float>> vectors, LinkedList<Float> gravity) {
        //Long init = System.currentTimeMillis();

        LinkedList<Float> vertical = VerticalAxis(vectors, gravity);
        LinkedList<LinkedList<Float>> verticalProjection = ExtraAlgorithms.verticalProjection(vertical, gravity);
        LinkedList<LinkedList<Float>> horizontalProjection = ExtraAlgorithms.horizontalProjection(vectors, verticalProjection);
        LinkedList<Float> horizontal = new LinkedList<>();
        for (LinkedList<Float> horiz : horizontalProjection) {
            horizontal.add(ExtraAlgorithms.vectorNorma(horiz));
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("HorizontalAxis: " + DateUtil.timeInterval(init, end));
        return horizontal;
    }

    public static TimeSeries HorizontalAxis(TimeSeries[] data, LinkedList<Float> gravity) {
        //Long init = System.currentTimeMillis();

        TimeSeries vertical = VerticalAxis(data, gravity);
        LinkedList<LinkedList<Float>> verticalProjection = ExtraAlgorithms.verticalProjection(vertical, gravity);
        LinkedList<LinkedList<Float>> horizontalProjection = ExtraAlgorithms.horizontalProjection(data, verticalProjection);
        double[] horizontal = new double[data[0].getLength()];
        int i = 0;
        for (LinkedList<Float> horiz : horizontalProjection) {
            horizontal[i] = ExtraAlgorithms.vectorNorma(horiz);
            i++;
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("HorizontalAxis: " + DateUtil.timeInterval(init, end));
        TimeSeries timeSeries = new TimeSeries(horizontal);
        return timeSeries;
    }

    public static LinkedList<Float> FastFourierTransform(LinkedList<Float> N, boolean fwd) {
        //Long init = System.currentTimeMillis();

        int n = Math.round((N.size() - 1) / 2);
        double omega, tempr, tempi, fscale, sin, cos;
        double xtemp, xr, xi;
        int j, M;
        float k;
        j = 0;
        for (int i = 0; i < n - 1; i++) {
            if (i < j) {
                tempr = N.get(2 * i);
                tempi = N.get(2 * i + 1);
                N.set(2 * i, N.get(2 * j));
                N.set(2 * i + 1, N.get(2 * j + 1));
                N.set(2 * j, (float) tempr);
                N.set(2 * j + 1, (float) tempi);
            }
            k = n / 2;
            while (k <= j) {
                j -= k;
                k = k / 2;
            }
            j += k;
        }
        if (fwd) {
            fscale = 1.0;
        } else {
            fscale = -1.0;
        }
        M = 2;
        while (M < 2 * n) {
            omega = fscale * 2.0 * Math.PI / M;
            sin = Math.sin(omega);
            cos = Math.cos(omega) - 1.0;
            xr = 1.0;
            xi = 0.0;
            for (int m = 0; m < M - 1; m += 2) {
                for (int i = m; i < 2 * n; i += M * 2) {
                    tempr = xr * N.get(i) - xi * N.get(i + 1);
                    tempi = xr * N.get(i + 1) + xi * N.get(i);
                    N.set(i, N.get(i) - (float) tempr);
                    N.set(i + 1, N.get(i + 1) - (float) tempi);
                    N.set(i, N.get(i) + (float) tempr);
                    N.set(i + 1, N.get(i + 1) + (float) tempi);
                }
                xtemp = xr;
                xr = xr + xr * cos - xi * sin;
                xi = xi + xtemp * sin + xi * cos;
            }
            M *= 2;
        }
        if (fwd) {
            for (int i = 0; i < n; i++) {
                N.set(2 * i, N.get(2 * i) / n);
                N.set(2 * i + 1, N.get(2 * i + 1) / n);
            }
        }
        LinkedList<Float> newBuffer = new LinkedList<>();
        for (int w = 0; w < N.size(); w++) {
            newBuffer.add(N.get(w));
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("FastFourierTransform: " + DateUtil.timeInterval(init, end));
        return newBuffer;
    }

    public static LinkedList<Float> HaarWaveletTransform(LinkedList<Float> N, boolean preserveEnergy) {
        //Long init = System.currentTimeMillis();

        int m = N.size();
        float sqrtTwo = (float) Math.sqrt(2.0);
        assert ExtraAlgorithms.isPowerOfTwo(m);
        int n = ExtraAlgorithms.log2(m);
        int j = 2;
        int i = 1;
        for (int l = 0; l < n; l++) {
            m = m / 2;
            for (int k = 0; k < m; k++) {
                float a = (N.get(j * k) + N.get(j * k + i)) / 2.0f;
                float c = (N.get(j * k) - N.get(j * k + i)) / 2.0f;
                if (preserveEnergy) {
                    a = a / sqrtTwo;
                    c = c / sqrtTwo;
                }
                N.set(j * k, a);
                N.set(j * k + i, c);
            }
            i = j;
            j = j * 2;
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("HaarWaveletTransform: " + DateUtil.timeInterval(init, end));
        return N;
    }

}
