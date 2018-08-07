/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.Features;

import controle.constants.ConstDataset;
import controle.weka.WekaUtil;
import datasets.generic.GenericRowBean;
import datasets.timeseries.TimeSeries;
import java.util.LinkedList;
import util.FileUtil;
import util.Validation;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.PrincipalComponents;

/**
 *
 * @author Wesllen Sousa
 */
public class DataFusion {

    public static LinkedList<Float> Magnitude(LinkedList<GenericRowBean> data) {
        LinkedList<Float> magnitude = new LinkedList<>();

        for (GenericRowBean bean : data) {
            float sum = 0;
            for (String value : bean.getTupla()) {
                if (Validation.isFloat(value)) {
                    float v = Float.parseFloat(value);
                    sum += Math.pow(v, 2);
                }
            }
            magnitude.add((float) Math.sqrt(sum));
        }

        return magnitude;
    }

    public static double Magnitude(double[] values) {
        double sum = 0.0;
        for (int i = 0; i < values.length; i++) {
            sum += Math.pow(values[i], 2);
        }
        return Math.sqrt(sum);
    }

    public static TimeSeries Magnitude(TimeSeries[] data) {
        double[] magnitude = new double[data[0].getData().length];
        for (int j = 0; j < data[0].getData().length; j++) {
            double sum = 0.0;
            //data.length - 1 = ganbiarra para o NOHAR = pq a classe é a ultima coluna, e o calculo da mag nao inclui a classe
            for (int i = 0; i < data.length - 1; i++) {
                sum += Math.pow(data[i].getData(j), 2);
            }
            magnitude[j] = Math.sqrt(sum);
        }
        TimeSeries timeSeries = new TimeSeries();
        timeSeries.setData(magnitude);
        return timeSeries;
    }

    public static LinkedList<Double> PCA(String dir, String nameFile) {
        try {

            LinkedList<String> columns = FileUtil.extractNamesColumnFromFile(ConstDataset.SEPARATOR,
                    dir + nameFile);
            WekaUtil wekaUtil = new WekaUtil(dir + nameFile, columns.size());

            if (wekaUtil.getData() == null) {
                return new LinkedList<>();
            }

            PrincipalComponents pca = new PrincipalComponents();
            pca.setMaximumAttributeNames(3);
            pca.setMaximumAttributes(1);
            pca.setVarianceCovered(0.95);
            pca.setInputFormat(wekaUtil.getData());

            Instances newData = Filter.useFilter(wekaUtil.getData(), pca);

            LinkedList<Double> firstComponent = new LinkedList<>();
            for (Instance instance : newData) {
                //Get the first component
                firstComponent.add(instance.value(0));
            }
            return firstComponent;
        } catch (Exception ex) {
            System.out.println("PCA: " + ex);
        }
        return new LinkedList<>();
    }

}
