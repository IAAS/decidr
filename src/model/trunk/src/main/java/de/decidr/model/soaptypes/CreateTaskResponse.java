package de.decidr.model.soaptypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element name=&quot;taskID&quot; type=&quot;{http://decidr.de/schema/DecidrTypes}tID&quot;/&gt;
 *         &lt;element name=&quot;processID&quot; type=&quot;{http://decidr.de/schema/DecidrTypes}tID&quot;/&gt;
 *         &lt;element name=&quot;userID&quot; type=&quot;{http://decidr.de/schema/DecidrTypes}tID&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "taskID", "processID", "userID" })
@XmlRootElement(name = "createTaskResponse")
public class CreateTaskResponse {

	protected long taskID;
	protected long processID;
	protected long userID;

	/**
	 * Gets the value of the taskID property.
	 * 
	 */
	public long getTaskID() {
		return taskID;
	}

	/**
	 * Sets the value of the taskID property.
	 * 
	 */
	public void setTaskID(long value) {
		this.taskID = value;
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

}
