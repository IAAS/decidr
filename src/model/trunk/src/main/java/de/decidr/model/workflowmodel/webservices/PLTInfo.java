//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.14 at 04:34:51 PM MEZ 
//

package de.decidr.model.workflowmodel.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for PLTInfo complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="PLTInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}NCName"/>
 *         &lt;element name="myRole" type="{http://www.w3.org/2001/XMLSchema}NCName"/>
 *         &lt;element name="partnerRole" type="{http://www.w3.org/2001/XMLSchema}NCName" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PLTInfo", propOrder = { "name", "myRole", "partnerRole" })
public class PLTInfo {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String myRole;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String partnerRole;

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

    public boolean isSetName() {
        return (this.name != null);
    }

    /**
     * Gets the value of the myRole property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getMyRole() {
        return myRole;
    }

    /**
     * Sets the value of the myRole property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setMyRole(String value) {
        this.myRole = value;
    }

    public boolean isSetMyRole() {
        return (this.myRole != null);
    }

    /**
     * Gets the value of the partnerRole property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPartnerRole() {
        return partnerRole;
    }

    /**
     * Sets the value of the partnerRole property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPartnerRole(String value) {
        this.partnerRole = value;
    }

    public boolean isSetPartnerRole() {
        return (this.partnerRole != null);
    }

}
