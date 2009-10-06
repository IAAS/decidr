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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for tAbstractUserList complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;tAbstractUserList&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;user&quot; type=&quot;{http://decidr.de/schema/DecidrWSTypes}tAbstractUser&quot; maxOccurs=&quot;unbounded&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tAbstractUserList", propOrder = { "user" })
public class AbstractUserList {

    @XmlElement(required = true)
    protected List<? extends AbstractUser> user;

    /**
     * Gets the value of the user property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the abstractUser property.
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link TRoleUser }{@code >}
     * {@link JAXBElement }{@code <}{@link TAbstractUser }{@code >}
     * {@link JAXBElement }{@code <}{@link TActorUser }{@code >}
     * {@link JAXBElement }{@code <}{@link TEmailUser }{@code >}
     */
    public List<? extends AbstractUser> getUser() {
        if (user == null) {
            user = new ArrayList<AbstractUser>();
        }
        return this.user;
    }

    /**
     * Sets the value of the user property.
     */
    public void setUser(List<? extends AbstractUser> user) {
        this.user = user;
    }
}
