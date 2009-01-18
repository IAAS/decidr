package de.decidr.seminar.tenantmgr;

/**
 * Object factory for the TenantManager
 * @author Johannes Engelhardt
 *
 */
public class TenantManagerFactory {

	/**
	 * Returns an instance that implements the TenantManager interface
	 * @return	the TenantManager instance
	 */
	public static TenantManager getTenantManager() {
		return new TenantManagerImpl();
	}
	
}
