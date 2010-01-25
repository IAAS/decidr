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