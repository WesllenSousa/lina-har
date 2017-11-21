
import constants.ConstDataset;
import controle.weka.WekaUtil;
import java.util.LinkedList;
import util.FileUtil;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.PrincipalComponents;

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

        String dataset = "MPU_WEKA.arff";

        LinkedList<String> columns = FileUtil.extractNamesColumnFromFile(ConstDataset.SEPARATOR,
                ConstDataset.DS_RAW + dataset);
        WekaUtil wekaUtil = new WekaUtil(ConstDataset.DS_RAW + dataset, columns.size());

        PrincipalComponents pca = new PrincipalComponents();
        pca.setMaximumAttributeNames(3);
        pca.setMaximumAttributes(1);
        pca.setVarianceCovered(0.95);
        pca.setInputFormat(wekaUtil.getData());

        Instances newData = Filter.useFilter(wekaUtil.getData(), pca);

        for (Instance instance : newData) {
            System.out.println(instance);
        }

    }

}
