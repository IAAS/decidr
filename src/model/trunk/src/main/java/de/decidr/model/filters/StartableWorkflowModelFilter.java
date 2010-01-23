/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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