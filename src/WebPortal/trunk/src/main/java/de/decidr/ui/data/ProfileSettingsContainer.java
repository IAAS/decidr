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
 * @author GH
 */
public class ProfileSettingsContainer {
	private static ProfileSettingsContainer cont = null;
	
    private String firstNameText = "Danny";
    private String lastNameText = "Street";
    private String streetText = "";
    private String postalCode = "";
    private String cityText = "";
    private String eMail = "";
    private String userName ="";
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
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * TODO: add comment
     *
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
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
       
    /**
     * TODO: add comment
     *
     * @return the firstNameText
     */
    public String getFirstNameText() {
        return firstNameText;
    }
    /**
     * TODO: add comment
     *
     * @param firstNameText the firstNameText to set
     */
    public void setFirstNameText(String firstNameText) {
        this.firstNameText = firstNameText;
    }
    /**
     * TODO: add comment
     *
     * @return the lastNameText
     */
    public String getLastNameText() {
        return lastNameText;
    }
    /**
     * TODO: add comment
     *
     * @param lastNameText the lastNameText to set
     */
    public void setLastNameText(String lastNameText) {
        this.lastNameText = lastNameText;
    }
    /**
     * TODO: add comment
     *
     * @return the streetText
     */
    public String getStreetText() {
        return streetText;
    }
    /**
     * TODO: add comment
     *
     * @param streetText the streetText to set
     */
    public void setStreetText(String streetText) {
        this.streetText = streetText;
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
     * @return the cityText
     */
    public String getCityText() {
        return cityText;
    }
    /**
     * TODO: add comment
     *
     * @param cityText the cityText to set
     */
    public void setCityText(String cityText) {
        this.cityText = cityText;
    }
}
