package de.decidr.seminar.tenantmgr;

import java.util.Map;
import java.util.TreeMap;

public class TenantList {

	private Map<String, Tenant> tenants = new TreeMap<String, Tenant>();
	
	public void addTenant(Tenant tenant) throws TenantManagerException {
		if (tenants.containsKey(tenant.getName())) {
			throw new TenantManagerException("Tenant already exists.");
		} else {
			tenants.put(tenant.getName(), tenant);
		}
	}
	
	public Tenant getTenant(String name) throws TenantManagerException {
		if (!tenants.containsKey(name)) {
			throw new TenantManagerException("Tenant not found.");
		} else {
			return tenants.get(name);
		}
	}
	
}
