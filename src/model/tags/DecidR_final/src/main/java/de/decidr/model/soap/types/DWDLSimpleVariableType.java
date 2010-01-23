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
 * <p>
 * Java class for DWDLSimpleVariableType.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name=&quot;DWDLSimpleVariableType&quot;&gt;
 *   &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *     &lt;enumeration value=&quot;integer&quot;/&gt;
 *     &lt;enumeration value=&quot;float&quot;/&gt;
 *     &lt;enumeration value=&quot;string&quot;/&gt;
 *     &lt;enumeration value=&quot;boolean&quot;/&gt;
 *     &lt;enumeration value=&quot;date&quot;/&gt;
 *     &lt;enumeration value=&quot;anyURI&quot;/&gt;
 *     &lt;enumeration value=&quot;time&quot;/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "DWDLSimpleVariableType", namespace = "http://decidr.de/schema/DecidrProcessTypes")
@XmlEnum
public enum DWDLSimpleVariableType {

    @XmlEnumValue("integer")
    INTEGER("integer"), @XmlEnumValue("float")
    FLOAT("float"), @XmlEnumValue("string")
    STRING("string"), @XmlEnumValue("boolean")
    BOOLEAN("boolean"), @XmlEnumValue("date")
    DATE("date"), @XmlEnumValue("anyURI")
    ANY_URI("anyURI"), @XmlEnumValue("time")
    TIME("time");
    public static DWDLSimpleVariableType fromValue(String v) {
        for (DWDLSimpleVariableType c : DWDLSimpleVariableType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    private final String value;

    DWDLSimpleVariableType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

}
