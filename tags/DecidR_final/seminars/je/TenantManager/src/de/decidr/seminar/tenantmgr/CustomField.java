package de.decidr.seminar.tenantmgr;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Entity class for custom tenant datafields.
 * @author Johannes Engelhardt
 *
 */
@Entity(name="custom_fields")
public class CustomField {

	/** unique id of the custom field */
	@Id
	@GeneratedValue
	private int id;
	
	/** name of the custom field */
	private String name;
	
	/** value of the custom field */
	private String value;
	
	/** needed by JPA */
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

}
