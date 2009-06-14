//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.06.13 at 08:32:08 PM GMT+01:00 
//


package de.decidr.model.workflowmodel.bpel.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tRoles.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tRoles">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="myRole"/>
 *     &lt;enumeration value="partnerRole"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tRoles")
@XmlEnum
public enum TRoles {

    @XmlEnumValue("myRole")
    MY_ROLE("myRole"),
    @XmlEnumValue("partnerRole")
    PARTNER_ROLE("partnerRole");
    private final String value;

    TRoles(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TRoles fromValue(String v) {
        for (TRoles c: TRoles.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
