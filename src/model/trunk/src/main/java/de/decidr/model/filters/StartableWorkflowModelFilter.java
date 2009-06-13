package de.decidr.model.filters;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * Allows only startable models. A workflow model is considered startable if its
 * executable flag is set to true and a deployed workflow model that has the
 * same version exists.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class StartableWorkflowModelFilter implements Filter {

    /**
     * {@inheritDoc}
     */
    public void apply(Criteria criteria) {
        criteria.add(Restrictions.eq("executable", true));
        criteria.createCriteria("deployedWorkflowModels", "dwm").add(
                Restrictions.eqProperty("dwm.version", "version"));
    }
}