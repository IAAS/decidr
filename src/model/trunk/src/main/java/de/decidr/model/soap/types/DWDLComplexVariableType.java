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
 * Java class for DWDLComplexVariableType.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name=&quot;DWDLComplexVariableType&quot;&gt;
 *   &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *     &lt;enumeration value=&quot;list-integer&quot;/&gt;
 *     &lt;enumeration value=&quot;list-float&quot;/&gt;
 *     &lt;enumeration value=&quot;list-string&quot;/&gt;
 *     &lt;enumeration value=&quot;list-boolean&quot;/&gt;
 *     &lt;enumeration value=&quot;list-date&quot;/&gt;
 *     &lt;enumeration value=&quot;list-file&quot;/&gt;
 *     &lt;enumeration value=&quot;form&quot;/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "DWDLComplexVariableType", namespace = "http://decidr.de/schema/DecidrProcessTypes")
@XmlEnum
public enum DWDLComplexVariableType {

    @XmlEnumValue("list-integer")
    LIST_INTEGER("list-integer"), @XmlEnumValue("list-float")
    LIST_FLOAT("list-float"), @XmlEnumValue("list-string")
    LIST_STRING("list-string"), @XmlEnumValue("list-boolean")
    LIST_BOOLEAN("list-boolean"), @XmlEnumValue("list-date")
    LIST_DATE("list-date"), @XmlEnumValue("list-file")
    LIST_FILE("list-file"), @XmlEnumValue("form")
    FORM("form");
    public static DWDLComplexVariableType fromValue(String v) {
        for (DWDLComplexVariableType c : DWDLComplexVariableType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    private final String value;

    DWDLComplexVariableType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

}
