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

    public AuthKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthKeyException() {
        super("Authentication key does not match.");
    }

    public AuthKeyException(String message) {
        super(message);
    }

    public AuthKeyException(Throwable cause) {
        super(cause);
    }
}