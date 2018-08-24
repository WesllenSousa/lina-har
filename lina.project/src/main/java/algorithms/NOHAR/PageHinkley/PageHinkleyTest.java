/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR.PageHinkley;

import controle.constants.ConstDataset;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;

/**
 *
 * @author Wesllen Sousa
 */
public class PageHinkleyTest {

    public static void main(String[] args) {
        //0 = all lines
        TimeSeries[] timeSeries = TimeSeriesLoader.loadVerticalData("0", ConstDataset.DS_STREAM + "215218.csv", false, ",");

        PageHinkley pageHinkley = new PageHinkley(0.25);
        pageHinkley.runTs(timeSeries[0].getData());
    }

}
