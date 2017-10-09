/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.SAX;

import net.seninp.jmotif.sax.datastructure.SAXRecord;
import net.seninp.jmotif.sax.datastructure.SAXRecords;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;

/**
 *
 * @author Wesllen Sousa
 */
public class SAXExample {

    public static void main(String[] args) {

        int WINDOW_SIZE = 28;
        int SAX_PAA = 7;
        int SAX_ALPHABET = 4;
        Double NORMALIZATION_THRESHOLD = 0.05;
        Params params = new Params(WINDOW_SIZE, SAX_PAA, SAX_ALPHABET,
                NORMALIZATION_THRESHOLD, NumerosityReductionStrategy.NONE);

        //0 = all lines
        //double[] ts = HandleData.loadData("0", "samples/TimeSeries/sinusoid.csv");
        TimeSeries timeSeries = TimeSeriesLoader.loadVerticalData("0", "C:\\Users\\Wesllen Sousa\\Lina\\Datasets\\TimeSeries\\sinusoid.csv");

//        String test = SAX.serieToWord(timeSeries.getData(), params);
//        System.out.println(test);

        SAXRecords sAXRecords = SAX.slideWindow(timeSeries.getData(), params);
        System.out.println(">> " + sAXRecords.getIndexes().size());
        System.out.println(">> " + sAXRecords.getIndexes());
        for (SAXRecord sAXRecord : sAXRecords) {
            System.out.println("=====");
            System.out.println(sAXRecord.toString());
            System.out.println(sAXRecord.getIndexes().size());
        }
    }

}
