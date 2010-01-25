/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for tForEachNode complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
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
@XmlType(name = "tForEachNode", propOrder = { "startCounterValue",
        "finalCounterValue", "completionCondition", "nodes", "arcs" })
public class ForEachNode extends BasicNode {

    @XmlElement(required = true)
    protected String startCounterValue;
    @XmlElement(required = true)
    protected String finalCounterValue;
    protected String completionCondition;
    @XmlElement(required = true)
    protected Nodes nodes;
    protected Arcs arcs;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String counterName;
    @XmlAttribute(required = true)
    protected Boolean parallel;

    /**
     * Gets the value of the arcs property.
     * 
     * @return possible object is {@link Arcs }
     * 
     */
    public Arcs getArcs() {
        return arcs;
    }

    /**
     * Gets the value of the completionCondition property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCompletionCondition() {
        return completionCondition;
    }

    /**
     * Gets the value of the counterName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCounterName() {
        return counterName;
    }

    /**
     * Gets the value of the finalCounterValue property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getFinalCounterValue() {
        return finalCounterValue;
    }

    /**
     * Gets the value of the nodes property.
     * 
     * @return possible object is {@link Nodes }
     * 
     */
    public Nodes getNodes() {
        return nodes;
    }

    /**
     * Gets the value of the parallel property.
     * 
     * @return possible object is {@link Boolean }
     * 
     */
    public Boolean getParallel() {
        return parallel;
    }

    /**
     * Gets the value of the startCounterValue property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getStartCounterValue() {
        return startCounterValue;
    }

    public boolean isSetArcs() {
        return (this.arcs != null);
    }

    public boolean isSetCompletionCondition() {
        return (this.completionCondition != null);
    }

    public boolean isSetCounterName() {
        return (this.counterName != null);
    }

    public boolean isSetFinalCounterValue() {
        return (this.finalCounterValue != null);
    }

    public boolean isSetNodes() {
        return (this.nodes != null);
    }

    public boolean isSetParallel() {
        return (this.parallel != null);
    }

    public boolean isSetStartCounterValue() {
        return (this.startCounterValue != null);
    }

    /**
     * Sets the value of the arcs property.
     * 
     * @param value
     *            allowed object is {@link Arcs }
     * 
     */
    public void setArcs(Arcs value) {
        this.arcs = value;
    }

    /**
     * Sets the value of the completionCondition property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCompletionCondition(String value) {
        this.completionCondition = value;
    }

    /**
     * Sets the value of the counterName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCounterName(String value) {
        this.counterName = value;
    }

    /**
     * Sets the value of the finalCounterValue property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setFinalCounterValue(String value) {
        this.finalCounterValue = value;
    }

    /**
     * Sets the value of the nodes property.
     * 
     * @param value
     *            allowed object is {@link Nodes }
     * 
     */
    public void setNodes(Nodes value) {
        this.nodes = value;
    }

    /**
     * Sets the value of the parallel property.
     * 
     * @param value
     *            allowed object is {@link Boolean }
     * 
     */
    public void setParallel(Boolean value) {
        this.parallel = value;
    }

    /**
     * Sets the value of the startCounterValue property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setStartCounterValue(String value) {
        this.startCounterValue = value;
    }

}