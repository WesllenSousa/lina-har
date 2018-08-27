/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasets.memory;

import algorithms.NOHAR.BOP;
import datasets.timeseries.TimeSeries;
import java.util.LinkedList;

/**
 *
 * @author Wesllen Sousa
 */
public class BufferStreaming {

    private TimeSeries[] subSequences;

    private BOP BOP = new BOP();
    private LinkedList<BOP> listUBOP = new LinkedList<>();
    private LinkedList<BOP> listNovelBOP = new LinkedList<>();
    private LinkedList<BOP> model = new LinkedList<>();

    /*
     *   Geters Setters
     */
    public TimeSeries[] getSubSequences() {
        return subSequences;
    }

    public void setSubSequences(TimeSeries[] subSequences) {
        this.subSequences = subSequences;
    }

    public BOP getBOP() {
        return BOP;
    }

    public void setBOP(BOP BOP) {
        this.BOP = BOP;
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

    public void clearBuffer() {
        BOP = new BOP();
        listUBOP.clear();
        listNovelBOP.clear();
        model.clear();
    }

}
