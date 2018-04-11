package algorithms.SFA.transformation;

import algorithms.SFA.classification.ClassifierMD.Words;
import algorithms.SFA.transformation.SFA.HistogramType;
import datasets.timeseries.MultiVariateTimeSeries;
import datasets.timeseries.TimeSeries;
import java.util.ArrayList;

public class BOSSMDWords extends BOSSMDStack {

    public SFA[] signatures;

    public BOSSMDWords(int maxF, int maxS, int windowLength, boolean normMean) {
        super(maxF, maxS, windowLength, normMean);
    }

    public long[][] createWordsMDWords(final MultiVariateTimeSeries[] samples) {

        final int numSources = samples[0].getNumSources();

        TimeSeries[][] samplesModified = splitMultiDimTimeSeries(numSources, samples);

        if (this.signatures == null) {
            this.signatures = new SFA[numSources];
            for (int i = 0; i < numSources; i++) {
                signatures[i] = new SFA(HistogramType.EQUI_DEPTH);
                signatures[i].fitWindowing(samplesModified[i], windowLength, maxF, symbols, normMean, true);
            }
        }

        short[][][][] words = new short[samples.length][numSources][][];

        for (int i = 0; i < samples.length; i++) {
            for (int idSource = 0; idSource < numSources; idSource++) {
                short[][] sfaWords = signatures[idSource].transformWindowing(samples[i].getTimeSeriesOfOneSource(idSource));
                words[i][idSource] = sfaWords;
            }
        }

        long[][] mdWordsLong = new long[samples.length][];

        for (int i = 0; i < samples.length; i++) {
            short[][] mdWordsMerged = mergeSFAWords(words[i], numSources);

            mdWordsLong[i] = new long[mdWordsMerged.length];
            for (int j = 0; j < mdWordsMerged.length; j++) {
                mdWordsLong[i][j] = Words.createWord(mdWordsMerged[j], numSources * maxF, (byte) Words.binlog(symbols));
            }
        }

        return mdWordsLong;
    }

    short[][] mergeSFAWords(short[][][] words, int numSources) {
        int countOfWords = words[0].length;
        int wordLength = words[0][0].length;

        short[][] result;
        ArrayList<short[]> mylist = new ArrayList<>();
        for (int i = 0; i < countOfWords; i++) {
            short[] output = merded(words, numSources, i, wordLength);
            mylist.add(output);

        }
        result = mylist.toArray(new short[][]{});

        return result;
    }

    short[] merded(short[][][] words, int numSources, int i_index, int lenghtWord) {

        int lenght = numSources * lenghtWord;
        short[] mdword = new short[lenght];
        int count = 0;

        for (int idSource = 0; idSource < numSources; idSource++) {
            for (int j = 0; j < lenghtWord; j++) {

                short c = words[idSource][i_index][j];
                mdword[count] = c;
                count++;
            }
        }

        return mdword;
    }

    public BagOfPattern[] createBagOfPatternMDWords(
            final long[][] mdWords,
            final MultiVariateTimeSeries[] samples) {

        final BagOfPattern[] bagOfPatterns = new BagOfPattern[samples.length];

        for (int j = 0; j < samples.length; j++) {

            bagOfPatterns[j] = new BagOfPattern(mdWords[j].length, samples[j].getLabel());

            long lastWord = Long.MIN_VALUE;
            try {
                for (int offset = 0; offset < mdWords[j].length; offset++) {

                    long word = mdWords[j][offset];
                    if (word != lastWord) { // ignore adjacent samples
                        bagOfPatterns[j].bag.putOrAdd((int) word, (short) 1, (short) 1);
                    }
                    //numerosity reduction
                    //lastWord = word;
                }
            } catch (NullPointerException e) {
                System.out.println("Erro!");
            }
        }

        return bagOfPatterns;
    }
}
