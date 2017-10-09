/*
 * Created on May 4, 2005
 *
 */
package controle.CRF.BSegment;

import controle.CRF.CRF.DataSequence;

/**
 * @author sunita
 *
 */
public interface BoundaryFeatureFunctions {
   // public void assignBoundary(BFeatureImpl feature, int pos);
   int maxBoundaryGap();
    void next(BFeatureImpl feature);
    boolean startScanFeaturesAt(DataSequence data, int pos);
}
