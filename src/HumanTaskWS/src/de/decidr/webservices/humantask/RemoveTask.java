
package de.decidr.webservices.humantask;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import de.decidr.schema.decidrtypes.TIDList;


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
 *         &lt;element name="taskIDList" type="{http://decidr.de/schema/DecidrTypes}tIDList"/>
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
    "taskIDList"
})
@XmlRootElement(name = "removeTask")
public class RemoveTask {

    @XmlElement(required = true)
    protected TIDList taskIDList;

    /**
     * Gets the value of the taskIDList property.
     * 
     * @return
     *     possible object is
     *     {@link TIDList }
     *     
     */
    public TIDList getTaskIDList() {
        return taskIDList;
    }

    /**
     * Sets the value of the taskIDList property.
     * 
     * @param value
     *     allowed object is
     *     {@link TIDList }
     *     
     */
    public void setTaskIDList(TIDList value) {
        this.taskIDList = value;
    }

}
