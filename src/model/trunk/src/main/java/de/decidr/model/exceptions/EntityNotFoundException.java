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

package de.decidr.model.exceptions;

import java.io.Serializable;

/**
 * Thrown when an entity is not found in the database, but is required to
 * perform the operation.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class EntityNotFoundException extends TransactionException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new EntityNotFoundException that indicates that an entity was
     * expected, but not found.
     * 
     * @param entityClass
     *            class of the entity that wasn't found
     * @param entityId
     *            if known, ID of the entity that was expected to be found.
     */
    public EntityNotFoundException(Class<? extends Serializable> entityClass,
            Long entityId) {
        super(String.format(
                "The %1$s with ID %2$d was not found in the database",
                entityClass == null ? "null" : entityClass.getSimpleName(),
                entityId));
    }

    /**
     * Creates a new EntityNotFoundException that indicates that an entity was
     * expected, but not found.
     * 
     * @param entityClass
     *            class of the entity that wasn't found
     */
    public EntityNotFoundException(Class<? extends Serializable> entityClass) {
        super(String.format("The %1$s was not found in the database",
                entityClass == null ? "null" : entityClass.getSimpleName()));
    }

    /**
     * Creates a new EntityNotFoundException that indicates that an entity was
     * expected, but not found.
     * 
     * @param entityClass
     *            class of the entity that wasn't found
     * @param entityDescription
     *            a string describing the entity.
     */
    public EntityNotFoundException(Class<? extends Serializable> entityClass,
            String entityDescription) {
        super(String.format("The %1$s \"%2$s\" was not found in the database",
                entityClass == null ? "null" : entityClass.getSimpleName(),
                entityDescription));
    }
}
