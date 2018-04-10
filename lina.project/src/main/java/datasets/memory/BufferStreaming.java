/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasets.memory;

import com.carrotsearch.hppc.IntFloatHashMap;
import com.carrotsearch.hppc.ObjectObjectHashMap;
import controle.SAX.saxvsm.text.WordBag;
import controle.SFA.transformation.BOSS.BagOfPattern;
import controle.SFA.transformation.SFA;
import controle.SFA.transformation.WEASEL.BagOfBigrams;
import datasets.timeseries.TimeSeries;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Wesllen Sousa
 */
public class BufferStreaming {

    private LinkedList<TimeSeries> bufferMCB = new LinkedList<>();
    private SFA mcb;

    private List<WordRecord> bufferWord = new ArrayList<>();
    private List<WordRecord> bufferWordOOV = new ArrayList<>();

    private List<WordBag> BOPSax = new ArrayList<>();
    private List<BagOfPattern> BOPBoss = new ArrayList<>();
    private List<BagOfBigrams> BOPWeasel = new ArrayList<>();

    private ObjectObjectHashMap<String, IntFloatHashMap> matrixBossVs;
    private HashMap<String, HashMap<String, Double>> matrixSaxVsm;
    //private WScore weaselModel;

    public SFA getMcb() {
        return mcb;
    }

    public void setMcb(SFA mcb) {
        this.mcb = mcb;
    }

    public LinkedList<TimeSeries> getBufferMCB() {
        return bufferMCB;
    }

    public List<WordRecord> getBufferWord() {
        return bufferWord;
    }

    public List<WordRecord> getBufferWordOOV() {
        return bufferWordOOV;
    }

    public List<WordBag> getBOPSax() {
        return BOPSax;
    }

    public List<BagOfPattern> getBOPBoss() {
        return BOPBoss;
    }

    public List<BagOfBigrams> getBOPWeasel() {
        return BOPWeasel;
    }

    public ObjectObjectHashMap<String, IntFloatHashMap> getMatrixBossVs() {
        return matrixBossVs;
    }

    public void setMatrixBossVs(ObjectObjectHashMap<String, IntFloatHashMap> matrixBossVs) {
        this.matrixBossVs = matrixBossVs;
    }

    public HashMap<String, HashMap<String, Double>> getMatrixSaxVsm() {
        return matrixSaxVsm;
    }

    public void setMatrixSaxVsm(HashMap<String, HashMap<String, Double>> matrixSaxVsm) {
        this.matrixSaxVsm = matrixSaxVsm;
    }

//    public WScore getWeaselModel() {
//        return weaselModel;
//    }
//
//    public void setWeaselModel(WScore weaselModel) {
//        this.weaselModel = weaselModel;
//    }

}
