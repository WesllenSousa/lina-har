// Copyright (c) 2016 - Patrick Schäfer (patrick.schaefer@zib.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package controle.SFA.multDimension;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import com.carrotsearch.hppc.*;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.carrotsearch.hppc.cursors.FloatCursor;
import com.carrotsearch.hppc.cursors.IntCursor;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesMD;

public abstract class ClassifierMD {

    public TimeSeriesMD[] testSamples;
    public TimeSeriesMD[] trainSamples;
    public static int threads = 4;
    public static boolean DEBUG = true;

    public static boolean[] NORMALIZATION = new boolean[]{true, false};

    public static boolean ENSEMBLE_WEIGHTS = true;

    public AtomicInteger correctTraining = new AtomicInteger(0);

    protected int[][] testIndices;
    protected int[][] trainIndices;
    public static int folds = 10;
    public static String resultString = "";

    protected static int maxWordLength = 16; // 12
    protected static int minWordLenth = 6;  // 4
    protected static int maxSymbol = 4;  // 8
    protected static int maxWindowLength = 250;
    protected static int minWindowLength = 10;

    // Blocks for parallel execution
    public final int BLOCKS = 8;

    static {
        Runtime runtime = Runtime.getRuntime();
        if (runtime.availableProcessors() <= 4) {
            threads = runtime.availableProcessors() - 1;
        } else {
            threads = runtime.availableProcessors();
        }
    }

    public ClassifierMD(TimeSeriesMD[] train, TimeSeriesMD[] test) throws IOException {
        this.trainSamples = train;
        this.testSamples = test;
    }

    public abstract Score eval() throws IOException;

    public static class Words {

        public static int binlog(int bits) {
            int log = 0;
            if ((bits & 0xffff0000) != 0) {
                bits >>>= 16;
                log = 16;
            }
            if (bits >= 256) {
                bits >>>= 8;
                log += 8;
            }
            if (bits >= 16) {
                bits >>>= 4;
                log += 4;
            }
            if (bits >= 4) {
                bits >>>= 2;
                log += 2;
            }
            return log + (bits >>> 1);
        }

        public static long createWord(short[] words, int features, byte usedBits) {
            return fromByteArrayOne(words, features, usedBits);
        }

        /**
         * Returns a long containing the values in bytes.
         *
         * @param bytes
         * @return
         */
        public static long fromByteArrayOne(short[] bytes, int to, byte usedBits) {
            int shortsPerLong = 60 / usedBits;
            to = Math.min(bytes.length, to);

            long bits = 0;
            int start = 0;
            long shiftOffset = 1;
            for (int i = start, end = Math.min(to, shortsPerLong + start); i < end; i++) {
                for (int j = 0, shift = 1; j < usedBits; j++, shift <<= 1) {
                    if ((bytes[i] & shift) != 0) {
                        bits |= shiftOffset;
                    }
                    shiftOffset <<= 1;
                }
            }

            return bits;
        }
    }

    public static class Score implements Comparable<Score> {

        public String name;
        public double training;
        public double testing;
        public boolean normed;
        public int windowLength;

        public Score(
                String name,
                double testing,
                double training,
                boolean normed,
                int windowLength
        ) {
            this.name = name;
            this.training = training;
            this.testing = testing;
            this.normed = normed;
            this.windowLength = windowLength;
        }

        @Override
        public String toString() {
            return "Name: " + this.name + "\n"
                    + "Training: " + this.training + "\n"
                    + "Test: " + this.testing + "\n"
                    + "> Window: " + windowLength + "\n"
                    + "> Normed: " + normed + "\n"
                    + "> Symbol: " + maxSymbol + "\n"
                    + "> Max word length: " + maxWordLength + ", Min word length: " + minWordLenth + "\n"
                    + "> Max window length: " + maxWindowLength + ", Min window length: " + minWindowLength + "\n"
                    + "Confusion Matrix: \n" + resultString;
        }

        @Override
        public int compareTo(Score bestScore) {
            if (this.training > bestScore.training
                    || this.training == bestScore.training
                    && this.windowLength > bestScore.windowLength) {
                return 1;
            }
            return -1;
        }

        public void clear() {
            this.testing = 0;
            this.training = 0;
        }
    }

    public static class Predictions {

        public String[] labels;
        public AtomicInteger correct;

        public Predictions(String[] labels, int bestCorrect) {
            this.labels = labels;
            this.correct = new AtomicInteger(bestCorrect);
        }
    }

    public static void outputResult(int correct, long time, int testSize) {
        double error = formatError(correct, testSize);
        //String errorStr = MessageFormat.format("{0,number,#.##%}", error);
        String correctStr = MessageFormat.format("{0,number,#.##%}", 1 - error);

        System.out.print("Correct:\t");
        System.out.print("" + correctStr + "");
        resultString += "Correct: " + correctStr + "\n";
        System.out.println("\tTime: \t" + (System.currentTimeMillis() - time) / 1000.0 + " s");
        resultString += "Time: " + (System.currentTimeMillis() - time) / 1000.0 + " s\n";
    }

    public static void outputConfusionMatrix(ObjectObjectHashMap<String, ObjectLongHashMap> matrix) {
        try {
            int rows = matrix.size();
            List<String> labels = new ArrayList(rows);

            for (ObjectCursor<String> actual_class : matrix.keys()) {
                labels.add(actual_class.value);

            }
            Collections.sort(labels, ALPHABETICAL_ORDER);

            int columns = rows;
            String str = "\t";
            String str2 = "\t";
            for (String l : labels) {
                str += l + "\t";
                str2 += '-' + "\t";
            }
            System.out.println(str + "");
            System.out.println(str2 + "");
            resultString += str + "\n" + str2 + "\n";
            str = "|\t";
            resultString += str;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    str += matrix.get(labels.get(i)).get(labels.get(j)) + "\t";

                }
                str = labels.get(i) + str;
                System.out.println(str + "|");
                resultString += str + "|\n";
                str = "|\t";
                resultString += str;
            }

        } catch (Exception e) {
            System.out.println("Matrix is empty!!");
        }
    }

    private static final Comparator<String> ALPHABETICAL_ORDER = new Comparator<String>() {
        @Override
        public int compare(String str1, String str2) {
            int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
            if (res == 0) {
                res = str1.compareTo(str2);
            }
            return res;
        }
    };

    public static double formatError(int correct, int testSize) {
        double error = Math.round(1000 * (testSize - correct) / (double) (testSize)) / 1000.0;
        return error;
    }

    @SuppressWarnings("unchecked")
    public static TimeSeries[][] getStratifiedSplits(
            TimeSeries[] samples,
            int splits) {

        Map<String, LinkedList<Integer>> elements = splitByLabel(samples);

        // pick samples
        double trainTestSplit = 1.0 / (double) splits;
        ArrayList<TimeSeries>[] sets = new ArrayList[splits];
        for (int s = 0; s < splits; s++) {
            sets[s] = new ArrayList<>();
            for (Entry<String, LinkedList<Integer>> data : elements.entrySet()) {
                int count = (int) (data.getValue().size() * trainTestSplit);
                int i = 0;
                while (!data.getValue().isEmpty()
                        && i <= count) {
                    sets[s].add(samples[data.getValue().remove()]);
                    i++;
                }
            }
        }

        ArrayList<TimeSeries> testSet = new ArrayList<>();
        for (List<Integer> indices : elements.values()) {
            for (int index : indices) {
                testSet.add(samples[index]);
            }
        }

        TimeSeries[][] data = new TimeSeries[splits][];
        for (int s = 0; s < splits; s++) {
            data[s] = sets[s].toArray(new TimeSeries[]{});
        }

        return data;
    }

    public static Map<String, LinkedList<Integer>> splitByLabel(TimeSeries[] samples) {
        Map<String, LinkedList<Integer>> elements = new HashMap<>();

        for (int i = 0; i < samples.length; i++) {
            String label = samples[i].getLabel();
            if (!label.trim().isEmpty()) {
                LinkedList<Integer> sameLabel = elements.get(label);
                if (sameLabel == null) {
                    sameLabel = new LinkedList<>();
                    elements.put(label, sameLabel);
                }
                sameLabel.add(i);
            }
        }
        return elements;
    }

    public static class Pair<E, T> {

        public E key;
        public T value;

        public Pair(E e, T t) {
            this.key = e;
            this.value = t;
        }

        public static <E, T> Pair<E, T> create(E e, T t) {
            return new Pair<>(e, t);
        }

        @Override
        public int hashCode() {
            return this.key.hashCode();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            return this.key.equals(((Pair<E, T>) obj).key);
        }
    }

    public int score(
            final String name,
            final TimeSeriesMD[] samples,
            long startTime,
            final List<Pair<String, Double>>[] labels,
            final List<Integer> currentWindowLengths) {
        HashSet<String> uniqueLabels = uniqueClassLabels(samples); // OLHO somente as labels de TEST
        ObjectObjectHashMap<String, ObjectLongHashMap> confusionMatrix = new ObjectObjectHashMap<>(uniqueLabels.size());
        initConfusionMatrix(confusionMatrix, uniqueLabels);
        int correctTesting = 0;
        for (int i = 0; i < labels.length; i++) {

            String maxLabel = "";
            double maxCount = 0.0;

            HashMap<String, Double> counts = new HashMap<>();

            for (Pair<String, Double> k : labels[i]) {
                if (k != null && k.key != null) {
                    String s = k.key;
                    Double count = counts.get(s);
                    double increment = ENSEMBLE_WEIGHTS ? k.value : 1;
                    count = (count == null) ? increment : count + increment;
                    counts.put(s, count);
                    if (maxCount < count
                            || maxCount == count && maxLabel.compareTo(s) < 0) {
                        maxCount = count;
                        maxLabel = s;
                    }
                }
            }
            if (samples[i].getLabel().equals(maxLabel)) {
                correctTesting++;
                confusionMatrix.get(maxLabel).putOrAdd(maxLabel, (long) 1, (long) 1);
            } else {
                confusionMatrix.get(samples[i].getLabel()).putOrAdd(maxLabel, (long) 1, (long) 1);
            }

        }

        if (DEBUG) {
            System.out.println(name + " Testing with " + currentWindowLengths.size() + " models:\t");
            resultString += name + " Testing with " + currentWindowLengths.size() + " models:\t\n";
            outputResult(correctTesting, startTime, samples.length);
            outputConfusionMatrix(confusionMatrix);
        }
        return correctTesting;
    }

    protected Integer[] getWindowsBetween(int minWindowLength, int maxWindowLength) {
        List<Integer> windows = new ArrayList<>();
        for (int windowLength = maxWindowLength; windowLength >= minWindowLength; windowLength--) {
            windows.add(windowLength);
        }
        return windows.toArray(new Integer[]{});
    }

    public int getMax(TimeSeriesMD[] samples, int MAX_WINDOW_SIZE) {
        int max = MAX_WINDOW_SIZE;
        for (TimeSeriesMD ts : samples) {
            max = Math.min(ts.getLength(), max);
        }
        return max;
    }

    protected static HashSet<String> uniqueClassLabels(TimeSeriesMD[] ts) {
        HashSet<String> labels = new HashSet<>();
        for (TimeSeriesMD t : ts) {
            labels.add(t.getLabel());
        }
        return labels;
    }

    protected static double magnitude(FloatContainer values) {
        double mag = 0.0D;
        for (FloatCursor value : values) {
            mag = mag + value.value * value.value;
        }
        return Math.sqrt(mag);
    }

    protected static int[] createIndices(int length) {
        int[] indices = new int[length];
        for (int i = 0; i < length; i++) {
            indices[i] = i;
        }
        return indices;
    }

    protected void generateIndices() {
        IntArrayList[] sets = getStratifiedTrainTestSplitIndices(this.trainSamples, folds);
        this.testIndices = new int[folds][];
        this.trainIndices = new int[folds][];
        for (int s = 0; s < folds; s++) {
            this.testIndices[s] = convertToInt(sets[s]);
            this.trainIndices[s] = convertToInt(sets, s);
        }
    }

    protected IntArrayList[] getStratifiedTrainTestSplitIndices(
            TimeSeriesMD[] samples,
            int splits) {

        HashMap<String, IntArrayDeque> elements = new HashMap<>();

        for (int i = 0; i < samples.length; i++) {
            String label = samples[i].getLabel();
            IntArrayDeque sameLabel = elements.get(label);
            if (sameLabel == null) {
                sameLabel = new IntArrayDeque();
                elements.put(label, sameLabel);
            }
            sameLabel.addLast(i);
        }

        // pick samples
        IntArrayList[] sets = new IntArrayList[splits];
        for (int i = 0; i < splits; i++) {
            sets[i] = new IntArrayList();
        }

        // all but one
        for (Entry<String, IntArrayDeque> data : elements.entrySet()) {
            IntArrayDeque d = data.getValue();
            separate:
            while (true) {
                for (int s = 0; s < splits; s++) {
                    if (!d.isEmpty()) {
                        int dd = d.removeFirst();
                        sets[s].add(dd);
                    } else {
                        break separate;
                    }
                }
            }
        }

        return sets;
    }

    protected static int[] convertToInt(IntArrayList trainSet) {
        int[] train = new int[trainSet.size()];
        int a = 0;
        for (IntCursor i : trainSet) {
            train[a++] = i.value;
        }
        return train;
    }

    protected static int[] convertToInt(IntArrayList[] setToSplit, int exclude) {
        int count = 0;

        for (int i = 0; i < setToSplit.length; i++) {
            if (i != exclude) {
                count += setToSplit[i].size();
            }
        }

        int[] setData = new int[count];
        int a = 0;
        for (int i = 0; i < setToSplit.length; i++) {
            if (i != exclude) {
                for (IntCursor d : setToSplit[i]) {
                    setData[a++] = d.value;
                }
            }
        }

        return setData;
    }

    protected void initConfusionMatrix(
            final ObjectObjectHashMap<String, ObjectLongHashMap> matrix,
            final HashSet<String> uniqueLabels) {
        for (String label : uniqueLabels) {
            ObjectLongHashMap stat = matrix.get(label);
            if (stat == null) {
                matrix.put(label, new ObjectLongHashMap(uniqueLabels.size()));
            } else if (stat != null) {
                stat.clear();
            }
        }
    }

    public void setMaxWordLength(int maxWordLength) {
        ClassifierMD.maxWordLength = maxWordLength;
    }

    public void setMinWordLenth(int minWordLenth) {
        ClassifierMD.minWordLenth = minWordLenth;
    }

    public void setMaxSymbol(int maxSymbol) {
        ClassifierMD.maxSymbol = maxSymbol;
    }

    public void setMaxWindowLength(int maxWindowLength) {
        ClassifierMD.maxWindowLength = maxWindowLength;
    }

    public void setMinWindowLength(int minWindowLength) {
        ClassifierMD.minWindowLength = minWindowLength;
    }

}