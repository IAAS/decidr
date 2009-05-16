package de.decidr.model.soap.exceptions;

import javax.xml.ws.WebFault;

@WebFault(name = "InvalidUserException", targetNamespace = "http://decidr.de/webservices/Exceptions")
public class InvalidUserException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidUserException() {
		super();
	}

	public InvalidUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidUserException(String message) {
		super(message);
	}

	public InvalidUserException(Throwable cause) {
		super(cause);
	}
}
