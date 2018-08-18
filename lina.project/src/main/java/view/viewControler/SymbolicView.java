/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.viewControler;

import algorithms.Features.DataFusion;
import algorithms.NOHAR.BOP;
import algorithms.NOHAR.EvaluationNohar;
import algorithms.NOHAR.NOHAR;
import algorithms.SAX.Params;
import controle.constants.ConstGeneral;
import controle.constants.Parameters;
import datasets.memory.BufferStreaming;
import datasets.memory.WordRecord;
import datasets.timeseries.TimeSeries;
import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
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

    private final BufferStreaming bufferStreaming;
    private final NOHAR nohar;
    private EvaluationNohar eval;

    public SymbolicView(String dataset, LineGraphic lineGraphic, BarGraphic barGraphic) {
        this.lineGraphic = lineGraphic;
        this.barGraphic = barGraphic;
        this.bufferStreaming = new BufferStreaming();

        Params params = new Params(Parameters.WINDOW_SIZE, Parameters.WORD_LENGTH_PAA,
                Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.NORMALIZATION_THRESHOLD, NumerosityReductionStrategy.EXACT);

        this.nohar = new NOHAR(this, params);
        this.eval = new EvaluationNohar();
        eval.setDataset(dataset);
        eval.setOffset(Parameters.OFFSET);
        eval.setParams(params);
    }

    public void runDataset(TimeSeries[] data, boolean norm) {
        eval.setStartTime(System.currentTimeMillis());

        //this.lineGraphic.prepareStream(data);
        this.lineGraphic.prepareStream();

        //Access each position from time series - streaming
        for (int position = Parameters.WINDOW_SIZE; position < (data[0].getLength() - Parameters.WINDOW_SIZE); position++) {
            if (ConstGeneral.STOP_STREAM) {
                break;
            }

            //Get array subsequence
            TimeSeries[] subsequences = getSubsequences(data, position, norm);

            TimeSeries labels = subsequences[subsequences.length - 1];
            TimeSeries subsequence = DataFusion.Magnitude(subsequences);//====== atencao! metodo alterado

            bufferStreaming.setSubSequence(subsequence);

            //Get current value and process 
            double currentValue = subsequence.getData(subsequence.getLength() - 1);
            double label = classMoreFrequency(labels);
            //double label = labels.getData(labels.getLength() - 1);
            processStream(currentValue, position, label);

            //values to GUI
            double[] values = new double[1];
            values[0] = currentValue;
            addDataGraphLine(values);
            updateLabel(label + "");
        }
        eval.setEndTime(System.currentTimeMillis());

        printEvaluation();
    }

    private TimeSeries[] getSubsequences(TimeSeries[] data, int position, boolean norm) {
        //Get array subsequence
        TimeSeries[] subsequences = new TimeSeries[data.length];
        int dataColumn = 0;
        for (TimeSeries ts : data) {
            //Get current window - windowing
            if (dataColumn < data.length - 1) {
                TimeSeries subsequence = ts.getSubsequence(position - Parameters.WINDOW_SIZE, Parameters.WINDOW_SIZE, norm);
                subsequences[dataColumn] = subsequence;
            } else {//Because last column is label, norm is false
                TimeSeries subsequence = ts.getSubsequence(position - Parameters.WINDOW_SIZE, Parameters.WINDOW_SIZE, false);
                subsequences[dataColumn] = subsequence;
            }
            dataColumn++;
        }
        return subsequences;
    }

    public Double classMoreFrequency(TimeSeries labels) {
        HashMap<Double, Integer> frequency = new HashMap<>();
        for (int i = 0; i < labels.getLength(); i++) {
            Double value = labels.getData(i);
            if (frequency.containsKey(value)) {
                int cont = frequency.get(value);
                frequency.put(value, cont++);
            } else {
                frequency.put(value, 1);
            }
        }
        int maior = 0;
        Double classe = -1.;
        for (Double value : frequency.keySet()) {
            if (frequency.get(value) > maior) {
                maior = frequency.get(value);
                classe = value;
            }
        }
        return classe;
    }

    private void processStream(double currentValue, int position, double label) {
        if (nohar != null) {
            //System.out.println(position + ") " + label);
            nohar.runStream(currentValue, position, label);
        }
    }

    /*
     *   GUI
     */
    private void addDataGraphLine(double[] values) {
        if (ConstGeneral.UPDATE_GUI) {
            lineGraphic.addData(values);
            lineGraphic.espera(5);
        }
    }

    public void addMarkerGraphLine(int position, Color color) {
        if (ConstGeneral.UPDATE_GUI) {
            lineGraphic.addMarker(position, position, color);
        }
    }

    private void updateLabel(String label) {
        if (ConstGeneral.UPDATE_GUI) {
            ConstGeneral.TELA_PRINCIPAL.lb_label.setText(label + "");
        }
    }

    public void updateCurrentHistogram(WordRecord word, String title) {
        if (ConstGeneral.UPDATE_GUI) {
            barGraphic.setTitle(title);
            barGraphic.addUpdateData(word.getWord(), word.getFrequency());
            ConstGeneral.TELA_PRINCIPAL.updateSymbolicTab(word, bufferStreaming.getBufferBOP().size());
        }
    }

    public void addHistogramsNovel(LinkedList<BOP> BOP) {
        if (ConstGeneral.UPDATE_GUI) {
            ConstGeneral.TELA_PRINCIPAL.addHistograms(
                    ConstGeneral.TELA_PRINCIPAL.sc_novel, ConstGeneral.TELA_PRINCIPAL.pn_novel, BOP);
        }
    }

    public void addHistogramsModel(LinkedList<BOP> BOP) {
        if (ConstGeneral.UPDATE_GUI) {
            ConstGeneral.TELA_PRINCIPAL.addHistograms(
                    ConstGeneral.TELA_PRINCIPAL.sc_model, ConstGeneral.TELA_PRINCIPAL.pn_model, BOP);
        }
    }

    public void clearCurrentHistogram() {
        if (ConstGeneral.UPDATE_GUI) {
            ConstGeneral.TELA_PRINCIPAL.clearCurrentHistogram();
            bufferStreaming.setBOP(new BOP());
        }
    }

    public void updateLog(String text) {
        if (ConstGeneral.UPDATE_GUI) {
            ConstGeneral.TELA_PRINCIPAL.updateSymbolicLog(text);
        }
    }

    private void printEvaluation() {
        ConstGeneral.TELA_PRINCIPAL.updateSymbolicLog(eval.toString());
        ConstGeneral.STOP_STREAM = false;
    }

    public BufferStreaming getBuffer() {
        return bufferStreaming;
    }

    public EvaluationNohar getEval() {
        return eval;
    }

}
