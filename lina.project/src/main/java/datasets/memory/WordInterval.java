/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasets.memory;

/**
 *
 * @author Wesllen Sousa
 */
public class WordInterval {
    
    private int positionInit;
    private int positionEnd;

    public int getPositionInit() {
        return positionInit;
    }

    public void setPositionInit(int positionInit) {
        this.positionInit = positionInit;
    }

    public int getPositionEnd() {
        return positionEnd;
    }

    public void setPositionEnd(int positionEnd) {
        this.positionEnd = positionEnd;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.positionInit;
        hash = 97 * hash + this.positionEnd;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WordInterval other = (WordInterval) obj;
        if (this.positionInit != other.positionInit) {
            return false;
        }
        if (this.positionEnd != other.positionEnd) {
            return false;
        }
        return true;
    }
  
}
