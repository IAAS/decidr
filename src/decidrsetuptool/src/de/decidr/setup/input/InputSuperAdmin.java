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

package de.decidr.setup.input;

import de.decidr.setup.helpers.EmailRequest;
import de.decidr.setup.helpers.NumberRequest;
import de.decidr.setup.helpers.StringRequest;
import de.decidr.setup.model.SuperAdmin;

public class InputSuperAdmin {
        
    public static SuperAdmin getSuperAdmin() {
	System.out.println("------------------------------------------------");
	System.out.println("Set up Super Admin");
	System.out.println("------------------------------------------------");
	
	SuperAdmin sa = new SuperAdmin();

	sa.setEmail(EmailRequest.getResult("Email address:"));
	sa.setUsername(StringRequest.getResult("Username:"));
	sa.setPassword(StringRequest.getResult("Password:"));
	sa.setFirstName(StringRequest.getResult("First name:"));
	sa.setLastName(StringRequest.getResult("Last name:"));
	sa.setStreet(StringRequest.getResult("Street:"));
	sa.setPostalCode(NumberRequest.getResult("Postal code:"));
	sa.setCity(StringRequest.getResult("City:"));
	
	return sa;
    }
}
