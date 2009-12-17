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

import de.decidr.model.commands.TransactionalCommand;

/**
 * Represents the permission to execute a transactional command.
 * <p>
 * Internally the fully qualified name of the represented command class is used
 * to identify the permission: <br>
 * new CommandPermission(DeleteTenantCommand.class) is internally stored as the
 * string "de.decidr.model.commands.tenant.DeleteTenantCommand".
 * <p>
 * Two CommandPermissions only imply each other if they point to the exact same
 * command class. Class hierarchies are ignored:
 * <p>
 * <code>false == new CommandPermission(AbstractTenantCommand.class).implies(new
 * CommandPermission(DeleteTenantCommand.class)</code>
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 * 
 */
public class CommandPermission extends
        de.decidr.model.acl.permissions.Permission {

    private static final long serialVersionUID = 1L;

    private TransactionalCommand command = null;
    private Class<? extends TransactionalCommand> commandClass = null;

    /**
     * Constructor. Creates CommandPermission for the given command class.
     * 
     * @param command
     *            TODO document
     */
    public CommandPermission(TransactionalCommand command) {
        super(command.getClass().getName());
        this.command = command;
    }

    /**
     * Constructor. Creates CommandPermission for the given command Class.
     * 
     * @param clazz
     *            TODO document
     */
    public CommandPermission(Class<? extends TransactionalCommand> clazz) {
        super(clazz.getName());
        this.command = null;
        this.commandClass = clazz;
    }

    /**
     * @return Class of the Command which will be executed
     */
    public Class<? extends TransactionalCommand> getCommandClass() {
        if (command != null) {
            return this.command.getClass();
        } else {
            return this.commandClass;
        }
    }

    /**
     * 
     * @return the command instance
     * 
     */
    public TransactionalCommand getCommand() {
        return this.command;
    }
}