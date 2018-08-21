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
    private long startTime = 0;
    private long endTime = 0;
    private Params params;
    private int offset = 0;
    private int bopSize = 0;
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

    public void setBopSize(int bopSize) {
        this.bopSize = bopSize;
    }

    @Override
    public String toString() {
        float seconds = (endTime - startTime) / 1000;

        int totalInstances = hits + hitsNovel + errors + errorsNovel;
        float accuracy = 0.f;
        int tp = hits + hitsNovel;
        if (totalInstances > 0) {
            accuracy = (float) tp / (float) totalInstances;
        }
        //float error = (errors + errorsNovel) / countBOP;
        return "EvaluationNohar{" + "\n"
                + "  Dataset=" + dataset + "\n"
                + "  Accuracy=" + (accuracy * 100) + "%\n"
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
                + "  BOP Size=" + bopSize + "\n"
                + "  uBOP=" + buffer.getListUBOP().size() + "\n"
                + "  Novel BOP=" + buffer.getListNovelBOP().size() + "\n"
                + "  Model=" + buffer.getModel().size() + "\n"
                + '}';
    }

}
