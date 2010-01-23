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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacadeTest;
import de.decidr.model.testing.LowLevelDatabaseTest;

/**
 * Test case for <code>{@link UserIsAvailableAsserter}</code>.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class UserIsAvailableAsserterTest extends LowLevelDatabaseTest {

    // private static UserFacade userFacade;

    // private static Long superAdminId;

    // private static Long userId;

    @AfterClass
    public static void cleanUpAfterClass() {
        UserFacadeTest.deleteTestUsers();
    }

    @BeforeClass
    public static void setUpBeforeClass() {
        // UserFacadeTest.deleteTestUsers();

        // // create test users
        // superAdminId = DecidrGlobals.getSettings().getSuperAdmin().getId();
        // userFacade = new UserFacade(new SuperAdminRole(superAdminId));

        // UserProfile userProfile = new UserProfile();
        // userProfile.setFirstName("test");
        // userProfile.setLastName("user");
        // userProfile.setCity("testcity");
        // userProfile.setStreet("test st.");
        // userProfile.setPostalCode("12test");
        //
        // userProfile.setUsername(UserFacadeTest.USERNAME_PREFIX + "User");
        // userId = userFacade.registerUser(UserFacadeTest.getTestEmail(1),
        // "qwertz", userProfile);
    }

    /**
     * Test method for
     * {@link UserIsAvailableAsserter#assertRule(Role, Permission)}.
     */
    @Test
    public void testAssertRule() throws TransactionException {
        UserIsAvailableAsserter asserter = new UserIsAvailableAsserter();
        // assertTrue(asserter.assertRule(new UserRole(userId),
        // new Permission("*")));
        //
        // userFacade.setUnavailableSince(userId, new Date());
        //
        // assertFalse(asserter.assertRule(new UserRole(userId), new Permission(
        // "*")));
        assertFalse(asserter.assertRule(new UserRole(), new Permission("*")));
    }
}
