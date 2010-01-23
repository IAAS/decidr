package de.decidr.seminar.tenantmgr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Entity class to store a tenant.
 * @author Johannes Engelhardt
 *
 */
@Entity(name="tenants")
public class Tenant {
	
	/** unique tenant id */
	@Id
	@GeneratedValue
	private int id;
	
	/** name of the tenant */
	private String name;
	
	/** fields of the tenant */
	@OneToMany(cascade=CascadeType.ALL)
	private List<CustomField> customFields = new ArrayList<CustomField>();
	
	/** needed by JPA */
	protected Tenant() {}

	public Tenant(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	/**
	 * Adds a new custom field
	 * @param fieldName	the name of the field
	 * @param value		the value of the field
	 * @throws TenantManagerException	if field already exists
	 */
	public void addCustomField(String fieldName, String value)
			throws TenantManagerException {
		if (searchByName(fieldName) != null) {
			throw new TenantManagerException("Field already exists.");
		} else {
			customFields.add(new CustomField(this, fieldName, value));
		}
	}
	
	/**
	 * Returns a collection with all custom fields
	 * @return	the collection
	 */
	public Collection<CustomField> getCustomFields() {
		return customFields;
	}
	
	/**
	 * Sets the value of the given custom field
	 * @param fieldName	the name of the field
	 * @param value		the value of the field
	 * @throws TenantManagerException	if field doesn't exist
	 */
	public void setValue(String fieldName, String value)
			throws TenantManagerException {
		CustomField field = searchByName(fieldName);
		if (field == null) {
			throw new TenantManagerException("Field doesn't exist.");
		} else {
			field.setValue(value);
		}
	}
	
	/**
	 * Returns the value of a custom field
	 * @param fieldName	the name of the field
	 * @return			the value of the field
	 * @throws TenantManagerException	if field doesn't exist
	 */
	public String getValue(String fieldName) throws TenantManagerException {
		CustomField field = searchByName(fieldName);
		if (field == null) {
			throw new TenantManagerException("Field doesn't exist.");
		} else {
			return field.getValue();
		}
	}
	
	/**
	 * Returns a map with all custom field names and the assigned values
	 * @return	the map
	 */
	public Map<String, String> getValues() {
		Map<String, String> values = new TreeMap<String, String>();
		//values.put("Name", name);

		for (CustomField field: customFields) {
			values.put(field.getName(), field.getValue());
		}
		
		return values;
	}
	
	/**
	 * Returns the field with the given name
	 * @param name	the name of the field
	 * @return	the field. null, if no field exists with that name
	 */
	private CustomField searchByName(String name) {
		for (CustomField field: customFields) {
			if (field.getName().equals(name)) {
				return field;
			}
		}
		return null;
	}
	
}
