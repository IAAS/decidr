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

package de.decidr.ui.data;

import com.vaadin.ui.TextField;

/**
 * @deprecated needs to be removed, but is currently used for testing
 *
 * @author Geoffrey-Alexeij Heinze
 */
public class ProfileSettingsContainer {
	private static ProfileSettingsContainer cont = null;
	
    private String firstName = "Danny";
    private String lastName = "Street";
    private String street = "";
    private String postalCode = "";
    private String city = "";
    private String eMail = "";
    private String username ="";
    private String password = "";
    
    public static ProfileSettingsContainer getInstance(){
    	if(cont == null){
    		cont = new ProfileSettingsContainer();
    	}
    	return cont;
    }

    private ProfileSettingsContainer(){
        //Nothing
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
     * @param firstName the firstName to set
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
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * TODO: add comment
     *
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * TODO: add comment
     *
     * @param street the street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * TODO: add comment
     *
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * TODO: add comment
     *
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * TODO: add comment
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * TODO: add comment
     *
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * TODO: add comment
     *
     * @return the eMail
     */
    public String getEMail() {
        return eMail;
    }

    /**
     * TODO: add comment
     *
     * @param mail the eMail to set
     */
    public void setEMail(String mail) {
        eMail = mail;
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
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * TODO: add comment
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * TODO: add comment
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    
   
}
