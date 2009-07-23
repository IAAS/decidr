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
import org.hibernate.criterion.Order;

/**
 * Adds one or more {@link Order orders} to the query.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class OrderByFilter implements Filter {

    private Order[] orders;

    /**
     * Constructor TODO document
     * 
     * @param orders
     *            TODO document
     */
    public OrderByFilter(Order... orders) {
        this.orders = orders;
    }

    @Override
    public void apply(Criteria criteria) {
        for (Order order : orders) {
            criteria.addOrder(order);
        }
    }
}
