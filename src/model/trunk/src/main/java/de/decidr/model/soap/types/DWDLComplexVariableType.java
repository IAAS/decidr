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
@XmlType(name = "DWDLComplexVariableType", namespace = "http://decidr.de/schema/DecidrProcessTypes")
@XmlEnum
public enum DWDLComplexVariableType {

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

    DWDLComplexVariableType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DWDLComplexVariableType fromValue(String v) {
        for (DWDLComplexVariableType c: DWDLComplexVariableType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
