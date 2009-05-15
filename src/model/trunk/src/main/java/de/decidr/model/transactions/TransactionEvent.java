package de.decidr.model.transactions;

import org.hibernate.Session;

public class TransactionEvent {
	protected Session session;
	protected Boolean innerTransaction;

	public TransactionEvent(Session session) {
		this.session = session;
	}

	public Session getSession() {
		return this.session;
	}

	public Boolean getInnerTransaction() {
		return this.innerTransaction;
	}
}