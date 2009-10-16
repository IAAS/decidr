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
package de.decidr.model.acl.permissions;

/**
 * Base class for permissions that refer to a database entity that has a unique
 * identifier.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public abstract class EntityPermission extends Permission {

    private static final long serialVersionUID = 1L;

    /**
     * The entity id.
     */
    protected Long id;

    /**
     * Creates a new permission named prefix.id if the given id is not null,
     * prefix.* otherwise.
     * 
     * @param prefix
     * @param id
     */
    public EntityPermission(String prefix, Long id) {
        super(prefix + "." + (id == null ? "*" : id.toString()));
        this.id = id;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

}
