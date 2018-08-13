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
    private int COUNT_THRESHOLD_BOP = 5;

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
    private boolean classify(LinkedList<BOP> BOPs, BOP newBop) {
        int minDistance = Integer.MAX_VALUE;
        for (BOP bop : BOPs) {
            int distance = calcDistanceBetweenBOPs(bop, newBop);
            if (distance != -1 && distance < minDistance) {
                minDistance = distance;
                newBop.setLabel(bop.getLabel());
                symbolicView.updateLog(">> min distance: " + minDistance);
            }
        }
        return compareLabel(newBop);
    }

    private boolean compareLabel(BOP bop) {
        if (bop.getLabel() == currentLabel) {
            eval.incrementHists();
            symbolicView.updateLog(">> Classify acertou: " + bop.getLabel());
            return true;
        } else if (bop.getLabel() != -1) {
            eval.incrementErrors();
            symbolicView.updateLog(">> Classify errou: " + bop.getLabel() + ", right: " + currentLabel);
        }
        return false;
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
            symbolicView.updateLog("model");
        }
        return false;
    }

    //After active learning
    private boolean checkNovel(BufferStreaming buffer, BOP newBop) {
        boolean statusFusion = false;
        for (BOP novelBop : buffer.getListNovelBOP()) {
            
            int totalDistance = totalDistance(newBop);
            int distance = calcDistanceBetweenBOPs(novelBop, newBop);
            double percent = (distance * 100) / totalDistance;
            
            if (distance != -1 && percent <= 5) {//5% equal
                symbolicView.updateLog("Novel BOP: " + novelBop.getLabel());
                fusionHistogram(novelBop, newBop, distance);
                novelBop.incrementCountNovel();
                newBop.setLabel(novelBop.getLabel());
                statusFusion = true;
            }
            if (novelBop.getCountNovel() > COUNT_THRESHOLD_BOP) {
                buffer.getModel().add(novelBop);
                buffer.getListNovelBOP().remove(novelBop);
                symbolicView.updateLog("Added reference histogram!");
                break;
            }
        }
        compareLabel(newBop);
        return statusFusion;
    }

    //Before active learnig
    private void checkUnknown(BufferStreaming buffer, BOP newBop) {
        boolean similar = false;
        for (BOP uBOP : buffer.getListUBOP()) {
            
            int totalDistance = totalDistance(newBop);
            int distance = calcDistanceBetweenBOPs(uBOP, newBop);
            double percent = (distance * 100) / totalDistance;
            
            if (distance != -1 && percent <= 10) {//10% equal
                symbolicView.updateLog("Similar uBOP " + uBOP.getLabel() + " to " + currentLabel);
                fusionHistogram(uBOP, newBop, distance);
                uBOP.incrementCountUnk();
                similar = true;
            }
            if (uBOP.getCountUnk() > COUNT_THRESHOLD_BOP) {//10 times to be novel
                //Active learning
                Messages msg = new Messages();
                String label = msg.inserirDadosComValorInicial("What's activity name?", currentLabel + "");
                if (label != null) {
                    uBOP.setLabel(Double.parseDouble(label));
                    buffer.getListNovelBOP().add(uBOP);
                    buffer.getListUBOP().remove(uBOP);
                    symbolicView.updateLog("Added novel BOP...");
                    break;
                }
            }
        }
        if (!similar || buffer.getListUBOP().isEmpty()) {
            newBop.setLabel(currentLabel);//Only by test
            buffer.getListUBOP().add(newBop);
            symbolicView.updateLog("Added new unknown BOP...");
        }
    }

    private void checkForget(BufferStreaming buffer) {
        for (BOP bop : buffer.getModel()) {
            //update weight bop
        }
    }

    private int totalDistance(BOP newBop) {
        // Distance if there is no matching word
        int noMatchDistance = -1;
        for (WordRecord bop : newBop.getHistogram()) {
            noMatchDistance += bop.getFrequency() * bop.getFrequency();
        }
        return noMatchDistance;
    }

    private int calcDistanceBetweenBOPs(BOP bop, BOP newBop) {
        int distance = -1;
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

    private void fusionHistogram(BOP bop, BOP newBop, int distance) {
        if (distance > 0) {
            symbolicView.updateLog("Fusion histogram - distance " + distance);
            for (WordRecord wordBOP : bop.getHistogram()) {
                for (WordRecord wordNewBOP : newBop.getHistogram()) {
                    if (wordBOP.equals(wordNewBOP)) {
                        float mean = (wordBOP.getFrequency() + wordNewBOP.getFrequency()) / 2;
                        wordBOP.setFrequency(Math.round(mean));
                    }
                }
            }
        }
    }

}
