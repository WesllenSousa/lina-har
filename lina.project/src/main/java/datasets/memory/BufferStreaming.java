/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasets.memory;

import com.vividsolutions.jts.geom.Polygon;
import controle.constants.Parameters;
import datasets.timeseries.TimeSeries;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wesllen Sousa
 */
public class BufferStreaming {

    private TimeSeries subSequence = new TimeSeries();

    private List<WordRecord> histogram = new ArrayList<>();
    private List<WordRecord> histogramOOV = new ArrayList<>();
    private List<List<WordRecord>> histograms = new ArrayList<>();

    private ArrayList<Polygon> polygons = new ArrayList<>();

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

    public void setHistogram(List<WordRecord> histogram) {
        this.histogram = histogram;
    }

    public List<WordRecord> getHistogramOOV() {
        return histogramOOV;
    }

    public void setHistogramOOV(List<WordRecord> histogramOOV) {
        this.histogramOOV = histogramOOV;
    }

    public List<List<WordRecord>> getHistograms() {
        return histograms;
    }

    public void setHistograms(List<List<WordRecord>> histograms) {
        this.histograms = histograms;
    }

    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(ArrayList<Polygon> polygons) {
        this.polygons = polygons;
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
