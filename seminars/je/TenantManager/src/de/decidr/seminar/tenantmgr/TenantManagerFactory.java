package de.decidr.seminar.tenantmgr;

public class TenantManagerFactory {

	public static TenantManager getTenantManager() {
		return new TenantManagerImpl();
	}
	
}
