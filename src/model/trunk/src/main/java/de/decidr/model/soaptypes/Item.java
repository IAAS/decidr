package de.decidr.model.soaptypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for tItem complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;tItem&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;hint&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;label&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name=&quot;name&quot; use=&quot;required&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;readOnly&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; default=&quot;false&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tItem", propOrder = { "hint", "label" })
@XmlSeeAlso( { DateItem.class, FloatItem.class, URIItem.class,
		IntegerItem.class, StringItem.class, BooleanItem.class })
public class Item {

	@XmlElement(required = true)
	protected String hint;
	@XmlElement(required = true)
	protected String label;
	@XmlAttribute(required = true)
	protected String name;
	@XmlAttribute
	protected Boolean readOnly;

	/**
	 * Gets the value of the hint property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getHint() {
		return hint;
	}

	/**
	 * Sets the value of the hint property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setHint(String value) {
		this.hint = value;
	}

	/**
	 * Gets the value of the label property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the value of the label property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLabel(String value) {
		this.label = value;
	}

	/**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Gets the value of the readOnly property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public boolean isReadOnly() {
		if (readOnly == null) {
			return false;
		} else {
			return readOnly;
		}
	}

	/**
	 * Sets the value of the readOnly property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setReadOnly(Boolean value) {
		this.readOnly = value;
	}

}
