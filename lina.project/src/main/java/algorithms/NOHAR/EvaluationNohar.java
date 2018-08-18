/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR;

import algorithms.SAX.Params;

/**
 *
 * @author Wesllen Sousa
 */
public class EvaluationNohar {

    private String dataset;
    private int hits = 0;
    private int errors = 0;
    private long startTime = 0;
    private long endTime = 0;
    private Params params;
    private int offset = 0;

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public void incrementHists() {
        hits++;
    }

    public void incrementErrors() {
        errors++;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        float seconds = (endTime - startTime) / 1000;
        return "EvaluationNohar{" + "\n"
                    + "Dataset=" + dataset + "\n"
                    + "hits=" + hits + "\n"
                    + "errors=" + errors + "\n"
                    + "seconds=" + seconds + "s\n"
                    + "Window Size=" + params.windowSize + "\n"
                    + "Alphabet Size=" + params.alphabetSize + "\n"
                    + "PAA Size=" + params.paaSize + "\n"
                    + "Offset=" + offset + "\n"
                    + '}';
    }

}
