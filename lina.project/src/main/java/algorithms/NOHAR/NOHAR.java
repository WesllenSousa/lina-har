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
import controle.constants.ConstGeneral;
import controle.constants.Parameters;
import datasets.memory.BufferStreaming;
import datasets.memory.WordRecord;
import java.awt.Color;
import java.util.LinkedList;
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

    public NOHAR(SymbolicView symbolicView, Params params) {
        this.symbolicView = symbolicView;

        adwin = new Adwin(.002, Parameters.BOP_SIZE);
        pageHinkley = new PageHinkley();
        this.params = params;
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

            //Classify
            if (!classify(symbolicView.getBuffer(), symbolicView.getBuffer().getBOP())) {
                //Learning
                if (!checkNovel(symbolicView.getBuffer(), symbolicView.getBuffer().getBOP())) {
                    checkUnknown(symbolicView.getBuffer(), symbolicView.getBuffer().getBOP());
                }
                //Forget
                checkForget(symbolicView.getBuffer());
            }

            //*********View Updates*******************
            symbolicView.addMarkerGraphLine(position, Color.BLUE);
            symbolicView.addHistogramsNovel(symbolicView.getBuffer().getListNovelBOP());
            symbolicView.addHistogramsModel(symbolicView.getBuffer().getModel());
            symbolicView.clearCurrentHistogram();
            symbolicView.updateStatus();
            contConsistentChunkValue = 0;
            cleanBuffer();
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
    private boolean classify(BufferStreaming buffer, BOP newBop) {
        //Verificar se pode fazer fusão dos histogramas do modelo
        LinkedList<BOP> BOPs = listSimilarBOPs(buffer.getModel(), newBop);
        BOP minBOP = minDistanceBOP(BOPs, newBop, "classify");
        if (minBOP != null) {
            newBop.setLabel(minBOP.getLabel());
            compareLabel(newBop, "classify");
        }
        if (BOPs.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private void compareLabel(BOP bop, String origem) {
        if (bop.getLabel() == currentLabel) {
            if (bop.getCountNovel() > 0) {
                symbolicView.getEval().incrementHistsNovel();
            } else {
                symbolicView.getEval().incrementHists();
            }
            symbolicView.updateLog(">> Right: " + bop.getLabel() + " - " + origem);
        } else if (bop.getLabel() != -1) {
            if (bop.getCountNovel() > 0) {
                symbolicView.getEval().incrementErrorsNovel();
            } else {
                symbolicView.getEval().incrementErrors();
            }
            symbolicView.updateLog(">> Wrong: " + bop.getLabel() + ", Right: " + currentLabel + " - " + origem);
        }
    }

    /*
     *   Learning
     */
    private boolean checkNovel(BufferStreaming buffer, BOP newBop) {

        LinkedList<BOP> novelBOPs = listSimilarBOPs(buffer.getListNovelBOP(), newBop);
        BOP minNovelBOP = minDistanceBOP(novelBOPs, newBop, "Novel");

        if (minNovelBOP != null) {
            if (minNovelBOP.getDecision() == EnumHistogram.SLACK) {
                symbolicView.updateLog("Fusion: novel: " + minNovelBOP.getLabel() + " to " + currentLabel);
                fusionHistogram(buffer.getListNovelBOP(), minNovelBOP, newBop);
            } else {
                symbolicView.updateLog("Similar: novel: " + minNovelBOP.getLabel() + " to " + currentLabel);
            }
            minNovelBOP.incrementCountNovel();
            if (minNovelBOP.getCountNovel() > COUNT_THRESHOLD_BOP) {
                minNovelBOP.setCountNovel(0);
                buffer.getModel().add(minNovelBOP);
                buffer.getListNovelBOP().remove(minNovelBOP);
                symbolicView.updateLog("Added reference histogram!");
            }
            compareLabel(minNovelBOP, "novel");
        }
        return minNovelBOP != null;
    }

    //Before active learnig
    private void checkUnknown(BufferStreaming buffer, BOP newBop) {

        LinkedList<BOP> uBOPs = listSimilarBOPs(buffer.getListUBOP(), newBop);
        if (uBOPs.isEmpty()) {
            newBop.setLabel(currentLabel);//Only by test, print Similar uBOP up
            buffer.getListUBOP().add(newBop);
            symbolicView.updateLog("Added new unknown BOP...");
            return;
        }
        BOP minuBOP = minDistanceBOP(uBOPs, newBop, "Unknown");

        if (minuBOP != null) {
            if (minuBOP.getDecision() == EnumHistogram.SLACK) {
                symbolicView.updateLog("Fusion: unknown: " + minuBOP.getLabel() + " to " + currentLabel);
                fusionHistogram(buffer.getListUBOP(), minuBOP, newBop);
            } else {
                symbolicView.updateLog("Similar: unknown: " + minuBOP.getLabel() + " to " + currentLabel);
            }
            minuBOP.incrementCountUnk();
            //So entra aqui uma vez
            if (minuBOP.getCountUnk() > COUNT_THRESHOLD_BOP) {
                minuBOP.setCountUnk(0);
                String label = activeLearning(minuBOP.getLabel() + "", "");
                minuBOP.setLabel(Double.parseDouble(label));
                buffer.getListNovelBOP().add(minuBOP);
                buffer.getListUBOP().remove(minuBOP);
                symbolicView.updateLog("Added novel BOP...");
            }
        }
    }

    private String activeLearning(String posssibleLabel, String description) {
        //Active learning
        String label = posssibleLabel;
        if (ConstGeneral.UPDATE_GUI) {
            Messages msg = new Messages();
            label = msg.inserirDadosComValorInicial("Is it similar to " + posssibleLabel + "? " + description, posssibleLabel);
        }
        return label;
    }

    private void checkForget(BufferStreaming buffer) {
        for (BOP bop : buffer.getModel()) {
            //update weight bop
        }
    }

    /*
     *   Others
     */
    private int totalDistance(BOP newBop) {
        // Distance if there is no matching word
        int totalDistance = 0;
        for (WordRecord bop : newBop.getHistogram()) {
            totalDistance += bop.getFrequency();
        }
        return totalDistance;
    }

    private LinkedList<BOP> listSimilarBOPs(LinkedList<BOP> BOPs, BOP newBop) {
        LinkedList<BOP> similarBOP = new LinkedList<>();
        for (BOP bop : BOPs) {
            EnumHistogram decision;
            if (bop.getHistogram().size() > newBop.getHistogram().size()) {
                decision = classifyDistance(bop, newBop);
            } else {
                decision = classifyDistance(newBop, bop);
            }
            if (decision == EnumHistogram.EQUAL || decision == EnumHistogram.INSIDE || decision == EnumHistogram.SLACK) {
                bop.setDecision(decision);
                similarBOP.add(bop);
            }
        }
        return similarBOP;
    }

    private BOP minDistanceBOP(LinkedList<BOP> BOP, BOP newBop, String origem) {
        BOP minBop = null;
        if (BOP.size() == 1) {
            minBop = BOP.get(0);
        } else if (BOP.size() > 1) {
            int[] distances;
            int minDistance = Integer.MAX_VALUE;
            double lastLabel = -1;
            for (BOP bop : BOP) {
                //If different label call active learning, it works like update component
                if (lastLabel != -1 && lastLabel != bop.getLabel()) {
                    symbolicView.updateLog("Conflit labels! " + origem);
                    String label = activeLearning(bop.getLabel() + "", "Conflit labels! " + origem);
                    minBop = bop;
                    minBop.setLabel(Double.parseDouble(label));
                    break;
                }
                if (bop.getHistogram().size() > newBop.getHistogram().size()) {
                    distances = calcDistanceBetweenBOP(bop, newBop);
                } else {
                    distances = calcDistanceBetweenBOP(newBop, bop);
                }
                if (distances[0] != -1 && (distances[0] + distances[1]) < minDistance) {
                    minDistance = distances[0] + distances[1];
                    minBop = bop;
                }
                lastLabel = bop.getLabel();
            }
        }
        return minBop;
    }

    private int[] calcDistanceBetweenBOP(BOP bigBop, BOP smallBop) {
        int insideDistance = -1;
        int outsideDistance = 0;
        boolean hasWord;
        for (WordRecord big : bigBop.getHistogram()) {
            hasWord = false;
            for (WordRecord small : smallBop.getHistogram()) {
                if (big.equals(small)) {
                    if (insideDistance == -1) {
                        insideDistance = 0;
                    }
                    int diff = 0;
                    if (big.getFrequency() > small.getFrequency()) {
                        diff = big.getFrequency() - small.getFrequency();
                    } else {
                        diff = small.getFrequency() - big.getFrequency();
                    }
                    insideDistance += diff;
                    hasWord = true;
                    break;
                }
            }
            if (!hasWord) {
                outsideDistance += big.getFrequency();
            }
        }
        int[] distances = new int[2];
        distances[0] = insideDistance;
        distances[1] = outsideDistance;
        return distances;
    }

    private EnumHistogram classifyDistance(BOP bigBop, BOP smallBop) {

        int[] distances = calcDistanceBetweenBOP(bigBop, smallBop);
        int insideDistance = distances[0];
        int outsideDistance = distances[1];
        int totalSmallBopDistance = totalDistance(smallBop);
        int totalBigBopDistance = totalDistance(bigBop);

        //Confidence Level
        if (insideDistance == -1) {
            return EnumHistogram.OUTSIDE;
        } else if (insideDistance == 0 && outsideDistance == 0) {
            return EnumHistogram.EQUAL;
        } else {
            double percentSmall = (insideDistance * 100) / totalSmallBopDistance;
            double percentBig = (outsideDistance * 100) / totalBigBopDistance;
            if (percentSmall < 25 && percentBig < 25) {
                return EnumHistogram.INSIDE;
            } else if (percentSmall < 50 && percentBig < 50) {
                return EnumHistogram.SLACK;
            } else {
                return EnumHistogram.OUTSIDE;
            }
        }
    }

    private void fusionHistogram(LinkedList<BOP> BOPs, BOP minBop, BOP newBop) {
        for (BOP bop : BOPs) {
            if (bop.equals(minBop)) {
                BOP bigBop, smallBOP;
                if (bop.getHistogram().size() > newBop.getHistogram().size()) {
                    bigBop = bop;
                    smallBOP = newBop;
                } else {
                    bigBop = newBop;
                    smallBOP = bop;
                }
                for (WordRecord big : bigBop.getHistogram()) {
                    for (WordRecord small : smallBOP.getHistogram()) {
                        if (big.equals(small)) {
                            float mean = (big.getFrequency() + small.getFrequency()) / 2;
                            big.setFrequency(Math.round(mean));
                        }
                    }
                }
                break;
            }
        }
    }

    private void cleanBuffer() {
        //Clean buffer excess
        symbolicView.getBuffer().setBOP(new BOP());
        if (symbolicView.getBuffer().getBufferBOP().size() > 5) {
            symbolicView.getBuffer().getBufferBOP().remove(0);
        }
        //É muito importante monitorar a quantidade do uBOP
        if (symbolicView.getBuffer().getListUBOP().size() > 100) {
            symbolicView.getBuffer().getListUBOP().remove(0);
        }
    }

}
