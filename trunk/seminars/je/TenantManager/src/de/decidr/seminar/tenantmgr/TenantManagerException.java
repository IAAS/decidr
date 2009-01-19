package de.decidr.seminar.tenantmgr;

/**
 * Exception Class for all Exception thrown within the TenantManager classes.
 * @author Johannes Engelhardt
 *
 */
public class TenantManagerException extends Exception {
	
	public TenantManagerException() {
		super();
	}
	
	public TenantManagerException(String msg) {
		super(msg);
	}

}
