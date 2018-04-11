/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.Sequitur;

import net.seninp.gi.logic.GrammarRules;
import net.seninp.gi.sequitur.SAXRule;
import net.seninp.gi.sequitur.SequiturFactory;
import util.Messages;

/**
 *
 * @author Wesllen Sousa
 */
public class Sequitur {

    public static GrammarRules run(String text) {
        try {
            SAXRule sequiturGrammar = SequiturFactory.runSequitur(text);
            GrammarRules rules = sequiturGrammar.toGrammarRulesData();
            return rules;
        } catch (Exception ex) {
            Messages msg = new Messages();
            msg.bug("Sequitur: " + ex.toString());
        }
        return new GrammarRules();
    }

}
