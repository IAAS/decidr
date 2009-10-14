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

package de.decidr.model.acl.asserters;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Transaction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.permissions.CommandPermission;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.user.SetUserPropertyCommand;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.testing.LowLevelDatabaseTest;

/**
 * TODO: add comment
 *
 * @author GH
 */
public class IsRoleEqualToAccessedUserAsserterTest extends LowLevelDatabaseTest {

    private static UserFacade userFacade;
    
    private static Long superAdminId;
    private static Long userId;
    
        
    private static final String USER_EMAIL = "test3@acl.decidr.de";

    @BeforeClass
    public static void setUpBeforeClass() throws TransactionException {
        //create test users
        superAdminId = DecidrGlobals.getSettings().getSuperAdmin().getId();
        userFacade = new UserFacade(new SuperAdminRole(superAdminId));
        
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName("test");
        userProfile.setLastName("user");
        userProfile.setCity("testcity");
        userProfile.setStreet("test st.");
        userProfile.setPostalCode("12test");
        
        userProfile.setUsername("user78626");
        userId = userFacade.registerUser(USER_EMAIL, "qwertz", userProfile);
        
    }

    @AfterClass
    public static void cleanUpAfterClass() throws TransactionException {
        
        Transaction trans = session.beginTransaction();
        session.createQuery("delete from User WHERE email LIKE 'test%@acl.decidr.de'")
                .executeUpdate();
        trans.commit();
    }

    
    /**
     * Test method for {@link IsRoleEqualToAccessedUserAsserter#assertRule(Role, Permission)}.
     * @throws TransactionException 
     */
    @Test
    public void testAssertRule() throws TransactionException {
        Map<String, Date> properties = new HashMap<String, Date>();
        properties.put("moo", new Date());
        IsRoleEqualToAccessedUserAsserter asserter = new IsRoleEqualToAccessedUserAsserter();
        assertTrue(asserter.assertRule(new UserRole(userId), new CommandPermission(new SetUserPropertyCommand(new UserRole(userId), userId,
            properties))));
        assertFalse(asserter.assertRule(new UserRole(), new CommandPermission(new SetUserPropertyCommand(new UserRole(userId), userId,
                properties))));
    }
}
