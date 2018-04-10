// Copyright (c) 2016 - Patrick Schäfer (patrick.schaefer@zib.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package controle.SFA.transformation;

import com.carrotsearch.hppc.LongFloatHashMap;
import com.carrotsearch.hppc.LongShortHashMap;
import com.carrotsearch.hppc.ObjectObjectHashMap;
import com.carrotsearch.hppc.cursors.*;
import controle.SFA.classification.Classifier.Words;
import controle.SFA.transformation.SFA.HistogramType;
import datasets.timeseries.MultiVariateTimeSeries;
import datasets.timeseries.TimeSeries;

import java.util.HashSet;

/**
 * The Bag-of-SFA-Symbols in Vector Space model as published in Schäfer, P.:
 * Scalable time series classification. DMKD (Preprint)
 *
 * @author bzcschae
 */
public class BOSSMDStack extends BOSS {

    public SFA[] signatures;

    public BOSSMDStack(int maxF, int maxS, int windowLength, boolean normMean) {
        super(maxF, maxS, windowLength, normMean);
    }

    protected static int[] createIndices(int length) {
        int[] indices = new int[length];
        for (int i = 0; i < length; i++) {
            indices[i] = i;
        }
        return indices;
    }

    public ObjectObjectHashMap<Double, LongFloatHashMap> createTfIdf(
            final BagOfPattern[] bagOfPatterns,
            final HashSet<Double> uniqueLabels) {
        int[] sampleIndices = createIndices(bagOfPatterns.length);
        return createTfIdf(bagOfPatterns, sampleIndices, uniqueLabels);
    }

    /**
     * Obtains the TF-IDF representation based on the BOSS represenation. Only
     * those elements in sampleIndices are used (usefull for cross-validation).
     *
     * @param bagOfPatterns The BOSS (bag-of-patterns) representation of the
     * time series
     * @param sampleIndices The indices to use
     * @param uniqueLabels The unique class labels in the dataset
     * @return
     */
    public ObjectObjectHashMap<Double, LongFloatHashMap> createTfIdf(
            final BagOfPattern[] bagOfPatterns,
            final int[] sampleIndices,
            final HashSet<Double> uniqueLabels) {

        ObjectObjectHashMap<Double, LongFloatHashMap> matrix = new ObjectObjectHashMap<>(uniqueLabels.size());
        initMatrix(matrix, uniqueLabels, bagOfPatterns);

        for (int j : sampleIndices) {
            Double label = bagOfPatterns[j].label;
            LongFloatHashMap wordInBagFreq = matrix.get(label);
            for (IntIntCursor key : bagOfPatterns[j].bag) {
                wordInBagFreq.putOrAdd(key.key, key.value, key.value);
            }
        }

        // count the number of classes where the word is present
        LongShortHashMap wordInClassFreq = new LongShortHashMap(matrix.iterator().next().value.size());

        for (ObjectCursor<LongFloatHashMap> stat : matrix.values()) {
            // count the occurence of words
            for (LongFloatCursor key : stat.value) {
                wordInClassFreq.putOrAdd(key.key, (short) 1, (short) 1);
            }
        }

        // calculate the tfIDF value for each class
        for (ObjectObjectCursor<Double, LongFloatHashMap> stat : matrix) {
            LongFloatHashMap tfIDFs = stat.value;
            // calculate the tfIDF value for each word
            for (LongFloatCursor patternFrequency : tfIDFs) {
                short wordCount = wordInClassFreq.get(patternFrequency.key);
                if (patternFrequency.value > 0
                        && uniqueLabels.size() != wordCount // avoid Math.log(1)
                        ) {
                    double tfValue = 1.0 + Math.log10(patternFrequency.value); // smoothing
                    double idfValue = Math.log10(1.0 + uniqueLabels.size() / (double) wordCount); // smoothing
                    double tfIdf = tfValue / idfValue;

                    // update the tfIDF vector
                    tfIDFs.values[patternFrequency.index] = (float) tfIdf;
                } else {
                    tfIDFs.values[patternFrequency.index] = 0;
                }
            }
        }

        // norm the tf-idf-matrix
        normalizeTfIdf(matrix);

        return matrix;
    }

    protected void initMatrix(
            final ObjectObjectHashMap<Double, LongFloatHashMap> matrix,
            final HashSet<Double> uniqueLabels,
            final BagOfPattern[] bag) {
        for (Double label : uniqueLabels) {
            LongFloatHashMap stat = matrix.get(label);
            if (stat == null) {
                matrix.put(label, new LongFloatHashMap(bag[0].bag.size() * bag.length));
            } else if (stat != null) {
                stat.clear();
            }
        }
    }

    public TimeSeries[][] splitMultiDimTimeSeries(int numSources, MultiVariateTimeSeries[] samples) {

        TimeSeries[][] samplesModified = new TimeSeries[numSources][samples.length];
        for (int indexOfSource = 0; indexOfSource < numSources; indexOfSource++) {

            for (int indexOfSample = 0; indexOfSample < samples.length; indexOfSample++) {
                samplesModified[indexOfSource][indexOfSample] = samples[indexOfSample].getTimeSeriesOfOneSource(indexOfSource);
            }
        }
        return samplesModified;
    }

    /**
     * Norm the vector to length 1
     *
     * @param classStatistics
     */
    public void normalizeTfIdf(final ObjectObjectHashMap<Double, LongFloatHashMap> classStatistics) {
        for (ObjectCursor<LongFloatHashMap> classStat : classStatistics.values()) {
            double squareSum = 0.0;
            for (FloatCursor entry : classStat.value.values()) {
                squareSum += entry.value * entry.value;
            }
            double squareRoot = Math.sqrt(squareSum);
            if (squareRoot > 0) {
                for (FloatCursor entry : classStat.value.values()) {
                    //entry.value /= squareRoot;
                    classStat.value.values[entry.index] /= squareRoot;
                }
            }
        }
    }

    public long[][][] createWordsMDStack(final MultiVariateTimeSeries[] samples) {
        int numSources = samples[0].getNumSources();

//        System.out.println(numSources);
        final long[][][] words = new long[numSources][samples.length][];

        TimeSeries[][] samplesModified = splitMultiDimTimeSeries(numSources, samples);

        if (this.signatures == null) {
            this.signatures = new SFA[numSources];

            for (int i = 0; i < numSources; i++) {
                signatures[i] = new SFA(HistogramType.EQUI_DEPTH);
                signatures[i].fitWindowing(samplesModified[i], windowLength, maxF, symbols, normMean, true);
            }
        }

        for (int idSource = 0; idSource < numSources; idSource++) {
            for (int i = 0; i < samples.length; i++) {

//                System.out.println(">> " + samples[0].getLength());
                short[][] sfaWords = signatures[idSource].transformWindowing(samples[i].getTimeSeriesOfOneSource(idSource));
                words[idSource][i] = new long[sfaWords.length];
                for (int j = 0; j < sfaWords.length; j++) {
                    //Aqui pode ser long palabras maiores
                    words[idSource][i][j] = Words.createWord(sfaWords[j], maxF, (byte) Words.binlog(symbols));
                }
            }
        }

//        System.out.println("================");
        return words;
    }

    public BagOfPattern[] createBagOfPatternMDStack(
            final long[][][] words,
            final MultiVariateTimeSeries[] samples,
            final int wordLength) {
        BagOfPattern[] bagOfPatterns = new BagOfPattern[samples.length];

        int numSources = samples[0].getNumSources();

        final byte usedBits = (byte) Words.binlog(symbols);
        final byte dimensionBits = (byte) Words.binlog(numSources);
        int totalBitsToUse = usedBits * wordLength;
        if (totalBitsToUse > 60) {
            System.out.println("Problems with leght word!");
        }
        final long mask = (1l << (usedBits * wordLength)) - 1l;

        // iterate all samples
        for (int j = 0; j < samples.length; j++) {

            bagOfPatterns[j] = new BagOfPattern(numSources * words[0][j].length, samples[j].getLabel());

            for (int s = 0; s < numSources; s++) {
                // create subsequences
                long lastWord = Long.MIN_VALUE;

                for (int offset = 0; offset < words[s][j].length; offset++) {
                    // use the words of larger length to get words of smaller lengths
                    long word = words[s][j][offset] & mask;
                    long wordModificado = (word << dimensionBits) + s;
                    if (wordModificado != lastWord) { // ignore adjacent samples
                        bagOfPatterns[j].bag.putOrAdd((int) wordModificado, (short) 1, (short) 1);
                    }
                    lastWord = wordModificado;
                }
            }
        }
        return bagOfPatterns;
    }

}
