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
import datasets.memory.WordInterval;
import datasets.memory.WordRecord;
import java.awt.Color;
import java.util.LinkedList;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;
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
    
    private WordRecord previousWord;
    private int lastPosPreviusWord = 0;
    private double label = -1;
    
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
        this.label = label;
        //Monitor change the data
        if (changeDetected(currentValue, position)
                && symbolicView.getBuffer().getBOP().getHistogram().isEmpty()) {
            return;
        }
        if (changeDetected(currentValue, position)
                && !symbolicView.getBuffer().getBOP().getHistogram().isEmpty()) {
            //Analisar essa mudança de alguma forma, guardar o valor da media e verificar se volta ao normal
        }
        //Discretize
        WordRecord word = discretize(symbolicView.getBuffer(), position);
        //Update Histogram
        updateBOP(symbolicView.getBuffer(), word);
        //Handle model
        if (position % Parameters.BOP_SIZE == 0) { //*** change threhold to BOP 
            //Classify
            if (!classify(symbolicView.getBuffer().getModel(), symbolicView.getBuffer().getBOP())) {
                //Leaning
                learning(symbolicView.getBuffer());
            }

            //*********View Updates*******************
            symbolicView.addMarkerGraphLine(position, Color.BLACK);
            symbolicView.addHistograms();
            symbolicView.clearCurrentHistogram();
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
    private void updateBOP(BufferStreaming buffer, WordRecord word) {
        if (word == null) {
            return;
        }
        //Redução de numerosidade por EXACT Strategy 
        if (Parameters.NUM_REDUCTION && previousWord != null && lastPosPreviusWord < Parameters.WINDOW_SIZE
                && previousWord.getWord().equals(word.getWord())) {
            //For word that repeat always, we update the frequency
            lastPosPreviusWord++;
            return;
        }
        lastPosPreviusWord = 0;

        //Verify if word is in the buffer BOP
        if (buffer.getBOP().getHistogram().contains(word)) { //Bloco de código passivel de futuras otimizacoes
            for (WordRecord wordRecord : buffer.getBOP().getHistogram()) {
                if (wordRecord.getWord().equals(word.getWord())) {
                    //Verify interval overlaped to same words: alingments
                    if (Parameters.ALINGMENT && !overlap(wordRecord, word)) {
                        wordRecord.getIntervals().add(word.getIntervals().get(0));
                        wordRecord.incrementFrequency();
                    } else {
                        wordRecord.getIntervals().add(word.getIntervals().get(0));
                        wordRecord.incrementFrequency();
                    }
                    previousWord = word;
                    symbolicView.updateCurrentHistogram(buffer, wordRecord);
                }
            }
        } else {
            //Add word in buffer
            previousWord = word;
            buffer.getBOP().getHistogram().add(word);
            symbolicView.updateCurrentHistogram(buffer, word);
        }
    }

    //O overlap já é uma espécie de alinhamento de palavras iguais
    private boolean overlap(WordRecord words, WordRecord word) {
        int init = word.getIntervals().get(0).getPositionInit();
        int end = word.getIntervals().get(0).getPositionEnd();
        for (WordInterval interval : words.getIntervals()) {
            int cInit = interval.getPositionInit();
            int cEnd = interval.getPositionEnd();
            if ((init >= cInit && init <= cEnd) || (end >= cInit && end <= cEnd)
                    || (cInit >= init && cInit <= end) || (cEnd >= init && cEnd <= end)) {
                return true;
            }
        }
        return false;
    }

    /*
     *   Classification
     */
    private boolean classify(LinkedList<BOP> listBOP, BOP bop) {
        symbolicView.updateLog("Classifing new BOP...");
        for (BOP bopRef : listBOP) {
            if (bopRef.getEntropy() == bop.getEntropy()) {
                bop.setLabel(bopRef.getLabel());
            }
        }
        if (bop.getLabel() == label) {
            eval.incrementHists();
            symbolicView.updateLog(">> Acertou");
            return true;
        } else {
            eval.incrementErrors();
            symbolicView.updateLog(">> Errou: " + label);
            return false;
        }
    }

    /*
     *   Learning
     */
    private void learning(BufferStreaming buffer) {
        if (!checkUnknown(buffer)) {
            if (!checkNovel(buffer)) {
                update(buffer);
            }
        }
        checkForget(buffer);
    }

    //Before active learnig
    private boolean checkUnknown(BufferStreaming buffer) {
        for (BOP uBOP : buffer.getListUBOP()) {
            if (uBOP.getCountUnk() > 10) {
                //Active learning
                //Messages msg = new Messages();
                //msg.inserirDadosComValorInicial("Inform the activity!", pUnknown.getName());
                //buffer.getListNovelBOP().add(uBOP);
                //buffer.getListUBOP().remove(uBOP);
            }
        }
        return false;
    }

    //After active learning
    private boolean checkNovel(BufferStreaming buffer) {
        for (BOP novelBOP : buffer.getListNovelBOP()) {
            if (novelBOP.getCountNovel() > 10) {
                //buffer.getModel().add(novelBOP);
                //buffer.getListNovelBOP().remove(novelBOP);
            }
        }
        return false;
    }
    
    private void update(BufferStreaming buffer) {
        for (BOP bop : buffer.getModel()) {
            
        }
    }
    
    private void checkForget(BufferStreaming buffer) {
        for (BOP bop : buffer.getModel()) {
            //update weight bop
        }
    }
    
}
