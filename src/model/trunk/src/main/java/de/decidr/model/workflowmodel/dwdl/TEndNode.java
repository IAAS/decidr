//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.06.13 at 08:40:43 PM GMT+01:00 
//


package de.decidr.model.workflowmodel.dwdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tEndNode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tEndNode">
 *   &lt;complexContent>
 *     &lt;extension base="{http://decidr.de/schema/dwdl}tBasicNode">
 *       &lt;sequence>
 *         &lt;element name="notificationOfSuccess" type="{http://decidr.de/schema/dwdl}tNotification" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEndNode", propOrder = {
    "notificationOfSuccess"
})
public class TEndNode
    extends TBasicNode
{

    protected TNotification notificationOfSuccess;

    /**
     * Gets the value of the notificationOfSuccess property.
     * 
     * @return
     *     possible object is
     *     {@link TNotification }
     *     
     */
    public TNotification getNotificationOfSuccess() {
        return notificationOfSuccess;
    }

    /**
     * Sets the value of the notificationOfSuccess property.
     * 
     * @param value
     *     allowed object is
     *     {@link TNotification }
     *     
     */
    public void setNotificationOfSuccess(TNotification value) {
        this.notificationOfSuccess = value;
    }

}