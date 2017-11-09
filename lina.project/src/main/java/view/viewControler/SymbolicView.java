/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.viewControler;

import com.carrotsearch.hppc.IntFloatOpenHashMap;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import constants.ConstGeneral;
import constants.Parameters;
import controle.SAX.Params;
import controle.SAX.SAX;
import controle.SAX.saxvsm.text.TextProcessor;
import controle.SAX.saxvsm.text.WordBag;
import controle.SFA.classification.BOSSClassifier;
import controle.SFA.classification.BOSSVSClassifier;
import controle.SFA.classification.Classifier.Predictions;
import controle.SFA.classification.Classifier.Words;
import controle.SFA.classification.WEASELClassifier;
import controle.SFA.classification.WEASELClassifier.WScore;
import static controle.SFA.classification.WEASELClassifier.c;
import static controle.SFA.classification.WEASELClassifier.chi;
import static controle.SFA.classification.WEASELClassifier.iter;
import static controle.SFA.classification.WEASELClassifier.p;
import static controle.SFA.classification.WEASELClassifier.solverType;
import controle.SFA.transformation.BOSSModel;
import controle.SFA.transformation.BOSSModel.BagOfPattern;
import controle.SFA.transformation.BOSSVSModel;
import controle.SFA.transformation.SFA;
import controle.SFA.transformation.WEASELModel;
import controle.SFA.transformation.WEASELModel.BagOfBigrams;
import controle.pageHinkley.PageHinkley;
import controle.pageHinkley.PageHinkleyBean;
import datasets.memory.BufferStreaming;
import datasets.memory.WordInterval;
import datasets.memory.WordRecord;
import datasets.timeseries.TimeSeries;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;
import util.Messages;
import view.manualviews.BarGraphic;
import view.manualviews.LineGraphic;

/**
 *
 * @author Wesllen Sousa
 */
public class SymbolicView {

    private final LineGraphic lineGraphic;
    private final BarGraphic barGraphic;

    private final TimeSeries[] data;
    private List<BufferStreaming> bufferStreaming = new ArrayList<>();
    private List<PageHinkley> listPH = new ArrayList<>();

    private HashSet<String> uniqueLabels = new HashSet<>();
    private WordRecord previousWord;
    private List<WordRecord> previosBufferWord = new ArrayList<>();
    private Params params;

    public SymbolicView(LineGraphic lineGraphic, BarGraphic barGraphic, TimeSeries[] data) {
        this.lineGraphic = lineGraphic;
        this.barGraphic = barGraphic;
        this.data = data;

        this.lineGraphic.prepareStream(data);

        params = new Params(Parameters.WINDOW_SIZE, Parameters.WORD_LENGTH_PAA,
                Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.NORMALIZATION_THRESHOLD, NumerosityReductionStrategy.EXACT);
    }

    public void runDataset() {

        //Initial data from the first window
        ArrayList<TimeSeries> initialSample = new ArrayList<>();
        Random randomCor = new Random();
        int dataColumn = 0;
        for (TimeSeries ts : data) {
            //Get first subsequence
            TimeSeries sample = ts.getSubsequence(0, Parameters.WINDOW_SIZE);
            initialSample.add(sample);

            //Apply page hinkley to the first subsequence
            PageHinkley ph = new PageHinkley(new Color(randomCor.nextInt(255), randomCor.nextInt(255), randomCor.nextInt(255)), this);
            ph.runTs(sample.getData());
            listPH.add(dataColumn, ph);

            //Apply buffer streaming to the first subsequence
            BufferStreaming buffer = new BufferStreaming();
            buffer.getBufferMCB().add(sample);
            bufferStreaming.add(dataColumn, buffer);

            dataColumn++;
        }
        //Add subsequece in GUI
        lineGraphic.addSubsequenceData(initialSample.toArray(new TimeSeries[]{}));

        //Remaining of the data for each value - streaming
        for (int position = Parameters.WINDOW_SIZE; position < (data[0].getLength() - Parameters.WINDOW_SIZE); position += Parameters.OFFSET) {
            double[] values = new double[data.length];
            dataColumn = 0;
            for (TimeSeries ts : data) {
                //Get current value to add in the graphic and calcule the page hinkley
                double value = ts.getData(position);
                values[dataColumn] = value;
                //listPH.get(col).runStreaming(value, position); //Média negativa **********************

                //Process window 
                TimeSeries sample = ts.getSubsequence(position - Parameters.WINDOW_SIZE, Parameters.WINDOW_SIZE);
                BufferStreaming buffer = bufferStreaming.get(dataColumn);
                if (Parameters.SFA) {
                    SFA(buffer, sample, position - Parameters.WINDOW_SIZE);
                    updateModelToSFA(buffer, position);
                } else {
                    SAX(buffer, sample, position - Parameters.WINDOW_SIZE);
                    updateModelToSAX(buffer, position);
                }

                dataColumn++;
            }
            //Add values in GUI
            lineGraphic.addData(values);
            lineGraphic.espera(10);
        }
    }

    private void SAX(BufferStreaming buffer, TimeSeries sample, int position) {
        String word = SAX.serieToWord(sample.getData(), params);
        WordRecord wordRecord = buffer.getWordRecord(word, position);
        analyseWord(buffer, wordRecord);
        previousWord = wordRecord;
    }

    private void SFA(BufferStreaming buffer, TimeSeries sample, int position) {
        if (position <= Parameters.MCB_SIZE) {
            buffer.getBufferMCB().add(sample);

            if (position == Parameters.MCB_SIZE) { //*** change threhold to MCB 
                SFA sfa = new SFA(SFA.HistogramType.EQUI_DEPTH);
                sfa.fitTransform(buffer.getBufferMCB().toArray(new TimeSeries[]{}),
                        Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.NORM);
                buffer.setMcb(sfa);
                short[] wordQuery = buffer.getMcb().transform(sample);
                previousWord = buffer.getWordRecord(wordQuery, position);

                lineGraphic.addMarker(position, position, Color.black);
            }
        } else {
            short[] wordQuery = buffer.getMcb().transform(sample);
            WordRecord wordRecord = buffer.getWordRecord(wordQuery, position);
            analyseWord(buffer, wordRecord);
            previousWord = wordRecord;
        }
    }

    private void analyseWord(BufferStreaming buffer, WordRecord word) {
        if (previousWord == null) {
            return;
        }
        //Redução de numerozidade por EXACT Strategy 
        if (Parameters.NUM_REDUCTION && previousWord.getWord().equals(word.getWord())) {
            return;
        }
        //update frequency
        word.setFrequency(barGraphic.getWordFrequency(word.getWord()));
        //Verify if word is in the buffer BOP
        if (buffer.getBufferWord().contains(word)) { //Bloco de código passivel de futuras otimizacoes
            for (WordRecord wordRecord : buffer.getBufferWord()) {
                if (wordRecord.getWord().equals(word.getWord())) {
                    //Verify interval overlaped to same words: alingments
                    if (Parameters.ALINGMENT == false) {
                        wordRecord.getIntervals().add(word.getIntervals().get(0));
                        wordRecord.setFrequency(word.getFrequency());
                    } else if (Parameters.ALINGMENT && !overlap(wordRecord, word)) {
                        wordRecord.getIntervals().add(word.getIntervals().get(0));
                        wordRecord.setFrequency(word.getFrequency());
                    }
                    updateGUIbar(buffer, wordRecord);
                }
            }
        } else {
            //Add word in buffer
            buffer.getBufferWord().add(word);
            updateGUIbar(buffer, word);
        }
    }

    private void updateModelToSAX(BufferStreaming buffer, int position) {

        if (position % Parameters.BOP_SIZE == 0) { //*** change threhold to BOP 

            String label = "1"; //Elaborar uma estratégia pra cá
            uniqueLabels.add(label);

            switch (Parameters.MODEL) {
                case "SaxVsm":
                    WordBag bag1 = createBagOfPatternSAX(buffer.getBufferWord(), label);
                    buffer.getBOPSax().add(bag1); //verificar e mudar para SAX model?
                    updateModelSax(buffer);
                    classifySaxVsm(bag1, buffer.getMatrixSaxVsm());
                    break;
                default:
                    Messages messages = new Messages();
                    messages.aviso("Need to be choose SAX discretization algorithm!");
                    break;
            }
        }
    }

    private void updateModelToSFA(BufferStreaming buffer, int position) {

        if (position % Parameters.BOP_SIZE == 0) { //*** change threhold to BOP 

            String label = "1"; //Elaborar uma estratégia pra cá
            uniqueLabels.add(label);

            //Elaborar uma estrategia para atualizar todos os modelos
            switch (Parameters.MODEL) {
                case "BossModel":
                    BagOfPattern bag2 = createBagOfPatternSFA(buffer.getBufferWord(), label);
                    buffer.getBOPBoss().add(bag2); //Boss model?
                    classifyBossModel(bag2, buffer.getBOPBoss());
                    break;
                case "BossVS":
                    BagOfPattern bag3 = createBagOfPatternSFA(buffer.getBufferWord(), label);
                    buffer.getBOPBoss().add(bag3); //Boss model?
                    updateModelBossVs(buffer);
                    classifyBossVs(bag3, buffer.getMatrixBossVs());
                    break;
                case "Weasel":
                    int[] windowLengths = new int[]{Parameters.WINDOW_SIZE}; //Cria diferentes tamanhos de janelas, ver uma solucao pra ca
                    BagOfBigrams bag4 = createBagOfBigramWEASEL(buffer.getBufferWord(), label, windowLengths);
                    buffer.getBOPWeasel().add(bag4);
                    updateModelWeasel(buffer, windowLengths);
                    classifyWeasel(bag4, buffer);
                    break;
                default:
                    Messages messages = new Messages();
                    messages.aviso("Need to be choose SFA discretization algorithm!");
                    break;
            }

            clearGUIbar(buffer);
            lineGraphic.addMarker(position, position, Color.black);
        }
    }

    private WordBag createBagOfPatternSAX(List<WordRecord> listWords, String label) {
        WordBag bag = new WordBag(label);
        for (WordRecord word : listWords) {
            bag.addWord(word.getWord(), word.getFrequency());
        }
        return bag;
    }

    private BagOfPattern createBagOfPatternSFA(List<WordRecord> listWords, String label) {
        int[] words = new int[listWords.size()];
        for (int wordIndex = 0; wordIndex < listWords.size(); wordIndex++) {
            //Get word int value from word bit value
            int wordInt = (int) Words.createWord(listWords.get(wordIndex).getWordBit(), Parameters.WORD_LENGTH_PAA,
                    (byte) Words.binlog(Parameters.SYMBOLS_ALPHABET_SIZE));
            //Create column from matrix word equals the frequency of each word 
            words[wordIndex] = wordInt;
        }
        BOSSModel boss = new BOSSModel(Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.WINDOW_SIZE, true);
        BagOfPattern bag = boss.createOneBagOfPattern(words, label, Parameters.WORD_LENGTH_PAA);
        return bag;
    }

    private BagOfBigrams createBagOfBigramWEASEL(List<WordRecord> listWords, String label, int[] windowLengths) {
        int[][] words = new int[windowLengths.length][listWords.size()];
        for (int wordIndex = 0; wordIndex < listWords.size(); wordIndex++) {
            //Get word int value from word bit value
            int wordInt = (int) Words.createWord(listWords.get(wordIndex).getWordBit(), Parameters.WORD_LENGTH_PAA,
                    (byte) Words.binlog(Parameters.SYMBOLS_ALPHABET_SIZE));
            //Create column from matrix word equals the frequency of each word 
            words[0][wordIndex] = wordInt;
        }
        WEASELModel weasel = new WEASELModel(Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE,
                windowLengths, true, true);
        BagOfBigrams bag = weasel.createOneBagOfPatterns(words, label, Parameters.WORD_LENGTH_PAA);
        return bag;
    }

    private void updateModelSax(BufferStreaming buffer) {
        TextProcessor tp = new TextProcessor();
        HashMap<String, HashMap<String, Double>> tfidf = tp.computeTFIDF(buffer.getBOPSax());
        buffer.setMatrixSaxVsm(tfidf);
    }

    private void updateModelBossVs(BufferStreaming buffer) {
        BOSSVSModel bossVsModel = new BOSSVSModel(Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE,
                Parameters.WINDOW_SIZE, true);
        ObjectObjectOpenHashMap<String, IntFloatOpenHashMap> matrixTrain = bossVsModel.createTfIdf(
                buffer.getBOPBoss().toArray(new BagOfPattern[]{}), uniqueLabels);
        buffer.setMatrixBossVs(matrixTrain);
    }

    private void updateModelWeasel(BufferStreaming buffer, int[] windowLengths) {
        List<BagOfBigrams> bop = buffer.getBOPWeasel();
        WEASELModel weasel = new WEASELModel(Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE,
                windowLengths, true, true);
        weasel.filterChiSquared(bop.toArray(new BagOfBigrams[]{}), chi);
        Problem problem = WEASELClassifier.initLibLinearProblem(bop.toArray(new BagOfBigrams[]{}), weasel.dict,
                WEASELClassifier.bias);
        Model linearModel = Linear.train(problem, new Parameter(solverType, c, iter, p));
        WScore score = new WScore(0., true, Parameters.WORD_LENGTH_PAA, weasel, linearModel);
        buffer.setWeaselModel(score);
    }

    private void classifySaxVsm(WordBag bag, HashMap<String, HashMap<String, Double>> matrixSaxVsm) {
        TextProcessor tp = new TextProcessor();
        String result = tp.classify(bag, matrixSaxVsm);
        System.out.println("SAX-VSM - " + result);
    }

    private void classifyBossModel(BagOfPattern bag, List<BagOfPattern> BOP) {
        BOSSClassifier bossModel = new BOSSClassifier(Parameters.WINDOW_SIZE);
        Predictions pBoss = bossModel.predictStream(bag, BOP.toArray(new BagOfPattern[]{}));
        updateLog("BOSS - " + pBoss.labels[0]);
    }

    private void classifyBossVs(BagOfPattern bag, ObjectObjectOpenHashMap<String, IntFloatOpenHashMap> matrixBossVs) {
        BOSSVSClassifier bossVs = new BOSSVSClassifier();
        Predictions pBossVs = bossVs.predictStream(bag, matrixBossVs); //BOSS VS
        updateLog("BOSS VS - " + pBossVs.labels[0]);
    }

    private void classifyWeasel(BagOfBigrams bag, BufferStreaming buffer) {
        WEASELClassifier weasel = new WEASELClassifier();
        int result = weasel.predictStream(buffer.getWeaselModel(), bag);
        updateLog("Weasel - " + result);
    }

    /*
     *   Other methods
     */
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
     *   GUI
     */
    private void updateGUIbar(BufferStreaming buffer, WordRecord word) {
        barGraphic.addUpdateData(word.getWord(), word.getFrequency());
        ConstGeneral.TELA_PRINCIPAL.updateSymbolicTab(word, buffer.getBufferWord().size());
    }

    private void clearGUIbar(BufferStreaming buffer) {
        previosBufferWord = (ArrayList<WordRecord>) buffer.getBufferWord();
        buffer.getBufferWord().clear();
        ConstGeneral.TELA_PRINCIPAL.clearBarGraphic();
    }

    public void showPageHinkleyChanges() {
        for (PageHinkley ph : listPH) {
            for (PageHinkleyBean bean : ph.getListChanges()) {
                lineGraphic.addMarker(bean.getPosition(), bean.getPosition(), bean.getCor());
            }
        }
    }

    public void updateLog(String text) {
        ConstGeneral.TELA_PRINCIPAL.updateSymbolicLog(text);
    }

}
