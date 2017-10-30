/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasets.memory;

import constants.Parameters;
import controle.SFA.transformation.SFA;
import datasets.timeseries.TimeSeries;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author Wesllen Sousa
 */
public class BufferStreaming {

    private SFA sfa;
    private LinkedList<TimeSeries> bufferMCB = new LinkedList<>();
    private ArrayList<WordRecord> bufferBOP = new ArrayList<>();
    private ArrayList<WordRecord> bufferOOV = new ArrayList<>();

    public SFA getSfa() {
        return sfa;
    }

    public void setSfa(SFA sfa) {
        this.sfa = sfa;
    }

    public LinkedList<TimeSeries> getBufferMCB() {
        return bufferMCB;
    }

    public ArrayList<WordRecord> getBufferBOP() {
        return bufferBOP;
    }

    public ArrayList<WordRecord> getBufferOOV() {
        return bufferOOV;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.sfa);
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
        final BufferStreaming other = (BufferStreaming) obj;
        if (!Objects.equals(this.sfa, other.sfa)) {
            return false;
        }
        return true;
    }

    /*
        Another methods
     */
    public WordRecord getWordRecord(short[] word, int position) {
        WordInterval wordInterval = new WordInterval();
        wordInterval.setPositionInit(position);
        wordInterval.setPositionEnd(position + Parameters.WINDOW_SIZE);

        WordRecord wordRecord = new WordRecord();
        wordRecord.setWord(toSfaWord(word));
        wordRecord.getIntervals().add(wordInterval);

        return wordRecord;
    }

    private String toSfaWord(short[] word) {
        StringBuilder sfaWord = new StringBuilder();
        for (short c : word) {
            sfaWord.append((char) (Character.valueOf('a') + c));
        }
        return sfaWord.toString();
    }

}
