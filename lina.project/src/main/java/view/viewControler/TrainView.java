/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.viewControler;

import controle.weka.WekaUtil;
import java.util.HashMap;
import java.util.LinkedList;
import controle.constants.ConstDataset;
import controle.constants.ConstGeneral;
import controle.constants.Parameters;
import algorithms.SAX.Params;
import algorithms.SAX.SAXVSM;
import algorithms.SFA.classification.BOSSEnsembleClassifier;
import algorithms.SFA.classification.BOSSMDStackClassifier;
import algorithms.SFA.classification.BOSSMDWordsClassifier;
import algorithms.SFA.classification.BOSSVSClassifier;
import algorithms.SFA.classification.Classifier.Score;
import algorithms.SFA.classification.ClassifierMD;
import algorithms.SFA.classification.MUSEClassifier;
import algorithms.SFA.classification.ShotgunClassifier;
import algorithms.SFA.classification.ShotgunEnsembleClassifier;
import algorithms.SFA.classification.WEASELClassifier;
import datasets.timeseries.MultiVariateTimeSeries;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;
import net.seninp.util.UCRUtils;
import util.FileUtil;
import util.Messages;
import util.Validation;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;

/**
 *
 * @author Wesllen Sousa
 */
public class TrainView {

    private final Messages messages = new Messages();

    private HashMap<String, Classifier> wekaClassifier = new HashMap<>();
    private HashMap<String, String> listEvaluation = new HashMap();

    public void executeTrain(String train, String test, String algorithm) {
        System.out.println(train + " - " + test);
        if (algorithm.equals(ConstGeneral.AL_DecisionTable) || algorithm.equals(ConstGeneral.AL_Decision_Stump)
                || algorithm.equals(ConstGeneral.AL_J48) || algorithm.equals(ConstGeneral.AL_Random_Forest)
                || algorithm.equals(ConstGeneral.AL_KNN) || algorithm.equals(ConstGeneral.AL_NaiveBayes)
                || algorithm.equals(ConstGeneral.AL_Logistic) || algorithm.equals(ConstGeneral.AL_MultilayerPerceptron)
                || algorithm.equals(ConstGeneral.AL_AdaBoost) || algorithm.equals(ConstGeneral.AL_Random_Tree)
                || algorithm.equals(ConstGeneral.AL_SVM)) {
            executeTrainWeka(train, test, algorithm);
        } else if (algorithm.equals(ConstGeneral.AL_SAX_VSM)) {
            executeTrainSaxVsm(train, test, algorithm);
        } else if (algorithm.equals(ConstGeneral.AL_BOSS_ENSEMBLE) || algorithm.equals(ConstGeneral.AL_BOSS_VS)
                || algorithm.equals(ConstGeneral.AL_WEASEL) || algorithm.equals(ConstGeneral.AL_SHOTGUN)
                || algorithm.equals(ConstGeneral.AL_SHOTGUN_ENSEMBLE)) {
            executeTrainBoss(train, test, algorithm);
        } else if (algorithm.equals(ConstGeneral.AL_BOSS_MD_STACK) || algorithm.equals(ConstGeneral.AL_BOSS_MD_WORDS)) {
            executeTrainBossMD(train, test, algorithm);
        } else if (algorithm.equals(ConstGeneral.AL_MUSE)) {
            executeTrainMuse(train, test, algorithm);
        } else {
            messages.aviso("Unsuported algorithm!");
        }
    }

    private void executeTrainWeka(String train, String test, String algorithm) {
        LinkedList<String> columns = FileUtil.extractNamesColumnFromFile(ConstDataset.SEPARATOR,
                ConstDataset.DS_TRAIN + train);

        WekaUtil wekaUtil = null;
        String key = null;
        if (test != null) {
            wekaUtil = new WekaUtil(ConstDataset.DS_TRAIN + train, ConstDataset.DS_TEST + test, columns.size());
            key = FileUtil.extractNameFile(train) + "_" + algorithm + "_test";
        } else {
            wekaUtil = new WekaUtil(ConstDataset.DS_TRAIN + train, columns.size());
            key = FileUtil.extractNameFile(train) + "_" + algorithm;
        }

        Classifier classifier = null;
        if (algorithm.equals(ConstGeneral.AL_DecisionTable)) {
            classifier = wekaUtil.buildClassify(new DecisionTable());
        } else if (algorithm.equals(ConstGeneral.AL_Decision_Stump)) {
            classifier = wekaUtil.buildClassify(new DecisionStump());
        } else if (algorithm.equals(ConstGeneral.AL_J48)) {
            classifier = wekaUtil.buildClassify(new J48());
        } else if (algorithm.equals(ConstGeneral.AL_Random_Forest)) {
            classifier = wekaUtil.buildClassify(new RandomForest());
        } else if (algorithm.equals(ConstGeneral.AL_Random_Tree)) {
            classifier = wekaUtil.buildClassify(new RandomTree());
        } else if (algorithm.equals(ConstGeneral.AL_KNN)) {
            classifier = wekaUtil.buildClassify(new IBk(3));
        } else if (algorithm.equals(ConstGeneral.AL_NaiveBayes)) {
            classifier = wekaUtil.buildClassify(new NaiveBayes());
        } else if (algorithm.equals(ConstGeneral.AL_SVM)) {
            classifier = wekaUtil.buildClassify(new SMO());
        } else if (algorithm.equals(ConstGeneral.AL_Logistic)) {
            classifier = wekaUtil.buildClassify(new Logistic());
        } else if (algorithm.equals(ConstGeneral.AL_MultilayerPerceptron)) {
            classifier = wekaUtil.buildClassify(new MultilayerPerceptron());
        } else if (algorithm.equals(ConstGeneral.AL_AdaBoost)) {
            classifier = wekaUtil.buildClassify(new AdaBoostM1());
        }

        if (classifier != null) {
            String result = wekaUtil.evaluation(classifier);
            wekaClassifier.put(key, classifier);
            listEvaluation.put(key, result);
        }
    }

    private void executeTrainBoss(String train, String test, String algorithm) {
        int window = (int) (Parameters.WINDOW_SEC * Parameters.FREQUENCY);

        TimeSeries[] trainSamples = TimeSeriesLoader.loadHorizontalData(
                ConstDataset.DS_TRAIN + train, ConstDataset.SEPARATOR, true);
        TimeSeries[] testSamples = TimeSeriesLoader.loadHorizontalData(
                ConstDataset.DS_TEST + test, ConstDataset.SEPARATOR, true);

        if (trainSamples.length == 0 || testSamples.length == 0) {
            messages.aviso("Dataset format incorrect!\n"
                    + "Check separator config!");
            return;
        }
        if (trainSamples[0].getLength() < window) {
            window = trainSamples[0].getLength();
        }

        algorithms.SFA.classification.Classifier.DEBUG = true;
        algorithms.SFA.classification.Classifier classifier = null;

        if (algorithm.equals(ConstGeneral.AL_BOSS_ENSEMBLE)) {
            classifier = new BOSSEnsembleClassifier();
        } else if (algorithm.equals(ConstGeneral.AL_BOSS_VS)) {
            classifier = new BOSSVSClassifier();
        } else if (algorithm.equals(ConstGeneral.AL_WEASEL)) {
            classifier = new WEASELClassifier();
        } else if (algorithm.equals(ConstGeneral.AL_SHOTGUN)) {
            classifier = new ShotgunClassifier();
        } else if (algorithm.equals(ConstGeneral.AL_SHOTGUN_ENSEMBLE)) {
            classifier = new ShotgunEnsembleClassifier();
        }

        if (classifier != null) {
            //Parameters
            classifier.maxWindowLength = Parameters.MAX_WINDOW_LENGTH;
            classifier.minWindowLength = Parameters.MIN_WINDOW_LENGTH;
            classifier.maxSymbol = Parameters.MAX_SYMBOL;
            classifier.maxWordLength = Parameters.MAX_WORD_LENGTH;
            classifier.minWordLenth = Parameters.MIN_WORD_LENGTH;

            //Train model
            Score score = classifier.eval(trainSamples, testSamples);
            String result = classifier.toString() + score.toString();
            String key = FileUtil.extractNameFile(train) + "_" + algorithm;
            listEvaluation.put(key, result);
        }
    }

    private void executeTrainSaxVsm(String train, String test, String algorithm) {
        int window = (int) (Parameters.MIN_WINDOW_LENGTH);
        Params params = new Params(window, Parameters.WORD_LENGTH_PAA,
                Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.NORMALIZATION_THRESHOLD,
                NumerosityReductionStrategy.EXACT);

        try {
            Map<String, List<double[]>> trainData = UCRUtils.readUCRData(ConstDataset.DS_TRAIN + train);
            Map<String, List<double[]>> testData = UCRUtils.readUCRData(ConstDataset.DS_TEST + test);

            if (trainData.size() == 0 || testData.size() == 0) {
                messages.aviso("Dataset format incorrect!\n"
                        + "Check separator config!");
                return;
            }

            String result = null;
            if (algorithm.equals(ConstGeneral.AL_SAX_VSM)) {
                SAXVSM sax_vsm = new SAXVSM();
                result = sax_vsm.eval(trainData, testData, params);
            }

            if (result != null) {
                String key = FileUtil.extractNameFile(train) + "_" + algorithm;
                listEvaluation.put(key, result);
            }
        } catch (IOException | NumberFormatException ex) {
            messages.aviso("executeTrainSaxVsm: " + ex);
        }
    }

    private void executeTrainBossMD(String train, String test, String algorithm) {
        MultiVariateTimeSeries[] trainSamples = TimeSeriesLoader.loadHorizontalDataMultiDimensional(
                ConstDataset.DS_TRAIN + train, ConstDataset.SEPARATOR, true, 3, false);
        MultiVariateTimeSeries[] testSamples = TimeSeriesLoader.loadHorizontalDataMultiDimensional(
                ConstDataset.DS_TEST + test, ConstDataset.SEPARATOR, true, 3, false);

        ClassifierMD classifier = null;
        if (algorithm.equals(ConstGeneral.AL_BOSS_MD_STACK)) {
            classifier = new BOSSMDStackClassifier(trainSamples, testSamples);
        } else if (algorithm.equals(ConstGeneral.AL_BOSS_MD_WORDS)) {
            classifier = new BOSSMDWordsClassifier(trainSamples, testSamples);
        }

        if (classifier != null) {
            //Parameters
            classifier.maxWindowLength = Parameters.MAX_WINDOW_LENGTH;
            classifier.minWindowLength = Parameters.MIN_WINDOW_LENGTH;
            classifier.maxSymbol = Parameters.MAX_SYMBOL;
            classifier.maxWordLength = Parameters.MAX_WORD_LENGTH;
            classifier.minWordLenth = Parameters.MIN_WORD_LENGTH;

            try {
                ClassifierMD.Score scoreMD = classifier.eval();
                String result = scoreMD.toString();
                String key = FileUtil.extractNameFile(train) + "_" + algorithm;
                listEvaluation.put(key, result);
            } catch (IOException ex) {
                Logger.getLogger(TrainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void executeTrainMuse(String train, String test, String algorithm) {
        MultiVariateTimeSeries[] trainSamples = TimeSeriesLoader.loadHorizontalDataMultiDimensional(
                ConstDataset.DS_TRAIN + train, ConstDataset.SEPARATOR, true, 3, true);
        MultiVariateTimeSeries[] testSamples = TimeSeriesLoader.loadHorizontalDataMultiDimensional(
                ConstDataset.DS_TEST + test, ConstDataset.SEPARATOR, true, 3, true);

        MUSEClassifier classifier = null;
        if (algorithm.equals(ConstGeneral.AL_MUSE)) {
            classifier = new MUSEClassifier();
            MUSEClassifier.BIGRAMS = true;
        }

        if (classifier != null) {
            //Parameters
            classifier.maxWindowLength = Parameters.MAX_WINDOW_LENGTH;
            classifier.minWindowLength = Parameters.MIN_WINDOW_LENGTH;
            classifier.maxSymbol = Parameters.MAX_SYMBOL;
            classifier.maxWordLength = Parameters.MAX_WORD_LENGTH;
            classifier.minWordLenth = Parameters.MIN_WORD_LENGTH;

            //Train model
            Score score = classifier.eval(trainSamples, testSamples);
            String result = classifier.toString() + score.toString();
            String key = FileUtil.extractNameFile(train) + "_" + algorithm;
            listEvaluation.put(key, result);
        }

    }

    public void saveModel(String classifier) {
        if (wekaClassifier.get(classifier) != null) {
            WekaUtil wekaUtil = new WekaUtil();
            wekaUtil.saveModel(wekaClassifier.get(classifier), ConstDataset.DS_MODEL + classifier + ".model");
        } else {
            messages.aviso("This classifier can't be saved!");
        }
    }

    public void renameClassifier(String classifier) {
        String newName = messages.inserirDados("Insert new name!");
        if (!Validation.isEmptyString(newName)) {
            listEvaluation.put(newName, listEvaluation.get(classifier));
            listEvaluation.remove(classifier);

            if (wekaClassifier.get(classifier) != null) {
                wekaClassifier.put(newName, wekaClassifier.get(classifier));
                wekaClassifier.remove(classifier);
            }
        } else {
            messages.aviso("Empty name!");
        }
    }

    public void deleteClassifier(String classifier) {
        listEvaluation.remove(classifier);
        if (wekaClassifier.get(classifier) != null) {
            wekaClassifier.remove(classifier);
        }
    }

    /*
        Getters
     */
    public HashMap<String, String> getListEvaluation() {
        return listEvaluation;
    }

}
