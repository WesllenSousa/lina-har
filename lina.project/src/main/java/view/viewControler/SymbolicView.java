/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.viewControler;

import algorithms.NOHAR.NOHAR;
import com.vividsolutions.jts.geom.Polygon;
import controle.constants.ConstGeneral;
import controle.constants.Parameters;
import datasets.memory.BufferStreaming;
import datasets.memory.WordRecord;
import datasets.timeseries.TimeSeries;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import view.manualviews.BarGraphic;
import view.manualviews.LineGraphic;

/**
 *
 * @author Wesllen Sousa
 */
public class SymbolicView {

    private final LineGraphic lineGraphic;
    private final BarGraphic barGraphic;

    private final List<BufferStreaming> bufferStreaming;
    private final NOHAR nohar;

    public SymbolicView(LineGraphic lineGraphic, BarGraphic barGraphic) {
        this.lineGraphic = lineGraphic;
        this.barGraphic = barGraphic;
        this.bufferStreaming = new ArrayList<>();

        this.nohar = new NOHAR(this);
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

                processStream(buffer, currentValue, position);

                values[dataColumn] = currentValue;
                dataColumn++;
            }

            addDataGraphLine(values);
        }
    }

    private void processStream(BufferStreaming buffer, double currentValue, int position) {
        if (nohar != null) {
            nohar.runStream(buffer, currentValue, position);
        }
    }

    /*
     *   GUI
     */
    public void addDataGraphLine(double[] values) {
        //Add values in GUI
        lineGraphic.addData(values);
        lineGraphic.espera(10);
    }

    public void addMarkerGraphLine(int position, Color color) {
        lineGraphic.addMarker(position, position, color);
    }

    public void updateCurrentHistogram(BufferStreaming buffer, WordRecord word) {
        barGraphic.addUpdateData(word.getWord(), word.getFrequency());
        ConstGeneral.TELA_PRINCIPAL.updateSymbolicTab(word, buffer.getHistogram().size());
    }

    public void clearCurrentHistogram(BufferStreaming buffer) {
        buffer.getHistograms().add(buffer.getHistogram());
        ConstGeneral.TELA_PRINCIPAL.addHistograms(buffer.getHistograms());
        buffer.setHistogram(new ArrayList<>());
        ConstGeneral.TELA_PRINCIPAL.clearCurrentHistogram();
    }

    public void addPolygons(ArrayList<Polygon> polygons) {
        ConstGeneral.TELA_PRINCIPAL.addPolygons(polygons);
    }

    public void updateLog(String text) {
        ConstGeneral.TELA_PRINCIPAL.updateSymbolicLog(text);
    }

}
