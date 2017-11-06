/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.viewControler;

import constants.ConstGeneral;
import constants.Parameters;
import controle.SAX.Params;
import controle.SAX.SAX;
import controle.SAX.saxvsm.text.WordBag;
import controle.SFA.classification.BOSSClassifier;
import controle.SFA.classification.Classifier;
import controle.SFA.classification.Classifier.Predictions;
import controle.SFA.transformation.BOSSModel;
import controle.SFA.transformation.BOSSModel.BagOfPattern;
import controle.SFA.transformation.SFA;
import controle.pageHinkley.PageHinkley;
import controle.pageHinkley.PageHinkleyBean;
import datasets.memory.BufferStreaming;
import datasets.memory.WordInterval;
import datasets.memory.WordRecord;
import datasets.timeseries.TimeSeries;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;
import view.manualviews.BarGraphic;
import view.manualviews.LineGraphic;

/**
 *
 * @author Wesllen Sousa
 */
public class SymbolicView {

    private final LineGraphic lineGraphic;
    private final BarGraphic barGraphic;

    private final TimeSeries[] data;
    private ArrayList<BufferStreaming> bufferStreaming = new ArrayList<>();
    private ArrayList<PageHinkley> listPH = new ArrayList<>();

    private WordRecord previousWord;
    private Params params;

    public SymbolicView(LineGraphic lineGraphic, BarGraphic barGraphic, TimeSeries[] data) {
        this.lineGraphic = lineGraphic;
        this.barGraphic = barGraphic;
        this.data = data;

        this.lineGraphic.prepareStream(data);

        params = new Params(Parameters.WINDOW_SIZE, Parameters.WORD_LENGTH_PAA,
                Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.NORMALIZATION_THRESHOLD, NumerosityReductionStrategy.EXACT);
    }

    public void runDataset() {

        //Initial data from the first window
        ArrayList<TimeSeries> initialSample = new ArrayList<>();
        Random randomCor = new Random();
        int dataColumn = 0;
        for (TimeSeries ts : data) {
            //Get first subsequence
            TimeSeries sample = ts.getSubsequence(0, Parameters.WINDOW_SIZE);
            initialSample.add(sample);

            //Apply page hinkley to the first subsequence
            PageHinkley ph = new PageHinkley(new Color(randomCor.nextInt(255), randomCor.nextInt(255), randomCor.nextInt(255)));
            ph.runTs(sample.getData());
            listPH.add(dataColumn, ph);

            //Apply buffer streaming to the first subsequence
            BufferStreaming buffer = new BufferStreaming();
            buffer.getBufferMCB().add(sample);
            bufferStreaming.add(dataColumn, buffer);

            dataColumn++;
        }
        //Add subsequece in GUI
        lineGraphic.addSubsequenceData(initialSample.toArray(new TimeSeries[]{}));

        //Remaining of the data for each value - streaming
        for (int position = Parameters.WINDOW_SIZE; position < (data[0].getLength() - Parameters.WINDOW_SIZE); position += Parameters.OFFSET) {
            double[] values = new double[data.length];
            dataColumn = 0;
            for (TimeSeries ts : data) {
                //Get current value to add in the graphic and calcule the page hinkley
                double value = ts.getData(position);
                values[dataColumn] = value;
                //listPH.get(col).runStreaming(value, position); //Média negativa **********************

                //Process window 
                TimeSeries sample = ts.getSubsequence(position - Parameters.WINDOW_SIZE, Parameters.WINDOW_SIZE);
                BufferStreaming buffer = bufferStreaming.get(dataColumn);
                if (Parameters.SFA) {
                    SFA(buffer, sample, position - Parameters.WINDOW_SIZE);
                } else {
                    SAX(buffer, sample, position - Parameters.WINDOW_SIZE);
                }

                if (position % Parameters.BOP_SIZE == 0) {
                    String label = "1";
                    if (Parameters.SFA) {
                        createBagOfPatternSFA(buffer, label);
                        analyseBufferBOPsfa(buffer, position);
                    } else {
                        createBagOfPatternSAX(buffer, label);
                        analyseBufferBOPsax(buffer, position);
                    }
                }

                dataColumn++;
            }
            //Add values in GUI
            lineGraphic.addData(values);
            lineGraphic.espera(10);
        }
    }

    private void SAX(BufferStreaming buffer, TimeSeries sample, int position) {
        String word = SAX.serieToWord(sample.getData(), params);
        WordRecord wordRecord = buffer.getWordRecord(word, position);
        if (validateWord(buffer, wordRecord)) {
            buffer.getBufferWord().add(wordRecord);
        }
        previousWord = wordRecord;
    }

    private void SFA(BufferStreaming buffer, TimeSeries sample, int position) {
        if (position <= Parameters.MCB_SIZE) {
            buffer.getBufferMCB().add(sample);

            if (position == Parameters.MCB_SIZE) { //*** change threhold to MCB 
                SFA sfa = new SFA(SFA.HistogramType.EQUI_DEPTH);
                sfa.fitTransform(buffer.getBufferMCB().toArray(new TimeSeries[]{}),
                        Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.NORM);
                buffer.setSfa(sfa);

                short[] wordQuery = buffer.getSfa().transform(sample);
                previousWord = buffer.getWordRecord(wordQuery, position);

                lineGraphic.addMarker(position, position, Color.black);
            }
        } else {
            short[] wordQuery = buffer.getSfa().transform(sample);
            WordRecord wordRecord = buffer.getWordRecord(wordQuery, position);

            if (validateWord(buffer, wordRecord)) {
                buffer.getBufferWord().add(wordRecord);
                updateGUI(buffer, wordRecord);
            }
            previousWord = wordRecord;
        }
    }

    private boolean validateWord(BufferStreaming buffer, WordRecord word) {
        if (previousWord == null) {
            return false;
        }
        //Redução de numerozidade por EXACT Strategy 
        if (Parameters.NUM_REDUCTION && !previousWord.getWord().equals(word.getWord())) {
            return false;
        }
        //update frequency
        word.setFrequency(barGraphic.getWordFrequency(word.getWord()));
        //Verify if word is in the buffer BOP
        if (buffer.getBufferWord().contains(word)) { //Bloco de código passivel de futuras otimizacoes
            for (WordRecord wordRecord : buffer.getBufferWord()) {
                if (wordRecord.getWord().equals(word.getWord())) {
                    //Verify interval overlaped to same words: alingments
                    if (Parameters.ALINGMENT == false) {
                        wordRecord.getIntervals().add(word.getIntervals().get(0));
                        wordRecord.setFrequency(word.getFrequency());
                    } else if (Parameters.ALINGMENT && !overlap(wordRecord, word)) {
                        wordRecord.getIntervals().add(word.getIntervals().get(0));
                        wordRecord.setFrequency(word.getFrequency());
                    }
                    updateGUI(buffer, wordRecord);
                }
            }
        } else {
            return true;
        }
        return false;
    }

    private void createBagOfPatternSAX(BufferStreaming buffer, String label) {
        WordBag bag = new WordBag(label);
        for (WordRecord word : buffer.getBufferWord()) {
            bag.addWord(word.getWord(), word.getFrequency());
        }
        buffer.setCurrBOPsax(bag);

        //Analisar se é bom adicionar o bop na lista
        buffer.getBOPsax().add(buffer.getCurrBOPsax()); //*********

        //Criar ou atualizar matriz TF-IDF
    }

    private void createBagOfPatternSFA(BufferStreaming buffer, String label) {
        int[] words = new int[buffer.getBufferWord().size()];
        for (int wordIndex = 0; wordIndex < buffer.getBufferWord().size(); wordIndex++) {
            //Get word int value from word bit value
            int wordInt = (int) Classifier.Words.createWord(buffer.getBufferWord().get(wordIndex).getWordBit(),
                    Parameters.WORD_LENGTH_PAA, (byte) Classifier.Words.binlog(Parameters.SYMBOLS_ALPHABET_SIZE));
            //Create column from matrix word equals the frequency of each word 
            words[wordIndex] = wordInt;
        }
        BOSSModel boss = new BOSSModel(Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.WINDOW_SIZE, true);
        BagOfPattern bag = boss.createOneBagOfPattern(words, label, Parameters.WORD_LENGTH_PAA);
        buffer.setCurrBOPsfa(bag);

        //Analisar se é bom adicionar o bop na lista
        buffer.getBOPsfa().add(buffer.getCurrBOPsfa()); //*********

        //Se for o caso criar ou atualizar matriz do TF-IDF
    }

    private void analyseBufferBOPsax(BufferStreaming buffer, int position) {
        //Classificar com SAX-VSM
    }

    private void analyseBufferBOPsfa(BufferStreaming buffer, int position) {

        //BOSS Model
        BOSSClassifier classifier = new BOSSClassifier(Parameters.WINDOW_SIZE);
        Predictions p = classifier.predictStream(buffer.getCurrBOPsfa(), buffer.getBOPsfa().toArray(new BagOfPattern[]{}));

        //BOSS VS
        System.out.println(p.labels[0]);

        clearGUI(buffer);
        lineGraphic.addMarker(position, position, Color.black);

    }

    /*
     *   Other methods
     */
    //O overlap já é uma espécie de alinhamento de palavras iguais
    private boolean overlap(WordRecord words, WordRecord word) {
        int init = word.getIntervals().get(0).getPositionInit();
        int end = word.getIntervals().get(0).getPositionEnd();
        for (WordInterval interval : words.getIntervals()) {
            int cInit = interval.getPositionInit();
            int cEnd = interval.getPositionEnd();
            if ((init >= cInit && init <= cEnd) || (end >= cInit && end <= cEnd)
                    || (cInit >= init && cInit <= end) || (cEnd >= init && cEnd <= end)) {
                return true;
            }
        }
        return false;
    }

    /*
     *   GUI
     */
    private void updateGUI(BufferStreaming buffer, WordRecord word) {
        barGraphic.addUpdateData(word.getWord(), word.getFrequency());
        ConstGeneral.TELA_PRINCIPAL.updateSymbolicTab(word, buffer.getBufferWord().size());
    }

    private void clearGUI(BufferStreaming buffer) {
        buffer.getBufferWord().clear();
        ConstGeneral.TELA_PRINCIPAL.clearBarGraphic();
    }

    public void showPageHinkleyChanges() {
        for (PageHinkley ph : listPH) {
            for (PageHinkleyBean bean : ph.getListChanges()) {
                lineGraphic.addMarker(bean.getPosition(), bean.getPosition(), bean.getCor());
            }
        }
    }

}
