//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.05 at 06:27:20 PM MEZ 
//


package de.decidr.model.workflowmodel.dwdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for tInvokeNode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tInvokeNode">
 *   &lt;complexContent>
 *     &lt;extension base="{http://decidr.de/schema/dwdl}tBasicNode">
 *       &lt;sequence>
 *         &lt;element ref="{http://decidr.de/schema/dwdl}setProperty" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://decidr.de/schema/dwdl}getProperty" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://decidr.de/schema/dwdl}parameter" minOccurs="0"/>
 *         &lt;element name="attachment" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="activity" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tInvokeNode", propOrder = {
    "setProperty",
    "getProperty",
    "parameter",
    "attachment"
})
public class InvokeNode
    extends BasicNode
{

    @XmlElement(required = true)
    protected List<SetProperty> setProperty;
    protected List<GetProperty> getProperty;
    protected Parameter parameter;
    protected Object attachment;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String activity;

    /**
     * Gets the value of the setProperty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the setProperty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSetProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SetProperty }
     * 
     * 
     */
    public List<SetProperty> getSetProperty() {
        if (setProperty == null) {
            setProperty = new ArrayList<SetProperty>();
        }
        return this.setProperty;
    }

    public boolean isSetSetProperty() {
        return ((this.setProperty!= null)&&(!this.setProperty.isEmpty()));
    }

    public void unsetSetProperty() {
        this.setProperty = null;
    }

    /**
     * Gets the value of the getProperty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the getProperty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGetProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GetProperty }
     * 
     * 
     */
    public List<GetProperty> getGetProperty() {
        if (getProperty == null) {
            getProperty = new ArrayList<GetProperty>();
        }
        return this.getProperty;
    }

    public boolean isSetGetProperty() {
        return ((this.getProperty!= null)&&(!this.getProperty.isEmpty()));
    }

    public void unsetGetProperty() {
        this.getProperty = null;
    }

    /**
     * Gets the value of the parameter property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getParameter() {
        return parameter;
    }

    /**
     * Sets the value of the parameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setParameter(Parameter value) {
        this.parameter = value;
    }

    public boolean isSetParameter() {
        return (this.parameter!= null);
    }

    /**
     * Gets the value of the attachment property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getAttachment() {
        return attachment;
    }

    /**
     * Sets the value of the attachment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setAttachment(Object value) {
        this.attachment = value;
    }

    public boolean isSetAttachment() {
        return (this.attachment!= null);
    }

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

}