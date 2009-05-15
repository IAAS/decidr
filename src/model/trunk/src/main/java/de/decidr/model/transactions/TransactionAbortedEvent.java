package de.decidr.model.transactions;

import org.hibernate.Session;

public class TransactionAbortedEvent extends TransactionEvent {
	protected java.lang.Exception exception;

	public TransactionAbortedEvent(Session session, Exception exception) {
		super(session);
		this.exception = exception;
	}

	public java.lang.Exception getException() {
		return this.exception;
	}
}