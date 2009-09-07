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
package de.decidr.model.commands.system;

import java.util.Collection;

import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;

/**
 * The abstract system command. All system commands are derived from this class.
 * 
 * @author Markus Fischer
 * @version 0.1
 */
public abstract class SystemCommand extends AclEnabledCommand {

    /**
     * Creates a new SystemCommand.
     * 
     * @param role
     *            user/system executing the command
     * @param permission
     *            addtional resource that are being accessed by this command
     */
    public SystemCommand(Role role, Collection<Permission> permission) {
        super(role, permission);
    }
}