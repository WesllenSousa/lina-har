

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wesllen Sousa
 */
public class Test {

    public static void main(String[] args) throws Exception {

        float c = 50;
        int test = (int) Math.round(0.15);
        System.out.println(test);
        c += Math.round(0.15);
        System.out.println(c);

        System.out.println(System.getProperty("user.home"));

        String teste = "SIT_TO_LIE";

        System.out.println(teste.contains("_TO_p"));

//        String dataset = "MPU_WEKA.arff";
//
//        LinkedList<String> columns = FileUtil.extractNamesColumnFromFile(ConstDataset.SEPARATOR,
//                ConstDataset.DS_RAW + dataset);
//        WekaUtil wekaUtil = new WekaUtil(ConstDataset.DS_RAW + dataset, columns.size());
//
//        PrincipalComponents pca = new PrincipalComponents();
//        pca.setMaximumAttributeNames(3);
//        pca.setMaximumAttributes(1);
//        pca.setVarianceCovered(0.95);
//        pca.setInputFormat(wekaUtil.getData());
//
//        Instances newData = Filter.useFilter(wekaUtil.getData(), pca);
//
//        for (Instance instance : newData) {
//            System.out.println(instance);
//        }
    }

    //    private WordRecord getWordFromSample(BufferStreaming buffer, TimeSeries sample, int position) {
//        if (ConstGeneral.SFA) {
//            return SFA(buffer, sample, position - Parameters.WINDOW_SIZE);
//        } else {
//            return SAX(sample, position - Parameters.WINDOW_SIZE);
//        }
//    }
//    private WordRecord SFA(BufferStreaming buffer, TimeSeries sample, int position) {
//        if (position <= Parameters.MCB_SIZE) {
//            buffer.getBufferMCB().add(sample);
//            if (position == Parameters.MCB_SIZE) { //*** change threhold to MCB 
//                SFA sfa = new SFA(SFA.HistogramType.EQUI_DEPTH);
//                sfa.fitTransform(buffer.getBufferMCB().toArray(new TimeSeries[]{}),
//                        Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE, ConstGeneral.NORM);
//                buffer.setMcb(sfa);
//                short[] wordQuery = buffer.getMcb().transform(sample);
//                WordRecord wordRecord = populaWordRecord(wordQuery, position);
//
//                lineGraphic.addMarker(position, position, Color.black);
//                return wordRecord;
//            }
//        } else {
//            short[] wordQuery = buffer.getMcb().transform(sample);
//            WordRecord wordRecord = populaWordRecord(wordQuery, position);
//            return wordRecord;
//        }
//        return null;
//    }
//            case "BossModel":
//                if (ConstGeneral.SFA) {
//                    BOSS boss = new BOSS(Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.WINDOW_SIZE, true);
//                    BagOfPattern bag2 = createBagOfPatternBOSS(boss, buffer.getBufferWord(), label);
//                    buffer.getBOPBoss().add(bag2); //Boss model?
//
//                    classifyBossModel(bag2, buffer.getBOPBoss());
//                } else {
//                    updateLog("Need to be choose SFA discretization algorithm!");
//                }
//                break;
//            case "BossVS":
//                if (ConstGeneral.SFA) {
//                    BOSS boss = new BOSS(Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.WINDOW_SIZE, true);
//                    BagOfPattern bag3 = createBagOfPatternBOSS(boss, buffer.getBufferWord(), label);
//                    buffer.getBOPBoss().add(bag3); //Boss model?
//
//                    updateModelBossVs(buffer);
//                    classifyBossVs(bag3, buffer.getMatrixBossVs());
//                } else {
//                    updateLog("Need to be choose SFA discretization algorithm!");
//                }
//                break;
//            case "Weasel":
//                if (ConstGeneral.SFA) {
//                    int[] windowLengths = new int[]{Parameters.WINDOW_SIZE}; //Cria diferentes tamanhos de janelas, ver uma solucao pra ca
//                    WEASEL weasel = new WEASEL(Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE,
//                            windowLengths, true, true);
//                    BagOfBigrams bag4 = createBagOfBigramWEASEL(weasel, buffer.getBufferWord(), label, windowLengths);
//                    buffer.getBOPWeasel().add(bag4);
//
//                    updateModelWeasel(weasel, buffer);
//                    classifyWeasel(bag4, buffer);
//                } else {
//                    updateLog("Need to be choose SFA discretization algorithm!");
//                }
//                break;
//    private BagOfPattern createBagOfPatternBOSS(BOSS boss, List<WordRecord> listWords, double label) {
//        int[] words = new int[listWords.size()];
//        for (int wordIndex = 0; wordIndex < listWords.size(); wordIndex++) {
//            //Get word int value from word bit value
//            int wordInt = (int) Words.createWord(listWords.get(wordIndex).getWordBit(), Parameters.WORD_LENGTH_PAA,
//                    (byte) Words.binlog(Parameters.SYMBOLS_ALPHABET_SIZE));
//            //Create column from matrix word equals the frequency of each word 
//            words[wordIndex] = wordInt;
//        }
//        BagOfPattern bag = boss.createOneBagOfPattern(words, label, Parameters.WORD_LENGTH_PAA);
//        return bag;
//    }
//
//    private BagOfBigrams createBagOfBigramWEASEL(WEASEL weasel, List<WordRecord> listWords, double label,
//            int[] windowLengths) {
//        int[][] words = new int[windowLengths.length][listWords.size()];
//        for (int wordIndex = 0; wordIndex < listWords.size(); wordIndex++) {
//            //Get word int value from word bit value
//            int wordInt = (int) Words.createWord(listWords.get(wordIndex).getWordBit(), Parameters.WORD_LENGTH_PAA,
//                    (byte) Words.binlog(Parameters.SYMBOLS_ALPHABET_SIZE));
//            //Create column from matrix word equals the frequency of each word 
//            words[0][wordIndex] = wordInt;
//        }
//        BagOfBigrams bag = weasel.createOneBagOfPatterns(words, label, Parameters.WORD_LENGTH_PAA);
//        return bag;
//    }
//    private void updateModelBossVs(BufferStreaming buffer) {
//        BOSSVSModel bossVsModel = new BOSSVSModel(Parameters.WORD_LENGTH_PAA, Parameters.SYMBOLS_ALPHABET_SIZE,
//                Parameters.WINDOW_SIZE, true);
//        ObjectObjectHashMap<String, IntFloatHashMap> matrixTrain = bossVsModel.createTfIdf(
//                buffer.getBOPBoss().toArray(new BagOfPattern[]{}), uniqueLabels);
//        buffer.setMatrixBossVs(matrixTrain);
//    }
//
//    private void updateModelWeasel(WEASEL weasel, BufferStreaming buffer) {
//        List<BagOfBigrams> bop = buffer.getBOPWeasel();
//        weasel.filterChiSquared(bop.toArray(new BagOfBigrams[]{}), chi);
//        Problem problem = WEASELClassifier.initLibLinearProblem(bop.toArray(new BagOfBigrams[]{}), weasel.dict,
//                WEASELClassifier.bias);
//        Model linearModel = Linear.train(problem, new Parameter(solverType, c, iterations, p));
//        WScore score = new WScore(0., true, Parameters.WORD_LENGTH_PAA, weasel, linearModel);
//        buffer.setWeaselModel(score);
//    }
//
//    private void classifyBossModel(BagOfPattern bag, List<BagOfPattern> BOP) {
//        BOSSClassifier bossModel = new BOSSClassifier(Parameters.WINDOW_SIZE);
//        Predictions pBoss = bossModel.predictStream(bag, BOP.toArray(new BagOfPattern[]{}));
//        updateLog("BOSS: " + pBoss.labels[0]);
//    }
//    private void classifyBossVs(BagOfPattern bag, ObjectObjectHashMap<String, IntFloatHashMap> matrixBossVs) {
//        BOSSVSClassifier bossVs = new BOSSVSClassifier();
//        Predictions pBossVs = bossVs.predictStream(bag, matrixBossVs); //BOSS VS
//        updateLog("BOSS VS: " + pBossVs.labels[0]);
//    }
//
//    private void classifyWeasel(BagOfBigrams bag, BufferStreaming buffer) {
//        WEASELClassifier weasel = new WEASELClassifier();
//        int result = weasel.predictStream(buffer.getWeaselModel(), bag);
//        updateLog("Weasel: " + result);
//    }
//    private WordRecord populaWordRecord(short[] wordBit, int position) {
//        WordRecord wordRecord = new WordRecord();
//        wordRecord.setWordBit(wordBit);
//        wordRecord.setWord(toSfaWord(wordBit));
//        wordRecord.getIntervals().add(getWordInterval(position));
//
//        return wordRecord;
//    }
//    public void showPageHinkleyChanges() {
//        for (PageHinkley2 ph : listPH) {
//            for (PageHinkleyBean bean : ph.getListChanges()) {
//                lineGraphic.addMarker(bean.getPosition(), bean.getPosition(), bean.getCor());
//            }
//        }
//    }
//    private String toSfaWord(short[] word) {
//        StringBuilder sfaWord = new StringBuilder();
//        for (short c : word) {
//            sfaWord.append((char) ('a' + c));
//        }
//        return sfaWord.toString();
//    }
}
