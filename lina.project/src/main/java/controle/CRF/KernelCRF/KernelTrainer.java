/*
 * Created on Jun 28, 2008
 * @author sunita
 */
package controle.CRF.KernelCRF;

import java.util.Vector;


import controle.CRF.CRF.CrfParams;
import controle.CRF.CRF.Trainer;
import controle.CRF.KernelCRF.KernelCRF.SupportVector;

public abstract class KernelTrainer extends Trainer {

    public KernelTrainer(CrfParams p) {
        super(p);
    }
    public abstract Vector<SupportVector> getSupportVectors();
    public abstract SequenceKernel getKernel();
}
