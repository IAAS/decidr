package de.decidr.model.exceptions;

/**
 * Thrown to indicate that a transaction was aborted due to missing
 * rights/permissions.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class AccessDeniedException extends TransactionException {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public AccessDeniedException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public AccessDeniedException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public AccessDeniedException(Throwable cause) {
        super(cause);
    }
}