//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.07.19 at 05:10:45 PM GMT+01:00 
//


package de.decidr.model.workflowmodel.dwdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for tRole complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tRole">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://decidr.de/schema/dwdl}actor" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://decidr.de/schema/dwdl}DWDLVariableName" />
 *       &lt;attribute name="configurationVariable" type="{http://decidr.de/schema/dwdl}tBoolean" default="no" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tRole", propOrder = {
    "actor"
})
public class TRole {

    @XmlElement(required = true)
    protected List<TActor> actor;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;
    @XmlAttribute
    protected TBoolean configurationVariable;

    /**
     * Gets the value of the actor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TActor }
     * 
     * 
     */
    public List<TActor> getActor() {
        if (actor == null) {
            actor = new ArrayList<TActor>();
        }
        return this.actor;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the configurationVariable property.
     * 
     * @return
     *     possible object is
     *     {@link TBoolean }
     *     
     */
    public TBoolean getConfigurationVariable() {
        if (configurationVariable == null) {
            return TBoolean.NO;
        } else {
            return configurationVariable;
        }
    }

    /**
     * Sets the value of the configurationVariable property.
     * 
     * @param value
     *     allowed object is
     *     {@link TBoolean }
     *     
     */
    public void setConfigurationVariable(TBoolean value) {
        this.configurationVariable = value;
    }

}
