//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.10.30 at 04:10:57 PM MEZ 
//


package de.decidr.model.workflowmodel.dwdl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tInitialValues complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tInitialValues">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="initialValue" type="{http://decidr.de/schema/dwdl}tLiteral" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tInitialValues", propOrder = {
    "initialValue"
})
public class InitialValues {

    @XmlElement(required = true)
    protected List<Literal> initialValue;

    /**
     * Gets the value of the initialValue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the initialValue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInitialValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Literal }
     * 
     * 
     */
    public List<Literal> getInitialValue() {
        if (initialValue == null) {
            initialValue = new ArrayList<Literal>();
        }
        return this.initialValue;
    }

    public boolean isSetInitialValue() {
        return ((this.initialValue!= null)&&(!this.initialValue.isEmpty()));
    }

    public void unsetInitialValue() {
        this.initialValue = null;
    }

}
