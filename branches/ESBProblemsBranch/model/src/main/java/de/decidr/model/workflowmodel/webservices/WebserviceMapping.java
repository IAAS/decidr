//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.14 at 04:34:51 PM MEZ 
//


package de.decidr.model.workflowmodel.webservices;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for WebserviceMapping complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WebserviceMapping">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="activity" type="{http://www.w3.org/2001/XMLSchema}NCName"/>
 *         &lt;element name="portType" type="{http://www.w3.org/2001/XMLSchema}NCName"/>
 *         &lt;element name="operation" type="{http://www.w3.org/2001/XMLSchema}NCName"/>
 *         &lt;element name="binding" type="{http://www.w3.org/2001/XMLSchema}NCName"/>
 *         &lt;element name="partnerLinkType" type="{http://decidr.de/schema/wsmapping}PLTInfo"/>
 *         &lt;element name="service" type="{http://www.w3.org/2001/XMLSchema}NCName"/>
 *         &lt;element name="servicePort" type="{http://www.w3.org/2001/XMLSchema}NCName"/>
 *         &lt;element name="properties" type="{http://decidr.de/schema/wsmapping}Properties" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="propertyAliases" type="{http://decidr.de/schema/wsmapping}PropertyAliases" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WebserviceMapping", propOrder = {
    "activity",
    "portType",
    "operation",
    "binding",
    "partnerLinkType",
    "service",
    "servicePort",
    "properties",
    "propertyAliases"
})
public class WebserviceMapping {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String activity;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String portType;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String operation;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String binding;
    @XmlElement(required = true)
    protected PLTInfo partnerLinkType;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String service;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String servicePort;
    protected List<Properties> properties;
    protected List<PropertyAliases> propertyAliases;

    /**
     * Gets the value of the activity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivity() {
        return activity;
    }

    /**
     * Sets the value of the activity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivity(String value) {
        this.activity = value;
    }

    public boolean isSetActivity() {
        return (this.activity!= null);
    }

    /**
     * Gets the value of the portType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortType() {
        return portType;
    }

    /**
     * Sets the value of the portType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortType(String value) {
        this.portType = value;
    }

    public boolean isSetPortType() {
        return (this.portType!= null);
    }

    /**
     * Gets the value of the operation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Sets the value of the operation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperation(String value) {
        this.operation = value;
    }

    public boolean isSetOperation() {
        return (this.operation!= null);
    }

    /**
     * Gets the value of the binding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBinding() {
        return binding;
    }

    /**
     * Sets the value of the binding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBinding(String value) {
        this.binding = value;
    }

    public boolean isSetBinding() {
        return (this.binding!= null);
    }

    /**
     * Gets the value of the partnerLinkType property.
     * 
     * @return
     *     possible object is
     *     {@link PLTInfo }
     *     
     */
    public PLTInfo getPartnerLinkType() {
        return partnerLinkType;
    }

    /**
     * Sets the value of the partnerLinkType property.
     * 
     * @param value
     *     allowed object is
     *     {@link PLTInfo }
     *     
     */
    public void setPartnerLinkType(PLTInfo value) {
        this.partnerLinkType = value;
    }

    public boolean isSetPartnerLinkType() {
        return (this.partnerLinkType!= null);
    }

    /**
     * Gets the value of the service property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getService() {
        return service;
    }

    /**
     * Sets the value of the service property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setService(String value) {
        this.service = value;
    }

    public boolean isSetService() {
        return (this.service!= null);
    }

    /**
     * Gets the value of the servicePort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServicePort() {
        return servicePort;
    }

    /**
     * Sets the value of the servicePort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServicePort(String value) {
        this.servicePort = value;
    }

    public boolean isSetServicePort() {
        return (this.servicePort!= null);
    }

    /**
     * Gets the value of the properties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the properties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Properties }
     * 
     * 
     */
    public List<Properties> getProperties() {
        if (properties == null) {
            properties = new ArrayList<Properties>();
        }
        return this.properties;
    }

    public boolean isSetProperties() {
        return ((this.properties!= null)&&(!this.properties.isEmpty()));
    }

    public void unsetProperties() {
        this.properties = null;
    }

    /**
     * Gets the value of the propertyAliases property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the propertyAliases property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropertyAliases().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropertyAliases }
     * 
     * 
     */
    public List<PropertyAliases> getPropertyAliases() {
        if (propertyAliases == null) {
            propertyAliases = new ArrayList<PropertyAliases>();
        }
        return this.propertyAliases;
    }

    public boolean isSetPropertyAliases() {
        return ((this.propertyAliases!= null)&&(!this.propertyAliases.isEmpty()));
    }

    public void unsetPropertyAliases() {
        this.propertyAliases = null;
    }

}