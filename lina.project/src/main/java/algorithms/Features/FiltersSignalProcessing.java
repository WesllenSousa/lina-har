package algorithms.Features;

import java.util.LinkedList;

/**
 *
 * @author Wesllen Sousa
 */
public class FiltersSignalProcessing {

    public static LinkedList<Float> ButterworthLowpass(LinkedList<Float> N, float GAIN) {
        //Long init = System.currentTimeMillis();

        LinkedList<Float> dest = new LinkedList<>();
        int NZEROS = 6;
        int NPOLES = 6;

        float[] xv = new float[NZEROS + 1];
        float[] yv = new float[NPOLES + 1];

        for (Float k : N) {
            xv[0] = xv[1];
            xv[1] = xv[2];
            xv[2] = xv[3];
            xv[3] = xv[4];
            xv[4] = xv[5];
            xv[5] = xv[6];
            xv[6] = k / GAIN;
            yv[0] = yv[1];
            yv[1] = yv[2];
            yv[2] = yv[3];
            yv[3] = yv[4];
            yv[4] = yv[5];
            yv[5] = yv[6];
            yv[6] = (float) ((xv[0] + xv[6]) + 6.0 * (xv[1] + xv[5]) + 15.0 * (xv[2] + xv[4])
                    + 20.0 * xv[3]
                    + (-0.2951724313 * yv[0]) + (2.1290387500 * yv[1])
                    + (-6.4411118810 * yv[2]) + (10.4690788930 * yv[3])
                    + (-9.6495177287 * yv[4]) + (4.7871354989 * yv[5]));
            dest.add(yv[6]);
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("ButterworthLowpass2: " + DateUtil.timeInterval(init, end));
        return dest;
    }

    public static double[] ButterworthLowpass(double[] N, float GAIN) {
        //Long init = System.currentTimeMillis();

        double[] dest = new double[N.length];
        int NZEROS = 6;
        int NPOLES = 6;

        double[] xv = new double[NZEROS + 1];
        double[] yv = new double[NPOLES + 1];

        for (int i = 0; i < N.length; i++) {
            xv[0] = xv[1];
            xv[1] = xv[2];
            xv[2] = xv[3];
            xv[3] = xv[4];
            xv[4] = xv[5];
            xv[5] = xv[6];
            xv[6] = N[i] / GAIN;
            yv[0] = yv[1];
            yv[1] = yv[2];
            yv[2] = yv[3];
            yv[3] = yv[4];
            yv[4] = yv[5];
            yv[5] = yv[6];
            yv[6] = (float) ((xv[0] + xv[6]) + 6.0 * (xv[1] + xv[5]) + 15.0 * (xv[2] + xv[4])
                    + 20.0 * xv[3]
                    + (-0.2951724313 * yv[0]) + (2.1290387500 * yv[1])
                    + (-6.4411118810 * yv[2]) + (10.4690788930 * yv[3])
                    + (-9.6495177287 * yv[4]) + (4.7871354989 * yv[5]));
            dest[i] = yv[6];
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("ButterworthLowpass2: " + DateUtil.timeInterval(init, end));
        return dest;
    }

    public static LinkedList<Float> MovingAverageFilter(LinkedList<Float> N, int windowMean) {
        //System.out.println("MovingAverageFilter: ");
        LinkedList<Float> newVector = new LinkedList<>();
        LinkedList<Float> meanVector = new LinkedList<>();
        int window = 0;
        for (Float value : N) {
            window++;
            meanVector.add(value);
            if (window == windowMean) {
                newVector.add(TimeFeature.Mean(meanVector));
                window = 0;
                meanVector.clear();
            }
        }
        return newVector;
    }

    //frequency sampleRate: 20Hz
    public static LinkedList<Float> SingleLowPass(LinkedList<Float> N, float sampleRate) {
        //Long init = System.currentTimeMillis();

        float frequency = 1;
        float fracFreq = frequency / sampleRate;
        float x = (float) Math.exp(-2 * Math.PI * fracFreq);
        float[] a = new float[]{1 - x};
        float[] b = new float[]{x};
        LinkedList<Float> LIST = filter(N, a, b);

        //Long end = System.currentTimeMillis();
        //System.out.println("SingleLowPass: " + DateUtil.timeInterval(init, end));
        return LIST;
    }

    public static double[] SingleLowPass(double[] N, float sampleRate) {
        //Long init = System.currentTimeMillis();

        float frequency = 1;
        float fracFreq = frequency / sampleRate;
        float x = (float) Math.exp(-2 * Math.PI * fracFreq);
        float[] a = new float[]{1 - x};
        float[] b = new float[]{x};
        double[] LIST = filter(N, a, b);

        //Long end = System.currentTimeMillis();
        //System.out.println("SingleLowPass: " + DateUtil.timeInterval(init, end));
        return LIST;
    }

    public static LinkedList<Float> FourStageLowPass(LinkedList<Float> N, float sampleRate) {
        //Long init = System.currentTimeMillis();

        //frequency = frequency > 10 ? frequency : 10;
        float frequency = 1;
        float freqFrac = frequency / sampleRate;
        float x = (float) Math.exp(-14.445 * freqFrac);
        float[] a = new float[]{(float) Math.pow(1 - x, 4)};
        float[] b = new float[]{4 * x, -6 * x * x, 4 * x * x * x, -x * x * x * x};
        LinkedList<Float> LIST = filter(N, a, b);

        //Long end = System.currentTimeMillis();
        //System.out.println("FourStageLowPass: " + DateUtil.timeInterval(init, end));
        return LIST;
    }

    public static double[] FourStageLowPass(double[] N, float sampleRate) {
        //Long init = System.currentTimeMillis();

        //frequency = frequency > 10 ? frequency : 10;
        float frequency = 1;
        float freqFrac = frequency / sampleRate;
        float x = (float) Math.exp(-14.445 * freqFrac);
        float[] a = new float[]{(float) Math.pow(1 - x, 4)};
        float[] b = new float[]{4 * x, -6 * x * x, 4 * x * x * x, -x * x * x * x};
        double[] LIST = filter(N, a, b);

        //Long end = System.currentTimeMillis();
        //System.out.println("FourStageLowPass: " + DateUtil.timeInterval(init, end));
        return LIST;
    }

    public static LinkedList<Float> BandPass(LinkedList<Float> N, float sampleRate) {
        //Long init = System.currentTimeMillis();

        float frequency = 1;
        float bandWidth = (sampleRate * 30) / 100; //30% da taxa de frequencia
        float bw = bandWidth / sampleRate;
        float R = 1 - 3 * bw;
        float fracFreq = frequency / sampleRate;
        float T = 2 * (float) Math.cos(2 * Math.PI * fracFreq);
        float K = (1 - R * T + R * R) / (2 - T);
        float[] a = new float[]{1 - K, (K - R) * T, R * R - K};
        float[] b = new float[]{R * T, -R * R};
        LinkedList<Float> LIST = filter(N, a, b);

        //Long end = System.currentTimeMillis();
        //System.out.println("BandPass: " + DateUtil.timeInterval(init, end));
        return LIST;
    }

    public static double[] BandPass(double[] N, float sampleRate) {
        //Long init = System.currentTimeMillis();

        float frequency = 1;
        float bandWidth = (sampleRate * 30) / 100; //30% da taxa de frequencia
        float bw = bandWidth / sampleRate;
        float R = 1 - 3 * bw;
        float fracFreq = frequency / sampleRate;
        float T = 2 * (float) Math.cos(2 * Math.PI * fracFreq);
        float K = (1 - R * T + R * R) / (2 - T);
        float[] a = new float[]{1 - K, (K - R) * T, R * R - K};
        float[] b = new float[]{R * T, -R * R};
        double[] LIST = filter(N, a, b);

        //Long end = System.currentTimeMillis();
        //System.out.println("BandPass: " + DateUtil.timeInterval(init, end));
        return LIST;
    }

    public static LinkedList<Float> HighPass(LinkedList<Float> N, float sampleRate) {
        //Long init = System.currentTimeMillis();

        float frequency = 1;
        float fracFreq = frequency / sampleRate;
        float x = (float) Math.exp(-2 * Math.PI * fracFreq);
        float[] a = new float[]{(1 + x) / 2, -(1 + x) / 2};
        float[] b = new float[]{x};
        LinkedList<Float> LIST = filter(N, a, b);

        //Long end = System.currentTimeMillis();
        //System.out.println("HighPass: " + DateUtil.timeInterval(init, end));
        return LIST;
    }

    public static double[] HighPass(double[] N, float sampleRate) {
        //Long init = System.currentTimeMillis();

        float frequency = 1;
        float fracFreq = frequency / sampleRate;
        float x = (float) Math.exp(-2 * Math.PI * fracFreq);
        float[] a = new float[]{(1 + x) / 2, -(1 + x) / 2};
        float[] b = new float[]{x};
        double[] LIST = filter(N, a, b);

        //Long end = System.currentTimeMillis();
        //System.out.println("HighPass: " + DateUtil.timeInterval(init, end));
        return LIST;
    }

    private static LinkedList<Float> filter(LinkedList<Float> N, float[] a, float[] b) {
        float[] in = new float[a.length];
        float[] out = new float[b.length];
        for (int i = 0; i < N.size(); i++) {
            //shift the in array
            System.arraycopy(in, 0, in, 1, in.length - 1);
            in[0] = N.get(i);
            //calculate y based on a and b coefficients
            //and in and out.
            float y = 0;
            for (int j = 0; j < a.length; j++) {
                y += a[j] * in[j];
            }
            for (int j = 0; j < b.length; j++) {
                y += b[j] * out[j];
            }
            //shift the out array
            System.arraycopy(out, 0, out, 1, out.length - 1);
            out[0] = y;
            N.set(i, y);
        }
        return N;
    }

    private static double[] filter(double[] N, float[] a, float[] b) {
        float[] in = new float[a.length];
        float[] out = new float[b.length];
        for (int i = 0; i < N.length; i++) {
            //shift the in array
            System.arraycopy(in, 0, in, 1, in.length - 1);
            in[0] = (float) N[i];
            //calculate y based on a and b coefficients
            //and in and out.
            float y = 0;
            for (int j = 0; j < a.length; j++) {
                y += a[j] * in[j];
            }
            for (int j = 0; j < b.length; j++) {
                y += b[j] * out[j];
            }
            //shift the out array
            System.arraycopy(out, 0, out, 1, out.length - 1);
            out[0] = y;
            N[i] = y;
        }
        return N;
    }

    public static double[] SingleExponential(double[] data, double alpha) {
        double[] y = new double[data.length];
        y[0] = 0;
        y[1] = data[0];
        int i = 2;
        for (i = 2; i < data.length; i++) {
            y[i] = alpha * data[i - 1] + (1 - alpha) * y[i - 1];
        }
        return y;
    }

}
