package de.decidr.model.exceptions;

import javax.xml.ws.WebFault;

/**
 * Thrown whenever a failed transaction has been rolled back by the transaction
 * manager. There are several subtypes that specify the reason for the rollback.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
@WebFault(targetNamespace = "http://decidr.de/exceptions", name = "transactionException")
public class TransactionException extends Exception {

    private static final long serialVersionUID = 1L;
    private String serviceDetail = "";

    /**
     * {@inheritDoc}
     */
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Implementation of {@link #TransactionException(String, Throwable)} needed
     * for {@link WebFault} annotation.
     */
    public TransactionException(String message, String detail, Throwable cause) {
        this(message, cause);
        serviceDetail = detail;
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
     * Implementation of {@link #TransactionException(String)} needed for
     * {@link WebFault} annotation.
     */
    public TransactionException(String message, String detail) {
        this(message);
        serviceDetail = detail;
    }

    /**
     * {@inheritDoc}
     */
    public TransactionException(Throwable cause) {
        super(cause);
    }

    /**
     * Method returning {@link TransactionException#serviceDetail} needed for
     * {@link WebFault} annotation.
     */
    String getServiceDetail() {
        return serviceDetail;
    }
}