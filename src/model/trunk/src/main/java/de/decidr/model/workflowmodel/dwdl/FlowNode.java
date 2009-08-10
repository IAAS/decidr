//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.10 at 02:17:19 PM MESZ 
//


package de.decidr.model.workflowmodel.dwdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tFlowNode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tFlowNode">
 *   &lt;complexContent>
 *     &lt;extension base="{http://decidr.de/schema/dwdl}tBasicNode">
 *       &lt;sequence>
 *         &lt;element ref="{http://decidr.de/schema/dwdl}nodes"/>
 *         &lt;element ref="{http://decidr.de/schema/dwdl}arcs"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tFlowNode", propOrder = {
    "nodes",
    "arcs"
})
public class FlowNode
    extends BasicNode
{

    @XmlElement(required = true)
    protected Nodes nodes;
    @XmlElement(required = true)
    protected Arcs arcs;

    /**
     * Gets the value of the nodes property.
     * 
     * @return
     *     possible object is
     *     {@link Nodes }
     *     
     */
    public Nodes getNodes() {
        return nodes;
    }

    /**
     * Sets the value of the nodes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Nodes }
     *     
     */
    public void setNodes(Nodes value) {
        this.nodes = value;
    }

    public boolean isSetNodes() {
        return (this.nodes!= null);
    }

    /**
     * Gets the value of the arcs property.
     * 
     * @return
     *     possible object is
     *     {@link Arcs }
     *     
     */
    public Arcs getArcs() {
        return arcs;
    }

    /**
     * Sets the value of the arcs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Arcs }
     *     
     */
    public void setArcs(Arcs value) {
        this.arcs = value;
    }

    public boolean isSetArcs() {
        return (this.arcs!= null);
    }

}
