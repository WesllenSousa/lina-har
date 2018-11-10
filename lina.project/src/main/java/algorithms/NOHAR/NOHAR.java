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
import datasets.memory.WordInterval;
import datasets.memory.WordRecord;
import datasets.timeseries.TimeSeries;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import util.Messages;
import view.viewControler.SymbolicView;

/**
 *
 * @author Wesllen Sousa
 */
public class NOHAR {

    private final SymbolicView symbolicView;
    private final Params params;
    private final int COUNT_THRESHOLD_BOP = 3;
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
//        if(currentLabel == 5.0) {
//            ConstGeneral.UPDATE_GUI = true;
//        } else {
//            ConstGeneral.UPDATE_GUI = false;
//        }

        //Monitor change the data
        if (Parameters.CHANGE_DETECTION > 0
                && changeDetected(currentValues, position)) {
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
                double variance = subSequences[index].calculateVariance();
                int frequency = (int) Math.round(mean + (variance * 2) + PESO);
                //Discretize
                WordRecord word = discretize(subSequences[index], position, index, frequency);
                //Update BOP
                updateBOP(symbolicView.getBuffer().getBOP(), word);
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
                //100: millisegundos, 1000: segundos, 1000000: minutos
                long time = System.currentTimeMillis() / 100;
                checkForget(symbolicView.getBuffer(), time, 1000);
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
                pageHinkley[i] = new PageHinkley(0.4);
            }
        }
        for (int i = 0; i < currentValues.length; i++) {
            double value = currentValues[i];
            if (value < 0) {//Because the mean can not be negative
                value = currentValues[i] * currentValues[i];
            }
            if (Parameters.CHANGE_DETECTION == 1) {
                if (adwin[i].setInput(value)) {
                    symbolicView.addMarkerGraphLine(position, Color.RED);
                    symbolicView.getEval().incrementChangeDetected();
                    return true;
                }
            } else if (Parameters.CHANGE_DETECTION == 2
                    && pageHinkley[i].runStreaming(value, position)) {
                symbolicView.addMarkerGraphLine(position, Color.RED);
                symbolicView.getEval().incrementChangeDetected();
                return true;
            }
        }
        return false;
    }

    /*
     *   Discretização
     */
    private WordRecord discretize(TimeSeries subSequence, int position, int index, int frequency) {
        SAX sax = new SAX(params);
        String word = sax.serieToWord(subSequence.getData());
        word += index; //Different word for different axis
        WordRecord wordRecord = populaWordRecord(word, position - Parameters.WINDOW_SIZE, frequency);
        return wordRecord;
    }

    private WordRecord populaWordRecord(String word, int initialPosition, int frequency) {
        WordRecord wordRecord = new WordRecord();
        wordRecord.setWord(word);
        wordRecord.setFrequency(frequency);
        wordRecord.getIntervals().add(getWordInterval(initialPosition));
        return wordRecord;
    }

    private WordInterval getWordInterval(int initialPosition) {
        WordInterval wordInterval = new WordInterval();
        wordInterval.setPositionInit(initialPosition);
        wordInterval.setPositionEnd(initialPosition + Parameters.WINDOW_SIZE);
        return wordInterval;
    }

    /*
     *   Criação de histogramas
     */
    private void updateBOP(BOP bop, WordRecord wordRecord) {
        if (wordRecord == null) {
            return;
        }
        //Verify if word is in the buffer BOP
        boolean existed = updateWordFrequency(bop.getHistogram(), wordRecord);
        if (!existed) {
            bop.getHistogram().add(wordRecord);
        }
        symbolicView.updateCurrentHistogram(bop.getHistogram(), wordRecord, currentLabel + "");
    }

    private boolean updateWordFrequency(List<WordRecord> histogram, WordRecord wordRecord) {
        LinkedList<String> words = getWordsDictionary(wordRecord);
        for (WordRecord wordBop : histogram) {
            for (String word : words) {
                if (wordBop.getWord().equals(word)) {
                    wordBop.getIntervals().add(wordRecord.getIntervals().get(0));
                    wordBop.incrementFrequency(wordRecord.getFrequency());
                    return true;
                }
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

    /*
     *   Classification
     */
    private boolean classify(BufferStreaming buffer, BOP newBop) {

        LinkedList<BOP> BOPs = listSimilarBOPs(buffer.getModel(), newBop);
        BOP minBOP = minDistanceBOP(BOPs, newBop, "Classify");

        if (minBOP != null) {
            if (fusionHistogram(buffer.getModel(), minBOP, newBop)) {
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
            if (fusionHistogram(buffer.getListNovelBOP(), minNovelBOP, newBop)) {
                symbolicView.updateLog("Fusion novel: " + minNovelBOP.getLabel() + " to " + currentLabel);
            } else {
                symbolicView.updateLog("Similar novel: " + minNovelBOP.getLabel() + " to " + currentLabel);
            }
            minNovelBOP.incrementCountNovel();
            if (minNovelBOP.getCountNovel() > (COUNT_THRESHOLD_BOP * 2)) {
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
            symbolicView.getEval().incrementCountBOP(currentLabel, -1);
            symbolicView.updateLog("Added new unknown BOP...");
            return;
        }
        BOP minuBOP = minDistanceBOP(uBOPs, newBop, "Unknown");

        if (minuBOP != null) {
            if (fusionHistogram(buffer.getListUBOP(), minuBOP, newBop)) {
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
                symbolicView.getEval().incrementCountBOP(currentLabel, 0);
                compareLabel(minuBOP, "Novel");
                symbolicView.updateLog("Added novel BOP...");
            }
        }
    }

    private void checkForget(BufferStreaming buffer, long time, long toleranceTime) {
        if (time % toleranceTime == 0) {
            for (BOP bop : buffer.getListNovelBOP()) {
                bop.updateWeight();
                if (bop.getKlinkenberg() < 0.001) {
                    if (confirmForget(bop)) {
                        buffer.getListNovelBOP().remove(bop);
                        break;
                    } else {
                        bop.resetWeight();
                    }
                }
            }
            for (BOP bop : buffer.getModel()) {
                bop.updateWeight();
                if (bop.getKlinkenberg() < 0.001) {
                    if (confirmForget(bop)) {
                        buffer.getModel().remove(bop);
                        break;
                    } else {
                        bop.resetWeight();
                    }
                }
            }
        }
    }

    /*
     *   User interaction
     */
    private String activeLearning(String posssibleLabel, String description) {
        //Active learning
        String label = posssibleLabel;
        if (ConstGeneral.UPDATE_GUI) {
            Messages msg = new Messages();
            label = msg.inserirDadosComValorInicial("Is it similar to " + posssibleLabel + "? " + description, posssibleLabel);
        }
        symbolicView.getEval().setActiveLearning(true);
        return label;
    }

    private boolean confirmForget(BOP bop) {
        Messages msg = new Messages();
        if (ConstGeneral.UPDATE_GUI) {
            if (msg.confirmacao("Forget BOP " + bop.getLabel() + "?")) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /*
     *   Others
     */
    private int totalDistance(List<WordRecord> histogram) {
        // Distance if there is no matching word
        int totalDistance = 0;
        for (WordRecord word : histogram) {
            totalDistance += word.getFrequency();
        }
        return totalDistance;
    }

    private LinkedList<BOP> listSimilarBOPs(LinkedList<BOP> BOPs, BOP newBop) {
        LinkedList<BOP> similarBOP = new LinkedList<>();
        for (BOP bop : BOPs) {
            List<WordRecord> bopHistogram = bop.getHistogram();
            List<WordRecord> newHistogram = newBop.getHistogram();
            EnumHistogram decision;
            if (bop.getHistogram().size() > newBop.getHistogram().size()) {
                decision = classifyDistance(bopHistogram, newHistogram);
            } else {
                decision = classifyDistance(newHistogram, bopHistogram);
            }
            if (decision == EnumHistogram.EQUAL || decision == EnumHistogram.INSIDE || decision == EnumHistogram.SLACK) {
                bop.setDecision(decision);
                similarBOP.add(bop);
            }
        }
        return similarBOP;
    }

    private EnumHistogram classifyDistance(List<WordRecord> bigBop, List<WordRecord> smallBop) {

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
            } else if (percentInside < 30 && percentOutside < 30) {
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
        for (BOP bop : BOP) {            
            List<WordRecord> bopHistogram = bop.getHistogram();
            List<WordRecord> newHistogram = newBop.getHistogram();
            if (bop.getHistogram().size() > newBop.getHistogram().size()) {
                distances = calcDistanceBetweenBOP(bopHistogram, newHistogram);
            } else {
                distances = calcDistanceBetweenBOP(newHistogram, bopHistogram);
            }
            int distance = distances[0] + distances[1];
            if (distances[0] != -1 && distance < minDistance) {
                minDistance = distance;
                minBop = bop;
            }
        }

        return minBop;
    }

    private int[] calcDistanceBetweenBOP(List<WordRecord> bigBop, List<WordRecord> smallBop) {
        int insideDistance = -1;
        int outsideDistance = 0;
        boolean hasWord;
        for (WordRecord big : bigBop) {
            hasWord = false;
            for (WordRecord small : smallBop) {
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
            if (bop.equals(minBop)) {
                if (bop.getDecision() == EnumHistogram.SLACK
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
                } else {
                    break;
                }
            }
        }
        return false;
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
