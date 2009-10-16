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

package de.decidr.model.soap.types;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for tHumanTaskData complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;tHumanTaskData&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;parameters&quot; type=&quot;{http://decidr.de/schema/DecidrProcessTypes}tParameters&quot;/&gt;
 *         &lt;choice maxOccurs=&quot;unbounded&quot;&gt;
 *           &lt;element name=&quot;taskItem&quot; type=&quot;{http://decidr.de/schema/DecidrProcessTypes}tTaskItem&quot;/&gt;
 *           &lt;element name=&quot;information&quot; type=&quot;{http://decidr.de/schema/DecidrProcessTypes}tInformation&quot;/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tHumanTaskData", namespace = "http://decidr.de/schema/DecidrProcessTypes", propOrder = {
        "parameters", "taskItemOrInformation" })
public class HumanTaskData {

    @XmlElement(required = true)
    protected Parameters parameters;
    @XmlElements( {
            @XmlElement(name = "information", type = Information.class),
            @XmlElement(name = "taskItem", type = TaskItem.class) })
    protected List<Object> taskItemOrInformation;

    /**
     * Gets the value of the parameters property.
     * 
     * @return possible object is {@link Parameters }
     */
    public Parameters getParameters() {
        return parameters;
    }

    /**
     * Sets the value of the parameters property.
     * 
     * @param value
     *            allowed object is {@link Parameters }
     */
    public void setParameters(Parameters value) {
        this.parameters = value;
    }

    /**
     * Gets the value of the taskItemOrInformation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the taskItemOrInformation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getTaskItemOrInformation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Information } {@link TaskItem }
     */
    public List<Object> getTaskItemOrInformation() {
        if (taskItemOrInformation == null) {
            taskItemOrInformation = new ArrayList<Object>();
        }
        return this.taskItemOrInformation;
    }
}
