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
            //Add histogram to buffer
            symbolicView.getBuffer().getBOP().orderWordsHistogram(); //important!
            symbolicView.getBuffer().getBufferBOP().add(symbolicView.getBuffer().getBOP());
            //Clean buffer excess
            if (symbolicView.getBuffer().getBufferBOP().size() > 5) {
                symbolicView.getBuffer().getBufferBOP().remove(0);
            }

            //Classify
            if (!classify(symbolicView.getBuffer().getModel(), symbolicView.getBuffer().getBOP())) {
                //Learning
                learning(symbolicView.getBuffer(), symbolicView.getBuffer().getBOP());
            }

            //*********View Updates*******************
            symbolicView.addMarkerGraphLine(position, Color.BLUE);
            symbolicView.addHistogramsNovel(symbolicView.getBuffer().getListNovelBOP());
            symbolicView.addHistogramsModel(symbolicView.getBuffer().getModel());
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
                    symbolicView.updateCurrentHistogram(wordRecord, currentLabel + "");
                }
            }
        } else {
            //Add word in buffer
            bop.getHistogram().add(word);
            symbolicView.updateCurrentHistogram(word, currentLabel + "");
        }
    }

    /*
     *   Classification
     */
    private boolean classify(LinkedList<BOP> BOPs, BOP newBop) {
        BOP bop = minDistanceBop(BOPs, newBop);
        if (bop != null) {
            newBop.setLabel(bop.getLabel());
            return compareLabel(newBop, "classify");
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
            //symbolicView.updateLog("model");
        }
        return false;
    }

    //After active learning
    private boolean checkNovel(BufferStreaming buffer, BOP newBop) {
        boolean similar = false;
        BOP novelBop = minDistanceBop(buffer.getListNovelBOP(), newBop);
        if (novelBop != null) {
            int distance = calcDistanceBetweenBOPs(novelBop, newBop);
            if (distance > 0) {
                symbolicView.updateLog("Novel BOP fusion: " + novelBop.getLabel());
                fusionHistogram(novelBop, newBop, distance);
            }
            novelBop.incrementCountNovel();
            if (novelBop.getCountNovel() > COUNT_THRESHOLD_BOP) {
                novelBop.setCountNovel(0);
                buffer.getModel().add(novelBop);
                buffer.getListNovelBOP().remove(novelBop);
                symbolicView.updateLog("Added reference histogram!");
            }
            similar = true;
            symbolicView.updateLog("Novel BOP similar: " + novelBop.getLabel());
        }
        return similar;
    }

    //Before active learnig
    private void checkUnknown(BufferStreaming buffer, BOP newBop) {
        boolean similar = false;
        BOP uBOP = minDistanceBop(buffer.getListUBOP(), newBop);
        if (uBOP != null) {
            int distance = calcDistanceBetweenBOPs(uBOP, newBop);
            if (distance > 0) {
                symbolicView.updateLog("uBOP fusion: " + uBOP.getLabel() + " to " + currentLabel);
                fusionHistogram(uBOP, newBop, distance);
            } else {
                symbolicView.updateLog("uBOP Similar: " + uBOP.getLabel() + " to " + currentLabel);
            }
            uBOP.incrementCountUnk();
            if (uBOP.getCountUnk() > COUNT_THRESHOLD_BOP) {//10 times to be novel
                //Active learning
                Messages msg = new Messages();
                String label = msg.inserirDadosComValorInicial("Is it similar to " + uBOP.getLabel() + "?", uBOP.getLabel() + "");
                if (label != null) {
                    uBOP.setCountUnk(0);
                    uBOP.setLabel(Double.parseDouble(label));
                    buffer.getListNovelBOP().add(uBOP);
                    buffer.getListUBOP().remove(uBOP);
                    symbolicView.updateLog("Added novel BOP...");
                }
            }
            similar = true;
        }
        if (!similar) {
            newBop.setLabel(currentLabel);//Only by test, print Similar uBOP up
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
        int totalDistance = 0;
        for (WordRecord bop : newBop.getHistogram()) {
            totalDistance += bop.getFrequency();
        }
        return totalDistance;
    }

    private BOP minDistanceBop(LinkedList<BOP> BOPs, BOP newBop) {
        BOP b = null;
        int minDistance = Integer.MAX_VALUE;
        for (BOP bop : BOPs) {
            int distance = -1;
            if (bop.getHistogram().size() > newBop.getHistogram().size()) {
                distance = calcDistanceBetweenBOPs(bop, newBop);
            } else {
                distance = calcDistanceBetweenBOPs(newBop, bop);
            }
            if (distance != -1 && distance < minDistance) {
                minDistance = distance;
                b = bop;
            }
        }
        return b;
    }

    private int calcDistanceBetweenBOPs(BOP bigBop, BOP smallBop) {
        int totalSmallBopDistance = totalDistance(smallBop);
        int totalBigBopDistance = totalDistance(bigBop);
        int insideDistance = totalSmallBopDistance;
        int outsideDistance = totalBigBopDistance;
        boolean hasWord;
        for (WordRecord big : bigBop.getHistogram()) {
            hasWord = false;
            for (WordRecord small : smallBop.getHistogram()) {
                if (big.equals(small)) {
                    int diff = 0;
                    if (big.getFrequency() > small.getFrequency()) {
                        diff = big.getFrequency() - small.getFrequency();
                    } else {
                        diff = small.getFrequency() - big.getFrequency();
                    }
                    insideDistance -= small.getFrequency() - diff;
                    hasWord = true;
                    break;
                }
            }
            if (!hasWord) {
                outsideDistance -= big.getFrequency();
            }
        }
        double percentSmall = (insideDistance * 100) / totalSmallBopDistance;
        double percentBig = (outsideDistance * 100) / totalBigBopDistance;
        if (percentSmall > 30 && percentBig < 70) {
            return -1;
        } else {
            return insideDistance + outsideDistance;
        }
    }

    private void fusionHistogram(BOP bop, BOP newBop, int distance) {
        if (distance > 0) {
            //symbolicView.updateLog("Fusion histogram - distance " + distance);
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

    private boolean compareLabel(BOP bop, String origem) {
        if (bop.getLabel() == currentLabel) {
            eval.incrementHists();
            symbolicView.updateLog(">> Right: " + bop.getLabel() + " - " + origem);
            return true;
        } else if (bop.getLabel() != -1) {
            eval.incrementErrors();
            symbolicView.updateLog(">> Wrong: " + bop.getLabel() + ", Right: " + currentLabel + " - " + origem);
        }
        return false;
    }

}
