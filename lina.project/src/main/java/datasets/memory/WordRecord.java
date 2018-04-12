/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasets.memory;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Wesllen Sousa
 */
public class WordRecord {

    private String word;
    private short[] wordBit;
    private String label;
    private ArrayList<WordInterval> intervals = new ArrayList<>();
    private int frequency;
    
    public void incrementFrequency() {
        frequency += 1;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public short[] getWordBit() {
        return wordBit;
    }

    public void setWordBit(short[] wordBit) {
        this.wordBit = wordBit;
    }

    public ArrayList<WordInterval> getIntervals() {
        return intervals;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setIntervals(ArrayList<WordInterval> intervals) {
        this.intervals = intervals;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
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
        return Objects.equals(this.word, other.word);
    }

}
