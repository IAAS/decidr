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

package de.decidr.setup.model;

import de.decidr.model.acl.Password;
import de.decidr.setup.helpers.DateUtils;

public class SuperAdmin {

    // user fields
    private String id = "'1'";
    private String authkey = "NULL";
    private String email;
    private String disabledSince = "NULL";
    private String unavailableSince = "NULL";
    private String registeredSince;
    private String creationDate;
    private String currentTenantId = "'1'";

    // user profile fields
    private String userId = "'1'";
    private String username;
    private String passwordHash;
    private String passwordSalt;
    private String firstName;
    private String lastName;
    private String street;
    private String postalCode;
    private String city;

    public SuperAdmin() {
        String now = DateUtils.now();
        registeredSince = now;
        creationDate = now;
    }

    public String getAuthkey() {
        return authkey;
    }

    public String getCity() {
        return city;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getCurrentTenantId() {
        return currentTenantId;
    }

    public String getDisabledSince() {
        return disabledSince;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getRegisteredSince() {
        return registeredSince;
    }

    public String getStreet() {
        return street;
    }

    public String getUnavailableSince() {
        return unavailableSince;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        String salt = Password.getRandomSalt();
        this.passwordSalt = "'" + salt + "'";
        this.passwordHash = "'" + Password.getHash(password, salt) + "'";
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}