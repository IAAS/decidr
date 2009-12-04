//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.07 at 09:21:14 PM MEZ 
//


package de.decidr.model.workflowmodel.bpel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * 
 * 				XSD Authors: The child element correlations needs to be a Local Element Declaration, 
 * 				because there is another correlations element defined for the invoke activity.
 * 			
 * 
 * <p>Java class for tOnMsgCommon complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tOnMsgCommon">
 *   &lt;complexContent>
 *     &lt;extension base="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tExtensibleElements">
 *       &lt;sequence>
 *         &lt;element name="correlations" type="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tCorrelations" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}fromParts" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="partnerLink" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="portType" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="operation" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="messageExchange" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="variable" type="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}BPELVariableName" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tOnMsgCommon", propOrder = {
    "correlations",
    "fromParts"
})
@XmlSeeAlso({
    OnMessage.class,
    OnEvent.class
})
public class OnMsgCommon
    extends ExtensibleElements
{

    protected Correlations correlations;
    protected FromParts fromParts;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String partnerLink;
    @XmlAttribute
    protected QName portType;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String operation;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String messageExchange;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String variable;

    /**
     * Gets the value of the correlations property.
     * 
     * @return
     *     possible object is
     *     {@link Correlations }
     *     
     */
    public Correlations getCorrelations() {
        return correlations;
    }

    /**
     * Sets the value of the correlations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Correlations }
     *     
     */
    public void setCorrelations(Correlations value) {
        this.correlations = value;
    }

    public boolean isSetCorrelations() {
        return (this.correlations!= null);
    }

    /**
     * Gets the value of the fromParts property.
     * 
     * @return
     *     possible object is
     *     {@link FromParts }
     *     
     */
    public FromParts getFromParts() {
        return fromParts;
    }

    /**
     * Sets the value of the fromParts property.
     * 
     * @param value
     *     allowed object is
     *     {@link FromParts }
     *     
     */
    public void setFromParts(FromParts value) {
        this.fromParts = value;
    }

    public boolean isSetFromParts() {
        return (this.fromParts!= null);
    }

    /**
     * Gets the value of the partnerLink property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartnerLink() {
        return partnerLink;
    }

    /**
     * Sets the value of the partnerLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartnerLink(String value) {
        this.partnerLink = value;
    }

    public boolean isSetPartnerLink() {
        return (this.partnerLink!= null);
    }

    /**
     * Gets the value of the portType property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getPortType() {
        return portType;
    }

    /**
     * Sets the value of the portType property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setPortType(QName value) {
        this.portType = value;
    }

    public boolean isSetPortType() {
        return (this.portType!= null);
    }

    /**
     * Gets the value of the operation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Sets the value of the operation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperation(String value) {
        this.operation = value;
    }

    public boolean isSetOperation() {
        return (this.operation!= null);
    }

    /**
     * Gets the value of the messageExchange property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageExchange() {
        return messageExchange;
    }

    /**
     * Sets the value of the messageExchange property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageExchange(String value) {
        this.messageExchange = value;
    }

    public boolean isSetMessageExchange() {
        return (this.messageExchange!= null);
    }

    /**
     * Gets the value of the variable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVariable() {
        return variable;
    }

    /**
     * Sets the value of the variable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVariable(String value) {
        this.variable = value;
    }

    public boolean isSetVariable() {
        return (this.variable!= null);
    }

}