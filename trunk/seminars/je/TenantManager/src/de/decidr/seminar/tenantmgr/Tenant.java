package de.decidr.seminar.tenantmgr;

import java.util.Map;
import java.util.TreeMap;

public class Tenant {
	
	/** name of the tenant */
	private String name;
	
	/** fields of the tenant */
	private Map<String, String> customFields = new TreeMap<String, String>();

	public Tenant(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void addCustomField(String fieldName, String value)
			throws TenantManagerException {
		if (customFields.containsKey(fieldName)) {
			throw new TenantManagerException("Field already exists.");
		} else {
			customFields.put(fieldName, value);
		}
	}
	
	public void setValue(String fieldName, String value)
			throws TenantManagerException {
		if (!customFields.containsKey(fieldName)) {
			throw new TenantManagerException("Field doesn't exist.");
		} else {
			customFields.put(fieldName, value);
		}
	}
	
	public String getValue(String fieldName) throws TenantManagerException {
		if (!customFields.containsKey(fieldName)) {
			throw new TenantManagerException("Field doesn't exist.");
		} else {
			return customFields.get(fieldName);
		}
	}
	
	public Map<String, String> getValues() {
		Map<String, String> values = new TreeMap<String, String>();
		values.put("Name", name);
		values.putAll(customFields);
		return values;
	}
}
