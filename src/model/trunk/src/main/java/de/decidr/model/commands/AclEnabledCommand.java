package de.decidr.model.commands;

import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.commands.workflowmodel.WorkflowModelCommand;
import de.decidr.model.commands.workitem.WorkItemCommand;
import de.decidr.model.commands.workflowinstance.WorkflowInstanceCommand;
import de.decidr.model.commands.user.UserCommand;
import de.decidr.model.commands.tenant.TenantCommand;
import de.decidr.model.commands.system.SystemCommand;

public abstract class AclEnabledCommand extends WorkflowModelCommand {
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