/*
 * Created on Dec 29, 2007
 * @author sunita
 */
package controle.CRF.CRF;

public interface KeyedDataSequence extends DataSequence {
    int getKey();
    void setKey(int key);
}
