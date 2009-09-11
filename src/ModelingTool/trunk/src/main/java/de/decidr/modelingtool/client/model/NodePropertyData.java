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

package de.decidr.modelingtool.client.model;

import java.util.Collection;
import java.util.HashMap;

/**
 * This class serves as a container for the model data (properties) of the
 * different invoke nodes and containers. All model data is stored separately in
 * this class in order to making changes (and reverting them) to the model data
 * easier. This container class is not visible to the user of the model classes
 * of an invoke node or container. The model classes provide their own method to
 * access this container. The properties themselves are stored in a hash map.
 * 
 * @author Jonas Schlaak
 */
public class NodePropertyData {

    private HashMap<String, Object> properties;

    /**
     * Constructs a new container and initializes the internal hash map.
     */
    public NodePropertyData() {
        properties = new HashMap<String, Object>();
    }

    /**
     * Returns the value that has the given key
     * 
     * @param key
     *            the king to find
     * @return the found value
     */
    @SuppressWarnings("unchecked")
    public <X extends Object> X get(String key) {
        return (X) properties.get(key);
    }

    /**
     * Sets the given key to a given value
     * 
     * @param key
     *            the key to set
     * @param value
     *            the value to set
     */
    public <X extends Object> void set(String key, X value) {
        properties.put(key, value);
    }

    /**
     * Returns all values of this container
     * 
     * @return the values
     */
    public Collection<Object> getValues() {
        return properties.values();
    }

    /**
     * Compares another container to this container and checks whether they are
     * equal or not. They are equal if they have the exact same keys the exact
     * same values associated with the keys.
     * 
     * @param container
     *            the container to which this container is compared to
     * @return true, if equal, otherwise false
     */
    public Boolean equals(NodePropertyData container) {
        Boolean result = true;

        /* Compare key set sizes */
        if (this.properties.keySet().size() != container.properties.keySet()
                .size()) {
            result = false;
        }

        /*
         * Check if one set contains all the keys of the other set, only branch
         * if result is still true
         */
        if (result
                && this.properties.keySet().containsAll(
                        container.properties.keySet()) == false) {
            result = false;
        }

        /* Check the values for each key, only branch if result is still true */
        if (result) {
            for (String key : this.properties.keySet()) {
                if (this.properties.get(key) != container.properties.get(key)) {
                    result = false;
                }
            }
        }

        return result;
    }
}
