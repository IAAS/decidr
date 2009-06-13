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
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for tRoleUser complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;tRoleUser&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{http://decidr.de/schema/DecidrTypes}tAbstractUser&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;user&quot; type=&quot;{http://decidr.de/schema/DecidrTypes}tRole&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tRoleUser", propOrder = { "user" })
public class RoleUser extends AbstractUser {

    @XmlElement(required = true)
    protected Role user;

    /**
     * Gets the value of the user property.
     * 
     * @return possible object is {@link Role }
     */
    public Role getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     * 
     * @param value
     *            allowed object is {@link Role }
     */
    public void setUser(Role value) {
        this.user = value;
    }
}
