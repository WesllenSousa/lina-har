/*
 * Created on Apr 13, 2005
 *
 */
package controle.CRF.BSegment;

import controle.CRF.BSegmentCRF.BFeature;
import controle.CRF.BSegmentCRF.BFeatureGenerator;
import controle.CRF.CRF.DataIter;
import controle.CRF.CRF.DataSequence;
import controle.CRF.CRF.Segmentation;
import controle.CRF.Model.ClassPriorFeature;
import controle.CRF.Model.ConcatRegexFeatures;
import controle.CRF.Model.EdgeFeatures;
import controle.CRF.Model.EndFeatures;
import controle.CRF.Model.FeatureGenImpl;
import controle.CRF.Model.FeatureImpl;
import controle.CRF.Model.FeatureTypes;
import controle.CRF.Model.Model;
import controle.CRF.Model.StartFeatures;
import controle.CRF.Model.WindowFeatures;
import controle.CRF.Model.WordFeatures;
import controle.CRF.Model.WordsInTrain;

/**
 * @author sunita
 * @since 1.2
 * @version 1.3
 */
public class BFeatureGenImpl extends FeatureGenImpl implements
        BFeatureGenerator {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    boolean bfeatureMode = false;
    BFeatureImpl bfeature = new BFeatureImpl(), bfeatureToReturn = new BFeatureImpl();
    int maxGap = 1;

    /**
     * @param arg0
     * @param arg1
     * @throws java.lang.Exception
     */
    public BFeatureGenImpl(String arg0, int arg1) throws Exception {
        super(arg0, arg1);
    }

    public BFeatureGenImpl(String arg0, int arg1, java.util.Properties options) throws Exception {
        super(arg0, arg1, false);
        if (options.getProperty("MaxMemory") != null) {
            maxGap = Integer.parseInt(options.getProperty("MaxMemory"));
        }
        addFeatures();
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws java.lang.Exception
     */
    public BFeatureGenImpl(String arg0, int arg1, boolean arg2)
            throws Exception {
        super(arg0, arg1, arg2);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @throws java.lang.Exception
     */
    public BFeatureGenImpl(Model arg0, int arg1, boolean arg2) throws Exception {
        super(arg0, arg1, arg2);
    }

    public void addFeature(BFeatureTypes fType, boolean retainThis) {
        super.addFeature(fType, retainThis);
        maxGap = Math.max(maxGap, fType.maxBoundaryGap());
    }

    public void addFeature(BFeatureTypes fType) {
        super.addFeature(fType);
        maxGap = Math.max(maxGap, fType.maxBoundaryGap());
    }

    /* (non-Javadoc)
     * @see iitb.BSegmentCRF.BFeatureGenerator#maxBoundaryGap()
     */
    public int maxBoundaryGap() {
        return maxGap;
    }

    protected void addFeatures() {
        addFeature(new BFeatureEachPosition(
                new BFeatureEachPosition.TypePosEndOpen(new StartFeatures(this))));
        addFeature(new BFeatureEachPosition(
                new BFeatureEachPosition.TypePosStartOpen(new EndFeatures(this))));
        addFeature(new BFeatureEachPosition(new BFeatureEachPosition.TypePosEndOpen(new ClassPriorFeature(this))), true);
        addFeature(new BFeatureEachPosition(
                new BFeatureEachPosition.TypePosEndOpen(new EdgeFeatures(this))), true);
        WindowFeatures.Window windows[] = new WindowFeatures.Window[]{
            new WindowFeatures.Window(0, true, 0, true, "start", 2, Integer.MAX_VALUE),
            new WindowFeatures.Window(0, false, 0, false, "end", 2, Integer.MAX_VALUE),
            new WindowFeatures.Window(1, true, -1, false, "continue", 3, Integer.MAX_VALUE),
            new WindowFeatures.Window(0, true, 0, false, "Unique", 1, 1),
            new WindowFeatures.Window(-1, true, -1, true, "left-1"),
            new WindowFeatures.Window(1, false, 1, false, "right+1"),};

        dict = new WordsInTrain();
        addFeature(new BFeatureEachPosition(new BWindowFeatureMulti(windows, new WordFeatures(this, dict))));

        FeatureTypes features = new ConcatRegexFeatures(this, 0, 0);
        addFeature(new BFeatureEachPosition(new BFeatureTypesEachLabel(this,
                new BWindowFeatureMulti(windows, features))));

        addFeature(new BFeatureEachPosition(new BFeatureTypesEachLabel(this,
                new BSegmentLength(this, maxGap))));
    }

    /* (non-Javadoc)
     * @see iitb.BSegmentCRF.BFeatureGenerator#startScanFeaturesAt(iitb.BSegmentCRF.BDataSequence)
     */
    public void startScanFeaturesAt(DataSequence d) {
        bfeatureMode = true;
        for (int i = numFeatureTypes() - 1; i >= 0; i--) {
            ((BFeatureTypes) getFeature(i)).startScanFeaturesAt(d);
        }
        super.initScanFeaturesAt(d);
    }

    protected void copyNextFeature(FeatureImpl featureToReturn) {
        if (!bfeatureMode) {
            super.copyNextFeature(featureToReturn);
            return;
        }
        ((BFeatureTypes) currentFeatureType).next(bfeatureToReturn);
        featureToReturn.copy(bfeatureToReturn);
    }

    @Override
    public boolean featureValid(DataSequence data, int cposStart, int cposEnd, FeatureImpl featureToReturn, Model model, boolean cacheEdgeFeatures) {
        return true;
    }

    /* (non-Javadoc)
     * @see iitb.BSegmentCRF.BFeatureGenerator#nextFeature()
     */
    public BFeature nextFeature() {
        bfeature.copy(bfeatureToReturn);
        bfeature.copy(featureToReturn);
        assert (featureToReturn.identifier().equals(bfeatureToReturn.identifier()));
        advance();
        return bfeature;
    }

    public int addTrainRecord(DataSequence seq) {
        int numF = 0;
        for (startScanFeaturesAt(seq); hasNext();) {
            BFeature feature = nextFeature();
            numF++;
        }
        return numF;
    }

    public boolean train(DataIter trainData, boolean cachedLabels) throws Exception {
        boolean retVal = super.train(trainData, cachedLabels, false);
        for (trainData.startScan(); trainData.hasNext();) {
            DataSequence seq = trainData.next();
            for (startScanFeaturesAt(seq); hasNext(); nextFeature());
        }
        freezeFeatures();
        return retVal;
    }

    protected boolean retainFeature(DataSequence seq, FeatureImpl f) {
        Segmentation data = (Segmentation) seq;
        BFeature feature = bfeatureToReturn;
        if (data.getSegmentId(feature.start()) != data.getSegmentId(feature.end())) {
            return false;
        }
        int segNum = data.getSegmentId(feature.start());
        if (data.segmentLabel(segNum) != feature.y()) {
            return false;
        }
        if (!feature.startOpen() && (data.segmentStart(segNum) != feature.start())) {
            return false;
        }
        if (!feature.endOpen() && (data.segmentEnd(segNum) != feature.end())) {
            return false;
        }
        if ((segNum == 0) && (feature.yprev() >= 0)) {
            return false;
        }
        return !((segNum > 0) && (feature.yprev() >= 0) && (data.segmentLabel(segNum - 1) != feature.yprev()));
    }

    public void startScanFeaturesAt(DataSequence d, int prev, int p) {
        bfeatureMode = false;
        super.startScanFeaturesAt(d, prev, p);
    }

}
