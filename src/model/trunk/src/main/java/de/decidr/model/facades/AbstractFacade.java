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
package de.decidr.model.facades;

import de.decidr.model.acl.roles.Role;

/**
 * This facade is used as parent of all facades. It guarantees that all facades
 * have a constructor with role parameter. This parameters specifies under which
 * role the facade executes the commands.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public abstract class AbstractFacade {

    protected Role actor;

    public AbstractFacade(Role actor) {
        this.actor = actor;
    }
}