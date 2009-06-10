package de.decidr.model.filters;

import org.hibernate.Criteria;

/**
 * Filters hibernate queries by adding criteria that are specific to each
 * implementation of Filter.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public interface Filter {

    /**
     * Adds the criteria of this filter to the given hibernate {@link Criteria}
     * 
     * @param criteria
     *            hibernate criteria to modify
     */
    public void apply(Criteria criteria);
}