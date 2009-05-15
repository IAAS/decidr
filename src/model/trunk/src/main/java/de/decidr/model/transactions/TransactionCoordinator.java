package de.decidr.model.transactions;

import de.decidr.model.commands.TransactionalCommand;

public interface TransactionCoordinator {

	public void runTransaction(TransactionalCommand command);

	public void runTransaction(TransactionalCommand[] commands);
}