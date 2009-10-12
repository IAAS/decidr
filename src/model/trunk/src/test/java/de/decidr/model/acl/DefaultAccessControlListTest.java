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

import de.decidr.model.acl.asserters.AssertMode;
import de.decidr.model.acl.asserters.Asserter;
import de.decidr.model.acl.asserters.UserIsEnabledAsserter;
import de.decidr.model.acl.asserters.UserIsLoggedInAsserter;
import de.decidr.model.acl.asserters.UserOwnsWorkItemAsserter;
import de.decidr.model.acl.permissions.CommandPermission;
import de.decidr.model.acl.permissions.FileDeletePermission;
import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.acl.permissions.FileReplacePermission;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.HumanTaskRole;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.ServerLoadUpdaterRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.commands.system.GetServersCommand;
import de.decidr.model.commands.system.UpdateServerLoadCommand;
import de.decidr.model.commands.tenant.AddTenantMemberCommand;
import de.decidr.model.commands.tenant.CreateTenantCommand;
import de.decidr.model.commands.tenant.CreateWorkflowModelCommand;
import de.decidr.model.commands.tenant.GetCurrentColorSchemeCommand;
import de.decidr.model.commands.tenant.GetTenantLogoCommand;
import de.decidr.model.commands.tenant.GetUsersOfTenantCommand;
import de.decidr.model.commands.tenant.GetWorkflowInstancesCommand;
import de.decidr.model.commands.tenant.GetWorkflowModelsCommand;
import de.decidr.model.commands.tenant.ImportPublishedWorkflowModelsCommand;
import de.decidr.model.commands.tenant.InviteUsersAsTenantMembersCommand;
import de.decidr.model.commands.tenant.SetAdvancedColorSchemeCommand;
import de.decidr.model.commands.tenant.SetCurrentColorSchemeCommand;
import de.decidr.model.commands.tenant.SetSimpleColorSchemeCommand;
import de.decidr.model.commands.tenant.SetTenantDescriptionCommand;
import de.decidr.model.commands.tenant.SetTenantLogoCommand;
import de.decidr.model.commands.user.CheckAuthKeyCommand;
import de.decidr.model.commands.user.ConfirmChangeEmailRequestCommand;
import de.decidr.model.commands.user.ConfirmInvitationCommand;
import de.decidr.model.commands.user.ConfirmPasswordResetCommand;
import de.decidr.model.commands.user.ConfirmRegistrationCommand;
import de.decidr.model.commands.user.GetAdministratedWorkflowInstancesCommand;
import de.decidr.model.commands.user.GetAdministratedWorkflowModelCommand;
import de.decidr.model.commands.user.GetHighestUserRoleCommand;
import de.decidr.model.commands.user.GetInvitationCommand;
import de.decidr.model.commands.user.GetJoinedTenantsCommand;
import de.decidr.model.commands.user.GetUserByLoginCommand;
import de.decidr.model.commands.user.GetUserRoleForTenantCommand;
import de.decidr.model.commands.user.GetUserWithProfileCommand;
import de.decidr.model.commands.user.GetWorkitemsCommand;
import de.decidr.model.commands.user.IsUserRegisteredCommand;
import de.decidr.model.commands.user.LeaveTenantCommand;
import de.decidr.model.commands.user.RefuseInvitationCommand;
import de.decidr.model.commands.user.RegisterUserCommand;
import de.decidr.model.commands.user.RemoveFromTenantCommand;
import de.decidr.model.commands.user.RequestPasswordResetCommand;
import de.decidr.model.commands.user.SetPasswordCommand;
import de.decidr.model.commands.user.SetUserProfileCommand;
import de.decidr.model.commands.user.SetUserPropertyCommand;
import de.decidr.model.commands.workflowinstance.GetAllWorkitemsCommand;
import de.decidr.model.commands.workflowinstance.GetParticipatingUsersCommand;
import de.decidr.model.commands.workflowinstance.RemoveAllWorkItemsCommand;
import de.decidr.model.commands.workflowinstance.StopWorkflowInstanceCommand;
import de.decidr.model.commands.workflowmodel.DeleteWorkflowModelCommand;
import de.decidr.model.commands.workflowmodel.GetLastStartConfigurationCommand;
import de.decidr.model.commands.workflowmodel.GetPublishedWorkflowModelsCommand;
import de.decidr.model.commands.workflowmodel.GetWorkflowAdministratorsCommand;
import de.decidr.model.commands.workflowmodel.GetWorkflowModelCommand;
import de.decidr.model.commands.workflowmodel.MakeWorkflowModelExecutableCommand;
import de.decidr.model.commands.workflowmodel.PublishWorkflowModelsCommand;
import de.decidr.model.commands.workflowmodel.SaveStartConfigurationCommand;
import de.decidr.model.commands.workflowmodel.SaveWorkflowModelCommand;
import de.decidr.model.commands.workflowmodel.SetWorkflowAdministratorsCommand;
import de.decidr.model.commands.workflowmodel.StartWorkflowInstanceCommand;
import de.decidr.model.commands.workitem.CreateWorkItemCommand;
import de.decidr.model.commands.workitem.DeleteWorkItemCommand;
import de.decidr.model.commands.workitem.GetWorkItemCommand;
import de.decidr.model.commands.workitem.SetDataCommand;
import de.decidr.model.commands.workitem.SetStatusCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.testing.DecidrDatabaseTest;

/**
 * GH: add comment
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class DefaultAccessControlListTest extends DecidrDatabaseTest {

    private static DefaultAccessControlList dacl;

    @BeforeClass
    public static void setUpBeforeClass() {
        dacl = DefaultAccessControlList.getInstance();
    }

    @AfterClass
    public static void cleanUpAfterClass() {
        dacl = null;
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
     * 
     * @throws TransactionException
     */
    @Test
    public void testInit() throws TransactionException {

        dacl.init();

        // super admin
        assertTrue(dacl.isAllowed(new SuperAdminRole(), new Permission("*")));

        // serverload updater
        assertTrue(dacl.isAllowed(ServerLoadUpdaterRole.getInstance(),
                new CommandPermission(UpdateServerLoadCommand.class)));

        assertTrue(dacl.isAllowed(ServerLoadUpdaterRole.getInstance(),
                new CommandPermission(GetServersCommand.class)));

        // tenant facade
        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                AddTenantMemberCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                CreateTenantCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                CreateWorkflowModelCommand.class)));

        assertTrue(dacl.isAllowed(new BasicRole(null), new CommandPermission(
                GetCurrentColorSchemeCommand.class)));

        assertTrue(dacl.isAllowed(new BasicRole(null), new CommandPermission(
                GetTenantLogoCommand.class)));

        assertTrue(dacl.isAllowed(new WorkflowAdminRole(),
                new CommandPermission(GetUsersOfTenantCommand.class)));

        assertTrue(dacl.isAllowed(new WorkflowAdminRole(),
                new CommandPermission(GetWorkflowInstancesCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                GetWorkflowModelsCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                ImportPublishedWorkflowModelsCommand.class)));

        assertTrue(dacl.isAllowed(new WorkflowAdminRole(),
                new CommandPermission(InviteUsersAsTenantMembersCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                SetAdvancedColorSchemeCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                SetCurrentColorSchemeCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                SetTenantDescriptionCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                SetTenantLogoCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                SetSimpleColorSchemeCommand.class)));

        // user facade

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                RegisterUserCommand.class)));

        assertTrue(dacl.isAllowed(new BasicRole(null), new CommandPermission(
                GetUserByLoginCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                CheckAuthKeyCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                SetPasswordCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                RequestPasswordResetCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                LeaveTenantCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                SetUserPropertyCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                RemoveFromTenantCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                ConfirmPasswordResetCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                ConfirmRegistrationCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                ConfirmChangeEmailRequestCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                ConfirmInvitationCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                RefuseInvitationCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                GetUserWithProfileCommand.class)));

        assertTrue(dacl.isAllowed(new BasicRole(null), new CommandPermission(
                GetHighestUserRoleCommand.class)));

        assertTrue(dacl.isAllowed(new BasicRole(null), new CommandPermission(
                GetUserRoleForTenantCommand.class)));

        assertTrue(dacl.isAllowed(new WorkflowAdminRole(),
                new CommandPermission(
                        GetAdministratedWorkflowInstancesCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                GetJoinedTenantsCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                GetAdministratedWorkflowModelCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                GetWorkitemsCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                GetInvitationCommand.class)));

        assertTrue(dacl.isAllowed(new BasicRole(null), new CommandPermission(
                IsUserRegisteredCommand.class)));

        assertTrue(dacl.isAllowed(new BasicRole(null), new CommandPermission(
                SetUserProfileCommand.class)));

        // workflow instance fassade

        assertTrue(dacl.isAllowed(new WorkflowAdminRole(),
                new CommandPermission(StopWorkflowInstanceCommand.class)));

        assertTrue(dacl.isAllowed(new WorkflowAdminRole(),
                new CommandPermission(GetParticipatingUsersCommand.class)));

        assertTrue(dacl.isAllowed(new WorkflowAdminRole(),
                new CommandPermission(StartWorkflowInstanceCommand.class)));

        assertTrue(dacl.isAllowed(HumanTaskRole.getInstance(),
                new CommandPermission(RemoveAllWorkItemsCommand.class)));

        assertTrue(dacl.isAllowed(new WorkflowAdminRole(),
                new CommandPermission(GetAllWorkitemsCommand.class)));

        // Workflow model facade

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                SaveWorkflowModelCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                GetWorkflowModelCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                PublishWorkflowModelsCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                MakeWorkflowModelExecutableCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                GetWorkflowAdministratorsCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                SetWorkflowAdministratorsCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                DeleteWorkflowModelCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                StartWorkflowInstanceCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                StopWorkflowInstanceCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                SaveStartConfigurationCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                GetLastStartConfigurationCommand.class)));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                GetPublishedWorkflowModelsCommand.class)));

        // workitem facade

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                GetWorkItemCommand.class)));

        assertTrue(dacl.isAllowed(HumanTaskRole.getInstance(),
                new CommandPermission(CreateWorkItemCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                SetDataCommand.class)));

        assertTrue(dacl.isAllowed(new UserRole(), new CommandPermission(
                SetStatusCommand.class)));

        assertTrue(dacl.isAllowed(new WorkflowAdminRole(),
                new CommandPermission(DeleteWorkItemCommand.class)));

        // file permissions

        assertTrue(dacl.isAllowed(new BasicRole(null),
                new FileDeletePermission(null)));

        assertTrue(dacl.isAllowed(new BasicRole(null), new FileReadPermission(
                null)));

        assertTrue(dacl.isAllowed(new BasicRole(null),
                new FileReplacePermission(null)));

        // false tests
        assertFalse(dacl.isAllowed(new BasicRole(null), new Permission("*")));
        assertFalse(dacl.isAllowed(new UserRole(), new Permission("*")));
        assertFalse(dacl
                .isAllowed(new WorkflowAdminRole(), new Permission("*")));
        assertFalse(dacl.isAllowed(new TenantAdminRole(), new Permission("*")));
        assertFalse(dacl.isAllowed(HumanTaskRole.getInstance(), new Permission(
                "*")));
        assertFalse(dacl.isAllowed(ServerLoadUpdaterRole.getInstance(),
                new Permission("*")));

    }

    /**
     * Test method for {@link DefaultAccessControlList#clearRules()}.
     * 
     * @throws TransactionException
     */
    @Test
    public void testClearRules() throws TransactionException {

        dacl.init();

        assertTrue(dacl.isAllowed(new SuperAdminRole(), new Permission("*")));

        dacl.clearRules();

        assertFalse(dacl.isAllowed(new SuperAdminRole(), new Permission("*")));

    }

    /**
     * Test method for
     * {@link DefaultAccessControlList#setRule(Role, Permission, AssertMode, Asserter[])}
     * .
     */
    @Test
    public void testSetRuleRolePermissionAssertModeAsserterArray() {

        Asserter[] asserters_single = { new UserOwnsWorkItemAsserter() };
        Asserter[] asserters_multiple = { new UserOwnsWorkItemAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter() };

        dacl.clearRules();

        assertTrue(dacl.setRule(new SuperAdminRole(), new Permission("*"),
                AssertMode.SatisfyAll, asserters_multiple));
        assertTrue(dacl.setRule(new SuperAdminRole(), new Permission("*"),
                AssertMode.SatisfyAll, asserters_single));

        dacl.clearRules();
        assertTrue(dacl.setRule(new SuperAdminRole(), new Permission("*"),
                AssertMode.SatisfyAll, asserters_single));
        assertTrue(dacl.setRule(new SuperAdminRole(), new Permission("*"),
                AssertMode.SatisfyAll, asserters_multiple));

        dacl.clearRules();
        assertFalse(dacl.hasRule(new SuperAdminRole(), new Permission("*")));

        assertTrue(dacl.setRule(new SuperAdminRole(), new Permission("*"),
                AssertMode.SatisfyAll, new UserOwnsWorkItemAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter()));

        assertTrue(dacl.hasRule(new SuperAdminRole(), new Permission("*")));

    }

    /**
     * Test method for
     * {@link DefaultAccessControlList#setRule(Role, Permission, AssertMode, Asserter)}
     * .
     */
    @Test
    public void testSetRuleRolePermissionAssertModeAsserter() {

        dacl.clearRules();
        assertFalse(dacl.hasRule(new SuperAdminRole(), new Permission("*")));

        assertTrue(dacl.setRule(new SuperAdminRole(), new Permission("*"),
                AssertMode.SatisfyAll, new UserOwnsWorkItemAsserter()));
        assertTrue(dacl.setRule(new SuperAdminRole(), new Permission("*"),
                AssertMode.SatisfyAll, new UserIsEnabledAsserter()));
        assertTrue(dacl.setRule(new SuperAdminRole(), new Permission("*"),
                AssertMode.SatisfyAll, new UserOwnsWorkItemAsserter()));

        assertTrue(dacl.hasRule(new SuperAdminRole(), new Permission("*")));
    }

    /**
     * Test method for {@link DefaultAccessControlList#allow(Role, Permission)}
     * .
     * 
     * @throws TransactionException
     */
    @Test
    public void testAllow() throws TransactionException {

        dacl.clearRules();

        assertFalse(dacl.isAllowed(new TenantAdminRole(),
                new CommandPermission(DeleteWorkflowModelCommand.class)));

        dacl.allow(new TenantAdminRole(), new CommandPermission(
                DeleteWorkflowModelCommand.class));

        assertTrue(dacl.isAllowed(new TenantAdminRole(), new CommandPermission(
                DeleteWorkflowModelCommand.class)));
    }

    /**
     * Test method for
     * {@link DefaultAccessControlList#isAllowed(Role, Permission)} .
     * 
     * @throws TransactionException
     */
    @Test
    public void testIsAllowed() throws TransactionException {

        dacl.init();

        assertTrue(dacl.isAllowed(new SuperAdminRole(), new Permission("*")));
        assertFalse(dacl.isAllowed(new TenantAdminRole(), new Permission("*")));

        dacl.clearRules();

        assertFalse(dacl.isAllowed(new SuperAdminRole(), new Permission("*")));
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
