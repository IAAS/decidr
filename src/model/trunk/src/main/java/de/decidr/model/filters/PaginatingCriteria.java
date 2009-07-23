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

import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.ResultTransformer;

/**
 * Implementation of Criteria specifically for paginated searches. Internally
 * two almost equal criteria objects are used. <br>
 * The only difference in the clone is an additional rowCount projection that is
 * used to retrieve the row count.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class PaginatingCriteria implements Criteria {

    private static final long serialVersionUID = 1L;

    /**
     * Criteria used for actual results.
     */
    private Criteria criteria;

    /**
     * Criteria used for row count.
     */
    private Criteria clone;

    /**
     * Constructor. Creates the "real" {@link Criteria} and a clone that is used
     * to retrieve the row count.
     * 
     * @param clazz
     *            TODO document
     * @param session
     *            TODO document
     */
    @SuppressWarnings("unchecked")
    public PaginatingCriteria(Class clazz, Session session) {
        this.criteria = session.createCriteria(clazz);
        this.clone = session.createCriteria(clazz);
        this.clone.setProjection(Projections.rowCount());
    }

    /**
     * Used internally to provide mehod chaining.
     * 
     * @param criteria
     *            TODO document
     * @param clone
     *            TODO document
     */
    private PaginatingCriteria(Criteria criteria, Criteria clone) {
        this.criteria = criteria;
        this.clone = clone;
    }

    @Override
    public Criteria add(Criterion criterion) {
        criteria.add(criterion);
        clone.add(criterion);
        return this;
    }

    @Override
    public Criteria addOrder(Order order) {
        criteria.addOrder(order);
        return this;
    }

    @Override
    public Criteria createAlias(String associationPath, String alias)
            throws HibernateException {
        return new PaginatingCriteria(criteria.createAlias(associationPath,
                alias), clone.createAlias(associationPath, alias));
    }

    @Override
    public Criteria createAlias(String associationPath, String alias,
            int joinType) throws HibernateException {
        return new PaginatingCriteria(criteria.createAlias(associationPath,
                alias, joinType), clone.createAlias(associationPath, alias,
                joinType));
    }

    @Override
    public Criteria createCriteria(String associationPath)
            throws HibernateException {
        return new PaginatingCriteria(criteria.createCriteria(associationPath),
                clone.createCriteria(associationPath));
    }

    @Override
    public Criteria createCriteria(String associationPath, int joinType)
            throws HibernateException {
        return new PaginatingCriteria(criteria.createCriteria(associationPath,
                joinType), clone.createCriteria(associationPath, joinType));
    }

    @Override
    public Criteria createCriteria(String associationPath, String alias)
            throws HibernateException {
        return new PaginatingCriteria(criteria.createCriteria(associationPath,
                alias), clone.createCriteria(associationPath, alias));
    }

    @Override
    public Criteria createCriteria(String associationPath, String alias,
            int joinType) throws HibernateException {
        return new PaginatingCriteria(criteria.createCriteria(associationPath,
                alias, joinType), clone.createCriteria(associationPath, alias,
                joinType));
    }

    @Override
    public String getAlias() {
        return criteria.getAlias();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List list() throws HibernateException {
        return criteria.list();
    }

    @Override
    public ScrollableResults scroll() throws HibernateException {
        return criteria.scroll();
    }

    @Override
    public ScrollableResults scroll(ScrollMode scrollMode)
            throws HibernateException {
        return criteria.scroll(scrollMode);
    }

    @Override
    public Criteria setCacheMode(CacheMode cacheMode) {
        criteria.setCacheMode(cacheMode);
        return this;
    }

    @Override
    public Criteria setCacheRegion(String cacheRegion) {
        criteria.setCacheRegion(cacheRegion);
        return this;
    }

    @Override
    public Criteria setCacheable(boolean cacheable) {
        criteria.setCacheable(cacheable);
        return this;
    }

    @Override
    public Criteria setComment(String comment) {
        criteria.setComment(comment);
        clone.setComment(comment);
        return this;
    }

    @Override
    public Criteria setFetchMode(String associationPath, FetchMode mode)
            throws HibernateException {
        criteria.setFetchMode(associationPath, mode);
        clone.setFetchMode(associationPath, mode);
        return this;
    }

    @Override
    public Criteria setFetchSize(int fetchSize) {
        criteria.setFetchSize(fetchSize);
        return this;
    }

    @Override
    public Criteria setFirstResult(int firstResult) {
        criteria.setFirstResult(firstResult);
        return this;
    }

    @Override
    public Criteria setFlushMode(FlushMode flushMode) {
        criteria.setFlushMode(flushMode);
        clone.setFlushMode(flushMode);
        return this;
    }

    @Override
    public Criteria setLockMode(LockMode lockMode) {
        criteria.setLockMode(lockMode);
        clone.setLockMode(lockMode);
        return this;
    }

    @Override
    public Criteria setLockMode(String alias, LockMode lockMode) {
        criteria.setLockMode(alias, lockMode);
        clone.setLockMode(alias, lockMode);
        return this;
    }

    @Override
    public Criteria setMaxResults(int maxResults) {
        criteria.setMaxResults(maxResults);
        return this;
    }

    @Override
    public Criteria setProjection(Projection projection) {
        criteria.setProjection(projection);
        return this;
    }

    @Override
    public Criteria setResultTransformer(ResultTransformer resultTransformer) {
        return new PaginatingCriteria(criteria
                .setResultTransformer(resultTransformer), clone
                .setResultTransformer(resultTransformer));
    }

    @Override
    public Criteria setTimeout(int timeout) {
        criteria.setTimeout(timeout);
        clone.setTimeout(timeout);
        return this;
    }

    @Override
    public Object uniqueResult() throws HibernateException {
        return criteria.uniqueResult();
    }

    /**
     * Returns the row count for this Criteria.
     * 
     * @return the row count
     * @throws HibernateException
     *             iff the row count cannot be retrieved.
     */
    public Integer rowCount() throws HibernateException {
        Number result = (Number) clone.uniqueResult();

        if (result != null) {
            return result.intValue();
        } else {
            throw new HibernateException(
                    "The row count query did not return a number.");
        }
    }
}
