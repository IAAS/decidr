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

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.DefaultAccessControlListTest;
import de.decidr.model.acl.permissions.CommandPermission;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.commands.tenant.AddTenantMemberCommand;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.facades.UserFacadeTest;
import de.decidr.model.testing.LowLevelDatabaseTest;

/**
 * Test case for <code>{@link UserIsTenantAdminAsserter}</code>. NOTE: see
 * <code>{@link DefaultAccessControlListTest}</code>
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class UserIsTenantAdminAsserterTest extends LowLevelDatabaseTest {

    private static UserFacade userFacade;
    // private static TenantFacade tenantFacade;
    // private static WorkflowModelFacade wfmFacade;

    private static Long superAdminId;
    private static Long tenantAdminId;
    private static Long userId;

    private static Long tenantId;
    // private static Long wfmId;

    private static final String USERNAME_PREFIX = "testuser";

    @BeforeClass
    public static void setUpBeforeClass() throws TransactionException {
        UserFacadeTest.deleteTestUsers();

        // create test users
        superAdminId = DecidrGlobals.getSettings().getSuperAdmin().getId();
        userFacade = new UserFacade(new SuperAdminRole(superAdminId));

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName("test");
        userProfile.setLastName("user");
        userProfile.setCity("testcity");
        userProfile.setStreet("test st.");
        userProfile.setPostalCode("12test");

        userProfile.setUsername(USERNAME_PREFIX + "TenantAdmin");
        tenantAdminId = userFacade.registerUser(UserFacadeTest.getTestEmail(1),
                "qwertz", userProfile);

        userProfile.setUsername(USERNAME_PREFIX + "User");
        userId = userFacade.registerUser(UserFacadeTest.getTestEmail(2),
                "qwertz", userProfile);

        // //create test tenant
        // tenantFacade = new TenantFacade(new SuperAdminRole(superAdminId));
        // tenantId = tenantFacade.createTenant("acl.decidr", "mooomoo",
        // tenantAdminId);
    }

    @AfterClass
    public static void cleanUpAfterClass() {
        // tenantFacade.deleteTenant(tenantId);

        UserFacadeTest.deleteTestUsers();
    }

    /**
     * Test method for
     * {@link UserIsTenantAdminAsserter#assertRule(Role, Permission)}.
     */
    @Test
    public void testAssertRule() throws TransactionException {
        UserIsTenantAdminAsserter asserter = new UserIsTenantAdminAsserter();
        assertTrue(asserter.assertRule(new TenantAdminRole(tenantAdminId),
                new CommandPermission(new AddTenantMemberCommand(
                        new TenantAdminRole(tenantAdminId), tenantId, userId))));
        assertFalse(asserter.assertRule(new TenantAdminRole(userId),
                new CommandPermission(new AddTenantMemberCommand(
                        new TenantAdminRole(userId), tenantId, userId))));
        assertFalse(asserter.assertRule(new UserRole(userId),
                new CommandPermission(new AddTenantMemberCommand(new UserRole(
                        userId), tenantId, userId))));
    }
}
