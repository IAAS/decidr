package de.decidr.seminar.tenantmgr;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="custom_fields")
public class CustomField {

	/** unique id of the custom field */
	@Id
	@GeneratedValue
	private int id;
	
	//private Tenant tenant;
	
	/** name of th ecustom field */
	private String name;
	
	/** value of th ecustom field */
	private String value;
	
	protected CustomField() {}
	
	public CustomField(Tenant tenant, String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	/*
	public Tenant getTenant() {
		return tenant;
	}
	*/

}
