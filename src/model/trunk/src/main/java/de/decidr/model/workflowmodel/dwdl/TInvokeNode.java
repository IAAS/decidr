//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.06.13 at 08:40:43 PM GMT+01:00 
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
 *         &lt;element ref="{http://decidr.de/schema/dwdl}humanTaskData" minOccurs="0"/>
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
    "humanTaskData"
})
public class TInvokeNode
    extends TBasicNode
{

    @XmlElement(required = true)
    protected List<TSetProperty> setProperty;
    protected List<TGetProperty> getProperty;
    protected THumanTaskData humanTaskData;
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
     * {@link TSetProperty }
     * 
     * 
     */
    public List<TSetProperty> getSetProperty() {
        if (setProperty == null) {
            setProperty = new ArrayList<TSetProperty>();
        }
        return this.setProperty;
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
     * {@link TGetProperty }
     * 
     * 
     */
    public List<TGetProperty> getGetProperty() {
        if (getProperty == null) {
            getProperty = new ArrayList<TGetProperty>();
        }
        return this.getProperty;
    }

    /**
     * Gets the value of the humanTaskData property.
     * 
     * @return
     *     possible object is
     *     {@link THumanTaskData }
     *     
     */
    public THumanTaskData getHumanTaskData() {
        return humanTaskData;
    }

    /**
     * Sets the value of the humanTaskData property.
     * 
     * @param value
     *     allowed object is
     *     {@link THumanTaskData }
     *     
     */
    public void setHumanTaskData(THumanTaskData value) {
        this.humanTaskData = value;
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

}
