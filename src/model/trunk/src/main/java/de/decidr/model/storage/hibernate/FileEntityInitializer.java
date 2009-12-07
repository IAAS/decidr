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

package de.decidr.model.storage.hibernate;

/**
 * Initializes new file entities created by the
 * {@link HibernateEntityStorageProvider} by using sensible default values for
 * all properties that are not nullable.
 * <p>
 * By convention, every {@link FileEntityInitializer} must have a parameterless
 * constructor.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public interface FileEntityInitializer {

    /**
     * Prepares the new entity to be saved to the database. All properties that
     * are not nullable must be assigned sensible default alues.
     * 
     * @param entity
     *            Entity to initialize
     */
    public void initEntity(Object entity);
}
