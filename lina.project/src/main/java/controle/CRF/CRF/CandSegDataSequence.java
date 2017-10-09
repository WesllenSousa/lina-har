/*
 * Created on Nov 25, 2004
 *
 */
package controle.CRF.CRF;

import java.util.Iterator;


/**
 * @author sunita
 *
 */
public interface CandSegDataSequence extends SegmentDataSequence, CandidateSegments {
    boolean holdsInTrainingData(Feature feature, int prevPos, int pos);
    Iterator constraints(int prevPos, int pos);
}
