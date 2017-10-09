/** BFeatureGenerator.java
 * Created on Apr 2, 2005
 * 
 * @author Sunita Sarawagi
 * @since 1.2
 * @version 1.3
 *
 */
package controle.CRF.BSegmentCRF;

import controle.CRF.CRF.DataSequence;
import controle.CRF.CRF.FeatureGeneratorNested;

public interface BFeatureGenerator extends FeatureGeneratorNested {
    /**
     * @return: the maximum gap between start and end boundary of features
     */
    int maxBoundaryGap();
    void startScanFeaturesAt(DataSequence data);
    BFeature nextFeature();
}
