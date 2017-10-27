/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.Repair;

import constants.ConstDataset;
import net.seninp.gi.logic.GrammarRules;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;

/**
 *
 * @author Wesllen Sousa
 */
public class RepairExample {

    public static void main(String[] args) {

        //0 = all lines
        TimeSeries[] timeSeries = TimeSeriesLoader.loadVerticalData("0", ConstDataset.DS_TIME_SERIES + "sinusoid.csv", false, ",");

        GrammarRules grammarRules = Repair.run(timeSeries[0].getData(), false);

        System.out.println("Normal Rules");
        System.out.println(grammarRules);
    }

}
