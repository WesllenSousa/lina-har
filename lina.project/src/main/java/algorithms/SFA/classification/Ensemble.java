package algorithms.SFA.classification;

import java.util.List;

/**
 * An Ensemble of Classifiers
 * @param <E>
 */
public class Ensemble<E> {

    public List<E> model;

    public Ensemble() {
    }

    /**
     * Create an Ensemble
     *
     * @param models List of models
     */
    public Ensemble(List<E> models) {
        this.model = models;
    }

    public E getHighestScoringModel() {
        return model.get(0);
    }

    public E get(int i) {
        return model.get(i);
    }

    public int size() {
        return model.size();
    }
}
