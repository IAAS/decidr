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

    
    public String getStreet() {
        return street;
    }

    
    public void setStreet(String street) {
        this.street = street;
    }

    
    public String getPostalCode() {
        return postalCode;
    }

    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    
    public String getCity() {
        return city;
    }

    
    public void setCity(String city) {
        this.city = city;
    }

    
    public String getEMail() {
        return eMail;
    }

    
    public void setEMail(String mail) {
        eMail = mail;
    }

    
    public String getUsername() {
        return username;
    }

    
    public void setUsername(String username) {
        this.username = username;
    }

    
    public String getPassword() {
        return password;
    }

    
    public void setPassword(String password) {
        this.password = password;
    }
    
    
   
}
