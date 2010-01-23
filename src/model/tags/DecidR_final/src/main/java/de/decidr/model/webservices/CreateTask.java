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

package de.decidr.model.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.decidr.model.workflowmodel.humantask.THumanTaskData;

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
 *         &lt;element name=&quot;wfmID&quot; type=&quot;{http://decidr.de/schema/DecidrTypes}tID&quot;/&gt;
 *         &lt;element name=&quot;processID&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;userID&quot; type=&quot;{http://decidr.de/schema/DecidrTypes}tID&quot;/&gt;
 *         &lt;element name=&quot;taskName&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;userNotification&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}boolean&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;description&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;taskData&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "wfmID", "processID", "userID", "taskName",
        "userNotification", "description", "taskData" })
@XmlRootElement(name = "createTask", namespace = "http://decidr.de/webservices/HumanTask")
public class CreateTask {

    @XmlElement(namespace = "")
    protected long wfmID;
    @XmlElement(namespace = "", required = true)
    protected String processID;
    @XmlElement(namespace = "")
    protected long userID;
    @XmlElement(namespace = "", required = true)
    protected String taskName;
    @XmlElement(namespace = "", defaultValue = "false")
    protected Boolean userNotification;
    @XmlElement(namespace = "", required = true)
    protected String description;
    @XmlElement(namespace = "")
    protected THumanTaskData taskData;

    /**
     * Gets the value of the description property.
     * 
     * @return possible object is {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the value of the processID property.
     * 
     * @return possible object is {@link String }
     */
    public String getProcessID() {
        return processID;
    }

    /**
     * Gets the value of the taskData property.
     * 
     * @return possible object is {@link THumanTaskData }
     */
    public THumanTaskData getTaskData() {
        return taskData;
    }

    /**
     * Gets the value of the taskName property.
     * 
     * @return possible object is {@link String }
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Gets the value of the userID property.
     */
    public long getUserID() {
        return userID;
    }

    /**
     * Gets the value of the wfmID property.
     */
    public long getWfmID() {
        return wfmID;
    }

    /**
     * Gets the value of the userNotification property.
     * 
     * @return possible object is {@link Boolean }
     */
    public Boolean isUserNotification() {
        return userNotification;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *            allowed object is {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Sets the value of the processID property.
     * 
     * @param value
     *            allowed object is {@link String }
     */
    public void setProcessID(String value) {
        this.processID = value;
    }

    /**
     * Sets the value of the taskData property.
     * 
     * @param value
     *            allowed object is {@link THumanTaskData }
     */
    public void setTaskData(THumanTaskData value) {
        this.taskData = value;
    }

    /**
     * Sets the value of the taskName property.
     * 
     * @param value
     *            allowed object is {@link String }
     */
    public void setTaskName(String value) {
        this.taskName = value;
    }

    /**
     * Sets the value of the userID property.
     */
    public void setUserID(long value) {
        this.userID = value;
    }

    /**
     * Sets the value of the userNotification property.
     * 
     * @param value
     *            allowed object is {@link Boolean }
     */
    public void setUserNotification(Boolean value) {
        this.userNotification = value;
    }

    /**
     * Sets the value of the wfmID property.
     */
    public void setWfmID(long value) {
        this.wfmID = value;
    }
}
