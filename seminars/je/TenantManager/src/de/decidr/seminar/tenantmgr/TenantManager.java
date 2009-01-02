package de.decidr.seminar.tenantmgr;

public interface TenantManager {
	
	/** Tenant hinzufügen
	 * 
	 * @param name	Name des Tenants
	 * @return		Tenant-ID
	 */
	public int addTenant(String name);
	
	public boolean login(String name);
	
	public void addCustomField(String fieldName, String value);
	
	public void setValue(String fieldName, String value);
	
	public String getValue(String fieldName);
	
}
