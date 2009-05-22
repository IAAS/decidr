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
package de.decidr.model.permissions;

import de.decidr.model.commands.TransactionalCommand;

/**
 * Represents the permission to execute a transactional command.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 *
 * @version 0.1
 *
 */
public class CommandPermission extends de.decidr.model.permissions.Permission {
	
	private static final long serialVersionUID = 1L;

	/**
	 * The class of the command that is to be executed.
	 */
	protected Class<? extends TransactionalCommand> commandClass;

	/**
	 * Constructor. Creates CommandPermission for the given command Class.
	 * 
	 * @param commandClass
	 */
	public CommandPermission(Class<? extends TransactionalCommand> commandClass) {
		super(commandClass.getCanonicalName());
		this.commandClass = commandClass;
	}

	/**
	 *  
	 * @return Class of the Command which will be executed
	 */
	public Class<? extends TransactionalCommand> getCommandClass() {
		return this.commandClass;
	}
}