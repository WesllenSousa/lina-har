/*
 * Created on May 4, 2005
 *
 */
package controle.CRF.BSegment;

import controle.CRF.Model.FeatureGenImpl;
import controle.CRF.Model.FeatureImpl;
import controle.CRF.Model.FeatureTypes;
import controle.CRF.Model.FeatureTypesEachLabel;

/**
 * @author sunita
 *
 */
public class BFeatureTypesEachLabel extends FeatureTypesEachLabel implements BoundaryFeatureFunctions {

    BoundaryFeatureFunctions bsingle;
    BFeatureImpl bfeatureImpl = new BFeatureImpl();

    /**
     * @param fgen
     * @param single
     */
    public BFeatureTypesEachLabel(FeatureGenImpl fgen, FeatureTypes single) {
        super(fgen, single);
        bsingle = (BoundaryFeatureFunctions) single;
    }
    private static final long serialVersionUID = 1L;

    @Override
    public int maxBoundaryGap() {
        return bsingle.maxBoundaryGap();
    }

    @Override
    protected void nextFeature() {
        bsingle.next(bfeatureImpl);
    }

    /* (non-Javadoc)
     * @see iitb.BSegment.BoundaryFeatureFunctions#assignBoundary(iitb.BSegment.BFeatureImpl, int)
     */
 /* public void assignBoundary(BFeatureImpl feature, int pos) {
        bsingle.assignBoundary(feature,pos);
    }*/

 /* (non-Javadoc)
     * @see iitb.BSegment.BoundaryFeatureFunctions#next(iitb.BSegment.BFeatureImpl)
     */
    @Override
    public void next(BFeatureImpl f) {
        f.copy(bfeatureImpl);
        super.nextCopyDone(f);
    }

    @Override
    public void next(FeatureImpl f) {
        f.copy(bfeatureImpl);
        super.nextCopyDone(f);
    }
}
