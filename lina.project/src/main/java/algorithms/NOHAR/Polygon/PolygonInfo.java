/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithms.NOHAR.Polygon;

import com.vividsolutions.jts.geom.Polygon;
import java.util.Calendar;
import java.util.Objects;

/**
 *
 * @author Wesllen Sousa
 */
public class PolygonInfo {

    private Polygon polygon;
    private String name;
    private int countClassified = 0;
    private int countUpdated = 0;
    private Calendar created = Calendar.getInstance();

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountClassified() {
        return countClassified;
    }

    public void setCountClassified(int countClassified) {
        this.countClassified = countClassified;
    }

    public int getCountUpdated() {
        return countUpdated;
    }

    public void setCountUpdated(int countUpdated) {
        this.countUpdated = countUpdated;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.polygon);
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + this.countClassified;
        hash = 37 * hash + this.countUpdated;
        hash = 37 * hash + Objects.hashCode(this.created);
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
        final PolygonInfo other = (PolygonInfo) obj;
        if (this.countClassified != other.countClassified) {
            return false;
        }
        if (this.countUpdated != other.countUpdated) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.polygon, other.polygon)) {
            return false;
        }
        if (!Objects.equals(this.created, other.created)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PolygonInfo{" + "name=" + name + ", countClassified=" + countClassified + ", countUpdated=" + countUpdated + ", created=" + created + '}';
    }

}
