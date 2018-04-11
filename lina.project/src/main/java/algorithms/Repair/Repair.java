/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.Repair;

import algorithms.SAX.SAX;
import algorithms.SAX.Params;
import net.seninp.gi.logic.GrammarRules;
import net.seninp.gi.repair.RePairFactory;
import net.seninp.gi.repair.RePairGrammar;
import net.seninp.gi.rulepruner.RulePrunerFactory;
import net.seninp.jmotif.sax.NumerosityReductionStrategy;
import net.seninp.jmotif.sax.datastructure.SAXRecords;

/**
 *
 * @author Wesllen Sousa
 */
public class Repair {

    public static GrammarRules run(double[] ts, boolean rulePrune) {

        int WINDOW_SIZE = 28;
        int SAX_PAA = 7;
        int SAX_ALPHABET = 4;
        Double NORMALIZATION_THRESHOLD = 0.05;
        Params params = new Params(WINDOW_SIZE, SAX_PAA, SAX_ALPHABET,
                NORMALIZATION_THRESHOLD, NumerosityReductionStrategy.NONE);

        SAX sax = new SAX(params);
        SAXRecords saxRecords = sax.slideWindowParallel(ts);
        RePairGrammar rePairGrammar = RePairFactory.buildGrammar(saxRecords);

        rePairGrammar.expandRules();
        rePairGrammar.buildIntervals(saxRecords, ts, params.windowSize);

        GrammarRules rules = rePairGrammar.toGrammarRulesData();

        if (rulePrune) {
            return RulePrunerFactory.performPruning(ts, rules);
        } else {
            return rules;
        }
    }

}
