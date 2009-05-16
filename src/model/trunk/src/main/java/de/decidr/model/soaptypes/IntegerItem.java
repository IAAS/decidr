package de.decidr.model.soaptypes;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for tIntegerItem complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;tIntegerItem&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{http://decidr.de/schema/DecidrTypes}tItem&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;value&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}integer&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tIntegerItem", propOrder = { "value" })
public class IntegerItem extends Item {

	@XmlElement(required = true)
	protected BigInteger value;

	/**
	 * Gets the value of the value property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getValue() {
		return value;
	}

	/**
	 * Sets the value of the value property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setValue(BigInteger value) {
		this.value = value;
	}

}
