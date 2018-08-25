/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR;

import algorithms.SAX.Params;
import datasets.memory.BufferStreaming;
import java.util.HashMap;

/**
 *
 * @author Wesllen Sousa
 */
public class EvaluationNohar {

    private HashMap<Double, HashMap<Double, Integer>> confusionMatrix = new HashMap<>();

    private String dataset;
    private int hits = 0;
    private int hitsNovel = 0;
    private int errors = 0;
    private int errorsNovel = 0;
    private int countBOPs = 0;
    private long startTime = 0;
    private long endTime = 0;
    private Params params;
    private int offset = 0;
    private int bopSize = 0;
    private BufferStreaming buffer;

    public void incrementHists(double label) {
        populaConfusionMatrix(label, label);
        hits++;
    }

    public void incrementHistsNovel(double label) {
        populaConfusionMatrix(label, label);
        hitsNovel++;
    }

    public void incrementErrors(double labelRight, double labelWrong) {
        populaConfusionMatrix(labelRight, labelWrong);
        errors++;
    }

    public void incrementErrorsNovel(double labelRight, double labelWrong) {
        populaConfusionMatrix(labelRight, labelWrong);
        errorsNovel++;
    }

    public void incrementCountBOP(double label) {
        populaConfusionMatrix(label, -9.);
        countBOPs++;
        //calculeAccuracy();
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    private float calculeAccuracy() {
        int totalInstances = hits + hitsNovel + errors + errorsNovel;
        float accuracy = 0.f;
        int TP = hits + hitsNovel;
        if (totalInstances > 0) {
            accuracy = (float) TP / (float) totalInstances;
        }
//        System.out.println(accuracy);
        return accuracy;
    }

    private void populaConfusionMatrix(double label1, double label2) {
        if (confusionMatrix.get(label1) == null) {
            HashMap<Double, Integer> item = new HashMap<>();
            item.put(label2, 1);
            confusionMatrix.put(label1, item);
        } else {
            HashMap<Double, Integer> item = confusionMatrix.get(label1);
            if (confusionMatrix.get(label1).containsKey(label2)) {
                int value = item.get(label2);
                item.put(label2, ++value);
            } else {
                item.put(label2, 1);
            }
        }
    }

    private String confusionMatrix() {
        String matriz = "";
        for (Double label : confusionMatrix.keySet()) {
            matriz += "> " + label + "\n";
            HashMap<Double, Integer> item = confusionMatrix.get(label);
            for (Double label2 : item.keySet()) {
                Integer value = item.get(label2);
                matriz += "  >> " + label + " and " + label2 + " = " + value + "\n";
            }
        }
        return matriz;
    }

    /*
        Getters and Setters
     */
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
        float accuracy = calculeAccuracy();
        String matriz = confusionMatrix();

        return "EvaluationNohar{" + "\n"
                + "  Dataset=" + dataset + "\n"
                + "  Accuracy=" + (accuracy * 100) + "%\n"
                + "  Hits=" + hits + "\n"
                + "  Hits novel=" + hitsNovel + "\n"
                + "  Errors=" + errors + "\n"
                + "  Errors novel=" + errorsNovel + "\n"
                + "  Count BOPs=" + countBOPs + "\n"
                + "  Window Size=" + params.windowSize + "\n"
                + "  Alphabet Size=" + params.alphabetSize + "\n"
                + "  PAA Size=" + params.paaSize + "\n"
                + "  Offset=" + offset + "\n"
                + "  BOP Size=" + bopSize + "\n"
                + "  uBOP=" + buffer.getListUBOP().size() + "\n"
                + "  Novel BOP=" + buffer.getListNovelBOP().size() + "\n"
                + "  Model=" + buffer.getModel().size() + "\n"
                + "  seconds=" + seconds + "s\n"
                + "  " + matriz + "\n"
                + '}';
    }

}
