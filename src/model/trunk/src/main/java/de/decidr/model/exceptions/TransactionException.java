package de.decidr.model.exceptions;

/**
 * Thrown whenever a failed transaction has been rolled back by the transaction
 * manager.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class TransactionException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public TransactionException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public TransactionException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public TransactionException(Throwable cause) {
        super(cause);
    }
}