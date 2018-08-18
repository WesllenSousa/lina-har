/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR;

import datasets.memory.WordRecord;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    public void incrementCountUnk() {
        countUnk++;
    }

    public void incrementCountNovel() {
        countNovel++;
    }

    public void updateWeight(boolean direction) {
        if(direction) { //true: aumenta, false: diminui
            weight++;
        } else {
            weight--;
        }
    }

    public void orderWordsHistogram() {
        Collections.sort(histogram);
    }

    /*
        Getters and Setters
     */
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

    public void setCountUnk(int countUnk) {
        this.countUnk = countUnk;
    }

    public int getCountNovel() {
        return countNovel;
    }

    public void setCountNovel(int countNovel) {
        this.countNovel = countNovel;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public double getEntropy() {
        return entropy;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.histogram);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BOP other = (BOP) obj;
        if (!Objects.equals(this.histogram, other.histogram)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BOP{" + "label=" + label + '}';
    }

}
