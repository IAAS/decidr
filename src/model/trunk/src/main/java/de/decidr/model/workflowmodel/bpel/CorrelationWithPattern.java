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
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for tCorrelationWithPattern complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="tCorrelationWithPattern">
 *   &lt;complexContent>
 *     &lt;extension base="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tCorrelation">
 *       &lt;attribute name="pattern" type="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tPattern" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tCorrelationWithPattern")
public class CorrelationWithPattern extends Correlation {

    @XmlAttribute
    protected Pattern pattern;

    /**
     * Gets the value of the pattern property.
     * 
     * @return possible object is {@link Pattern }
     * 
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Sets the value of the pattern property.
     * 
     * @param value
     *            allowed object is {@link Pattern }
     * 
     */
    public void setPattern(Pattern value) {
        this.pattern = value;
    }

    public boolean isSetPattern() {
        return (this.pattern != null);
    }

}
