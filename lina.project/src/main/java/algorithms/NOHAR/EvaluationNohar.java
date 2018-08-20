/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR;

import algorithms.SAX.Params;
import datasets.memory.BufferStreaming;

/**
 *
 * @author Wesllen Sousa
 */
public class EvaluationNohar {

    private String dataset;
    private int hits = 0;
    private int hitsNovel = 0;
    private int errors = 0;
    private int errorsNovel = 0;
    private int countBOP;
    private long startTime = 0;
    private long endTime = 0;
    private Params params;
    private int offset = 0;
    private BufferStreaming buffer;

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public void incrementHists() {
        hits++;
    }

    public void incrementHistsNovel() {
        hitsNovel++;
    }

    public void incrementErrors() {
        errors++;
    }

    public void incrementErrorsNovel() {
        errorsNovel++;
    }

    public void incrementCountBOP() {
        countBOP++;
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

    public void setBuffer(BufferStreaming buffer) {
        this.buffer = buffer;
    }

    @Override
    public String toString() {
        float seconds = (endTime - startTime) / 1000;
        float accuracy = (hits + hitsNovel) / countBOP;
        //float error = (errors + errorsNovel) / countBOP;
        return "EvaluationNohar{" + "\n"
                + "  Dataset=" + dataset + "\n"
                + "  Count BOPs=" + countBOP + "\n"
                + "  Accuracy=" + accuracy + "%\n"
                + "  Hits=" + hits + "\n"
                + "  Hits novel=" + hitsNovel + "\n"
                //+ "  Errors=" + error + "%\n"
                + "  Errors=" + errors + "\n"
                + "  Errors novel=" + errorsNovel + "\n"
                + "  seconds=" + seconds + "s\n"
                + "  Window Size=" + params.windowSize + "\n"
                + "  Alphabet Size=" + params.alphabetSize + "\n"
                + "  PAA Size=" + params.paaSize + "\n"
                + "  Offset=" + offset + "\n"
                + "  uBOP=" + buffer.getListUBOP().size() + "\n"
                + "  Novel BOP=" + buffer.getListNovelBOP().size() + "\n"
                + "  Model=" + buffer.getModel().size() + "\n"
                + '}';
    }

}
