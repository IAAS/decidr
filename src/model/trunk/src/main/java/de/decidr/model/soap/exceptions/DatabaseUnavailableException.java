package de.decidr.model.soap.exceptions;

import javax.xml.ws.WebFault;

@WebFault(name = "DatabaseUnavailableException", targetNamespace = "http://decidr.de/webservices/Exceptions")
public class DatabaseUnavailableException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DatabaseUnavailableException() {
		super();
	}

	public DatabaseUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatabaseUnavailableException(String message) {
		super(message);
	}

	public DatabaseUnavailableException(Throwable cause) {
		super(cause);
	}
}
