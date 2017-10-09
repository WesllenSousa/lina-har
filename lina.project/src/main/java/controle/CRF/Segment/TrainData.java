package controle.CRF.Segment;

import controle.CRF.CRF.DataIter;
import controle.CRF.CRF.DataSequence;

/**
 *
 * @author Sunita Sarawagi
 *
 */
public interface TrainData extends DataIter {

    int size();   // number of training records

    void startScan(); // start scanning the training data

    boolean hasMoreRecords();

    TrainRecord nextRecord();

    boolean hasNext();

    DataSequence next();
}
