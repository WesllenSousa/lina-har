/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasets.memory;

import com.carrotsearch.hppc.IntFloatOpenHashMap;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import controle.SAX.saxvsm.text.WordBag;
import controle.SFA.classification.WEASELClassifier.WScore;
import controle.SFA.transformation.BOSSModel.BagOfPattern;
import controle.SFA.transformation.SFA;
import controle.SFA.transformation.WEASELModel.BagOfBigrams;
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

    private ObjectObjectOpenHashMap<String, IntFloatOpenHashMap> matrixBossVs;
    private HashMap<String, HashMap<String, Double>> matrixSaxVsm;
    private WScore weaselModel;

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

    public ObjectObjectOpenHashMap<String, IntFloatOpenHashMap> getMatrixBossVs() {
        return matrixBossVs;
    }

    public void setMatrixBossVs(ObjectObjectOpenHashMap<String, IntFloatOpenHashMap> matrixBossVs) {
        this.matrixBossVs = matrixBossVs;
    }

    public HashMap<String, HashMap<String, Double>> getMatrixSaxVsm() {
        return matrixSaxVsm;
    }

    public void setMatrixSaxVsm(HashMap<String, HashMap<String, Double>> matrixSaxVsm) {
        this.matrixSaxVsm = matrixSaxVsm;
    }

    public WScore getWeaselModel() {
        return weaselModel;
    }

    public void setWeaselModel(WScore weaselModel) {
        this.weaselModel = weaselModel;
    }

}
