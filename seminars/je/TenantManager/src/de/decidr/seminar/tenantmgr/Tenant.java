package de.decidr.seminar.tenantmgr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name="tenants")
public class Tenant {
	
	/** unique tenant id */
	@Id
	@GeneratedValue
	private int id;
	
	/** name of the tenant */
	private String name;
	
	/** fields of the tenant */
	@OneToMany(cascade=CascadeType.PERSIST)
	private List<CustomField> customFields = new ArrayList<CustomField>();
	
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

	public void addCustomField(String fieldName, String value)
			throws TenantManagerException {
		if (searchByName(fieldName) != null) {
			throw new TenantManagerException("Field already exists.");
		} else {
			customFields.add(new CustomField(this, fieldName, value));
		}
	}
	
	public Collection<CustomField> getCustomFields() {
		return customFields;
	}
	
	public void setValue(String fieldName, String value)
			throws TenantManagerException {
		if (searchByName(fieldName) == null) {
			throw new TenantManagerException("Field doesn't exist.");
		} else {
			customFields.add(new CustomField(this, fieldName, value));
		}
	}
	
	public String getValue(String fieldName) throws TenantManagerException {
		CustomField field = searchByName(fieldName);
		if (field == null) {
			throw new TenantManagerException("Field doesn't exist.");
		} else {
			return field.getValue();
		}
	}
	
	public Map<String, String> getValues() {
		Map<String, String> values = new TreeMap<String, String>();
		values.put("Name", name);

		for (CustomField field: customFields) {
			values.put(field.getName(), field.getValue());
		}
		
		return values;
	}
	
	private CustomField searchByName(String name) {
		for (CustomField field: customFields) {
			if (field.getName().equals(name)) {
				return field;
			}
		}
		return null;
	}
	
}
