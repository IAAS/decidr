//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.28 at 06:27:43 PM MESZ 
//


package de.decidr.model.workflowmodel.dd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tProvide complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tProvide">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="service" type="{http://www.apache.org/ode/schemas/dd/2007/03}tService"/>
 *         &lt;element name="enableSharing" type="{http://www.apache.org/ode/schemas/dd/2007/03}tEnableSharing" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="partnerLink" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tProvide", propOrder = {
    "service",
    "enableSharing"
})
public class TProvide {

    @XmlElement(required = true)
    protected TService service;
    protected TEnableSharing enableSharing;
    @XmlAttribute(required = true)
    protected String partnerLink;

    /**
     * Gets the value of the service property.
     * 
     * @return
     *     possible object is
     *     {@link TService }
     *     
     */
    public TService getService() {
        return service;
    }

    /**
     * Sets the value of the service property.
     * 
     * @param value
     *     allowed object is
     *     {@link TService }
     *     
     */
    public void setService(TService value) {
        this.service = value;
    }

    /**
     * Gets the value of the enableSharing property.
     * 
     * @return
     *     possible object is
     *     {@link TEnableSharing }
     *     
     */
    public TEnableSharing getEnableSharing() {
        return enableSharing;
    }

    /**
     * Sets the value of the enableSharing property.
     * 
     * @param value
     *     allowed object is
     *     {@link TEnableSharing }
     *     
     */
    public void setEnableSharing(TEnableSharing value) {
        this.enableSharing = value;
    }

    /**
     * Gets the value of the partnerLink property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartnerLink() {
        return partnerLink;
    }

    /**
     * Sets the value of the partnerLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartnerLink(String value) {
        this.partnerLink = value;
    }

}
