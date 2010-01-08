//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.07 at 10:09:06 PM MEZ 
//

package de.decidr.model.workflowmodel.bpel.varprop;

import javax.wsdl.extensions.ExtensibilityElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

import de.decidr.model.workflowmodel.dwdl.transformation.Constants;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://docs.oasis-open.org/wsbpel/2.0/varprop}tExtensibleElements">
 *       &lt;sequence>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/varprop}query" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="propertyName" use="required" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="messageType" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="part" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="element" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "query" })
@XmlRootElement(name = "propertyAlias")
public class PropertyAlias extends ExtensibleElements implements
        ExtensibilityElement {

    protected Query query;
    @XmlAttribute(required = true)
    protected QName propertyName;
    @XmlAttribute
    protected QName messageType;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String part;
    @XmlAttribute
    protected QName type;
    @XmlAttribute
    protected QName element;

    /**
     * Gets the value of the query property.
     * 
     * @return possible object is {@link Query }
     * 
     */
    public Query getQuery() {
        return query;
    }

    /**
     * Sets the value of the query property.
     * 
     * @param value
     *            allowed object is {@link Query }
     * 
     */
    public void setQuery(Query value) {
        this.query = value;
    }

    public boolean isSetQuery() {
        return (this.query != null);
    }

    /**
     * Gets the value of the propertyName property.
     * 
     * @return possible object is {@link QName }
     * 
     */
    public QName getPropertyName() {
        return propertyName;
    }

    /**
     * Sets the value of the propertyName property.
     * 
     * @param value
     *            allowed object is {@link QName }
     * 
     */
    public void setPropertyName(QName value) {
        this.propertyName = value;
    }

    public boolean isSetPropertyName() {
        return (this.propertyName != null);
    }

    /**
     * Gets the value of the messageType property.
     * 
     * @return possible object is {@link QName }
     * 
     */
    public QName getMessageType() {
        return messageType;
    }

    /**
     * Sets the value of the messageType property.
     * 
     * @param value
     *            allowed object is {@link QName }
     * 
     */
    public void setMessageType(QName value) {
        this.messageType = value;
    }

    public boolean isSetMessageType() {
        return (this.messageType != null);
    }

    /**
     * Gets the value of the part property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPart() {
        return part;
    }

    /**
     * Sets the value of the part property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPart(String value) {
        this.part = value;
    }

    public boolean isSetPart() {
        return (this.part != null);
    }

    /**
     * Gets the value of the type property.
     * 
     * @return possible object is {@link QName }
     * 
     */
    public QName getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *            allowed object is {@link QName }
     * 
     */
    public void setType(QName value) {
        this.type = value;
    }

    public boolean isSetType() {
        return (this.type != null);
    }

    /**
     * Gets the value of the element property.
     * 
     * @return possible object is {@link QName }
     * 
     */
    public QName getElement() {
        return element;
    }

    /**
     * Sets the value of the element property.
     * 
     * @param value
     *            allowed object is {@link QName }
     * 
     */
    public void setElement(QName value) {
        this.element = value;
    }

    public boolean isSetElement() {
        return (this.element != null);
    }

    @Override
    public QName getElementType() {
        return new QName(Constants.VARPROP_NAMESPACE, "propertyAlias");
    }

    @Override
    public Boolean getRequired() {
        return null;
    }

    @Override
    public void setElementType(QName elementType) {
        // TODO document empty block
    }

    @Override
    public void setRequired(Boolean required) {
        // TODO document empty block
    }

}
