package de.decidr.model.exceptions;

/**
 * Thrown to indicate that a request or invitation has expired.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class RequestExpiredException extends TransactionException {

    private static final long serialVersionUID = 1L;

    public RequestExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestExpiredException() {
        super();
    }

    public RequestExpiredException(String message) {
        super(message);
    }

    public RequestExpiredException(Throwable cause) {
        super(cause);
    }
}