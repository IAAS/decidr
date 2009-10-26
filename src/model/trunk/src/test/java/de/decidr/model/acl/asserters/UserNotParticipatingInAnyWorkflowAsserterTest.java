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

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.permissions.CommandPermission;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.commands.user.RemoveFromTenantCommand;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.facades.WorkflowModelFacade;
import de.decidr.model.testing.LowLevelDatabaseTest;

/**
 * TODO: add comment
 * 
 * @author GH
 */
public class UserNotParticipatingInAnyWorkflowAsserterTest extends
        LowLevelDatabaseTest {

    private static UserFacade userFacade;
    private static TenantFacade tenantFacade;
    private static WorkflowModelFacade wfmFacade;

    private static Long superAdminId;
    private static Long tenantAdminId;
    private static Long workflowAdminId;
    private static Long userId;

    private static Long tenantId;
    private static Long wfmId;

    private static final String TENANT_ADMIN_EMAIL = "test1@acl.decidr.de";
    private static final String WORKFLOW_ADMIN_EMAIL = "test20@acl.decidr.de";
    private static final String USER_EMAIL = "test3@acl.decidr.de";

    @BeforeClass
    public static void setUpBeforeClass() throws TransactionException {
        // create test users
        superAdminId = DecidrGlobals.getSettings().getSuperAdmin().getId();
        userFacade = new UserFacade(new SuperAdminRole(superAdminId));

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName("test");
        userProfile.setLastName("user");
        userProfile.setCity("testcity");
        userProfile.setStreet("test st.");
        userProfile.setPostalCode("12test");

        userProfile.setUsername("tenantadmin17565");
        tenantAdminId = userFacade.registerUser(TENANT_ADMIN_EMAIL, "qwertz",
                userProfile);

        userProfile.setUsername("newwfadmin12377");
        workflowAdminId = userFacade.registerUser(WORKFLOW_ADMIN_EMAIL,
                "qwertz", userProfile);

        userProfile.setUsername("user78626");
        userId = userFacade.registerUser(USER_EMAIL, "qwertz", userProfile);

        // create test tenant
        tenantFacade = new TenantFacade(new SuperAdminRole(superAdminId));
        tenantId = tenantFacade.createTenant("acl.decidr", "mooomoo",
                tenantAdminId);

        // create workflow admin
        tenantFacade.addTenantMember(tenantId, workflowAdminId);
        wfmId = tenantFacade.createWorkflowModel(tenantId, "wfm.ACL");

        wfmFacade = new WorkflowModelFacade(new SuperAdminRole(superAdminId));
        List<String> wfmAdmins = new ArrayList<String>();
        List<String> wfmAdminsEmail = new ArrayList<String>();
        wfmAdmins.add("newwfadmin12377");
        wfmFacade.setWorkflowAdministrators(wfmId, wfmAdminsEmail, wfmAdmins);

    }

    @AfterClass
    public static void cleanUpAfterClass() throws TransactionException {

        List<Long> wfm = new ArrayList<Long>();
        wfm.add(wfmId);
        wfmFacade.deleteWorkflowModels(wfm);
        tenantFacade.deleteTenant(tenantId);

        Transaction trans = session.beginTransaction();
        session.createQuery(
                "delete from User WHERE email LIKE 'test%@acl.decidr.de'")
                .executeUpdate();
        trans.commit();
    }

    /**
     * Test method for
     * {@link UserNotParticipatingInAnyWorkflowAsserter#assertRule(Role, Permission)}
     * .
     * 
     * @throws TransactionException
     */
    @Test
    public void testAssertRule() throws TransactionException {
        UserNotParticipatingInAnyWorkflowAsserter asserter = new UserNotParticipatingInAnyWorkflowAsserter();
        assertTrue(asserter.assertRule(new TenantAdminRole(tenantAdminId),
                new CommandPermission(new RemoveFromTenantCommand(
                        new TenantAdminRole(tenantAdminId), userId, tenantId))));
        assertTrue(asserter.assertRule(new UserRole(userId),
                new CommandPermission(new RemoveFromTenantCommand(new UserRole(
                        userId), userId, tenantId))));
        // gh since no user can implicitly participate in a workflow, I have no
        // idea why this assertion fails. Are you sure workflowAdminId is not
        // participating in any workflow?
        // GH: added new wf admin, that is only used in this test case
        assertFalse(asserter.assertRule(new WorkflowAdminRole(workflowAdminId),
                new CommandPermission(new RemoveFromTenantCommand(
                        new WorkflowAdminRole(workflowAdminId), userId,
                        tenantId))));
    }

}
