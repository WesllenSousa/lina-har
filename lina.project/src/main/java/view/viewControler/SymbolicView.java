/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.viewControler;

import algorithms.Features.DataFusion;
import algorithms.NOHAR.NOHAR;
import algorithms.NOHAR.Polygon.PolygonInfo;
import controle.constants.ConstGeneral;
import controle.constants.Parameters;
import datasets.memory.BufferStreaming;
import datasets.memory.WordRecord;
import datasets.timeseries.TimeSeries;
import java.awt.Color;
import java.util.ArrayList;
import view.manualviews.BarGraphic;
import view.manualviews.LineGraphic;

/**
 *
 * @author Wesllen Sousa
 */
public class SymbolicView {

    private final LineGraphic lineGraphic;
    private final BarGraphic barGraphic;

    private final BufferStreaming bufferStreaming;
    private final NOHAR nohar;

    public SymbolicView(LineGraphic lineGraphic, BarGraphic barGraphic) {
        this.lineGraphic = lineGraphic;
        this.barGraphic = barGraphic;
        this.bufferStreaming = new BufferStreaming();

        this.nohar = new NOHAR(this);
    }

    public void runDataset(TimeSeries[] data, boolean norm) {
        //this.lineGraphic.prepareStream(data);
        this.lineGraphic.prepareStream();

        //Access each position from time series - streaming
        for (int position = Parameters.WINDOW_SIZE; position < (data[0].getLength() - Parameters.WINDOW_SIZE); position += Parameters.OFFSET) {

            //Get array subsequence
            TimeSeries[] subsequences = getSubsequences(data, position, norm);

            //label nao ta muito certo
            TimeSeries labels = subsequences[subsequences.length - 1];
            TimeSeries subsequence = DataFusion.Magnitude(subsequences);//====== atencao! metodo alterado

            bufferStreaming.setSubSequence(subsequence);

            //Get current value and process 
            double currentValue = subsequence.getData(subsequence.getLength() - 1);
            double label = labels.getData(labels.getLength() - 1); //===========Pega o ultimo label: Verificar a maior quantidade de labels
            processStream(currentValue, position, label);

            //values to GUI
            double[] values = new double[1];
            values[0] = currentValue;
            addDataGraphLine(values);
        }
    }

    private TimeSeries[] getSubsequences(TimeSeries[] data, int position, boolean norm) {
        //Get array subsequence
        TimeSeries[] subsequences = new TimeSeries[data.length];
        int dataColumn = 0;
        for (TimeSeries ts : data) {
            //Get current window - windowing
            TimeSeries subsequence = ts.getSubsequence(position - Parameters.WINDOW_SIZE, Parameters.WINDOW_SIZE, norm);
            subsequences[dataColumn] = subsequence;
            dataColumn++;
        }
        return subsequences;
    }

    private void processStream(double currentValue, int position, double label) {
        if (nohar != null) {
            nohar.runStream(bufferStreaming, currentValue, position, label);
        }
    }

    /*
     *   GUI
     */
    private void addDataGraphLine(double[] values) {
        if (ConstGeneral.SHOW_GRAPHIC) {
            //Add values in GUI
            lineGraphic.addData(values);
            lineGraphic.espera(10);
        }
    }

    public void addMarkerGraphLine(int position, Color color) {
        if (ConstGeneral.SHOW_GRAPHIC) {
            lineGraphic.addMarker(position, position, color);
        }
    }

    public void updateCurrentHistogram(BufferStreaming buffer, WordRecord word) {
        barGraphic.addUpdateData(word.getWord(), word.getFrequency());
        ConstGeneral.TELA_PRINCIPAL.updateSymbolicTab(word, buffer.getHistogram().size());
    }

    public void clearCurrentHistogram(BufferStreaming buffer) {
        buffer.setHistogram(new ArrayList<>());
        ConstGeneral.TELA_PRINCIPAL.clearCurrentHistogram();
    }

    public void addHistogramsAndPolygons(BufferStreaming buffer) {
        ConstGeneral.TELA_PRINCIPAL.addHistograms(buffer.getHistograms());
        ConstGeneral.TELA_PRINCIPAL.addPolygons((ArrayList<PolygonInfo>) buffer.getPolygonUnknown());
    }

    public void updateLog(String text) {
        ConstGeneral.TELA_PRINCIPAL.updateSymbolicLog(text);
    }

}
