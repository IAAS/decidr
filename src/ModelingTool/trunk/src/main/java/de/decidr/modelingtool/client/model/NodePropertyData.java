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
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class NodePropertyData {

    private HashMap<String, Object> properties;

    public NodePropertyData() {
        properties = new HashMap<String, Object>();
    }

    @SuppressWarnings("unchecked")
    public <X extends Object> X get(String key) {
        return (X) properties.get(key);
    }

    public <X extends Object> void set(String key, X value) {
        properties.put(key, value);
    }
    
    public Collection<Object> getValues() {
        return properties.values();
    }

}
