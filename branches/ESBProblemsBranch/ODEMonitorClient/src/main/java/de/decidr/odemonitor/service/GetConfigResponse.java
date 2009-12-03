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

package de.decidr.odemonitor.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;updateInterval&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot;/&gt;
 *         &lt;element name=&quot;averagePeriod&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot;/&gt;
 *         &lt;element name=&quot;configChanged&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}dateTime&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "updateInterval", "averagePeriod",
        "configChanged" })
@XmlRootElement(name = "getConfigResponse")
public class GetConfigResponse {

    @XmlElement(required = true)
    protected Integer updateInterval;
    @XmlElement(required = true)
    protected Integer averagePeriod;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar configChanged;

    /**
     * Gets the value of the averagePeriod property.
     * 
     * @return possible object is {@link Integer }
     */
    public Integer getAveragePeriod() {
        return averagePeriod;
    }

    /**
     * Gets the value of the configChanged property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getConfigChanged() {
        return configChanged;
    }

    /**
     * Gets the value of the updateInterval property.
     * 
     * @return possible object is {@link Integer }
     */
    public Integer getUpdateInterval() {
        return updateInterval;
    }

    /**
     * Sets the value of the averagePeriod property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     */
    public void setAveragePeriod(Integer value) {
        this.averagePeriod = value;
    }

    /**
     * Sets the value of the configChanged property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setConfigChanged(XMLGregorianCalendar value) {
        this.configChanged = value;
    }

    /**
     * Sets the value of the updateInterval property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     */
    public void setUpdateInterval(Integer value) {
        this.updateInterval = value;
    }

}
