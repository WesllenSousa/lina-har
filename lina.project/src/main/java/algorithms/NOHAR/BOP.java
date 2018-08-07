/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR;

import datasets.memory.WordRecord;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Wesllen Sousa
 */
public class BOP {

    private List<WordRecord> histogram = new ArrayList<>();
    private double label = -1;

    private int countUnk = 0;
    private int countNovel = 0;
    private float weight = 0;

    private double entropy;

    public List<WordRecord> getHistogram() {
        return histogram;
    }

    public void setHistogram(List<WordRecord> histogram) {
        this.histogram = histogram;
    }

    public double getLabel() {
        return label;
    }

    public void setLabel(double label) {
        this.label = label;
    }

    public int getCountUnk() {
        return countUnk;
    }

    public int getCountNovel() {
        return countNovel;
    }

    public double getEntropy() {
        return entropy;
    }

    public float getWeight() {
        return weight;
    }
    
}
