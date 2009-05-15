
package de.decidr.webservices.humantask;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import de.decidr.schema.decidrtypes.TItemList;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="wfmID" type="{http://decidr.de/schema/DecidrTypes}tID"/>
 *         &lt;element name="processID" type="{http://decidr.de/schema/DecidrTypes}tID"/>
 *         &lt;element name="userID" type="{http://decidr.de/schema/DecidrTypes}tID"/>
 *         &lt;element name="taskName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="userNotification" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="taskData" type="{http://decidr.de/schema/DecidrTypes}tItemList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "wfmID",
    "processID",
    "userID",
    "taskName",
    "userNotification",
    "description",
    "taskData"
})
@XmlRootElement(name = "createTask")
public class CreateTask {

    protected long wfmID;
    protected long processID;
    protected long userID;
    @XmlElement(required = true)
    protected String taskName;
    @XmlElement(defaultValue = "false")
    protected Boolean userNotification;
    @XmlElement(required = true)
    protected String description;
    protected TItemList taskData;

    /**
     * Gets the value of the wfmID property.
     * 
     */
    public long getWfmID() {
        return wfmID;
    }

    /**
     * Sets the value of the wfmID property.
     * 
     */
    public void setWfmID(long value) {
        this.wfmID = value;
    }

    /**
     * Gets the value of the processID property.
     * 
     */
    public long getProcessID() {
        return processID;
    }

    /**
     * Sets the value of the processID property.
     * 
     */
    public void setProcessID(long value) {
        this.processID = value;
    }

    /**
     * Gets the value of the userID property.
     * 
     */
    public long getUserID() {
        return userID;
    }

    /**
     * Sets the value of the userID property.
     * 
     */
    public void setUserID(long value) {
        this.userID = value;
    }

    /**
     * Gets the value of the taskName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Sets the value of the taskName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaskName(String value) {
        this.taskName = value;
    }

    /**
     * Gets the value of the userNotification property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUserNotification() {
        return userNotification;
    }

    /**
     * Sets the value of the userNotification property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUserNotification(Boolean value) {
        this.userNotification = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the taskData property.
     * 
     * @return
     *     possible object is
     *     {@link TItemList }
     *     
     */
    public TItemList getTaskData() {
        return taskData;
    }

    /**
     * Sets the value of the taskData property.
     * 
     * @param value
     *     allowed object is
     *     {@link TItemList }
     *     
     */
    public void setTaskData(TItemList value) {
        this.taskData = value;
    }

}
