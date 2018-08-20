/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasets.memory;

import algorithms.NOHAR.BOP;
import controle.constants.Parameters;
import datasets.timeseries.TimeSeries;
import java.util.LinkedList;

/**
 *
 * @author Wesllen Sousa
 */
public class BufferStreaming {

    private TimeSeries subSequence = new TimeSeries();

    private BOP BOP = new BOP();
    private LinkedList<BOP> bufferBOP = new LinkedList<>();

    private LinkedList<BOP> listUBOP = new LinkedList<>();
    private LinkedList<BOP> listNovelBOP = new LinkedList<>();
    private LinkedList<BOP> model = new LinkedList<>();

    /*
     *   Geters Setters
     */
    public TimeSeries getSubSequence() {
        return subSequence;
    }

    public void setSubSequence(TimeSeries subSequence) {
        this.subSequence = subSequence;
    }

    public BOP getBOP() {
        return BOP;
    }

    public void setBOP(BOP BOP) {
        this.BOP = BOP;
    }

    public LinkedList<BOP> getBufferBOP() {
        return bufferBOP;
    }

    public void setBufferBOP(LinkedList<BOP> bufferBOP) {
        this.bufferBOP = bufferBOP;
    }

    public LinkedList<BOP> getListUBOP() {
        return listUBOP;
    }

    public void setListUBOP(LinkedList<BOP> listUBOP) {
        this.listUBOP = listUBOP;
    }

    public LinkedList<BOP> getListNovelBOP() {
        return listNovelBOP;
    }

    public void setListNovelBOP(LinkedList<BOP> listNovelBOP) {
        this.listNovelBOP = listNovelBOP;
    }

    public LinkedList<BOP> getModel() {
        return model;
    }

    public void setModel(LinkedList<BOP> model) {
        this.model = model;
    }

    /*
     *   Handle Word Record
     */
    public WordRecord populaWordRecord(String word, int initialPosition) {
        WordRecord wordRecord = new WordRecord();
        wordRecord.setWord(word);
        wordRecord.setFrequency(1);
        wordRecord.getIntervals().add(getWordInterval(initialPosition));
        return wordRecord;
    }

    public WordInterval getWordInterval(int initialPosition) {
        WordInterval wordInterval = new WordInterval();
        wordInterval.setPositionInit(initialPosition);
        wordInterval.setPositionEnd(initialPosition + Parameters.WINDOW_SIZE);
        return wordInterval;
    }

    public void clearBuffer() {
        bufferBOP.clear();
        listUBOP.clear();
        listNovelBOP.clear();
        model.clear();
    }

}
