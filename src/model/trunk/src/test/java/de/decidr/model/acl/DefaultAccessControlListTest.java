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

package de.decidr.model.acl;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.asserters.AssertMode;
import de.decidr.model.acl.asserters.Asserter;
import de.decidr.model.acl.asserters.UserIsEnabledAsserter;
import de.decidr.model.acl.asserters.UserIsSuperAdminAsserter;
import de.decidr.model.acl.asserters.UserOwnsWorkItemAsserter;
import de.decidr.model.acl.permissions.CommandPermission;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.HumanTaskRole;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.ServerLoadUpdaterRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.commands.workflowmodel.DeleteWorkflowModelCommand;
import de.decidr.model.commands.workflowmodel.GetWorkflowAdministratorsCommand;
import de.decidr.model.commands.workflowmodel.SaveStartConfigurationCommand;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.facades.UserFacadeTest;
import de.decidr.model.testing.LowLevelDatabaseTest;

/**
 * Test case for <code>{@link DefaulAccessControlList}</code>.
 * NOTE: Large parts are commented out due to the fact that 
 *       there is no web service support in our test environment.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class DefaultAccessControlListTest extends LowLevelDatabaseTest {

    private static DefaultAccessControlList dacl;

    private static UserFacade userFacade;
    // private static TenantFacade tenantFacade;
    // private static WorkflowModelFacade wfmFacade;

    private static Long superAdminId;
    private static Long tenantAdminId;
    private static Long workflowAdminId;
    private static Long userId;

    // private static Long tenantId;
    // private static Long wfmId;

    private static final String USERNAME_PREFIX = "testuser";

    @BeforeClass
    public static void setUpBeforeClass() throws TransactionException {
        dacl = DefaultAccessControlList.getInstance();

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

        userProfile.setUsername(USERNAME_PREFIX + "WFAdmin");
        workflowAdminId = userFacade.registerUser(UserFacadeTest
                .getTestEmail(2), "qwertz", userProfile);

        userProfile.setUsername(USERNAME_PREFIX + "User");
        userId = userFacade.registerUser(UserFacadeTest.getTestEmail(3),
                "qwertz", userProfile);

        // // create test tenant
        // tenantFacade = new TenantFacade(new SuperAdminRole(superAdminId));
        // tenantId = tenantFacade.createTenant("acl.decidr", "mooomoo",
        // tenantAdminId);
        //
        // // create workflow admin
        // tenantFacade.addTenantMember(tenantId, workflowAdminId);
        // wfmId = tenantFacade.createWorkflowModel(tenantId, "wfm.ACL");

        // wfmFacade = new WorkflowModelFacade(new
        // SuperAdminRole(superAdminId));
        // List<String> wfmAdmins = new ArrayList<String>();
        // List<String> wfmAdminsEmail = new ArrayList<String>();
        // wfmAdmins.add("wfadmin12377");
        // wfmFacade.setWorkflowAdministrators(wfmId, wfmAdminsEmail,
        // wfmAdmins);
    }

    @AfterClass
    public static void cleanUpAfterClass() {
        dacl.init();
        dacl = null;

        // if (wfmId != null) {
        // List<Long> wfm = new ArrayList<Long>();
        // wfm.add(wfmId);
        // wfmFacade.deleteWorkflowModels(wfm);
        // }

        // tenantFacade.deleteTenant(tenantId);

        UserFacadeTest.deleteTestUsers();
    }

    /**
     * Test method for {@link DefaultAccessControlList#getInstance()}.
     */
    @Test
    public void testGetInstance() {
        DefaultAccessControlList dacl2 = DefaultAccessControlList.getInstance();

        assertNotNull(dacl);
        assertEquals(dacl, dacl2);
    }

    /**
     * Test method for {@link DefaultAccessControlList#init()}.
     */
    @Test
    public void testInit() throws TransactionException {
        dacl.init();

        // super admin
        assertTrue(dacl.isAllowed(new SuperAdminRole(superAdminId),
                new Permission("*")));

        // // serverload updater
        // assertTrue(dacl.isAllowed(ServerLoadUpdaterRole.getInstance(),
        // new CommandPermission(UpdateServerLoadCommand.class)));
        //
        // assertTrue(dacl.isAllowed(ServerLoadUpdaterRole.getInstance(),
        // new CommandPermission(GetServersCommand.class)));
        //
        // // tenant facade
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // AddTenantMemberCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // CreateTenantCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // CreateWorkflowModelCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new BasicRole(null), new CommandPermission(
        // GetCurrentColorSchemeCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new BasicRole(null), new CommandPermission(
        // GetTenantLogoCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new WorkflowAdminRole(workflowAdminId),
        // new CommandPermission(GetUsersOfTenantCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new WorkflowAdminRole(workflowAdminId),
        // new CommandPermission(GetWorkflowInstancesCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // GetWorkflowModelsCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // ImportPublishedWorkflowModelsCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new WorkflowAdminRole(workflowAdminId),
        // new CommandPermission(InviteUsersAsTenantMembersCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // SetColorSchemeCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // SetCurrentColorSchemeCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // SetTenantDescriptionCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // SetTenantLogoCommand.class)));
        //
        // // user facade
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // RegisterUserCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new BasicRole(null), new CommandPermission(
        // GetUserByLoginCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // CheckAuthKeyCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // SetPasswordCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // RequestPasswordResetCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // LeaveTenantCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // SetUserPropertyCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // RemoveFromTenantCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // ConfirmPasswordResetCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // ConfirmRegistrationCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // ConfirmChangeEmailRequestCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // ConfirmInvitationCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // RefuseInvitationCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // GetUserWithProfileCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new BasicRole(null), new CommandPermission(
        // GetHighestUserRoleCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new BasicRole(null), new CommandPermission(
        // GetUserRoleForTenantCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new WorkflowAdminRole(workflowAminId),
        // new CommandPermission(
        // GetAdministratedWorkflowInstancesCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // GetJoinedTenantsCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // GetAdministratedWorkflowModelCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // GetWorkitemsCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // GetInvitationCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new BasicRole(null), new CommandPermission(
        // IsUserRegisteredCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new BasicRole(null), new CommandPermission(
        // SetUserProfileCommand.class)));
        //
        // // workflow instance fassade
        //
        // assertTrue(dacl.isAllowed(new WorkflowAdminRole(workflowAminId),
        // new CommandPermission(StopWorkflowInstanceCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new WorkflowAdminRole(workflowAminId),
        // new CommandPermission(GetParticipatingUsersCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new WorkflowAdminRole(workflowAminId),
        // new CommandPermission(StartWorkflowInstanceCommand.class)));
        //
        // assertTrue(dacl.isAllowed(HumanTaskRole.getInstance(),
        // new CommandPermission(RemoveAllWorkItemsCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new WorkflowAdminRole(workflowAminId),
        // new CommandPermission(GetAllWorkitemsCommand.class)));
        //
        // // Workflow model facade
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // SaveWorkflowModelCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // GetWorkflowModelCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // PublishWorkflowModelsCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // MakeWorkflowModelExecutableCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // GetWorkflowAdministratorsCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // SetWorkflowAdministratorsCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // DeleteWorkflowModelCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // StartWorkflowInstanceCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // StopWorkflowInstanceCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // SaveStartConfigurationCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // GetLastStartConfigurationCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new TenantAdminRole(tenantAdminId), new
        // CommandPermission(
        // GetPublishedWorkflowModelsCommand.class)));
        //
        // // workitem facade
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // GetWorkItemCommand.class)));
        //
        // assertTrue(dacl.isAllowed(HumanTaskRole.getInstance(),
        // new CommandPermission(CreateWorkItemCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // SetDataCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new UserRole(userId), new
        // CommandPermission(
        // SetStatusCommand.class)));
        //
        // assertTrue(dacl.isAllowed(new WorkflowAdminRole(workflowAdminId),
        // new CommandPermission(DeleteWorkItemCommand.class)));
        //
        // // file permissions
        //
        // assertTrue(dacl.isAllowed(new BasicRole(null),
        // new FileDeletePermission(null)));
        //
        // assertTrue(dacl.isAllowed(new BasicRole(null), new
        // FileReadPermission(
        // null)));
        //
        // assertTrue(dacl.isAllowed(new BasicRole(null),
        // new FileReplacePermission(null)));

        // false tests
        assertFalse(dacl.isAllowed(new BasicRole(null), new Permission("*")));
        assertFalse(dacl.isAllowed(new UserRole(userId), new Permission("*")));
        assertFalse(dacl.isAllowed(new WorkflowAdminRole(workflowAdminId),
                new Permission("*")));
        assertFalse(dacl.isAllowed(new TenantAdminRole(tenantAdminId),
                new Permission("*")));
        assertFalse(dacl.isAllowed(HumanTaskRole.getInstance(), new Permission(
                "*")));
        assertFalse(dacl.isAllowed(ServerLoadUpdaterRole.getInstance(),
                new Permission("*")));
    }

    /**
     * Test method for {@link DefaultAccessControlList#clearRules()}.
     */
    @Test
    public void testClearRules() throws TransactionException {
        dacl.init();

        assertTrue(dacl.isAllowed(new SuperAdminRole(superAdminId),
                new Permission("*")));

        dacl.clearRules();

        assertFalse(dacl.isAllowed(new SuperAdminRole(superAdminId),
                new Permission("*")));
    }

    /**
     * Test method for
     * {@link DefaultAccessControlList#setRule(Role, Permission, AssertMode, Asserter[])}
     * .
     */
    @Test
    public void testSetRuleRolePermissionAssertModeAsserterArray() {
        Asserter[] asserters_single = { new UserOwnsWorkItemAsserter() };
        Asserter[] asserters_multiple = { new UserIsSuperAdminAsserter(),
                new UserIsEnabledAsserter() };

        dacl.clearRules();

        assertTrue(dacl.setRule(new SuperAdminRole(superAdminId),
                new Permission("*"), AssertMode.SatisfyAny, asserters_multiple));
        assertTrue(dacl.setRule(new SuperAdminRole(superAdminId),
                new Permission("*"), AssertMode.SatisfyAny, asserters_single));

        dacl.clearRules();
        assertTrue(dacl.setRule(new SuperAdminRole(superAdminId),
                new Permission("*"), AssertMode.SatisfyAny, asserters_single));
        assertTrue(dacl.setRule(new SuperAdminRole(superAdminId),
                new Permission("*"), AssertMode.SatisfyAny, asserters_multiple));

        dacl.clearRules();
        assertFalse(dacl.hasRule(new SuperAdminRole(superAdminId),
                new Permission("*")));

        assertTrue(dacl.setRule(new SuperAdminRole(superAdminId),
                new Permission("*"), AssertMode.SatisfyAny,
                new UserIsSuperAdminAsserter(), new UserIsEnabledAsserter()));

        assertTrue(dacl.hasRule(new SuperAdminRole(superAdminId),
                new Permission("*")));
    }

    /**
     * Test method for
     * {@link DefaultAccessControlList#setRule(Role, Permission, AssertMode, Asserter)}
     * .
     */
    @Test
    public void testSetRuleRolePermissionAssertModeAsserter() {
        dacl.clearRules();
        assertFalse(dacl.hasRule(new SuperAdminRole(superAdminId),
                new Permission("*")));

        assertTrue(dacl.setRule(new SuperAdminRole(superAdminId),
                new Permission("*"), AssertMode.SatisfyAll,
                new UserIsSuperAdminAsserter()));
        assertTrue(dacl.setRule(new SuperAdminRole(superAdminId),
                new Permission("*"), AssertMode.SatisfyAll,
                new UserIsEnabledAsserter()));
        assertTrue(dacl.setRule(new SuperAdminRole(superAdminId),
                new Permission("*"), AssertMode.SatisfyAll,
                new UserIsSuperAdminAsserter()));

        assertTrue(dacl.hasRule(new SuperAdminRole(superAdminId),
                new Permission("*")));
    }

    /**
     * Test method for {@link DefaultAccessControlList#allow(Role, Permission)}
     * .
     */
    @Test
    public void testAllow() throws TransactionException {
        dacl.clearRules();

        assertFalse(dacl.isAllowed(new SuperAdminRole(superAdminId),
                new CommandPermission(DeleteWorkflowModelCommand.class)));

        dacl.allow(new SuperAdminRole(superAdminId), new CommandPermission(
                DeleteWorkflowModelCommand.class));

        assertTrue(dacl.isAllowed(new SuperAdminRole(superAdminId),
                new CommandPermission(DeleteWorkflowModelCommand.class)));
    }

    /**
     * Test method for
     * {@link DefaultAccessControlList#isAllowed(Role, Permission)} .
     */
    @Test
    public void testIsAllowed() throws TransactionException {
        dacl.init();

        assertTrue(dacl.isAllowed(new SuperAdminRole(superAdminId),
                new Permission("*")));
        assertFalse(dacl.isAllowed(new TenantAdminRole(tenantAdminId),
                new Permission("*")));

        dacl.clearRules();

        assertFalse(dacl.isAllowed(new SuperAdminRole(superAdminId),
                new Permission("*")));
    }

    /**
     * Test method for
     * {@link DefaultAccessControlList#findRule(DefaultAccessControlList.RuleKey)}
     * .
     */
    @Test
    public void testFindRule() {
        dacl.init();

        assertNull(dacl.findRule(dacl.new RuleKey(new TenantAdminRole(),
                new Permission("*"))));
        assertNotNull(dacl.findRule(dacl.new RuleKey(new TenantAdminRole(),
                new CommandPermission(SaveStartConfigurationCommand.class))));

        assertNotNull(dacl.findRule(dacl.new RuleKey(new SuperAdminRole(),
                new Permission("*"))));

        dacl.clearRules();

        assertNull(dacl.findRule(dacl.new RuleKey(new SuperAdminRole(),
                new Permission("*"))));
    }

    /**
     * Test method for
     * {@link DefaultAccessControlList#hasRule(Role, Permission)} .
     */
    @Test
    public void testHasRule() {
        dacl.init();

        assertTrue(dacl.hasRule(new SuperAdminRole(), new Permission("*")));

        assertTrue(dacl.hasRule(new TenantAdminRole(), new CommandPermission(
                GetWorkflowAdministratorsCommand.class)));

        assertTrue(dacl.hasRule(new SuperAdminRole(), new CommandPermission(
                GetWorkflowAdministratorsCommand.class)));

        assertFalse(dacl.hasRule(new UserRole(), new CommandPermission(
                GetWorkflowAdministratorsCommand.class)));

        assertFalse(dacl.hasRule(new WorkflowAdminRole(), new Permission("*")));

        dacl.clearRules();

        assertFalse(dacl.hasRule(new TenantAdminRole(), new CommandPermission(
                GetWorkflowAdministratorsCommand.class)));
    }
}
