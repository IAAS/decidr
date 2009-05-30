package de.decidr.model.commands;

import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public abstract class AclEnabledCommand extends AbstractTransactionalCommand {
	protected Role role;
	protected Permission permission;

	public final void transactionStarted(TransactionEvent evt) {
		throw new UnsupportedOperationException();
	}

	public abstract void transactionAllowed(TransactionEvent evt);

	public AclEnabledCommand(Role role, Permission permission) {
		throw new UnsupportedOperationException();
	}
}