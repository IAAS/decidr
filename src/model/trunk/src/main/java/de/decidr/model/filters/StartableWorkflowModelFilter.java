package de.decidr.model.filters;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;

import de.decidr.model.entities.StartableWorkflowModelView;

/**
 * Allows only startable models. A workflow model is considered startable if its
 * executable flag is set to true (which implies that deployed workflow model
 * which has the same version exists).
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class StartableWorkflowModelFilter implements Filter {

    /**
     * {@inheritDoc}
     */
    public void apply(Criteria criteria) {
        DetachedCriteria exists = DetachedCriteria
                .forClass(StartableWorkflowModelView.class);
        exists.add(Restrictions.eqProperty("id", criteria.getAlias() + ".id"));
        exists.setProjection(Projections.sqlProjection("*", new String[0],
                new Type[0]));
    }
}