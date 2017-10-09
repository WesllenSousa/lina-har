package controle.CRF.CRF;

import java.io.Serializable;

/**
 * A basic training/test instance needs to support the DataSequence interface.
 * @author Sunita Sarawagi
 *
 */ 


public interface DataSequence extends Serializable {
    int length();
    int y(int i);
    /** The type of x is never interpreted by the CRF package. This could be useful for your FeatureGenerator class */
    Object x(int i);
    void set_y(int i, int label);
}
