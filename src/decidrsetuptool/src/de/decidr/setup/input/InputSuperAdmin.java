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
import de.decidr.setup.helpers.StringRequest;
import de.decidr.setup.model.SuperAdmin;

/**
 * Retrieves information about te super admin from the user and genterates an
 * SQL script.
 * 
 * @author Johannes Engelhardt
 * 
 */
public class InputSuperAdmin {

    private static String createSql(SuperAdmin sa) {
        StringBuilder sql = new StringBuilder();
        sql.append("REPLACE INTO `user` (`id`,`authKey`,`email`,"
                + "`disabledSince`,`unavailableSince`,`registeredSince`,"
                + "`creationDate`,`currentTenantId`)\n");

        sql.append("VALUES (" + sa.getId() + "," + sa.getAuthkey() + ","
                + sa.getEmail() + "," + sa.getDisabledSince() + ","
                + sa.getUnavailableSince() + "," + sa.getRegisteredSince()
                + "," + sa.getCreationDate() + "," + sa.getCurrentTenantId()
                + ");\n\n");

        sql.append("REPLACE INTO `user_profile` (`id`,`username`,"
                + "`passwordHash`,`passwordSalt`,`firstName`,`lastName`,"
                + "`street`,`postalCode`,`city`)\n");

        sql.append("VALUES (" + sa.getUserId() + "," + sa.getUsername() + ","
                + sa.getPasswordHash() + "," + sa.getPasswordSalt() + ","
                + sa.getFirstName() + "," + sa.getLastName() + ","
                + sa.getStreet() + "," + sa.getPostalCode() + ","
                + sa.getCity() + ");\n\n");

        return sql.toString();
    }

    public static String getSql() {
        SuperAdmin sa = getSuperAdmin();
        return createSql(sa);
    }

    public static SuperAdmin getSuperAdmin() {
        System.out.println("------------------------------------------------");
        System.out.println("Set up Super Admin");
        System.out.println("------------------------------------------------");

        SuperAdmin sa = new SuperAdmin();

        sa.setEmail(EmailRequest.getResult("Email address"));
        // Username validation missing, max length validation missing for other
        // properties ~dh
        sa.setUsername(StringRequest.getResult("Username"));
        sa.setPassword(StringRequest.getString("Password"));
        sa.setFirstName(StringRequest.getResult("First name"));
        sa.setLastName(StringRequest.getResult("Last name"));
        sa.setStreet(StringRequest.getResult("Street"));
        sa.setPostalCode(StringRequest.getResult("Postal code"));
        sa.setCity(StringRequest.getResult("City"));

        return sa;
    }
}