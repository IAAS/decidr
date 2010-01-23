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
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element name=&quot;wfInstances&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot;/&gt;
 *         &lt;element name=&quot;wfModels&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot;/&gt;
 *         &lt;element name=&quot;avgLoad&quot; type=&quot;{http://decidr.de/webservices/ODEMonitor}zeroHundredInt&quot;/&gt;
 *         &lt;element name=&quot;odeID&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}long&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "wfInstances", "wfModels", "avgLoad", "odeID" })
@XmlRootElement(name = "updateStats")
public class UpdateStats {

    protected int wfInstances;
    protected int wfModels;
    protected int avgLoad;
    @XmlElement(required = true)
    protected long odeID;

    /**
     * Gets the value of the avgLoad property.
     * 
     */
    public int getAvgLoad() {
        return avgLoad;
    }

    /**
     * Gets the value of the odeID property.
     * 
     * @return possible object is {@link Long }
     */
    public long getOdeID() {
        return odeID;
    }

    /**
     * Gets the value of the wfInstances property.
     * 
     */
    public int getWfInstances() {
        return wfInstances;
    }

    /**
     * Gets the value of the wfModels property.
     * 
     */
    public int getWfModels() {
        return wfModels;
    }

    /**
     * Sets the value of the avgLoad property.
     * 
     */
    public void setAvgLoad(int value) {
        this.avgLoad = value;
    }

    /**
     * Sets the value of the odeID property.
     * 
     * @param value
     *            allowed object is {@link Long }
     */
    public void setOdeID(long value) {
        this.odeID = value;
    }

    /**
     * Sets the value of the wfInstances property.
     * 
     */
    public void setWfInstances(int value) {
        this.wfInstances = value;
    }

    /**
     * Sets the value of the wfModels property.
     * 
     */
    public void setWfModels(int value) {
        this.wfModels = value;
    }
}
