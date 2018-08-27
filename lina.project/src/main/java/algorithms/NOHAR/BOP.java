/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR;

import datasets.memory.WordRecord;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Wesllen Sousa
 */
public class BOP {
    
    private HashMap<Integer, List<WordRecord>> histograms = new HashMap<>();
//    private HashMap<Integer, Boolean> qtdeHistSimilar = new HashMap<>();
    private List<WordRecord> realHistogram = new ArrayList<>(); //Only by GUI
//    private List<WordRecord> comparableHistogram = new ArrayList<>();
    private double label = -1;
    
    private int countUnk = 0;
    private int countNovel = 0;
    private EnumHistogram decision = EnumHistogram.OUTSIDE;
    
    private float weight = 0;
    
    public void incrementCountUnk() {
        countUnk++;
    }
    
    public void incrementCountNovel() {
        countNovel++;
    }
    
    public void updateWeight(boolean direction) {
        if (direction) { //true: aumenta, false: diminui
            weight++;
        } else {
            weight--;
        }
    }
    
    public void orderWordsHistograms() {
        Collections.sort(realHistogram);
        for (List<WordRecord> histogram : histograms.values()) {
            Collections.sort(histogram);
        }
    }

//    public void addComparableHistogram(List<WordRecord> histogram) {
//        for (WordRecord word : histogram) {
//            comparableHistogram.add(word);
//        }
//    }

    /*
        Getters and Setters
     */
    public HashMap<Integer, List<WordRecord>> getHistograms() {
        return histograms;
    }
    
    public void setHistograms(HashMap<Integer, List<WordRecord>> histograms) {
        this.histograms = histograms;
    }

//    public HashMap<Integer, Boolean> getQtdeHistSimilar() {
//        return qtdeHistSimilar;
//    }
//
//    public void setQtdeHistSimilar(HashMap<Integer, Boolean> qtdeHistSimilar) {
//        this.qtdeHistSimilar = qtdeHistSimilar;
//    }
    public List<WordRecord> getRealHistogram() {
        return realHistogram;
    }
    
    public void setRealHistogram(List<WordRecord> realHistogram) {
        this.realHistogram = realHistogram;
    }

//    public List<WordRecord> getComparableHistogram() {
//        return comparableHistogram;
//    }
//
//    public void setComparableHistogram(List<WordRecord> comparableHistogram) {
//        this.comparableHistogram = comparableHistogram;
//    }
//    public HashMap<Integer, EnumHistogram> getDecisions() {
//        return decisions;
//    }
//
//    public void setDecisions(HashMap<Integer, EnumHistogram> decisions) {
//        this.decisions = decisions;
//    }
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
    
    public EnumHistogram getDecision() {
        return decision;
    }
    
    public void setDecision(EnumHistogram decision) {
        this.decision = decision;
    }
    
    public float getWeight() {
        return weight;
    }
    
    public void setWeight(float weight) {
        this.weight = weight;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.realHistogram);
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
        if (!Objects.equals(this.realHistogram, other.realHistogram)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "BOP{" + "label=" + label + '}';
    }
    
}
