/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.Sequitur;

import controle.constants.ConstDataset;
import algorithms.SAX.Params;
import algorithms.SAX.SAX;
import net.seninp.gi.logic.GrammarRules;
import datasets.timeseries.TimeSeries;
import datasets.timeseries.TimeSeriesLoader;
import net.seninp.gi.rulepruner.RulePrunerFactory;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;
import net.seninp.jmotif.sax.datastructure.SAXRecords;

/**
 *
 * @author Wesllen Sousa
 */
public class SequiturTest {

    public static void main(String[] args) {

        int WINDOW_SIZE = 28;
        int SAX_PAA = 7;
        int SAX_ALPHABET = 4;
        Double NORMALIZATION_THRESHOLD = 0.05;
        Params params = new Params(WINDOW_SIZE, SAX_PAA, SAX_ALPHABET,
                NORMALIZATION_THRESHOLD, NumerosityReductionStrategy.NONE);

        //0 = all lines
        TimeSeries[] timeSeries = TimeSeriesLoader.loadVerticalData("0", ConstDataset.DS_STREAM + "sinusoid.csv", false, ",");

        SAX sax = new SAX(params);
        SAXRecords saxRecords = sax.slideWindow(timeSeries[0].getData());
        GrammarRules grammarRules = Sequitur.run(saxRecords.getSAXString(" "));

        GrammarRules grammarRulesPruned = RulePrunerFactory.performPruning(timeSeries[0].getData(), grammarRules);

        System.out.println("Normal Rules");
        System.out.println(grammarRules);
        System.out.println("Normal Rules Pruned");
        System.out.println(grammarRulesPruned);
    }

}
