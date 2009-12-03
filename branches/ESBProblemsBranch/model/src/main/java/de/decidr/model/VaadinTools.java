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

import java.io.Serializable;

import com.sun.mail.imap.protocol.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.MethodProperty;
import com.vaadin.data.util.ObjectProperty;

/**
 * Tool class for working with Vaadin {@link Item Items}.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class VaadinTools {

    /**
     * Returns an empty property.
     * 
     * @return an empty <code>{@link Property}</code>
     */
    public static Property getEmptyProperty() {
        return new ObjectProperty(null, Object.class);
    }

    /**
     * Adds multiple properties from a source object to a bean item.
     * 
     * @param bean
     *            source object that holds the properties to add
     * @param item
     *            Vaadin item that receives the new properties
     * @param properties
     *            names of the properties to add
     */
    public static void addBeanPropertiesToItem(Serializable bean,
            BeanItem item, String... properties) {
        if (bean == null || item == null || properties == null) {
            throw new IllegalArgumentException(
                    "bean, item and properties cannot be null");
        }

        for (String property : properties) {
            item.addItemProperty(property, new MethodProperty(bean, property));
        }
    }

}
