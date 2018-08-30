/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR;

import algorithms.SAX.Params;
import datasets.memory.BufferStreaming;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Wesllen Sousa
 */
public class EvaluationNohar {

    private HashMap<Double, HashMap<Double, Integer>> confusionMatrix = new HashMap<>();
    private ArrayList<Long> timeElapsed = new ArrayList<>();

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
        //updateTime();
        populaConfusionMatrix(label, label);
        hits++;
        //printCurrentAccuracy();
        //printCurrentError();
    }

    public void incrementHistsNovel(double label) {
        //updateTime();
        populaConfusionMatrix(label, label);
        hitsNovel++;
        //printCurrentAccuracy();
        //printCurrentError();
    }

    public void incrementErrors(double labelRight, double labelWrong) {
        //updateTime();
        populaConfusionMatrix(labelRight, labelWrong);
        errors++;
        //printCurrentAccuracy();
        //printCurrentError();
    }

    public void incrementErrorsNovel(double labelRight, double labelWrong) {
        //updateTime();
        populaConfusionMatrix(labelRight, labelWrong);
        errorsNovel++;
        //printCurrentAccuracy();
        //printCurrentError();
    }

    public void incrementCountBOP(double label) {
        populaConfusionMatrix(label, 0);
        countBOPs++;
    }

    private void updateTime() {
        endTime = System.currentTimeMillis();
        long time = (endTime - startTime);
        timeElapsed.add(time);
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    private float calculeAccuracy(int TP, int totalInstances) {
        float accuracy = 0.f;
        if (totalInstances > 0) {
            accuracy = (float) TP / (float) totalInstances;
        }
        return accuracy;
    }

    private float calculeError(int FP, int totalInstances) {
        float error = 0.f;
        if (totalInstances > 0) {
            error = (float) FP / (float) totalInstances;
        }
        return error;
    }

    private void populaConfusionMatrix(double label1, double label2) {
        if (confusionMatrix.get(label1) == null) {
            HashMap<Double, Integer> item = new HashMap<>();
            item.put(label2, 1);
            confusionMatrix.put(label1, item);
        } else {
            HashMap<Double, Integer> item = confusionMatrix.get(label1);
            if (item.containsKey(label2)) {
                int value = item.get(label2);
                item.put(label2, ++value);
            } else {
                item.put(label2, 1);
            }
        }
    }

    private String confusionMatrix() {
        String matriz = "";
        for (Double label1 : confusionMatrix.keySet()) {
            matriz += "> " + label1 + "\n";
            HashMap<Double, Integer> item = confusionMatrix.get(label1);
            int contTotal = 0;
            int contRight = 0;
            for (Double label2 : item.keySet()) {
                Integer value = item.get(label2);
                if (label2 != 0) {
                    contTotal += value;
                }
                if (label1.equals(label2)) {
                    contRight += value;
                }
                matriz += "  >> " + label1 + " and " + label2 + " = " + value + "\n";
            }
            float accuracy = calculeAccuracy(contRight, contTotal);
            matriz += "  >>> " + (accuracy * 100) + "%\n";
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
        //printTime();
        float time = (endTime - startTime);

        int TP = hits + hitsNovel;
        int totalInstances = hits + hitsNovel + errors + errorsNovel;
        float accuracy = calculeAccuracy(TP, totalInstances);

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
                + "  Seconds=" + (time / 1000) + "s\n"
                + "  Times Millis=" + time + "ms\n"
                + "  " + matriz + "\n"
                + '}';
    }

    private void printCurrentAccuracy() {
        int TP = hits + hitsNovel;
        int totalInstances = hits + hitsNovel + errors + errorsNovel;
        float accuracy = calculeAccuracy(TP, totalInstances);
        System.out.println(accuracy);
    }

    private void printCurrentError() {
        int ERR = errors + errorsNovel;
        int totalInstances = hits + hitsNovel + errors + errorsNovel;
        float error = calculeError(ERR, totalInstances);
        System.out.println(error);
    }

    private void printTime() {
        for (Long t : timeElapsed) {
            System.out.println(t);
        }
    }

    public void printActiveLearning(boolean status) {
        float n = 0f;
        if (status) {
            n = 0.8f;
        }
        System.out.println(n);
    }

}
