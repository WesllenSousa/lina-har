/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.AnyNovel.conceptEvolution;

import algorithms.AnyNovel.StreamAR.BLM;
import controle.constants.ConstDataset;
import controle.weka.WekaUtil;
import java.util.HashMap;
import util.FileUtil;
import weka.core.Instances;

/**
 *
 * @author Wesllen Sousa
 */
public class AnyNovelMain {

    public static void main(String[] args) {

        String trainDataset = "train_shoaib_anynovel.arff"; //shoaib_sub1, train_shoaib_stream, train_shoaib_anynovel
        String dirTrain = ConstDataset.DS_STREAM + trainDataset;
        int numberOfColumns = FileUtil.extractNamesColumnFromFile(ConstDataset.SEPARATOR, dirTrain).size();
        WekaUtil wekaTrain = new WekaUtil(dirTrain, numberOfColumns);
        Instances trainData = wekaTrain.getData();

        String testDataset = "test_shoaib_anynovel.arff"; //test_shoaib_stream, test_shoaib_anynovel
        String dirTest = ConstDataset.DS_STREAM + testDataset;
        numberOfColumns = FileUtil.extractNamesColumnFromFile(ConstDataset.SEPARATOR, dirTest).size();
        WekaUtil wekaTest = new WekaUtil(dirTest, numberOfColumns);
        Instances testData = wekaTest.getData();

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("Segment_Size", "250");
        parameters.put("Stable_Size", "50");
        parameters.put("Slacks", "0.1");
        parameters.put("Novel_Slack", "0.1");
        parameters.put("Movement", "1");
        parameters.put("Away_Threshold", "0.1");
        parameters.put("Buffer_Flag", "true");
        parameters.put("JP_Layer", "true");
        parameters.put("Update_Flag", "true");
        parameters.put("Validate_Flag", "true");

        long startTime = System.currentTimeMillis();
        TrainingLauncher training = new TrainingLauncher();
        BLM BLModel = training.buildCWSCModel(trainData, "", "");
        long endTime = System.currentTimeMillis();
        long time = (endTime - startTime);
        System.out.println("Train time: " + time);
        //training.writeModelToFile(BLModel, ConstDataset.DS_MODEL, fileName);
        
//        ExpLauncher expLauncher = new ExpLauncher();
//        try {
//            BLModel = expLauncher.readModel("diretorio modelo");
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }
        AnyNovelLauncher any = new AnyNovelLauncher();
        BLM newModel = any.AnyNovel(BLModel, testData, parameters);
        System.out.println(newModel.modelStatistics());

        //Descobrir valores para gerar os graficos
        //Acuracia incremental
        //Active learning incremental (ALRate)
    }

}
