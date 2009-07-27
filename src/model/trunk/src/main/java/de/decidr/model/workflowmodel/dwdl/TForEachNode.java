//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.07.27 at 02:02:16 PM GMT+01:00 
//


package de.decidr.model.workflowmodel.dwdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for tForEachNode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tForEachNode">
 *   &lt;complexContent>
 *     &lt;extension base="{http://decidr.de/schema/dwdl}tBasicNode">
 *       &lt;sequence>
 *         &lt;element ref="{http://decidr.de/schema/dwdl}startCounterValue"/>
 *         &lt;element ref="{http://decidr.de/schema/dwdl}finalCounterValue"/>
 *         &lt;element ref="{http://decidr.de/schema/dwdl}completionCondition" minOccurs="0"/>
 *         &lt;element ref="{http://decidr.de/schema/dwdl}nodes"/>
 *         &lt;element ref="{http://decidr.de/schema/dwdl}arcs" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="counterName" use="required" type="{http://decidr.de/schema/dwdl}DWDLVariableName" />
 *       &lt;attribute name="parallel" use="required" type="{http://decidr.de/schema/dwdl}tBoolean" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tForEachNode", propOrder = {
    "startCounterValue",
    "finalCounterValue",
    "completionCondition",
    "nodes",
    "arcs"
})
public class TForEachNode
    extends TBasicNode
{

    @XmlElement(required = true)
    protected String startCounterValue;
    @XmlElement(required = true)
    protected String finalCounterValue;
    protected String completionCondition;
    @XmlElement(required = true)
    protected TNodes nodes;
    protected TArcs arcs;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String counterName;
    @XmlAttribute(required = true)
    protected TBoolean parallel;

    /**
     * Gets the value of the startCounterValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartCounterValue() {
        return startCounterValue;
    }

    /**
     * Sets the value of the startCounterValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartCounterValue(String value) {
        this.startCounterValue = value;
    }

    /**
     * Gets the value of the finalCounterValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinalCounterValue() {
        return finalCounterValue;
    }

    /**
     * Sets the value of the finalCounterValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinalCounterValue(String value) {
        this.finalCounterValue = value;
    }

    /**
     * Gets the value of the completionCondition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompletionCondition() {
        return completionCondition;
    }

    /**
     * Sets the value of the completionCondition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompletionCondition(String value) {
        this.completionCondition = value;
    }

    /**
     * Gets the value of the nodes property.
     * 
     * @return
     *     possible object is
     *     {@link TNodes }
     *     
     */
    public TNodes getNodes() {
        return nodes;
    }

    /**
     * Sets the value of the nodes property.
     * 
     * @param value
     *     allowed object is
     *     {@link TNodes }
     *     
     */
    public void setNodes(TNodes value) {
        this.nodes = value;
    }

    /**
     * Gets the value of the arcs property.
     * 
     * @return
     *     possible object is
     *     {@link TArcs }
     *     
     */
    public TArcs getArcs() {
        return arcs;
    }

    /**
     * Sets the value of the arcs property.
     * 
     * @param value
     *     allowed object is
     *     {@link TArcs }
     *     
     */
    public void setArcs(TArcs value) {
        this.arcs = value;
    }

    /**
     * Gets the value of the counterName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCounterName() {
        return counterName;
    }

    /**
     * Sets the value of the counterName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCounterName(String value) {
        this.counterName = value;
    }

    /**
     * Gets the value of the parallel property.
     * 
     * @return
     *     possible object is
     *     {@link TBoolean }
     *     
     */
    public TBoolean getParallel() {
        return parallel;
    }

    /**
     * Sets the value of the parallel property.
     * 
     * @param value
     *     allowed object is
     *     {@link TBoolean }
     *     
     */
    public void setParallel(TBoolean value) {
        this.parallel = value;
    }

}
