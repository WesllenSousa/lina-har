/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.viewControler;

import constants.ConstGeneral;
import constants.Parameters;
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

    public SymbolicView(LineGraphic lineGraphic, BarGraphic barGraphic, TimeSeries[] data) {
        this.lineGraphic = lineGraphic;
        this.barGraphic = barGraphic;
        this.data = data;

        this.lineGraphic.prepareStream(data);
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
                //listPH.get(col).runStreaming(value, position); //Média negativa

                //Process window 
                TimeSeries sample = ts.getSubsequence(position - Parameters.WINDOW_SIZE, Parameters.WINDOW_SIZE);
                discretization(bufferStreaming.get(dataColumn), sample, position - Parameters.WINDOW_SIZE);

                dataColumn++;
            }
            //Add values in GUI
            lineGraphic.addData(values);
            lineGraphic.espera(10);
        }
    }

    private void discretization(BufferStreaming buffer, TimeSeries sample, int position) {
        //Analyse the MCB
        if (buffer.getBufferMCB().size() <= 10) {//X?
            buffer.getBufferMCB().add(sample);
        } else if (buffer.getSfa() == null) {
            SFA sfa = new SFA(SFA.HistogramType.EQUI_DEPTH);
            sfa.fitTransform(buffer.getBufferMCB().toArray(new TimeSeries[]{}),
                    Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_PAA, Parameters.NORM);

            short[] wordQuery = sfa.transform(sample);
            previousWord = buffer.getWordRecord(wordQuery, position);

            buffer.setSfa(sfa);
        } else {
            //Apply SFA on word
            short[] wordQuery = buffer.getSfa().transform(sample);
            WordRecord wordRecord = buffer.getWordRecord(wordQuery, position);

            if (Parameters.NUM_REDUCTION) { //Redução de numerozidade por EXACT Strategy
                if (!previousWord.getWord().equals(wordRecord.getWord())) {
                    handleWord(buffer, wordRecord);
                }
                //Verifiacar alinhamento
            } else {
                handleWord(buffer, wordRecord);
            }
            previousWord = wordRecord;
        }
    }

    private void handleWord(BufferStreaming buffer, WordRecord word) {
        word.setFrequency(barGraphic.getWordFrequency(word.getWord()));

        if (buffer.getBufferBOP().contains(word)) {
            for (WordRecord wordRecord : buffer.getBufferBOP()) {
                if (wordRecord.getWord().equals(word.getWord())) {
                    if (!overlap(wordRecord, word)) {//Verify interval overlaped
                        wordRecord.getIntervals().add(word.getIntervals().get(0));
                        wordRecord.setFrequency(word.getFrequency());
                        updateGUI(buffer, wordRecord);
                    }
                }
            }
        } else {
            buffer.getBufferBOP().add(word);
            updateGUI(buffer, word);
        }
    }

    private void updateGUI(BufferStreaming buffer, WordRecord word) {
        //Show word in GUI
        barGraphic.addUpdateData(word.getWord(), word.getFrequency());
        ConstGeneral.TELA_PRINCIPAL.updateSymbolicTab(word, buffer.getBufferBOP().size());
    }

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

    public void showPageHinkleyChanges() {
        for (PageHinkley ph : listPH) {
            for (PageHinkleyBean bean : ph.getListChanges()) {
                lineGraphic.addMarker(bean.getPosition(), bean.getPosition(), bean.getCor());
            }
        }
    }

}
