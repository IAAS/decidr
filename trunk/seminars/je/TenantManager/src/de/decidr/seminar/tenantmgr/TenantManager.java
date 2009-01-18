package de.decidr.seminar.tenantmgr;

import java.util.Map;

/**
 * Interface for the TenantManager. Provides functions to add tenants to the
 * database, login by name. And some tenant-related functions. The session id
 * returned by the login function is needed for all the tenant-related
 * functions.
 * @author Johannes Engelhardt
 *
 */
public interface TenantManager {
	
	/**
	 * Adds a new tenant to the database
	 * @param name			name of the new tenant
	 * @throws Exception	if name already exists
	 */
	public void addTenant(String name) throws Exception;
	
	/**
	 * Login as a tenant, a new session id is created for further communication
	 * @param name			name of the tenant
	 * @return				the session id
	 * @throws Exception	if tenant name doesn't exist or login not successful
	 */
	public String login(String name) throws Exception;
	
	/**
	 * Logout, deletes the session, then the sid is invalid
	 * @param sid			the session id
	 * @throws Exception	if sid is not found or logout not successful
	 */
	public void logout(String sid) throws Exception;
	
	/**
	 * Adds a new custom field to the tenant.
	 * @param sid			the session id
	 * @param fieldName		name of the new custom field
	 * @param value			value of the new custom field
	 * @throws Exception	if sid is not found or fieldName already exists
	 */
	public void addCustomField(String sid, String fieldName, String value)
			throws Exception;
	
	/**
	 * Sets the value of a field
	 * @param sid			the session id
	 * @param fieldName		name of the field
	 * @param value			field value to set
	 * @throws Exception	if sid is not found or fieldName doesn't exist
	 */
	public void setValue(String sid, String fieldName, String value)
			throws Exception;
	
	/**
	 * Returns the value of a field
	 * @param sid			the session id
	 * @param fieldName		name of the field
	 * @return				value of the field
	 * @throws Exception	if sid is not found or fieldName doesn't exist
	 */
	public String getValue(String sid, String fieldName) throws Exception;
	
	/**
	 * Returns all tenant fields and values
	 * @param sid			the session id
	 * @return				map of fields and values
	 * @throws Exception	if sid is not found
	 */
	public Map<String, String> getValues(String sid) throws Exception;
	
}
