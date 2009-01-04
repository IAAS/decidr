package de.decidr.seminar.tenantmgr;

public class TenantManagerException extends Exception {
	
	public TenantManagerException() {
		super();
	}
	
	public TenantManagerException(String msg) {
		super("TenantManager error: " + msg);
	}

}
