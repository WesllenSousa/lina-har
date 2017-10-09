package controle.features;

import java.util.LinkedList;

/**
 *
 * @author Wesllen Sousa
 */
public class FiltersSignalProcessing {

    public static LinkedList<String> GausianLowpass(LinkedList<Float> N) {
        LinkedList<String> newBuffer = new LinkedList<>();
        return newBuffer;
    }

    public static LinkedList<Float> ButterworthLowpass(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        LinkedList<Float> dest = new LinkedList<>();
        int NZEROS = 6;
        int NPOLES = 6;
        float GAIN = 2.936532839f;
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
                    + (-0.0837564796 * yv[0]) + (0.7052741145 * yv[1])
                    + (-2.5294949058 * yv[2]) + (4.9654152288 * yv[3])
                    + (-5.6586671659 * yv[4]) + (3.5794347983 * yv[5]));
            dest.add(yv[6]);
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("ButterworthLowpass: " + DateUtil.timeInterval(init, end));
        return dest;
    }

    public static LinkedList<Float> ButterworthLowpass2(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        LinkedList<Float> dest = new LinkedList<>();
        int NZEROS = 6;
        int NPOLES = 6;
        float GAIN = 1.165969038f;

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

    public static LinkedList<Float> ButterworthLowpass3(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        LinkedList<Float> dest = new LinkedList<>();
        int NZEROS = 6;
        int NPOLES = 6;
        float GAIN = 9.339780497f;

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
                    + (-0.1396600417 * yv[0]) + (1.1086708553 * yv[1])
                    + (-3.7230194289 * yv[2]) + (6.7850160254 * yv[3])
                    + (-7.0995038188 * yv[4]) + (4.0616439992 * yv[5]));
            dest.add(yv[6]);
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("ButterworthLowpass3: " + DateUtil.timeInterval(init, end));
        return dest;
    }

    public static LinkedList<Float> ButterworthLowpass4(LinkedList<Float> N) {
        //Long init = System.currentTimeMillis();

        LinkedList<Float> dest = new LinkedList<>();
        int NZEROS = 6;
        int NPOLES = 6;
        float GAIN = 4.004448900f;
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
                    + (-0.3774523864 * yv[0]) + (2.6310551285 * yv[1])
                    + (-7.6754745482 * yv[2]) + (11.9993158160 * yv[3])
                    + (-10.6070421840 * yv[4]) + (5.0294383514 * yv[5]));
            dest.add(yv[6]);
        }

        //Long end = System.currentTimeMillis();
        //System.out.println("ButterworthLowpass4: " + DateUtil.timeInterval(init, end));
        return dest;
    }

    public static LinkedList<String> Kalman(LinkedList<Float> N) {
        LinkedList<String> newBuffer = new LinkedList<>();
        return newBuffer;
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

}
