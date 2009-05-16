package de.decidr.model.soaptypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for tFloatItem complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;tFloatItem&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{http://decidr.de/schema/DecidrTypes}tItem&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;value&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}float&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tFloatItem", propOrder = { "value" })
public class FloatItem extends Item {

	protected float value;

	/**
	 * Gets the value of the value property.
	 * 
	 */
	public float getValue() {
		return value;
	}

	/**
	 * Sets the value of the value property.
	 * 
	 */
	public void setValue(float value) {
		this.value = value;
	}

}
