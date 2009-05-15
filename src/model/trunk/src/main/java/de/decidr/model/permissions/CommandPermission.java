package de.decidr.model.permissions;

import de.decidr.model.commands.TransactionalCommand;

public class CommandPermission extends de.decidr.model.permissions.Permission {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Class<? extends TransactionalCommand> commandClass;

	public CommandPermission(Class<? extends TransactionalCommand> commandClass) {
		super(commandClass.getCanonicalName());
		this.commandClass = commandClass;
	}

	public Class<? extends TransactionalCommand> getCommandClass() {
		return this.commandClass;
	}
}