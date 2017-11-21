// Copyright (c) 2016 - Patrick Schäfer (patrick.schaefer@zib.de)
// Distributed under the GLP 3.0 (See accompanying file LICENSE)
package controle.SFA.multDimension;

import java.util.HashSet;
import com.carrotsearch.hppc.LongFloatOpenHashMap;
import com.carrotsearch.hppc.LongShortOpenHashMap;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.FloatCursor;
import com.carrotsearch.hppc.cursors.IntIntCursor;
import com.carrotsearch.hppc.cursors.LongFloatCursor;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import controle.SFA.classification.ParallelFor;
import controle.SFA.multDimension.ClassifierMD.Words;
import controle.SFA.transformation.BOSSModel;
import controle.SFA.transformation.SFA;
import controle.SFA.transformation.SFA.HistogramType;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesMD;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Bag-of-SFA-Symbols in Vector Space model as published in Schäfer, P.:
 * Scalable time series classification. DMKD (Preprint)
 *
 * @author bzcschae
 *
 */
public class BOSSMDModel extends BOSSModel {

    public SFA[] signatures;

    public BOSSMDModel(int maxF, int maxS, int windowLength, boolean normMean) {
        super(maxF, maxS, windowLength, normMean);
    }

    public ObjectObjectOpenHashMap<String, LongFloatOpenHashMap> createTfIdf(
            final BagOfPattern[] bagOfPatterns,
            final HashSet<String> uniqueLabels) {
        int[] sampleIndices = createIndices(bagOfPatterns.length);
        return createTfIdf(bagOfPatterns, sampleIndices, uniqueLabels);
    }

    protected static int[] createIndices(int length) {
        int[] indices = new int[length];
        for (int i = 0; i < length; i++) {
            indices[i] = i;
        }
        return indices;
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
    public ObjectObjectOpenHashMap<String, LongFloatOpenHashMap> createTfIdf(
            final BagOfPattern[] bagOfPatterns,
            final int[] sampleIndices,
            final HashSet<String> uniqueLabels) {

        ObjectObjectOpenHashMap<String, LongFloatOpenHashMap> matrix = new ObjectObjectOpenHashMap<>(uniqueLabels.size());
        initMatrix(matrix, uniqueLabels, bagOfPatterns);

        for (int j : sampleIndices) {
            String label = bagOfPatterns[j].label;
            LongFloatOpenHashMap wordInBagFreq = matrix.get(label);
            for (Iterator<IntIntCursor> it = bagOfPatterns[j].bag.iterator(); it.hasNext();) {
                IntIntCursor key = it.next();
                wordInBagFreq.putOrAdd(key.key, key.value, key.value);
            }
        }

        // count the number of classes where the word is present
        LongShortOpenHashMap wordInClassFreq = new LongShortOpenHashMap(matrix.iterator().next().value.size());

        for (ObjectCursor<LongFloatOpenHashMap> stat : matrix.values()) {
            // count the occurence of words
            for (LongFloatCursor key : stat.value) {
                wordInClassFreq.putOrAdd(key.key, (short) 1, (short) 1);
            }
        }

        // calculate the tfIDF value for each class
        for (ObjectObjectCursor<String, LongFloatOpenHashMap> stat : matrix) {
            LongFloatOpenHashMap tfIDFs = stat.value;
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
            final ObjectObjectOpenHashMap<String, LongFloatOpenHashMap> matrix,
            final HashSet<String> uniqueLabels,
            final BagOfPattern[] bag) {
        for (String label : uniqueLabels) {
            LongFloatOpenHashMap stat = matrix.get(label);
            if (stat == null) {
                matrix.put(label, new LongFloatOpenHashMap(bag[0].bag.size() * bag.length));
            } else if (stat != null) {
                stat.clear();
            }
        }
    }

    /**
     * Norm the vector to length 1
     *
     * @param classStatistics
     */
    public void normalizeTfIdf(final ObjectObjectOpenHashMap<String, LongFloatOpenHashMap> classStatistics) {
        for (ObjectCursor<LongFloatOpenHashMap> classStat : classStatistics.values()) {
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

    public int[][][] createWordsMD(final TimeSeriesMD[] samples) {
        int numSources = samples[0].getNumSources();
        final int[][][] words = new int[numSources][samples.length][];

        TimeSeries[][] samplesModificado = new TimeSeries[numSources][samples.length];
        for (int indexOfSource = 0; indexOfSource < numSources; indexOfSource++) {

            for (int indexOfSample = 0; indexOfSample < samples.length; indexOfSample++) {
                samplesModificado[indexOfSource][indexOfSample] = samples[indexOfSample].getTimeSeriesOfOneSource(indexOfSource);
            }
        }
        if (this.signatures == null) {
            this.signatures = new SFA[numSources];
            for (int i = 0; i < numSources; i++) {
                this.signatures[i] = new SFA(HistogramType.EQUI_DEPTH);
                this.signatures[i].fitWindowing(samplesModificado[i], windowLength, maxF, symbols, normMean, true);
            }
        }
        int teste = 0;
        // create sliding windows
        ParallelFor.withIndex(BLOCKS, new ParallelFor.Each() {
            @Override
            public void run(int id, AtomicInteger processed) {
                for (int idSource = 0; idSource < numSources; idSource++) {
                    for (int i = 0; i < samples.length; i++) {
                        if (i % BLOCKS == id) {
                            short[][] sfaWords = signatures[idSource].transformWindowing(samples[i].getTimeSeriesOfOneSource(idSource), maxF);
                            words[idSource][i] = new int[sfaWords.length];
                            for (int j = 0; j < sfaWords.length; j++) {
                                words[idSource][i][j] = (int) Words.createWord(sfaWords[j], maxF, (byte) Words.binlog(symbols));
                            }
                        }
                    }
                }
            }
        });
        return words;
    }

    /**
     * Create the BOSS model for a fixed window-length and SFA word length
     *
     * @param words the SFA words of the time series
     * @param samples
     * @param wordLength the SFA word length
     * @return
     */
    public BagOfPattern[] createBagOfPatternMD(
            final int[][][] words,
            final TimeSeriesMD[] samples,
            final int wordLength) {
        BagOfPattern[] bagOfPatterns = new BagOfPattern[samples.length];

        int numSources = samples[0].getNumSources();

        final byte usedBits = (byte) Words.binlog(symbols);
        final byte dimensionBits = 3;

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
