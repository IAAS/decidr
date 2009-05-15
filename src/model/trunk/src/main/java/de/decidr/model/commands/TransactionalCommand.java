package de.decidr.model.commands;

import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.transactions.TransactionAbortedEvent;

public interface TransactionalCommand {

	public void transactionStarted(TransactionEvent evt);

	public void transactionAborted(TransactionAbortedEvent evt);

	public void transactionCommitted(TransactionEvent evt);
}