/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasets.memory;

import constants.Parameters;
import controle.SFA.transformation.BOSSModel;
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
    private ArrayList<WordRecord> bufferWord = new ArrayList<>();
    private ArrayList<WordRecord> bufferWordOOV = new ArrayList<>();

    private ArrayList<BOSSModel.BagOfPattern> bagOfPatterns = new ArrayList<>();

    public SFA getSfa() {
        return sfa;
    }

    public void setSfa(SFA sfa) {
        this.sfa = sfa;
    }

    public LinkedList<TimeSeries> getBufferMCB() {
        return bufferMCB;
    }

    public ArrayList<WordRecord> getBufferWord() {
        return bufferWord;
    }

    public ArrayList<WordRecord> getBufferWordOOV() {
        return bufferWordOOV;
    }

    public ArrayList<BOSSModel.BagOfPattern> getBagOfPatterns() {
        return bagOfPatterns;
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
        return Objects.equals(this.sfa, other.sfa);
    }

    /*
        Another methods
     */
    public WordRecord getWordRecord(String word, int position) {
        WordRecord wordRecord = new WordRecord();
        wordRecord.setWord(word);
        wordRecord.getIntervals().add(getWordInterval(position));

        return wordRecord;
    }

    public WordRecord getWordRecord(short[] wordBit, int position) {
        WordRecord wordRecord = new WordRecord();
        wordRecord.setWordBit(wordBit);
        wordRecord.setWord(toSfaWord(wordBit));
        wordRecord.getIntervals().add(getWordInterval(position));

        return wordRecord;
    }

    private WordInterval getWordInterval(int position) {
        WordInterval wordInterval = new WordInterval();
        wordInterval.setPositionInit(position);
        wordInterval.setPositionEnd(position + Parameters.WINDOW_SIZE);
        return wordInterval;
    }

    private String toSfaWord(short[] word) {
        StringBuilder sfaWord = new StringBuilder();
        for (short c : word) {
            sfaWord.append((char) (Character.valueOf('a') + c));
        }
        return sfaWord.toString();
    }

}
