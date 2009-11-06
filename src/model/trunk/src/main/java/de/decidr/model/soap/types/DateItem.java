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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for tDateItem complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;tDateItem&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{http://decidr.de/schema/DecidrTypes}tItem&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;value&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}date&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDateItem", namespace = "http://decidr.de/schema/DecidrTypes", propOrder = { "value" })
public class DateItem extends Item {

    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar value;

    /**
     * Gets the value of the value property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     */
    public void setValue(XMLGregorianCalendar value) {
        this.value = value;
    }
}
