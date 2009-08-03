package de.decidr.model.exceptions;

/**
 * Thrown to indicate that a request or invitation has expired.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class RequestExpiredException extends TransactionException {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    public RequestExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public RequestExpiredException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public RequestExpiredException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public RequestExpiredException(Throwable cause) {
        super(cause);
    }
}