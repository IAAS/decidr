package de.decidr.model.commands;

import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.transactions.TransactionAbortedEvent;

public abstract class AbstractTransactionalCommand implements
		TransactionalCommand {

	public void transactionStarted(TransactionEvent evt) {
		throw new UnsupportedOperationException();
	}

	public void transactionAborted(TransactionAbortedEvent evt) {
		throw new UnsupportedOperationException();
	}

	public void transactionCommitted(TransactionEvent evt) {
		throw new UnsupportedOperationException();
	}
}