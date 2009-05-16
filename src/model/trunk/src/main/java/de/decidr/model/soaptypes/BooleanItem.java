package de.decidr.model.soaptypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for tBooleanItem complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;tBooleanItem&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{http://decidr.de/schema/DecidrTypes}tItem&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;value&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tBooleanItem", propOrder = { "value" })
public class BooleanItem extends Item {
	protected boolean value;

	/**
	 * Gets the value of the value property.
	 * 
	 */
	public boolean isValue() {
		return value;
	}

	/**
	 * Sets the value of the value property.
	 * 
	 */
	public void setValue(boolean value) {
		this.value = value;
	}
}
