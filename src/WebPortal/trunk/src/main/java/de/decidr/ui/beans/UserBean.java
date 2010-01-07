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

package de.decidr.ui.beans;

import java.io.Serializable;
import java.util.Date;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.User;

/**
 * This bean represents the user from the model in the GUI. The properties are
 * reachable through getters and setters. The bean has the following properties:
 * <ul>
 * <li>id : Long</li>
 * <li>email : String</li>
 * <li>disabledSince : Date</li>
 * <li>unavailableSince : Date</li>
 * <li>registeredSince : Date</li>
 * <li>creationDate : Date</li>
 * <li>firstName : String</li>
 * <li>lastName : String</li>
 * <li>username : String</li>
 * </ul>
 * 
 * @author AT
 */
@Reviewed(currentReviewState = State.PassedWithComments, lastRevision = "2498", reviewers = { "RR" })
public class UserBean implements Serializable {

    /**
     * Serial version uid
     */
    private static final long serialVersionUID = -3513535667821820509L;
    private Long id;
    private String email;
    private Date disabledSince;
    private Date unavailableSince;
    private Date registeredSince;
    private Date creationDate;
    private String firstName;
    private String lastName;
    private String username;

    /**
     * Constructor with a given user entitiy from the model
     * 
     */
    public UserBean(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.disabledSince = user.getDisabledSince();
        this.unavailableSince = user.getUnavailableSince();
        this.registeredSince = user.getRegisteredSince();
        this.creationDate = user.getCreationDate();
        if (user.getUserProfile() != null) {
            this.firstName = user.getUserProfile().getFirstName();
            this.lastName = user.getUserProfile().getLastName();
            this.username = user.getUserProfile().getUsername();
        } else {
            this.firstName = "";
            this.lastName = "";
            this.username = "";
        }
    }

   
    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    
    public String getEmail() {
        return email;
    }

    
    public void setEmail(String email) {
        this.email = email;
    }

    
    public Date getDisabledSince() {
        return disabledSince;
    }

    
    public void setDisabledSince(Date disabledSince) {
        this.disabledSince = disabledSince;
    }

    
    public Date getUnavailableSince() {
        return unavailableSince;
    }

    
    public void setUnavailableSince(Date unavailableSince) {
        this.unavailableSince = unavailableSince;
    }

    
    public Date getRegisteredSince() {
        return registeredSince;
    }

    
    public void setRegisteredSince(Date registeredSince) {
        this.registeredSince = registeredSince;
    }

    
    public Date getCreationDate() {
        return creationDate;
    }

    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    
    public String getFirstName() {
        return firstName;
    }

    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    
    public String getLastName() {
        return lastName;
    }

    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    
    public String getUsername() {
        return username;
    }

    
    public void setUsername(String username) {
        this.username = username;
    }
}
