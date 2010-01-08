//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.05 at 06:27:20 PM MEZ 
//

package de.decidr.model.workflowmodel.dwdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for tActor complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="tActor">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="userId" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="name" type="{http://decidr.de/schema/dwdl}DWDLVariableName" />
 *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="email" type="{http://decidr.de/schema/dwdl}tEmail" />
 *       &lt;attribute name="configurationVariable" type="{http://decidr.de/schema/dwdl}tBoolean" default="no" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tActor")
public class Actor {

    @XmlAttribute
    protected Long userId;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;
    @XmlAttribute
    protected String label;
    @XmlAttribute
    protected String email;
    @XmlAttribute
    protected Boolean configurationVariable;

    /**
     * Gets the value of the userId property.
     * 
     * @return possible object is {@link Long }
     * 
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *            allowed object is {@link Long }
     * 
     */
    public void setUserId(long value) {
        this.userId = value;
    }

    public boolean isSetUserId() {
        return (this.userId != null);
    }

    public void unsetUserId() {
        this.userId = null;
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

    public boolean isSetName() {
        return (this.name != null);
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

    public boolean isSetLabel() {
        return (this.label != null);
    }

    /**
     * Gets the value of the email property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setEmail(String value) {
        this.email = value;
    }

    public boolean isSetEmail() {
        return (this.email != null);
    }

    /**
     * Gets the value of the configurationVariable property.
     * 
     * @return possible object is {@link Boolean }
     * 
     */
    public Boolean getConfigurationVariable() {
        if (configurationVariable == null) {
            return Boolean.NO;
        } else {
            return configurationVariable;
        }
    }

    /**
     * Sets the value of the configurationVariable property.
     * 
     * @param value
     *            allowed object is {@link Boolean }
     * 
     */
    public void setConfigurationVariable(Boolean value) {
        this.configurationVariable = value;
    }

    public boolean isSetConfigurationVariable() {
        return (this.configurationVariable != null);
    }

}
