/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.viewControler;

import algorithms.ADWIN.ADWIN;
import controle.constants.ConstGeneral;
import controle.constants.Parameters;
import algorithms.SAX.Params;
import algorithms.SAX.SAX;
import datasets.memory.BufferStreaming;
import datasets.memory.WordInterval;
import datasets.memory.WordRecord;
import datasets.timeseries.TimeSeries;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
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

    private List<BufferStreaming> bufferStreaming;
    private ADWIN adwin;

    private WordRecord previousWord;
    private int lastPosPreviusWord = 0;
    private Params params;

    public SymbolicView(LineGraphic lineGraphic, BarGraphic barGraphic) {
        this.lineGraphic = lineGraphic;
        this.barGraphic = barGraphic;
        this.bufferStreaming = new ArrayList<>();
        adwin = new ADWIN(.01);

        params = new Params(Parameters.WINDOW_SIZE, Parameters.WORD_LENGTH_PAA,
                Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.NORMALIZATION_THRESHOLD, NumerosityReductionStrategy.EXACT);
    }

    public void runDataset(TimeSeries[] data, boolean norm) {
        this.lineGraphic.prepareStream(data);

        //Apply buffer streaming to the Multivate times series
        int dataColumn = 0;
        for (TimeSeries ts : data) {
            BufferStreaming buffer = new BufferStreaming();
            bufferStreaming.add(dataColumn, buffer);
            dataColumn++;
        }

        //Access each position from time series - streaming
        for (int position = Parameters.WINDOW_SIZE; position < (data[0].getLength() - Parameters.WINDOW_SIZE); position += ConstGeneral.OFFSET) {
            //Value for each axis
            double[] values = new double[data.length];

            dataColumn = 0; //Access multivate times series
            for (TimeSeries ts : data) {
                //Get current value to add in the graphic and calcule the page hinkley
                double currentValue = ts.getData(position);
                //Get current window - windowing
                TimeSeries subsequence = ts.getSubsequence(position - Parameters.WINDOW_SIZE, Parameters.WINDOW_SIZE, norm);
                //Get buffer for each axis
                BufferStreaming buffer = bufferStreaming.get(dataColumn);
                buffer.setSubSequence(subsequence); //Chunk size = 1 in this case

                processStream(currentValue, buffer, position);

                values[dataColumn] = currentValue;
                dataColumn++;
            }

            //Add values in GUI
            lineGraphic.addData(values);
            lineGraphic.espera(10);
        }
    }

    private void processStream(double currentValue, BufferStreaming buffer, int position) {
        //Monitor changin in the data
        if (!changeDetected(currentValue, position)) {
            //Discretize
            WordRecord word = discretize(buffer, position);

            //Histogram
            createHistogram(buffer, word);

            //Handle model
            if (position % Parameters.BOP_SIZE == 0) { //*** change threhold to BOP 
                //Learning
                learning(buffer);

                clearGUIbar(buffer);
                lineGraphic.addMarker(position, position, Color.black);
            }
        }
    }

    private boolean changeDetected(double currentValue, int position) {
        if (adwin.setInput(currentValue)) {
            System.out.println("Change Detected: " + position);
            updateLog("Change Detected: " + position);
            lineGraphic.addMarker(position, position, Color.red);
            return true;
        }
        return false;
    }

    private WordRecord discretize(BufferStreaming buffer, int position) {
        SAX sax = new SAX(params);
        String word = sax.serieToWord(buffer.getSubSequence().getData());
        WordRecord wordRecord = buffer.populaWordRecord(word, position - Parameters.WINDOW_SIZE);
        return wordRecord;
    }

    private void createHistogram(BufferStreaming buffer, WordRecord word) {
        if (word == null) {
            return;
        }
        //Redução de numerozidade por EXACT Strategy 
        if (ConstGeneral.NUM_REDUCTION && previousWord != null && lastPosPreviusWord < Parameters.WINDOW_SIZE
                && previousWord.getWord().equals(word.getWord())) {
            //For word that repeat always, we update the frequency
            lastPosPreviusWord++;
            return;
        }
        lastPosPreviusWord = 0;

        //Verify if word is in the buffer BOP
        if (buffer.getHistogram().contains(word)) { //Bloco de código passivel de futuras otimizacoes
            for (WordRecord wordRecord : buffer.getHistogram()) {
                if (wordRecord.getWord().equals(word.getWord())) {
                    //Verify interval overlaped to same words: alingments
                    if (ConstGeneral.ALINGMENT && !overlap(wordRecord, word)) {
                        wordRecord.getIntervals().add(word.getIntervals().get(0));
                        wordRecord.incrementFrequency();
                    } else {
                        wordRecord.getIntervals().add(word.getIntervals().get(0));
                        wordRecord.incrementFrequency();
                    }
                    previousWord = word;
                    updateGUIbar(buffer, wordRecord);
                }
            }
        } else {
            //Add word in buffer
            previousWord = word;
            buffer.getHistogram().add(word);
            updateGUIbar(buffer, word);
        }
    }

    private void learning(BufferStreaming buffer) {
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
    private void updateGUIbar(BufferStreaming buffer, WordRecord word) {
        barGraphic.addUpdateData(word.getWord(), word.getFrequency());
        ConstGeneral.TELA_PRINCIPAL.updateSymbolicTab(word, buffer.getHistogram().size());
    }

    private void clearGUIbar(BufferStreaming buffer) {
        if (ConstGeneral.CLEAR_HIST) {
            buffer.getHistogram().clear();
            ConstGeneral.TELA_PRINCIPAL.clearBarGraphic();
        }
    }

    private void updateLog(String text) {
        ConstGeneral.TELA_PRINCIPAL.updateSymbolicLog(text);
    }

}
