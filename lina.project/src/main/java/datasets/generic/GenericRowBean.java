package datasets.generic;

import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author Wesllen Sousa
 */
public class GenericRowBean {

    private Long id = null;
    private String timestamp = null;
    private LinkedList<String> tupla;
    private String classe = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public LinkedList<String> getTupla() {
        return tupla;
    }

    public void setTupla(LinkedList<String> tupla) {
        this.tupla = tupla;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final GenericRowBean other = (GenericRowBean) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
