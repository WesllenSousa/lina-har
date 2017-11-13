/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.pageHinkley;

import constants.ConstDataset;
import constants.ConstGeneral;
import java.util.ArrayList;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;
import java.awt.Color;

/**
 *
 * @author Wesllen Sousa
 */
public class PageHinkleyExample {

    public static void main(String[] args) {

        ConstGeneral.PERCENT_QUEDA_THRESHOULD = 30;

        //0 = all lines
        TimeSeries[] timeSeries = TimeSeriesLoader.loadVerticalData("0", ConstDataset.DS_STREAM + "215218.csv", false, ",");

        PageHinkley pageHinkley = new PageHinkley(Color.BLACK, null);
        ArrayList<PageHinkleyBean> list = pageHinkley.runTs(timeSeries[0].getData());

//        for (PageHinkleyBean page : list) {
//            System.out.println(page.toString());
//        }
    }

}
