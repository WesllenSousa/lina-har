/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR;

/**
 *
 * @author Wesllen Sousa
 */
public class EvaluationNohar {

    private int hits = 0;
    private int errors = 0;

    public void incrementHists() {
        hits++;
    }

    public void incrementErrors() {
        errors++;
    }

    @Override
    public String toString() {
        return "EvaluationNohar{" + "hits=" + hits + ", errors=" + errors + '}';
    }

}
