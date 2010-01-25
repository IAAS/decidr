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
package de.decidr.model.entities;

// Generated 22.01.2010 14:51:15 by Hibernate Tools 3.2.4.GA

import java.util.Date;

/**
 * PasswordResetRequest generated by hbm2java.
 */
public class PasswordResetRequest implements java.io.Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    private long id;

    /** The user. */
    private User user;

    /** The creation date. */
    private Date creationDate;

    /** The auth key. */
    private String authKey;

    /**
     * Instantiates a new password reset request.
     */
    public PasswordResetRequest() {
        // default empty JavaBean constructor
    }

    /**
     * Instantiates a new password reset request.
     * 
     * @param user
     *            the user
     * @param creationDate
     *            the creation date
     * @param authKey
     *            the auth key
     */
    public PasswordResetRequest(User user, Date creationDate, String authKey) {
        // generated full constructor
        this.user = user;
        this.creationDate = creationDate;
        this.authKey = authKey;
    }

    /**
     * Gets the auth key.
     * 
     * @return the auth key
     */
    public String getAuthKey() {
        return this.authKey;
    }

    /**
     * Gets the creation date.
     * 
     * @return the creation date
     */
    public Date getCreationDate() {
        return this.creationDate;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public long getId() {
        return this.id;
    }

    /**
     * Gets the user.
     * 
     * @return the user
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Sets the auth key.
     * 
     * @param authKey
     *            the new auth key
     */
    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    /**
     * Sets the creation date.
     * 
     * @param creationDate
     *            the new creation date
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            the new id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Sets the user.
     * 
     * @param user
     *            the new user
     */
    public void setUser(User user) {
        this.user = user;
    }

}