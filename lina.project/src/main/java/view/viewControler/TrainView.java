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

    private final HashMap<String, Classifier> listClassiers = new HashMap();
    private final HashMap<String, String> listEvaluation = new HashMap();

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
            listClassiers.put(key, classifier);
            listEvaluation.put(key, result);
        }
    }

    public void executeTrain(String train, String test, String algorithm) {

        if (algorithm.equals(ConstGeneral.AL_SAX_VSM)) {

        } else if (algorithm.equals(ConstGeneral.AL_BOSS_MODEL)) {

        } else if (algorithm.equals(ConstGeneral.AL_BOSS_VS)) {

        } else if (algorithm.equals(ConstGeneral.AL_WEASEL)) {

        } else {
            messages.aviso("Unsuported algorithm!");
        }

    }

    public void saveModel(String classifier) {
        WekaUtil wekaUtil = new WekaUtil();
        wekaUtil.saveModel(listClassiers.get(classifier), ConstDataset.DS_MODEL + classifier + ".model");
    }

    public void renameClassifier(String classifier) {
        String newName = messages.inserirDados("Insert new name!");
        if (!Validation.isEmptyString(newName)) {
            listClassiers.put(newName, listClassiers.get(classifier));
            listClassiers.remove(classifier);
        }
    }

    public void deleteClassifier(String classifier) {
        listClassiers.remove(classifier);
    }

    /*
        Getters
     */
    public HashMap<String, Classifier> getListClassiers() {
        return listClassiers;
    }

    public HashMap<String, String> getListEvaluation() {
        return listEvaluation;
    }

}
