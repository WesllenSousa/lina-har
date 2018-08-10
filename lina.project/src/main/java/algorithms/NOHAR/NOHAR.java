/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR;

import algorithms.NOHAR.ADWIN.Adwin;
import algorithms.NOHAR.PageHinkley.PageHinkley;
import algorithms.SAX.Params;
import algorithms.SAX.SAX;
import controle.constants.Parameters;
import datasets.memory.BufferStreaming;
import datasets.memory.WordRecord;
import java.awt.Color;
import java.util.LinkedList;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;
import util.Messages;
import view.viewControler.SymbolicView;

/**
 *
 * @author Wesllen Sousa
 */
public class NOHAR {

    private final SymbolicView symbolicView;

    private final Adwin adwin;
    private final PageHinkley pageHinkley;
    private final Params params;

    private double currentLabel = -1;
    private int contConsistentChunkValue = 0;

    private EvaluationNohar eval;

    public NOHAR(SymbolicView symbolicView) {
        this.symbolicView = symbolicView;

        adwin = new Adwin(.002, Parameters.BOP_SIZE);
        pageHinkley = new PageHinkley();
        params = new Params(Parameters.WINDOW_SIZE, Parameters.WORD_LENGTH_PAA,
                Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.NORMALIZATION_THRESHOLD, NumerosityReductionStrategy.EXACT);
        eval = new EvaluationNohar();
    }

    public EvaluationNohar getEval() {
        return eval;
    }

    public void runStream(double currentValue, int position, double label) {
        this.currentLabel = label;
        //Monitor change the data
        if (changeDetected(currentValue, position)) {
            contConsistentChunkValue = 0; //Reset chunk
            symbolicView.clearCurrentHistogram();
            return;
        }
        contConsistentChunkValue++;
        if (contConsistentChunkValue % Parameters.OFFSET == 0) {
            //Discretize
            WordRecord word = discretize(symbolicView.getBuffer(), position);
            //Update Histogram
            updateBOP(symbolicView.getBuffer().getBOP(), word);
        }
        //Handle model
        if (contConsistentChunkValue >= Parameters.BOP_SIZE) {
            symbolicView.getBuffer().getBOP().orderWordsHistogram();
            //Classify
            if (!classify(symbolicView.getBuffer().getModel(), symbolicView.getBuffer().getBOP())) {
                //Learning
                learning(symbolicView.getBuffer(), symbolicView.getBuffer().getBOP());
            }
            //*********View Updates*******************
            symbolicView.addMarkerGraphLine(position, Color.BLUE);
            symbolicView.addHistograms();
            symbolicView.clearCurrentHistogram();
            contConsistentChunkValue = 0;
        }
    }

    /*
     *   Detecção de mudança
     */
    private boolean changeDetected(double currentValue, int position) {
        if (Parameters.CHANGE_DETECTION == 0) {
            if (adwin.setInput(currentValue)) {
//                symbolicView.updateLog("Change Detected: " + position);
                symbolicView.addMarkerGraphLine(position, Color.RED);
                return true;
            }
        } else if (pageHinkley.runStreaming(currentValue, position)) {
//            symbolicView.updateLog("Change Detected: " + position);
            symbolicView.addMarkerGraphLine(position, Color.RED);
            return true;
        }
        return false;
    }

    /*
     *   Discretização
     */
    private WordRecord discretize(BufferStreaming buffer, int position) {
        SAX sax = new SAX(params);
        String word = sax.serieToWord(buffer.getSubSequence().getData());
        WordRecord wordRecord = buffer.populaWordRecord(word, position - Parameters.WINDOW_SIZE);
        return wordRecord;
    }

    /*
     *   Criação de histogramas
     */
    private void updateBOP(BOP bop, WordRecord word) {
        if (word == null) {
            return;
        }
        //Verify if word is in the buffer BOP
        if (bop.getHistogram().contains(word)) {
            for (WordRecord wordRecord : bop.getHistogram()) {
                if (wordRecord.getWord().equals(word.getWord())) {
                    wordRecord.getIntervals().add(word.getIntervals().get(0));
                    wordRecord.incrementFrequency();
                    symbolicView.updateCurrentHistogram(wordRecord);
                }
            }
        } else {
            //Add word in buffer
            bop.getHistogram().add(word);
            symbolicView.updateCurrentHistogram(word);
        }
    }

    /*
     *   Classification
     */
    private boolean classify(LinkedList<BOP> model, BOP newBop) {
        symbolicView.updateLog("Classifying new BOP...");
        int minDistance = Integer.MAX_VALUE;
        for (BOP bop : model) {
            int distance = calcDistanceBetweenBOPs(bop, newBop);
            if (distance != 0 && distance < minDistance) {
                minDistance = distance;
                newBop.setLabel(bop.getLabel());
                symbolicView.updateLog(">> min distance: " + minDistance);
            }
        }
        if (newBop.getLabel() == currentLabel) {
            eval.incrementHists();
            //symbolicView.updateLog(">> Acertou");
            return true;
        } else {
            eval.incrementErrors();
            //symbolicView.updateLog(">> Errou: " + currentLabel + " - " + newBop.getLabel());
            return false;
        }
    }

    /*
     *   Learning
     */
    private void learning(BufferStreaming buffer, BOP bop) {
        if (!update(buffer.getModel())) {
            if (!checkNovel(buffer, bop)) {
                checkUnknown(buffer, bop);
            }
        }
        checkForget(buffer);
    }

    private boolean update(LinkedList<BOP> model) {
        for (BOP bop : model) {
        }
        return false;
    }

    //After active learning
    private boolean checkNovel(BufferStreaming buffer, BOP bop) {
        for (BOP novelBOP : buffer.getListNovelBOP()) {
            if (novelBOP.getCountNovel() > 10) {
                //buffer.getModel().add(novelBOP);
                //buffer.getListNovelBOP().remove(novelBOP);
            }
        }
        return false;
    }

    //Before active learnig
    private void checkUnknown(BufferStreaming buffer, BOP newBop) {
        symbolicView.updateLog("Checking unknown BOP...");
        if (buffer.getListUBOP().isEmpty()) {
            buffer.getListUBOP().add(newBop);
            return;
        }

        boolean statusFusion = false;
        for (BOP uBOP : buffer.getListUBOP()) {

            int totalDistance = totalDistance(newBop);
            int distance = calcDistanceBetweenBOPs(uBOP, newBop);
            double percent = (distance * 100) / totalDistance;
            if (distance > 0 && percent <= 10) {//10% equal
                fusionHistogram(uBOP, newBop);
            }

            if (uBOP.getCountUnk() > 10) {
                //Active learning
                Messages msg = new Messages();
                String label = msg.inserirDadosComValorInicial("What's activity name?", currentLabel + "");
                uBOP.setLabel(Double.parseDouble(label));
                buffer.getListNovelBOP().add(uBOP);
                buffer.getListUBOP().remove(uBOP);
                break;
            }
        }
        if (!statusFusion) {
            buffer.getListUBOP().add(newBop);
        }
    }

    private void checkForget(BufferStreaming buffer) {
        for (BOP bop : buffer.getModel()) {
            //update weight bop
        }
    }

    private int totalDistance(BOP newBop) {
        // Distance if there is no matching word
        int noMatchDistance = 0;
        for (WordRecord nwBOP : newBop.getHistogram()) {
            noMatchDistance += nwBOP.getFrequency() * nwBOP.getFrequency();
        }
        return noMatchDistance;
    }

    private int calcDistanceBetweenBOPs(BOP bop, BOP newBop) {
        int distance = 0;
        for (WordRecord wordBOP : bop.getHistogram()) {
            for (WordRecord wordNewBOP : newBop.getHistogram()) {
                if (wordNewBOP.equals(wordBOP) && wordNewBOP.getFrequency() != 0) {
                    int diff = wordNewBOP.getFrequency() - wordBOP.getFrequency();
                    distance += diff * diff;
                }
            }
        }
        return distance;
    }

    private void fusionHistogram(BOP bop, BOP newBop) {
        symbolicView.updateLog("Fusion histogram...");
        for (WordRecord wordBOP : bop.getHistogram()) {
            for (WordRecord wordNewBOP : newBop.getHistogram()) {
                if (wordBOP.equals(wordNewBOP)) {
                    float mean = (wordBOP.getFrequency() + wordNewBOP.getFrequency()) / 2;
                    wordBOP.setFrequency(Math.round(mean));
                    bop.incrementCountUnk();
                }
            }
        }
    }

}
