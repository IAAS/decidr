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
 *         &lt;element name=&quot;actorUser&quot; type=&quot;{http://decidr.de/schema/DecidrTypes}tActorUser&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;emailUser&quot; type=&quot;{http://decidr.de/schema/DecidrTypes}tEmailUser&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;roleUser&quot; type=&quot;{http://decidr.de/schema/DecidrTypes}tRoleUser&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tAbstractUserList", namespace = "http://decidr.de/schema/DecidrTypes", propOrder = {
        "actorUser", "emailUser", "roleUser" })
public class AbstractUserList {

    @XmlElement(nillable = true)
    protected List<ActorUser> actorUser;
    @XmlElement(nillable = true)
    protected List<EmailUser> emailUser;
    @XmlElement(nillable = true)
    protected List<RoleUser> roleUser;

    /**
     * Gets the value of the actorUser property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the actorUser property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getActorUser().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ActorUser }
     */
    public List<ActorUser> getActorUser() {
        if (actorUser == null) {
            actorUser = new ArrayList<ActorUser>();
        }
        return this.actorUser;
    }

    /**
     * Gets the value of the emailUser property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the emailUser property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getEmailUser().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EmailUser }
     */
    public List<EmailUser> getEmailUser() {
        if (emailUser == null) {
            emailUser = new ArrayList<EmailUser>();
        }
        return this.emailUser;
    }

    /**
     * Gets the value of the roleUser property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the roleUser property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getRoleUser().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link RoleUser }
     */
    public List<RoleUser> getRoleUser() {
        if (roleUser == null) {
            roleUser = new ArrayList<RoleUser>();
        }
        return this.roleUser;
    }
}