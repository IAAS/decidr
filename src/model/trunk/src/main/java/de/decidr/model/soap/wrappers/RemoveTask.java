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

package de.decidr.model.soap.wrappers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.decidr.model.soap.types.IDList;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;taskIDList&quot; type=&quot;{http://decidr.de/schema/DecidrWSTypes}tIDList&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "taskIDList" })
@XmlRootElement(name = "removeTask", namespace = "http://decidr.de/webservices/HumanTask")
public class RemoveTask {

    @XmlElement(namespace = "", required = true)
    protected IDList taskIDList;

    /**
     * Gets the value of the taskIDList property.
     * 
     * @return possible object is {@link IDList }
     */
    public IDList getTaskIDList() {
        return taskIDList;
    }

    /**
     * Sets the value of the taskIDList property.
     * 
     * @param value
     *            allowed object is {@link IDList }
     */
    public void setTaskIDList(IDList value) {
        this.taskIDList = value;
    }
}