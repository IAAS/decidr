package de.decidr.model.exceptions;

/**
 * Thrown to indicate that two authentication keys do not match.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class AuthKeyException extends TransactionException {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    public AuthKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public AuthKeyException() {
        super("Authentication key does not match.");
    }

    /**
     * {@inheritDoc}
     */
    public AuthKeyException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public AuthKeyException(Throwable cause) {
        super(cause);
    }
}