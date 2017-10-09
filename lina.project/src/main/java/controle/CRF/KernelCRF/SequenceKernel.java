/*
 * Created on Jun 27, 2008
 * @author sunita
 */
package controle.CRF.KernelCRF;

import java.io.Serializable;

import controle.CRF.CRF.DataSequence;


public interface SequenceKernel extends Serializable {
    double kernel(DataSequence d1, int p1, DataSequence d2, int p2);
}
