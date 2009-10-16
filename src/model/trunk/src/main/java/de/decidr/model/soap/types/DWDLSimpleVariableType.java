/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


package de.decidr.model.soap.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DWDLSimpleVariableType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DWDLSimpleVariableType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="integer"/>
 *     &lt;enumeration value="float"/>
 *     &lt;enumeration value="string"/>
 *     &lt;enumeration value="boolean"/>
 *     &lt;enumeration value="date"/>
 *     &lt;enumeration value="anyURI"/>
 *     &lt;enumeration value="time"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DWDLSimpleVariableType", namespace = "http://decidr.de/schema/DecidrProcessTypes")
@XmlEnum
public enum DWDLSimpleVariableType {

    @XmlEnumValue("integer")
    INTEGER("integer"),
    @XmlEnumValue("float")
    FLOAT("float"),
    @XmlEnumValue("string")
    STRING("string"),
    @XmlEnumValue("boolean")
    BOOLEAN("boolean"),
    @XmlEnumValue("date")
    DATE("date"),
    @XmlEnumValue("anyURI")
    ANY_URI("anyURI"),
    @XmlEnumValue("time")
    TIME("time");
    private final String value;

    DWDLSimpleVariableType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DWDLSimpleVariableType fromValue(String v) {
        for (DWDLSimpleVariableType c: DWDLSimpleVariableType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
