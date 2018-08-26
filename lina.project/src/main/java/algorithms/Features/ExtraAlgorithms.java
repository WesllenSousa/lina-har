package algorithms.Features;

import datasets.timeseries.TimeSeries;
import java.util.LinkedList;

/**
 *
 * @author Wesllen Sousa
 */
public class ExtraAlgorithms {

    public static LinkedList<Float> gravity(LinkedList<LinkedList<Float>> vectors) {
        LinkedList<Float> meanVector = new LinkedList<>();
        for (LinkedList<Float> column : vectors) {
            meanVector.add(TimeFeature.Mean(column));
        }
        float norm = vectorNorma(meanVector);
        LinkedList<Float> newVector = new LinkedList<>();
        for (Float x : meanVector) {
            newVector.add(x / norm);
        }
        return newVector;
    }

    public static LinkedList<Float> gravity(TimeSeries[] data) {
        LinkedList<Float> meanVector = new LinkedList<>();
        for (int i = 0; i < data.length; i++) {
            meanVector.add(TimeFeature.Mean(data[i].getData()));
        }
        float norm = vectorNorma(meanVector);
        LinkedList<Float> newVector = new LinkedList<>();
        for (Float x : meanVector) {
            newVector.add(x / norm);
        }
        return newVector;
    }

    public static LinkedList<LinkedList<Float>> verticalProjection(LinkedList<Float> vertical, LinkedList<Float> gravity) {
        LinkedList<LinkedList<Float>> verticalProjection = new LinkedList<>();
        for (int i = 0; i < vertical.size(); i++) {
            LinkedList<Float> mult = new LinkedList<>();
            for (Float g : gravity) {
                mult.add(vertical.get(i) * g);
            }
            verticalProjection.add(mult);
        }
        return verticalProjection;
    }

    public static LinkedList<LinkedList<Float>> verticalProjection(TimeSeries vertical, LinkedList<Float> gravity) {
        LinkedList<LinkedList<Float>> verticalProjection = new LinkedList<>();
        for (int i = 0; i < vertical.getLength(); i++) {
            LinkedList<Float> mult = new LinkedList<>();
            for (Float g : gravity) {
                mult.add(((float) vertical.getData(i)) * g);
            }
            verticalProjection.add(mult);
        }
        return verticalProjection;
    }

    public static LinkedList<LinkedList<Float>> horizontalProjection(LinkedList<LinkedList<Float>> vectors,
            LinkedList<LinkedList<Float>> verticalProjection) {
        LinkedList<LinkedList<Float>> horizontalProjection = new LinkedList<>();
        for (int i = 0; i < vectors.get(0).size() - 1; i++) {
            LinkedList<Float> v = new LinkedList<>();
            for (int j = 0; j < vectors.size(); j++) {
                v.add(vectors.get(j).get(i) - verticalProjection.get(i).get(j));
            }
            horizontalProjection.add(v);
        }
        return horizontalProjection;
    }

    public static LinkedList<LinkedList<Float>> horizontalProjection(TimeSeries[] data,
            LinkedList<LinkedList<Float>> verticalProjection) {
        LinkedList<LinkedList<Float>> horizontalProjection = new LinkedList<>();
        for (int i = 0; i < data[0].getLength(); i++) {
            LinkedList<Float> v = new LinkedList<>();
            for (int j = 0; j < data.length; j++) {
                v.add(((float) data[j].getData(i)) - verticalProjection.get(i).get(j));
            }
            horizontalProjection.add(v);
        }
        return horizontalProjection;
    }

    public static float vectorNorma(LinkedList<Float> vector) {
        double sum = 0;
        for (Float value : vector) {
            sum += Math.pow(value, 2);
        }
        return (float) Math.sqrt(sum);
    }

    public static float vectorProductIntern(LinkedList<Float> v1, LinkedList<Float> v2) {
        float product = 0f;
        for (int i = 0; i < v1.size(); i++) {
            product += v1.get(i) * v2.get(i);
        }
        return product;
    }

    public static double vectorProductInternDouble(LinkedList<Double> v1, LinkedList<Float> v2) {
        float product = 0f;
        for (int i = 0; i < v1.size(); i++) {
            product += v1.get(i) * v2.get(i);
        }
        return product;
    }

    public static void quickSort(LinkedList<Float> vetor, int inicio, int fim) {
        if (inicio < fim) {
            int posicaoPivo = quickSortSeparator(vetor, inicio, fim);
            quickSort(vetor, inicio, posicaoPivo - 1);
            quickSort(vetor, posicaoPivo + 1, fim);
        }
    }

    private static int quickSortSeparator(LinkedList<Float> vetor, int inicio, int fim) {
        float pivo = vetor.get(inicio);
        int i = inicio + 1, f = fim;
        while (i <= f) {
            if (vetor.get(i) <= pivo) {
                i++;
            } else if (pivo < vetor.get(f)) {
                f--;
            } else {
                float troca = vetor.get(i);
                vetor.set(i, vetor.get(f));
                vetor.set(f, troca);
                i++;
                f--;
            }
        }
        vetor.set(inicio, vetor.get(f));
        vetor.set(f, pivo);
        return f;
    }

    public static boolean isPowerOfTwo(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("number: " + number);
        }
        return (number & -number) == number;
    }

    public static int log2(int bits) {
        if (bits == 0) {
            return 0;
        }
        return 31 - Integer.numberOfLeadingZeros(bits);
    }

}
