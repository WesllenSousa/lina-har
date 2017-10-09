/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasets.memory;

import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author Wesllen Sousa
 */
public class WordRecord {

    private String word;
    private LinkedList<WordInterval> intervals = new LinkedList<>();
    private double frequency;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public LinkedList<WordInterval> getIntervals() {
        return intervals;
    }

    public void setIntervals(LinkedList<WordInterval> intervals) {
        this.intervals = intervals;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.word);
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
        final WordRecord other = (WordRecord) obj;
        if (!Objects.equals(this.word, other.word)) {
            return false;
        }
        return true;
    }

}
