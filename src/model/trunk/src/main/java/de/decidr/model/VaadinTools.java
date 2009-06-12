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

package de.decidr.model;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;

/**
 * Tool class for working with Vaadin {@link Item}s.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class VaadinTools {

    /**
     * Returns an empty property.
     * 
     * @param propertyId
     * @return
     */
    public static Property getEmptyProperty(Object propertyId) {
        return new ObjectProperty(null, Object.class);
    }

}
