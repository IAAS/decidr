package de.decidr.model.transactions;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import de.decidr.model.commands.TransactionalCommand;

public class HibernateTransactionCoordinator implements TransactionCoordinator {
	protected static TransactionCoordinator instance;
	protected SessionFactory sessionFactory;
	protected Transaction currentTransaction;
	protected Integer nestedTransactionCount;

	private HibernateTransactionCoordinator() {
		throw new UnsupportedOperationException();
	}

	protected void beginTransaction() {
		throw new UnsupportedOperationException();
	}

	protected void commitCurrentTransaction() {
		throw new UnsupportedOperationException();
	}

	protected void rollbackCurrentTransaction() {
		throw new UnsupportedOperationException();
	}

	public void runTransaction(TransactionalCommand command) {
		throw new UnsupportedOperationException();
	}

	public void runTransaction(TransactionalCommand[] commands) {
		throw new UnsupportedOperationException();
	}

	public TransactionCoordinator getInstance() {
		throw new UnsupportedOperationException();
	}
}