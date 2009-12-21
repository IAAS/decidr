package de.decidr.model.filters;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
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
        DetachedCriteria exists = DetachedCriteria.forClass(
                StartableWorkflowModelView.class, "startableFilter");
        exists.add(Restrictions.eqProperty(exists.getAlias() + ".id", criteria
                .getAlias()
                + ".id"));
        /*
         * Workaround for Hibernate issue HHH-993: Criteria subquery without
         * projection fails throwing NullPointerException.
         * 
         * Additionally, Mysql doesn't seem to like aliases in EXISTS
         * subqueries, so we have to explicitly specify "*"
         */
        exists.setProjection(Projections.sqlProjection("*", new String[0],
                new Type[0]));
        criteria.add(Subqueries.exists(exists));
    }
}