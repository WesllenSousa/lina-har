/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.viewControler;

import algorithms.Features.FiltersSignalProcessing;
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

    private final BufferStreaming bufferStreaming;
    private final NOHAR nohar;
    private final EvaluationNohar eval;

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
        eval.setBuffer(bufferStreaming);
        eval.setBopSize(Parameters.BOP_SIZE);
    }

    public void runDataset(TimeSeries[] data) {
        bufferStreaming.clearBuffer();
        eval.setStartTime(System.currentTimeMillis());

        //Access each position from time series - streaming
        for (int position = Parameters.WINDOW_SIZE; position < (data[0].getLength() - Parameters.WINDOW_SIZE); position++) {
            if (ConstGeneral.STOP_STREAM) {
                break;
            }
            //Get array subsequence
            double label = 0;
            if (ConstGeneral.WITH_LABEL) {
                TimeSeries labels = getLabels(data, position);
                label = classMoreFrequency(labels);
            }
            //Get sub sequences
            TimeSeries[] subSequences = getSubsequences(data, position);
            double[] currentValues = new double[subSequences.length];
            for (int i = 0; i < subSequences.length; i++) {
                currentValues[i] = subSequences[i].getData(subSequences[i].getLength() - 1);
            }
            bufferStreaming.setSubSequences(subSequences);

            //Process subsequences
            processStream(currentValues, position, label);

            //values to GUI
            addDataGraphLine(currentValues, label + "");
            updateLabel(label + "");
            if (position % 100 == 0) {
//                eval.updateTime();
//                eval.printCurrentAccuracy();
//                eval.printCurrentError();
//                eval.printActiveLearning(label);
            }
        }
        eval.setEndTime(System.currentTimeMillis());

        printEvaluation();
    }

    private TimeSeries[] getSubsequences(TimeSeries[] data, int position) {
        int n = data.length;
        if (ConstGeneral.WITH_LABEL) {
            n = data.length - 1;
        }
        TimeSeries[] subsequences = new TimeSeries[n];
        for (int index = 0; index < n; index++) {
            //Get array subsequence
            TimeSeries subSequence = data[index].getSubsequence(position - Parameters.WINDOW_SIZE, Parameters.WINDOW_SIZE, false);
            TimeSeries exponencial = new TimeSeries(
                    FiltersSignalProcessing.SingleExponential(subSequence.getData(), 0.6)); // maior que 0.5 aumenta a variacia, menor 0.5 diminui a variacia
            TimeSeries lowpass = new TimeSeries(
                    FiltersSignalProcessing.SingleLowPass(exponencial.getData(), Parameters.WINDOW_SIZE));
            subsequences[index] = lowpass;
        }
        return subsequences;
    }

    private TimeSeries getLabels(TimeSeries[] data, int position) {
        return data[data.length - 1].getSubsequence(position - Parameters.WINDOW_SIZE, Parameters.WINDOW_SIZE, false);
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

    private void processStream(double[] currentValues, int position, double label) {
        if (nohar != null) {
            //System.out.println(position + ") " + label);
            nohar.runStream(currentValues, position, label);
        }
    }

    /*
     *   GUI
     */
    private void addDataGraphLine(double[] values, String label) {
        if (ConstGeneral.UPDATE_GUI) {
            if (lineGraphic.getSeriesCount() == 0) {
                this.lineGraphic.prepareStream(new TimeSeries[values.length]);
            }
            lineGraphic.addData(values, label);
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

    public void updateStatus() {
        if (ConstGeneral.UPDATE_GUI) {
            ConstGeneral.TELA_PRINCIPAL.lb_countuBOP.setText(bufferStreaming.getListUBOP().size() + "");
            ConstGeneral.TELA_PRINCIPAL.lb_countNovelBop.setText(bufferStreaming.getListNovelBOP().size() + "");
            ConstGeneral.TELA_PRINCIPAL.lb_model.setText(bufferStreaming.getModel().size() + "");
        }
    }

    public void updateCurrentHistogram(List<WordRecord> histogram, WordRecord word, String title) {
        if (ConstGeneral.UPDATE_GUI) {
            barGraphic.setTitle(title);
            barGraphic.addUpdateData(word.getWord(), word.getFrequency());
            ConstGeneral.TELA_PRINCIPAL.updateSymbolicTab(word, histogram.size());
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
        }
    }

    public void updateLog(String text) {
        if (ConstGeneral.UPDATE_GUI) {
            ConstGeneral.TELA_PRINCIPAL.updateSymbolicLog(text);
        }
    }

    private void printEvaluation() {
        ConstGeneral.TELA_PRINCIPAL.lb_countuBOP.setText(bufferStreaming.getListUBOP().size() + "");
        ConstGeneral.TELA_PRINCIPAL.lb_countNovelBop.setText(bufferStreaming.getListNovelBOP().size() + "");
        ConstGeneral.TELA_PRINCIPAL.lb_model.setText(bufferStreaming.getModel().size() + "");
        ConstGeneral.TELA_PRINCIPAL.updateSymbolicLog(eval.toString());
        ConstGeneral.TELA_PRINCIPAL.addHistograms(
                ConstGeneral.TELA_PRINCIPAL.sc_novel, ConstGeneral.TELA_PRINCIPAL.pn_novel,
                bufferStreaming.getListNovelBOP());
        ConstGeneral.TELA_PRINCIPAL.addHistograms(
                ConstGeneral.TELA_PRINCIPAL.sc_model, ConstGeneral.TELA_PRINCIPAL.pn_model,
                bufferStreaming.getModel());
        ConstGeneral.STOP_STREAM = false;
    }

    public BufferStreaming getBuffer() {
        return bufferStreaming;
    }

    public EvaluationNohar getEval() {
        return eval;
    }

}
