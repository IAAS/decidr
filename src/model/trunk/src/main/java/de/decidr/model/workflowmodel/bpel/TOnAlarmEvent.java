//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.06.13 at 08:32:08 PM GMT+01:00 
//


package de.decidr.model.workflowmodel.bpel;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tOnAlarmEvent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tOnAlarmEvent">
 *   &lt;complexContent>
 *     &lt;extension base="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tExtensibleElements">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;group ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}forOrUntilGroup"/>
 *             &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}repeatEvery" minOccurs="0"/>
 *           &lt;/sequence>
 *           &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}repeatEvery"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}scope"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tOnAlarmEvent", propOrder = {
    "rest"
})
public class TOnAlarmEvent
    extends TExtensibleElements
{

    @XmlElementRefs({
        @XmlElementRef(name = "for", namespace = "http://docs.oasis-open.org/wsbpel/2.0/process/executable", type = JAXBElement.class),
        @XmlElementRef(name = "until", namespace = "http://docs.oasis-open.org/wsbpel/2.0/process/executable", type = JAXBElement.class),
        @XmlElementRef(name = "repeatEvery", namespace = "http://docs.oasis-open.org/wsbpel/2.0/process/executable", type = JAXBElement.class),
        @XmlElementRef(name = "scope", namespace = "http://docs.oasis-open.org/wsbpel/2.0/process/executable", type = JAXBElement.class)
    })
    protected List<JAXBElement<?>> rest;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "RepeatEvery" is used by two different parts of a schema. See: 
     * line 537 of file:/C:/Programme/Java/jdk1.6.0_14/bin/ws-bpel_executable.xsd
     * line 535 of file:/C:/Programme/Java/jdk1.6.0_14/bin/ws-bpel_executable.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the rest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link TDurationExpr }{@code >}
     * {@link JAXBElement }{@code <}{@link TDurationExpr }{@code >}
     * {@link JAXBElement }{@code <}{@link TDeadlineExpr }{@code >}
     * {@link JAXBElement }{@code <}{@link TScope }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getRest() {
        if (rest == null) {
            rest = new ArrayList<JAXBElement<?>>();
        }
        return this.rest;
    }

}