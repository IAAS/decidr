//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.05 at 06:27:20 PM MEZ 
//


package de.decidr.model.workflowmodel.dwdl;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DWDLComplexVariableType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DWDLComplexVariableType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="list-integer"/>
 *     &lt;enumeration value="list-float"/>
 *     &lt;enumeration value="list-string"/>
 *     &lt;enumeration value="list-boolean"/>
 *     &lt;enumeration value="list-date"/>
 *     &lt;enumeration value="list-file"/>
 *     &lt;enumeration value="form"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DWDLComplexVariableType")
@XmlEnum
public enum ComplexType {

    @XmlEnumValue("list-integer")
    LIST_INTEGER("list-integer"),
    @XmlEnumValue("list-float")
    LIST_FLOAT("list-float"),
    @XmlEnumValue("list-string")
    LIST_STRING("list-string"),
    @XmlEnumValue("list-boolean")
    LIST_BOOLEAN("list-boolean"),
    @XmlEnumValue("list-date")
    LIST_DATE("list-date"),
    @XmlEnumValue("list-file")
    LIST_FILE("list-file"),
    @XmlEnumValue("form")
    FORM("form");
    private final String value;

    ComplexType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ComplexType fromValue(String v) {
        for (ComplexType c: ComplexType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
