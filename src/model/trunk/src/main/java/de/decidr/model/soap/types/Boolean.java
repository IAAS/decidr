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
 * Java class for tBoolean.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name=&quot;tBoolean&quot;&gt;
 *   &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;&gt;
 *     &lt;enumeration value=&quot;yes&quot;/&gt;
 *     &lt;enumeration value=&quot;no&quot;/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "tBoolean", namespace = "http://decidr.de/schema/DecidrProcessTypes")
@XmlEnum
public enum Boolean {

    @XmlEnumValue("yes")
    YES("yes"), @XmlEnumValue("no")
    NO("no");
    public static Boolean fromValue(String v) {
        for (Boolean c : Boolean.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    private final String value;

    Boolean(String v) {
        value = v;
    }

    public String value() {
        return value;
    }
}
