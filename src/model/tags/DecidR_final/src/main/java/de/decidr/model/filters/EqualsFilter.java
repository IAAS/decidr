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
import org.hibernate.criterion.Restrictions;

/**
 * A simple filter that includes or excludes entities where a given property
 * matches a given value.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class EqualsFilter implements Filter {

    /**
     * Are we including or excluding matches?
     */
    private Boolean include = true;

    /**
     * The property to compare
     */
    private String propertyName = null;

    /**
     * The value to match
     */
    private Object propertyValue = null;

    /**
     * Creates an instance of the EqualsFilter.
     * 
     * @param include
     *            whether matches are included or excluded
     * @param propertyName
     *            the property to compare
     * @param propertyValue
     *            the value to match
     */
    public EqualsFilter(Boolean include, String propertyName,
            Object propertyValue) {
        super();
        setPropertyName(propertyName);
        setInclude(include);
        setPropertyValue(propertyValue);
    }

    @Override
    public void apply(Criteria criteria) {
        if (propertyValue == null) {
            // checking against null is a special case
            if (include) {
                criteria.add(Restrictions.isNull(propertyName));
            } else {
                criteria.add(Restrictions.isNotNull(propertyName));
            }
        } else {
            if (include) {
                criteria.add(Restrictions.eq(propertyName, propertyValue));
            } else {
                criteria.add((Restrictions.not(Restrictions.eq(propertyName,
                        propertyValue))));
            }
        }
    }

    /**
     * @return whether matches are included or excluded
     */
    public Boolean getInclude() {
        return include;
    }

    /**
     * @return the property to compare
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * @return the value to match
     */
    public Object getPropertyValue() {
        return propertyValue;
    }

    /**
     * @param include
     *            whether matches are included or excluded
     */
    public void setInclude(Boolean include) {
        this.include = include;
    }

    /**
     * @param propertyName
     *            the property to compare
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * @param propertyValue
     *            the value to match
     */
    public void setPropertyValue(Object propertyValue) {
        this.propertyValue = propertyValue;
    }
}
