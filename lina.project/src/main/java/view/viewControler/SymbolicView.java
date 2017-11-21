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
import controle.SAX.SAX_VSM;
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
import java.util.LinkedList;
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
        for (int position = Parameters.WINDOW_SIZE; position < (data[0].getLength() - Parameters.WINDOW_SIZE); position += ConstGeneral.OFFSET) {
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
                //Get word
                WordRecord word = getWordFromSample(buffer, sample, position);
                analyseWord(buffer, word);
                //Handle model
                analyseModel(buffer, position);

                dataColumn++;
            }
            //Add values in GUI
            lineGraphic.addData(values);
            lineGraphic.espera(10);
        }
    }

    private WordRecord getWordFromSample(BufferStreaming buffer, TimeSeries sample, int position) {
        if (ConstGeneral.SFA) {
            return SFA(buffer, sample, position - Parameters.WINDOW_SIZE);
        } else {
            return SAX(sample, position - Parameters.WINDOW_SIZE);
        }
    }

    private WordRecord SAX(TimeSeries sample, int position) {
        SAX sax = new SAX(params);
        String word = sax.serieToWord(sample.getData());
        WordRecord wordRecord = populaWordRecord(word, position);
        return wordRecord;
    }

    private WordRecord SFA(BufferStreaming buffer, TimeSeries sample, int position) {
        if (position <= Parameters.MCB_SIZE) {
            buffer.getBufferMCB().add(sample);
            if (position == Parameters.MCB_SIZE) { //*** change threhold to MCB 
                SFA sfa = new SFA(SFA.HistogramType.EQUI_DEPTH);
                sfa.fitTransform(buffer.getBufferMCB().toArray(new TimeSeries[]{}),
                        Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE, ConstGeneral.NORM);
                buffer.setMcb(sfa);
                short[] wordQuery = buffer.getMcb().transform(sample);
                WordRecord wordRecord = populaWordRecord(wordQuery, position);

                lineGraphic.addMarker(position, position, Color.black);
                return wordRecord;
            }
        } else {
            short[] wordQuery = buffer.getMcb().transform(sample);
            WordRecord wordRecord = populaWordRecord(wordQuery, position);
            return wordRecord;
        }
        return null;
    }

    private void analyseWord(BufferStreaming buffer, WordRecord word) {
        if (word == null) {
            return;
        }
        //Redução de numerozidade por EXACT Strategy 
        if (ConstGeneral.NUM_REDUCTION && previousWord != null
                && previousWord.getWord().equals(word.getWord())) {
            return;
        }
        //update frequency
        word.setFrequency(barGraphic.getWordFrequency(word.getWord()));
        //Verify if word is in the buffer BOP
        if (buffer.getBufferWord().contains(word)) { //Bloco de código passivel de futuras otimizacoes
            for (WordRecord wordRecord : buffer.getBufferWord()) {
                if (wordRecord.getWord().equals(word.getWord())) {
                    //Verify interval overlaped to same words: alingments
                    if (ConstGeneral.ALINGMENT == false) {
                        wordRecord.getIntervals().add(word.getIntervals().get(0));
                        wordRecord.setFrequency(word.getFrequency());
                    } else if (ConstGeneral.ALINGMENT && !overlap(wordRecord, word)) {
                        wordRecord.getIntervals().add(word.getIntervals().get(0));
                        wordRecord.setFrequency(word.getFrequency());
                    }
                    previousWord = word;
                    updateGUIbar(buffer, wordRecord);
                }
            }
        } else {
            //Add word in buffer
            previousWord = word;
            buffer.getBufferWord().add(word);
            updateGUIbar(buffer, word);
        }
    }

    private void analyseModel(BufferStreaming buffer, int position) {

        if (position % Parameters.BOP_SIZE == 0) { //*** change threhold to BOP 

            String label = "1"; //Elaborar uma estratégia pra cá
            uniqueLabels.add(label);

            //Elaborar uma estrategia para atualizar todos os modelos
            switch (ConstGeneral.MODEL) {
                case "SaxVsm":
                    if (!ConstGeneral.SFA) {
                        SAX_VSM sax_vsm = new SAX_VSM();
                        WordBag bag1 = sax_vsm.wordsToWordBag(buffer.getBufferWord(), label);
                        buffer.getBOPSax().add(bag1); //verificar e mudar para SAX model?
                        
                        HashMap<String, HashMap<String, Double>> tfidf = sax_vsm.getTfIdfFromWordBags(buffer.getBOPSax());
                        buffer.setMatrixSaxVsm(tfidf);
                        
                        String result = sax_vsm.predict(bag1, tfidf);
                        updateLog("SAX-VSM: " + result);
                    } else {
                        updateLog("Need to be choose SAX discretization algorithm!");
                    }
                    break;
                case "BossModel":
                    if (ConstGeneral.SFA) {
                        BOSSModel boss = new BOSSModel(Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.WINDOW_SIZE, true);
                        BagOfPattern bag2 = createBagOfPatternBOSS(boss, buffer.getBufferWord(), label);
                        buffer.getBOPBoss().add(bag2); //Boss model?
                        
                        classifyBossModel(bag2, buffer.getBOPBoss());
                    } else {
                        updateLog("Need to be choose SFA discretization algorithm!");
                    }
                    break;
                case "BossVS":
                    if (ConstGeneral.SFA) {
                        BOSSModel boss = new BOSSModel(Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.WINDOW_SIZE, true);
                        BagOfPattern bag3 = createBagOfPatternBOSS(boss, buffer.getBufferWord(), label);
                        buffer.getBOPBoss().add(bag3); //Boss model?
                        
                        updateModelBossVs(buffer);
                        classifyBossVs(bag3, buffer.getMatrixBossVs());
                    } else {
                        updateLog("Need to be choose SFA discretization algorithm!");
                    }
                    break;
                case "Weasel":
                    if (ConstGeneral.SFA) {
                        LinkedList<Integer> windowLengths = new LinkedList<>(); //Cria diferentes tamanhos de janelas, ver uma solucao pra ca
                        windowLengths.add(Parameters.WINDOW_SIZE);
                        WEASELModel weasel = new WEASELModel(Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE,
                                windowLengths, true, true);
                        BagOfBigrams bag4 = createBagOfBigramWEASEL(weasel, buffer.getBufferWord(), label, windowLengths);
                        buffer.getBOPWeasel().add(bag4);
                        
                        updateModelWeasel(weasel, buffer);
                        classifyWeasel(bag4, buffer);
                    } else {
                        updateLog("Need to be choose SFA discretization algorithm!");
                    }
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

    private BagOfPattern createBagOfPatternBOSS(BOSSModel boss, List<WordRecord> listWords, String label) {
        int[] words = new int[listWords.size()];
        for (int wordIndex = 0; wordIndex < listWords.size(); wordIndex++) {
            //Get word int value from word bit value
            int wordInt = (int) Words.createWord(listWords.get(wordIndex).getWordBit(), Parameters.WORD_LENGTH_PAA,
                    (byte) Words.binlog(Parameters.SYMBOLS_ALPHABET_SIZE));
            //Create column from matrix word equals the frequency of each word 
            words[wordIndex] = wordInt;
        }
        BagOfPattern bag = boss.createOneBagOfPattern(words, label, Parameters.WORD_LENGTH_PAA);
        return bag;
    }

    private BagOfBigrams createBagOfBigramWEASEL(WEASELModel weasel, List<WordRecord> listWords, String label,
            LinkedList<Integer> windowLengths) {
        int[][] words = new int[windowLengths.size()][listWords.size()];
        for (int wordIndex = 0; wordIndex < listWords.size(); wordIndex++) {
            //Get word int value from word bit value
            int wordInt = (int) Words.createWord(listWords.get(wordIndex).getWordBit(), Parameters.WORD_LENGTH_PAA,
                    (byte) Words.binlog(Parameters.SYMBOLS_ALPHABET_SIZE));
            //Create column from matrix word equals the frequency of each word 
            words[0][wordIndex] = wordInt;
        }
        BagOfBigrams bag = weasel.createOneBagOfPatterns(words, label, Parameters.WORD_LENGTH_PAA);
        return bag;
    }

    private void updateModelBossVs(BufferStreaming buffer) {
        BOSSVSModel bossVsModel = new BOSSVSModel(Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE,
                Parameters.WINDOW_SIZE, true);
        ObjectObjectOpenHashMap<String, IntFloatOpenHashMap> matrixTrain = bossVsModel.createTfIdf(
                buffer.getBOPBoss().toArray(new BagOfPattern[]{}), uniqueLabels);
        buffer.setMatrixBossVs(matrixTrain);
    }

    private void updateModelWeasel(WEASELModel weasel, BufferStreaming buffer) {
        List<BagOfBigrams> bop = buffer.getBOPWeasel();
        weasel.filterChiSquared(bop.toArray(new BagOfBigrams[]{}), chi);
        Problem problem = WEASELClassifier.initLibLinearProblem(bop.toArray(new BagOfBigrams[]{}), weasel.dict,
                WEASELClassifier.bias);
        Model linearModel = Linear.train(problem, new Parameter(solverType, c, iter, p));
        WScore score = new WScore(0., true, Parameters.WORD_LENGTH_PAA, weasel, linearModel);
        buffer.setWeaselModel(score);
    }

    private void classifyBossModel(BagOfPattern bag, List<BagOfPattern> BOP) {
        BOSSClassifier bossModel = new BOSSClassifier(Parameters.WINDOW_SIZE);
        Predictions pBoss = bossModel.predictStream(bag, BOP.toArray(new BagOfPattern[]{}));
        updateLog("BOSS: " + pBoss.labels[0]);
    }

    private void classifyBossVs(BagOfPattern bag, ObjectObjectOpenHashMap<String, IntFloatOpenHashMap> matrixBossVs) {
        BOSSVSClassifier bossVs = new BOSSVSClassifier();
        Predictions pBossVs = bossVs.predictStream(bag, matrixBossVs); //BOSS VS
        updateLog("BOSS VS: " + pBossVs.labels[0]);
    }

    private void classifyWeasel(BagOfBigrams bag, BufferStreaming buffer) {
        WEASELClassifier weasel = new WEASELClassifier();
        int result = weasel.predictStream(buffer.getWeaselModel(), bag);
        updateLog("Weasel: " + result);
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
        if (ConstGeneral.CLEAR_HIST) {
            buffer.getBufferWord().clear();
            ConstGeneral.TELA_PRINCIPAL.clearBarGraphic();
        }
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

    /*
     *   Handle Word Record
     */
    private WordRecord populaWordRecord(String word, int position) {
        WordRecord wordRecord = new WordRecord();
        wordRecord.setWord(word);
        wordRecord.getIntervals().add(getWordInterval(position));

        return wordRecord;
    }

    private WordRecord populaWordRecord(short[] wordBit, int position) {
        WordRecord wordRecord = new WordRecord();
        wordRecord.setWordBit(wordBit);
        wordRecord.setWord(toSfaWord(wordBit));
        wordRecord.getIntervals().add(getWordInterval(position));

        return wordRecord;
    }

    private WordInterval getWordInterval(int position) {
        WordInterval wordInterval = new WordInterval();
        wordInterval.setPositionInit(position);
        wordInterval.setPositionEnd(position + Parameters.WINDOW_SIZE);
        return wordInterval;
    }

    private String toSfaWord(short[] word) {
        StringBuilder sfaWord = new StringBuilder();
        for (short c : word) {
            sfaWord.append((char) (Character.valueOf('a') + c));
        }
        return sfaWord.toString();
    }

}
