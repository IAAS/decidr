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
 * TODO: add comment
 * 
 * @author AT
 */
@Reviewed(currentReviewState = State.PassedWithComments, lastRevision = "2498", reviewers = { "RR" })
public class UserBean implements Serializable {

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
     * TODO: add comment
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

    /**
     * TODO: add comment
     * 
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * TODO: add comment
     * 
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * TODO: add comment
     * 
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * TODO: add comment
     * 
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * TODO: add comment
     * 
     * @return the disabledSince
     */
    public Date getDisabledSince() {
        return disabledSince;
    }

    /**
     * TODO: add comment
     * 
     * @param disabledSince
     *            the disabledSince to set
     */
    public void setDisabledSince(Date disabledSince) {
        this.disabledSince = disabledSince;
    }

    /**
     * TODO: add comment
     * 
     * @return the unavailableSince
     */
    public Date getUnavailableSince() {
        return unavailableSince;
    }

    /**
     * TODO: add comment
     * 
     * @param unavailableSince
     *            the unavailableSince to set
     */
    public void setUnavailableSince(Date unavailableSince) {
        this.unavailableSince = unavailableSince;
    }

    /**
     * TODO: add comment
     * 
     * @return the registeredSince
     */
    public Date getRegisteredSince() {
        return registeredSince;
    }

    /**
     * TODO: add comment
     * 
     * @param registeredSince
     *            the registeredSince to set
     */
    public void setRegisteredSince(Date registeredSince) {
        this.registeredSince = registeredSince;
    }

    /**
     * TODO: add comment
     * 
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * TODO: add comment
     * 
     * @param creationDate
     *            the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * TODO: add comment
     * 
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * TODO: add comment
     * 
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * TODO: add comment
     * 
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * TODO: add comment
     * 
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * TODO: add comment
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * TODO: add comment
     * 
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
