/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR.PageHinkley;

import java.awt.Color;
import java.util.Objects;

/**
 *
 * @author Wesllen Sousa
 */
public class PageHinkleyBean {

    private int position;
    private String status;
    private double intensity;
    private Color cor;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public Color getCor() {
        return cor;
    }

    public void setCor(Color cor) {
        this.cor = cor;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.position;
        hash = 53 * hash + Objects.hashCode(this.status);
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.intensity) ^ (Double.doubleToLongBits(this.intensity) >>> 32));
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
        final PageHinkleyBean other = (PageHinkleyBean) obj;
        if (this.position != other.position) {
            return false;
        }
        if (Double.doubleToLongBits(this.intensity) != Double.doubleToLongBits(other.intensity)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Posicao: " + position + ", Direcao: " + status + ", Itensidade: " + intensity + "%";
    }

}
