//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.10 at 02:17:19 PM MESZ 
//


package de.decidr.model.workflowmodel.dwdl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tHumanTaskData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tHumanTaskData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="parameters" type="{http://decidr.de/schema/dwdl}tParameters"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="taskItem" type="{http://decidr.de/schema/dwdl}tTaskItem"/>
 *           &lt;element name="information" type="{http://decidr.de/schema/dwdl}tInformation"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tHumanTaskData", propOrder = {
    "parameters",
    "taskItemOrInformation"
})
public class HumanTaskData {

    @XmlElement(required = true)
    protected Parameters parameters;
    @XmlElements({
        @XmlElement(name = "taskItem", type = TaskItem.class),
        @XmlElement(name = "information", type = Information.class)
    })
    protected List<Object> taskItemOrInformation;

    /**
     * Gets the value of the parameters property.
     * 
     * @return
     *     possible object is
     *     {@link Parameters }
     *     
     */
    public Parameters getParameters() {
        return parameters;
    }

    /**
     * Sets the value of the parameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameters }
     *     
     */
    public void setParameters(Parameters value) {
        this.parameters = value;
    }

    public boolean isSetParameters() {
        return (this.parameters!= null);
    }

    /**
     * Gets the value of the taskItemOrInformation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taskItemOrInformation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaskItemOrInformation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaskItem }
     * {@link Information }
     * 
     * 
     */
    public List<Object> getTaskItemOrInformation() {
        if (taskItemOrInformation == null) {
            taskItemOrInformation = new ArrayList<Object>();
        }
        return this.taskItemOrInformation;
    }

    public boolean isSetTaskItemOrInformation() {
        return ((this.taskItemOrInformation!= null)&&(!this.taskItemOrInformation.isEmpty()));
    }

    public void unsetTaskItemOrInformation() {
        this.taskItemOrInformation = null;
    }

}
