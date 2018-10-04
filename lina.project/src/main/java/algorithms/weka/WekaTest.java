/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.weka;

import controle.constants.ConstDataset;
import java.util.LinkedList;
import util.FileUtil;
import weka.classifiers.rules.DecisionTable;

/**
 *
 * @author Wesllen Sousa
 */
public class WekaTest {

    public static void main(String[] args) {

        String train = "shoaib_tf.arff";

        LinkedList<String> columns = FileUtil.extractNamesColumnFromFile(ConstDataset.SEPARATOR,
                ConstDataset.DS_TRAIN + train);
        WekaUtil wekaUtil = new WekaUtil(ConstDataset.DS_TRAIN + train, columns.size());

        long init = System.currentTimeMillis();

        wekaUtil.buildClassify(new DecisionTable());

        long end = System.currentTimeMillis();
        long time = end - init;
        System.out.println("Train time: " + time);
    }

}
