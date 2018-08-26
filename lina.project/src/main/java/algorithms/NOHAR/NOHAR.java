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
import datasets.timeseries.TimeSeries;
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
    private final Params params;
    private final int COUNT_THRESHOLD_BOP = 5;
    private final int PESO = 20; //For frequency normalization where mean must be positive

    private Adwin adwin[];
    private PageHinkley pageHinkley[];

    private double currentLabel = -1;
    private int contConsistentChunkValue = 0;

    public NOHAR(SymbolicView symbolicView, Params params) {
        this.symbolicView = symbolicView;
        this.params = params;
    }

    public void runStream(double[] currentValues, int position, double label) {
        this.currentLabel = label;
//        if(currentLabel == 1.0) {
//            ConstGeneral.UPDATE_GUI = true;
//        } else {
//            ConstGeneral.UPDATE_GUI = false;
//        }

        //Monitor change the data
        if (changeDetected(currentValues, position)) {
            contConsistentChunkValue = 0; //Reset chunk
            symbolicView.clearCurrentHistogram();
            return;
        }

        contConsistentChunkValue++;
        if (contConsistentChunkValue % Parameters.OFFSET == 0) {
            TimeSeries[] subSequences = symbolicView.getBuffer().getSubSequences();
            //Discretization and update histogram
            for (int index = 0; index < currentValues.length; index++) {
                //Calcule frequency
                double mean = subSequences[index].calculateMean();
                //double std = subSequences[index].calculateStddev();
                double variance = subSequences[index].calculateVariance();
                int frequency = (int) Math.round(mean + variance + PESO);
//                if(frequency < 0) {
//                    System.out.println("");
//                }
                //Discretize
                WordRecord word = discretize(symbolicView.getBuffer(), position, index, frequency);
                //Update BOP
                updateBOP(symbolicView.getBuffer().getBOP(), word, frequency);
            }
        }

        //Handle model
        if (contConsistentChunkValue >= Parameters.BOP_SIZE) {
            //Add histogram to buffer
            symbolicView.getBuffer().getBOP().orderWordsHistogram(); //important!

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
    private boolean changeDetected(double[] currentValues, int position) {
        if (adwin == null && pageHinkley == null) {
            adwin = new Adwin[currentValues.length];
            pageHinkley = new PageHinkley[currentValues.length];
            for (int i = 0; i < currentValues.length; i++) {
                adwin[i] = new Adwin(.002, Parameters.BOP_SIZE, Parameters.WINDOW_SIZE);
                pageHinkley[i] = new PageHinkley(0.30);
            }
        }
        for (int i = 0; i < currentValues.length; i++) {
            double value = currentValues[i];
            if (value < 0) {//Because the mean can not be negative
                value = currentValues[i] * currentValues[i];
            }
            if (Parameters.CHANGE_DETECTION == 0) {
                if (adwin[i].setInput(value)) {
                    symbolicView.addMarkerGraphLine(position, Color.RED);
                    return true;
                }
            } else if (pageHinkley[i].runStreaming(value, position)) {
                symbolicView.addMarkerGraphLine(position, Color.RED);
                return true;
            }
        }
        return false;
    }

    /*
     *   Discretização
     */
    private WordRecord discretize(BufferStreaming buffer, int position, int index, int frequency) {
        SAX sax = new SAX(params);
        String word = sax.serieToWord(buffer.getSubSequences()[index].getData());
        word += index; //Different word for different axis
        WordRecord wordRecord = buffer.populaWordRecord(word, position - Parameters.WINDOW_SIZE, frequency);
        return wordRecord;
    }

    /*
     *   Criação de histogramas
     */
    private void updateBOP(BOP bop, WordRecord wordRecord, int frequency) {
        if (wordRecord == null) {
            return;
        }
        LinkedList<String> words = getWordsDictionary(wordRecord);

        //Verify if word is in the buffer BOP
        boolean existed = false;
        for (WordRecord wordBop : bop.getHistogram()) {
            for (String word : words) {
                if (wordBop.getWord().equals(word)) {
                    wordBop.getIntervals().add(wordRecord.getIntervals().get(0));
                    wordBop.incrementFrequency(frequency);
                    symbolicView.updateCurrentHistogram(wordBop, currentLabel + "");
                    existed = true;
                    break;
                }
            }
            if (existed) {
                break;
            }
        }
        if (!existed) {
            //Add word in buffer
            bop.getHistogram().add(wordRecord);
            symbolicView.updateCurrentHistogram(wordRecord, currentLabel + "");
        }
    }

    /*
     *   Classification
     */
    private boolean classify(BufferStreaming buffer, BOP newBop) {

        LinkedList<BOP> BOPs = listSimilarBOPs(buffer.getModel(), newBop);
        BOP minBOP = minDistanceBOP(BOPs, newBop, "Classify");

        if (minBOP != null) {
            if (minBOP.getDecision() == EnumHistogram.SLACK
                    && fusionHistogram(buffer.getModel(), minBOP, newBop)) {
                symbolicView.updateLog("Fusion model: " + minBOP.getLabel() + " to " + currentLabel);
            }
            newBop.setLabel(minBOP.getLabel());
            compareLabel(newBop, "Classify");
        }
        if (BOPs.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /*
     *   Learning
     */
    private boolean checkNovel(BufferStreaming buffer, BOP newBop) {

        LinkedList<BOP> novelBOPs = listSimilarBOPs(buffer.getListNovelBOP(), newBop);
        BOP minNovelBOP = minDistanceBOP(novelBOPs, newBop, "Novel");

        if (minNovelBOP != null) {
            if (minNovelBOP.getDecision() == EnumHistogram.SLACK
                    && fusionHistogram(buffer.getListNovelBOP(), minNovelBOP, newBop)) {
                symbolicView.updateLog("Fusion novel: " + minNovelBOP.getLabel() + " to " + currentLabel);
            } else {
                symbolicView.updateLog("Similar novel: " + minNovelBOP.getLabel() + " to " + currentLabel);
            }
            minNovelBOP.incrementCountNovel();
            if (minNovelBOP.getCountNovel() > COUNT_THRESHOLD_BOP) {
                minNovelBOP.setCountNovel(0);
                buffer.getModel().add(minNovelBOP);
                buffer.getListNovelBOP().remove(minNovelBOP);
                symbolicView.updateLog("Added reference histogram!");
            }
            compareLabel(minNovelBOP, "Novel");
        }
        return minNovelBOP != null;
    }

    //Before active learnig
    private void checkUnknown(BufferStreaming buffer, BOP newBop) {

        LinkedList<BOP> uBOPs = listSimilarBOPs(buffer.getListUBOP(), newBop);
        if (uBOPs.isEmpty()) {
            newBop.setLabel(currentLabel);//Only by test, print Similar uBOP up
            buffer.getListUBOP().add(newBop);
            symbolicView.getEval().incrementCountBOP(currentLabel);
            symbolicView.updateLog("Added new unknown BOP...");
            return;
        }
        BOP minuBOP = minDistanceBOP(uBOPs, newBop, "Unknown");

        if (minuBOP != null) {
            if (minuBOP.getDecision() == EnumHistogram.SLACK
                    && fusionHistogram(buffer.getListUBOP(), minuBOP, newBop)) {
                symbolicView.updateLog("Fusion unknown: " + minuBOP.getLabel() + " to " + currentLabel);
            } else {
                symbolicView.updateLog("Similar unknown: " + minuBOP.getLabel() + " to " + currentLabel);
            }
            minuBOP.incrementCountUnk();
            //So entra aqui uma vez
            if (minuBOP.getCountUnk() > COUNT_THRESHOLD_BOP) {
                minuBOP.setCountUnk(0);
                String label = activeLearning(minuBOP.getLabel() + "", "");
                minuBOP.setLabel(Double.parseDouble(label));
                buffer.getListNovelBOP().add(minuBOP);
                buffer.getListUBOP().remove(minuBOP);
                compareLabel(minuBOP, "Novel");
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
            double percentInside = (insideDistance * 100) / totalSmallBopDistance;
            double percentOutside = (outsideDistance * 100) / totalBigBopDistance;
            if (percentInside < 20 && percentOutside < 20) {
                return EnumHistogram.INSIDE;
            } else if (percentInside < 35 && percentOutside < 35) {
                return EnumHistogram.SLACK;
            } else {
                return EnumHistogram.OUTSIDE;
            }
        }
    }

    private BOP minDistanceBOP(LinkedList<BOP> BOP, BOP newBop, String origem) {
        BOP minBop = null;

        int[] distances;
        int minDistance = Integer.MAX_VALUE;
        double lastLabel = -1;
        int conflited = 0;
        for (BOP bop : BOP) {
            if (lastLabel != -1 && lastLabel != bop.getLabel()) {
                conflited++;
            }
            if (bop.getHistogram().size() > newBop.getHistogram().size()) {
                distances = calcDistanceBetweenBOP(bop, newBop);
            } else {
                distances = calcDistanceBetweenBOP(newBop, bop);
            }
            int distance = distances[0] + distances[1];
            if (distances[0] != -1 && distance < minDistance) {
                minDistance = distance;
                minBop = bop;
            }
            lastLabel = bop.getLabel();
        }

        //If different label call active learning, it works like update component
        if (minBop != null && conflited > 1) {
            symbolicView.updateLog("Conflit labels! " + origem);
            String label = activeLearning(minBop.getLabel() + "", "Conflit labels! " + origem);
            minBop.setLabel(Double.parseDouble(label));
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

    private boolean fusionHistogram(LinkedList<BOP> BOPs, BOP minBop, BOP newBop) {
        for (BOP bop : BOPs) {
            if (bop.equals(minBop)
                    && bop.getHistogram().size() > newBop.getHistogram().size()) {//Only if bop is greater
                for (WordRecord big : bop.getHistogram()) {
                    for (WordRecord small : newBop.getHistogram()) {
                        if (big.equals(small)) {
                            float mean = (big.getFrequency() + small.getFrequency()) / 2;
                            big.setFrequency(Math.round(mean));
                            return true;
                        }
                    }
                }
                break;
            }
        }
        return false;
    }

    //Verify aligned words
    private LinkedList<String> getWordsDictionary(WordRecord wordRecord) {
        LinkedList<String> words = new LinkedList<>();
        String word = wordRecord.getWord();
        String newWord = "";
        for (int i = 0; i < word.length() - 1; i++) {
            String firstLetter = word.charAt(0) + "";
            for (int j = 1; j < word.length() - 1; j++) {
                newWord += word.charAt(j);
            }
            String lastLetter = word.charAt(word.length() - 1) + "";//Axis identification
            newWord += firstLetter + lastLetter;
            words.add(newWord);
            word = newWord;
            newWord = "";
        }
        return words;
    }

    private void compareLabel(BOP bop, String origem) {
        if (bop.getLabel() == currentLabel) {
            if (bop.getCountNovel() > 0) {
                symbolicView.getEval().incrementHistsNovel(currentLabel);
            } else {
                symbolicView.getEval().incrementHists(currentLabel);
            }
            symbolicView.updateLog(">> Right: " + bop.getLabel() + " - " + origem);
        } else {
            if (bop.getCountNovel() > 0) {
                symbolicView.getEval().incrementErrorsNovel(currentLabel, bop.getLabel());
            } else {
                symbolicView.getEval().incrementErrors(currentLabel, bop.getLabel());
            }
            symbolicView.updateLog(">> Wrong: " + bop.getLabel() + ", Right: " + currentLabel + " - " + origem);
        }
    }

    private void cleanBuffer() {
        //Clean buffer excess
        symbolicView.getBuffer().setBOP(new BOP());
        if (symbolicView.getBuffer().getListNovelBOP().size() > 50) {
            symbolicView.getBuffer().getListNovelBOP().remove(0);
        }
        if (symbolicView.getBuffer().getListUBOP().size() > 100) {
            symbolicView.getBuffer().getListUBOP().remove(0);
        }
    }

}
