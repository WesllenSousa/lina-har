/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasets.memory;

import algorithms.NOHAR.Polygon.PolygonInfo;
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

    private int countChangeHistogram = 0; //count the amount of change in the subsequence that generate the histogram
    private List<WordRecord> histogram = new ArrayList<>();
    private List<WordRecord> histogramOOV = new ArrayList<>(); //Maybe polygonUnknown
    private List<List<WordRecord>> histograms = new ArrayList<>();

    private List<PolygonInfo> polygonKnown = new ArrayList<>();
    private List<PolygonInfo> polygonNovel = new ArrayList<>();
    private List<PolygonInfo> polygonUnknown = new ArrayList<>();

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

    public List<PolygonInfo> getPolygonKnown() {
        return polygonKnown;
    }

    public void setPolygonKnown(List<PolygonInfo> polygonKnown) {
        this.polygonKnown = polygonKnown;
    }

    public List<PolygonInfo> getPolygonNovel() {
        return polygonNovel;
    }

    public void setPolygonNovel(List<PolygonInfo> polygonNovel) {
        this.polygonNovel = polygonNovel;
    }

    public List<PolygonInfo> getPolygonUnknown() {
        return polygonUnknown;
    }

    public void setPolygonUnknown(List<PolygonInfo> polygonUnknown) {
        this.polygonUnknown = polygonUnknown;
    }

    public int getCountChangeHistogram() {
        return countChangeHistogram;
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

    public int getAmoutPolygons() {
        return polygonKnown.size() + polygonNovel.size() + polygonUnknown.size();
    }

    public void resetChangeHistogram() {
        countChangeHistogram = 0;
    }

    public void incrementChangeHistogram() {
        countChangeHistogram++;
    }

}
