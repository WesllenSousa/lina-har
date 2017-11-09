/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.viewControler;

import controle.weka.WekaUtil;
import datasets.generic.HandleGenericDataset;
import java.util.HashMap;
import java.util.LinkedList;
import constants.ConstDataset;
import constants.ConstGeneral;
import constants.Parameters;
import controle.SAX.Params;
import controle.SAX.SAX;
import controle.SFA.classification.BOSSClassifier;
import controle.SFA.classification.BOSSEnsembleClassifier;
import controle.SFA.classification.BOSSVSClassifier;
import controle.SFA.classification.Classifier.Score;
import controle.SFA.classification.ShotgunClassifier;
import controle.SFA.classification.ShotgunEnsembleClassifier;
import controle.SFA.classification.WEASELClassifier;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;
import java.io.IOException;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;
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

    public void executeTrain(String dataset, String algorithm) {
        WekaUtil wekaUtil = new WekaUtil();
        LinkedList<String> columns = HandleGenericDataset.extractNamesColumnFromFile(ConstDataset.SEPARATOR, ConstDataset.DS_TRAIN + dataset);
        wekaUtil.readData(ConstDataset.DS_TRAIN + dataset, columns.size());

        Classifier classifier = null;
        if (algorithm.equals(ConstGeneral.AL_DecisionTable)) {
            classifier = wekaUtil.buildClassfy(new DecisionTable());
        } else if (algorithm.equals(ConstGeneral.AL_Decision_Stump)) {
            classifier = wekaUtil.buildClassfy(new DecisionStump());
        } else if (algorithm.equals(ConstGeneral.AL_J48)) {
            classifier = wekaUtil.buildClassfy(new J48());
        } else if (algorithm.equals(ConstGeneral.AL_Random_Forest)) {
            classifier = wekaUtil.buildClassfy(new RandomForest());
        } else if (algorithm.equals(ConstGeneral.AL_Random_Tree)) {
            classifier = wekaUtil.buildClassfy(new RandomTree());
        } else if (algorithm.equals(ConstGeneral.AL_KNN)) {
            classifier = wekaUtil.buildClassfy(new IBk(3));
        } else if (algorithm.equals(ConstGeneral.AL_NaiveBayes)) {
            classifier = wekaUtil.buildClassfy(new NaiveBayes());
        } else if (algorithm.equals(ConstGeneral.AL_SVM)) {
            classifier = wekaUtil.buildClassfy(new SMO());
        } else if (algorithm.equals(ConstGeneral.AL_Logistic)) {
            classifier = wekaUtil.buildClassfy(new Logistic());
        } else if (algorithm.equals(ConstGeneral.AL_MultilayerPerceptron)) {
            classifier = wekaUtil.buildClassfy(new MultilayerPerceptron());
        } else if (algorithm.equals(ConstGeneral.AL_AdaBoost)) {
            classifier = wekaUtil.buildClassfy(new AdaBoostM1());
        } else {
            messages.aviso("Unsuported algorithm!");
        }

        if (classifier != null) {
            String result = wekaUtil.evaluation(classifier);
            String key = FileUtil.extractNameFile(dataset) + "_" + algorithm;
            wekaClassifier.put(key, classifier);
            listEvaluation.put(key, result);
        }
    }

    public void executeTrain(String train, String test, String algorithm) throws IOException {

        if (algorithm.equals(ConstGeneral.AL_SAX_VSM)) {

            String trainSamples = ConstDataset.DS_TRAIN + train;
            String testSamples = ConstDataset.DS_TEST + test;

            Params params = new Params(Parameters.WINDOW_SIZE, Parameters.WORD_LENGTH_PAA,
                    Parameters.SYMBOLS_ALPHABET_SIZE, Parameters.NORMALIZATION_THRESHOLD, NumerosityReductionStrategy.EXACT);

            String result = SAX.SAX_VSM(trainSamples, testSamples, params);
            if (result != null) {
                String key = FileUtil.extractNameFile(train) + "_" + algorithm;
                listEvaluation.put(key, result);
            }
        } else {
            // Load the train/test splits
            TimeSeries[] trainSamples = TimeSeriesLoader.loadHorizontalData(
                    ConstDataset.DS_TRAIN + train, " ", true);
            TimeSeries[] testSamples = TimeSeriesLoader.loadHorizontalData(
                    ConstDataset.DS_TEST + test, " ", true);

            if (trainSamples.length == 0 || testSamples.length == 0) {
                messages.aviso("Dataset format incorrect!");
            }

            controle.SFA.classification.Classifier.DEBUG = false;
            controle.SFA.classification.Classifier classifier = null;

            if (algorithm.equals(ConstGeneral.AL_BOSS_MODEL)) {
                classifier = new BOSSClassifier(trainSamples, testSamples, Parameters.WINDOW_SIZE);
            } else if (algorithm.equals(ConstGeneral.AL_BOSS_ENSEMBLE)) {
                classifier = new BOSSEnsembleClassifier(trainSamples, testSamples);
            } else if (algorithm.equals(ConstGeneral.AL_BOSS_VS)) {
                classifier = new BOSSVSClassifier(trainSamples, testSamples);
            } else if (algorithm.equals(ConstGeneral.AL_WEASEL)) {
                classifier = new WEASELClassifier(trainSamples, testSamples);
            } else if (algorithm.equals(ConstGeneral.AL_SHOTGUN)) {
                classifier = new ShotgunClassifier(trainSamples, testSamples, Parameters.WINDOW_SIZE);
            } else if (algorithm.equals(ConstGeneral.AL_SHOTGUN_ENSEMBLE)) {
                classifier = new ShotgunEnsembleClassifier(trainSamples, testSamples);
            } else {
                messages.aviso("Unsuported algorithm!");
            }

            if (classifier != null) {
                //Parameters
                classifier.setMaxWindowLength(Parameters.MAX_WINDOW_LENGTH);
                classifier.setMinWindowLength(Parameters.MIN_WINDOW_LENGTH);
                classifier.setMaxSymbol(Parameters.MAX_SYMBOL);
                classifier.setMaxWordLength(Parameters.MAX_WORD_LENGTH);
                classifier.setMinWordLenth(Parameters.MIN_WORD_LENGTH);

                //Train model
                Score score = classifier.eval();
                String result = score.toString();
                String key = FileUtil.extractNameFile(train) + "_" + algorithm;
                listEvaluation.put(key, result);
            }
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
