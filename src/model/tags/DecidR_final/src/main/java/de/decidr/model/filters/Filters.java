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

import java.util.ArrayList;
import java.util.List;

/**
 * Contains convenience methods for working with {@link Filter}s. Also serves as
 * a factory.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class Filters {

    /**
     * Applies a single filter and a criteria
     * 
     * @param criteria
     *            the Criteria to modify
     * @param filter
     *            the filter to apply to the criteria
     * @param paginator
     *            optional: the paginator to apply to the criteria. This
     *            parameter may be null.
     */
    public static void apply(PaginatingCriteria criteria, Filter filter,
            Paginator paginator) {
        List<Filter> list = new ArrayList<Filter>(1);
        if (filter != null) {
            list.add(filter);
        }
        apply(criteria, list, paginator);
    }

    /**
     * Applies a set of filters and an optional to the give
     * 
     * @param criteria
     *            the Criteria to modify
     * @param filters
     *            the filters to apply to the criteria
     * @param paginator
     *            optional: the paginator to apply to the criteria. This
     *            parameter may be null.
     */
    public static void apply(PaginatingCriteria criteria, List<Filter> filters,
            Paginator paginator) {

        if (criteria == null) {
            throw new IllegalArgumentException("Criteria must not be null");
        }

        if (filters != null) {
            for (Filter filter : filters) {
                filter.apply(criteria);
            }
        }

        if (paginator != null) {
            paginator.apply(criteria);
        }
    }

    /**
     * Private constructor prevents instantiation
     */
    private Filters() {
        // prevent instantiation
    }
}
