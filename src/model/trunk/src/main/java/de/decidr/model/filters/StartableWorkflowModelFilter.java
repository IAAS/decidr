package de.decidr.model.filters;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Allows only startable models.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class StartableWorkflowModelFilter implements Filter {

    /**
     * {@inheritDoc}
     */
    public void apply(Criteria criteria) {
        // TODO I have no bloody idea if this works.
        criteria.createAlias("StartableWorkflowModel",
                "StartableWorkflowModelFilter").add(
                Restrictions
                        .eqProperty("id", "StartableWorkflowModelFilter.id"));
    }
}