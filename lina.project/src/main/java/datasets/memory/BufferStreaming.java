/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasets.memory;

import controle.constants.Parameters;
import datasets.timeseries.TimeSeries;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Wesllen Sousa
 */
public class BufferStreaming {

    private TimeSeries subSequence = new TimeSeries();

    private List<WordRecord> histogram = new ArrayList<>();
    private List<WordRecord> histogramOOV = new ArrayList<>();

    private HashMap<String, HashMap<String, Double>> modelSaxVsm;

    /*
     *   Geters Setters
     */
    public TimeSeries getSubSequence() {
        return subSequence;
    }

    public void setSubSequence(TimeSeries subSequence) {
        this.subSequence = subSequence;
    }

    public List<WordRecord> getHistogram() {
        return histogram;
    }

    public List<WordRecord> getHistogramOOV() {
        return histogramOOV;
    }

    public HashMap<String, HashMap<String, Double>> getModelSaxVsm() {
        return modelSaxVsm;
    }

    public void setModelSaxVsm(HashMap<String, HashMap<String, Double>> modelSaxVsm) {
        this.modelSaxVsm = modelSaxVsm;
    }

    /*
     *   Handle Word Record
     */
    public WordRecord populaWordRecord(String word, int initialPosition) {
        WordRecord wordRecord = new WordRecord();
        wordRecord.setWord(word);
        wordRecord.setFrequency(1);
        wordRecord.getIntervals().add(getWordInterval(initialPosition));
        return wordRecord;
    }

    public WordInterval getWordInterval(int initialPosition) {
        WordInterval wordInterval = new WordInterval();
        wordInterval.setPositionInit(initialPosition);
        wordInterval.setPositionEnd(initialPosition + Parameters.WINDOW_SIZE);
        return wordInterval;
    }

}
