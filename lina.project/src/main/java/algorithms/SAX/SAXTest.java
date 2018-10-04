/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.SAX;

import controle.constants.ConstDataset;
import net.seninp.jmotif.sax.datastructure.SAXRecord;
import net.seninp.jmotif.sax.datastructure.SAXRecords;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;

/**
 *
 * @author Wesllen Sousa
 */
public class SAXTest {

    public static void main(String[] args) {

        int WINDOW_SIZE = 10;
        int SAX_PAA = 4;
        int SAX_ALPHABET = 4;
        double NORMALIZATION_THRESHOLD = 0.05;
        Params params = new Params(WINDOW_SIZE, SAX_PAA, SAX_ALPHABET,
                NORMALIZATION_THRESHOLD, NumerosityReductionStrategy.NONE);
        SAX sax = new SAX(params);

        //esses tres geram a mesma palavra
        double[] data1 = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        double[] data2 = new double[]{-10, -9, -8, -7, -6, -5, -4, -3, -2, -1};
        double[] data3 = new double[]{-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5};
        TimeSeries serie1 = new TimeSeries(data1);
        TimeSeries serie2 = new TimeSeries(data2);
        TimeSeries serie3 = new TimeSeries(data3);

        String word1 = sax.serieToWord(serie1.getData());
        String word2 = sax.serieToWord(serie2.getData());
        String word3 = sax.serieToWord(serie3.getData());
        System.out.println(word1);
        System.out.println(word2);
        System.out.println(word3);
        System.out.println("=================================================");

        //0 = all lines
        //double[] ts = HandleData.loadData("0", "samples/TimeSeries/sinusoid.csv");
        TimeSeries[] timeSeries = TimeSeriesLoader.loadVerticalData("0", ConstDataset.DS_TRAIN + "shoaib_tf.arff", ",");

        SAXRecords sAXRecords = sax.slideWindow(timeSeries[0].getData());
        System.out.println(">> " + sAXRecords.getIndexes().size());
        System.out.println(">> " + sAXRecords.getIndexes());
        for (SAXRecord sAXRecord : sAXRecords) {
            System.out.println("=====");
            System.out.println(sAXRecord.toString());
            System.out.println(sAXRecord.getIndexes().size());
        }
    }

}
