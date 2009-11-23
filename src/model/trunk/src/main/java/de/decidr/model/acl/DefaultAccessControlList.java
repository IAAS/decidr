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

import static de.decidr.model.acl.asserters.AssertMode.SatisfyAll;
import static de.decidr.model.acl.asserters.AssertMode.SatisfyAny;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.decidr.model.acl.asserters.AlwaysTrueAsserter;
import de.decidr.model.acl.asserters.AssertMode;
import de.decidr.model.acl.asserters.Asserter;
import de.decidr.model.acl.asserters.FileAccessAsserter;
import de.decidr.model.acl.asserters.ImplicitFileAccessAsserter;
import de.decidr.model.acl.asserters.IsRoleEqualToAccessedUserAsserter;
import de.decidr.model.acl.asserters.UserAdministratesWorkflowInstanceAsserter;
import de.decidr.model.acl.asserters.UserAdministratesWorkflowModelAsserter;
import de.decidr.model.acl.asserters.UserIsEnabledAsserter;
import de.decidr.model.acl.asserters.UserIsInvitationReceiverAsserter;
import de.decidr.model.acl.asserters.UserIsLoggedInAsserter;
import de.decidr.model.acl.asserters.UserIsSuperAdminAsserter;
import de.decidr.model.acl.asserters.UserIsTenantAdminAsserter;
import de.decidr.model.acl.asserters.UserIsWorkflowAdminWithinTenantAsserter;
import de.decidr.model.acl.asserters.UserNotParticipatingInAnyWorkflowAsserter;
import de.decidr.model.acl.asserters.UserOwnsWorkItemAsserter;
import de.decidr.model.acl.asserters.UserOwnsWorkflowModelAsserter;
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
import de.decidr.model.commands.file.AssociateFileWithWorkItemCommand;
import de.decidr.model.commands.file.CreateFileCommand;
import de.decidr.model.commands.file.DeleteFileCommand;
import de.decidr.model.commands.file.GetFileCommand;
import de.decidr.model.commands.file.GetFileDataCommand;
import de.decidr.model.commands.file.ReplaceFileCommand;
import de.decidr.model.commands.system.GetServerCommand;
import de.decidr.model.commands.system.GetServersCommand;
import de.decidr.model.commands.system.LockServerCommand;
import de.decidr.model.commands.system.UpdateServerLoadCommand;
import de.decidr.model.commands.tenant.AddTenantMemberCommand;
import de.decidr.model.commands.tenant.CreateTenantCommand;
import de.decidr.model.commands.tenant.CreateWorkflowModelCommand;
import de.decidr.model.commands.tenant.GetAllTenantsCommand;
import de.decidr.model.commands.tenant.GetCurrentColorSchemeCommand;
import de.decidr.model.commands.tenant.GetTenantCommand;
import de.decidr.model.commands.tenant.GetTenantLogoCommand;
import de.decidr.model.commands.tenant.GetTenantSettingsCommand;
import de.decidr.model.commands.tenant.GetUsersOfTenantCommand;
import de.decidr.model.commands.tenant.GetWorkflowInstancesCommand;
import de.decidr.model.commands.tenant.GetWorkflowModelsCommand;
import de.decidr.model.commands.tenant.ImportPublishedWorkflowModelsCommand;
import de.decidr.model.commands.tenant.InviteUsersAsTenantMembersCommand;
import de.decidr.model.commands.tenant.SetColorSchemeCommand;
import de.decidr.model.commands.tenant.SetCurrentColorSchemeCommand;
import de.decidr.model.commands.tenant.SetTenantDescriptionCommand;
import de.decidr.model.commands.tenant.SetTenantLogoCommand;
import de.decidr.model.commands.user.CheckAuthKeyCommand;
import de.decidr.model.commands.user.ConfirmChangeEmailRequestCommand;
import de.decidr.model.commands.user.ConfirmInvitationCommand;
import de.decidr.model.commands.user.ConfirmPasswordResetCommand;
import de.decidr.model.commands.user.ConfirmRegistrationCommand;
import de.decidr.model.commands.user.CreateNewUnregisteredUserCommand;
import de.decidr.model.commands.user.GetAdministratedWorkflowInstancesCommand;
import de.decidr.model.commands.user.GetAdministratedWorkflowModelsCommand;
import de.decidr.model.commands.user.GetHighestUserRoleCommand;
import de.decidr.model.commands.user.GetInvitationCommand;
import de.decidr.model.commands.user.GetJoinedTenantsCommand;
import de.decidr.model.commands.user.GetUserByLoginCommand;
import de.decidr.model.commands.user.GetUserPropertiesCommand;
import de.decidr.model.commands.user.GetUserRoleForTenantCommand;
import de.decidr.model.commands.user.GetUserWithProfileCommand;
import de.decidr.model.commands.user.GetWorkItemsCommand;
import de.decidr.model.commands.user.IsUserRegisteredCommand;
import de.decidr.model.commands.user.LeaveTenantCommand;
import de.decidr.model.commands.user.RefuseInvitationCommand;
import de.decidr.model.commands.user.RegisterUserCommand;
import de.decidr.model.commands.user.RemoveFromTenantCommand;
import de.decidr.model.commands.user.RequestChangeEmailCommand;
import de.decidr.model.commands.user.RequestPasswordResetCommand;
import de.decidr.model.commands.user.SetCurrentTenantCommand;
import de.decidr.model.commands.user.SetPasswordCommand;
import de.decidr.model.commands.user.SetUserProfileCommand;
import de.decidr.model.commands.user.SetUserPropertyCommand;
import de.decidr.model.commands.workflowinstance.GetAllWorkItemsCommand;
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

/**
 * Provides a centralized mechanism for permissions checking. The default ACL
 * acts as a whitelist that grants or denies roles access to permissions based
 * on a set of conditions that must be met. These conditions are represented by
 * asserters.
 * <p>
 * The ruleset is currently hardcoded into the init method. In the future these
 * rules could potentially be externalized to a configuration file or the
 * database.
 * <p>
 * <h1>Usage:</h1>
 * <p>
 * Roles and permissions are hierarchically ordered. The role hierarchy is
 * equivalent to the class hierarchy of all classes that implement the
 * {@link Role} interface:
 * 
 * <pre>
 * +Role
 *  +--BasicRole
 *  |  +--UserRole
 *  |  |   +--WorkflowAdminRole
 *  |  |   |  +--TenantAdminRole
 *  |  |   |     +--SuperAdminRole
 *  |  +--WebserviceRole
 * </pre>
 * 
 * <br>
 * The permission hierarchy is independent from any class hierarchy because it
 * is derived from the permission <b>name</b>:
 * 
 * <pre>
 * +&quot;*&quot;
 *  +--&quot;de.*&quot;
 *  |  +--&quot;decidr.*&quot;
 *  |  |  +--&quot;model.*&quot;
 *  |  |  |  +--&quot;Tenant.*&quot;
 *  |  |  |  |  +--&quot;98883&quot;
 *  |  |  |  |  +--&quot;25&quot;
 *  |  |  |  |  +--&quot;10&quot;  
 *  |  |  |  +--&quot;WorkflowModel.*&quot;
 *  |  |  |  |  +--&quot;42&quot;
 * </pre>
 * 
 * The above tree contains the following permissions:
 * <ul>
 * <li>de.decidr.model.Tenant.98883</li>
 * <li>de.decidr.model.Tenant.25</li>
 * <li>de.decidr.model.Tenant.10</li>
 * <li>de.decidr.model.WorkflowModel.42</li>
 * </ul>
 * 
 * <strong>Important: in this ACL implementation the permission hierarchy takes
 * precedence over the role hierarchy.</strong> The <code>isAllowed()</code>
 * method will always traverse the permission hierarchy before traversing the
 * role hierarchy.
 * <p>
 * Example:
 * <ol>
 * <li>deny(UserRole, "Tenant.*")</li>
 * <li>allow(SuperAdminRole, "*")</li>
 * <li>deny(SuperAdminRole, "Tenant.*")</li>
 * <li>allow(SuperAdminRole, "Tenant.42")</li>
 * <li>allow(WorkflowAdminRole, "WorkflowModel.20")</li>
 * </ol>
 * 
 * <ul>
 * <li>isAllowed(UserRole, "Tenant.2") -> <code>false</code> due to rule #1</li>
 * <li>isAllowed(UserRole, "Tenant.*") -> <code>false</code> due to rule #1</li>
 * <li>isAllowed(SuperAdminRole, "WorkflowModel.*") -> <code>true</code> due to
 * rule #2</li>
 * <li>isAllowed(BasicRole, "Tenant.39") -> <code>false</code> because no rule
 * applies</li>
 * <li>isAllowed(SuperAdminRole, "Tenant.15") -> <code>false</code> due to rule
 * #3</li>
 * <li>isAllowed(WorkflowAdminRole, "Tenant.42") -> <code>false</code> due to
 * rule #1</li>
 * <li>isAllowed(SuperAdminRole, "Tenant.42")> <code>true</code> due to rule #4</li>
 * <li>isAllowed(WorkflowAdminRole, "WorkflowModel.*")-> <code>false</code>
 * because no rule applies</li>
 * <li>isAllowed(WorkflowAdminRole, "WorkflowModel.20")-> <code>true</code> due
 * to rule #5</li>
 * <li>TODO: if you can think of a good example, put it here :-) ~dh</li>
 * </ul>
 * <p>
 * XXX Future extension case: separate source of rules from rest
 * ("RulesetProvider", "HardcodedRulesetProvider", "DatabaseRulesetProvider",
 * ...?) ~dh
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class DefaultAccessControlList implements AccessControlList {

    Logger logger = DefaultLogger.getLogger(DefaultAccessControlList.class);

    /**
     * The compound key of the ruleset which maps one role and one permission to
     * a set of asserters and an assert mode.
     * 
     * @author Markus Fischer
     * @author Daniel Huss
     * 
     * @version 0.1
     */
    class RuleKey {

        /**
         * The role that is being assumed (is never null).
         */
        private Role role;
        /**
         * The accessed permission (is never null).
         */
        private Permission permission;

        /**
         * Constructor of the compound key.
         * 
         * @param role
         *            must not be null.
         * @param permission
         *            must not be null.
         */
        public RuleKey(Role role, Permission permission) {
            super();

            if (role == null) {
                throw new IllegalArgumentException("Role must not be null.");
            }
            if (permission == null) {
                throw new IllegalArgumentException(
                        "Permission must not be null");
            }

            this.role = role;
            this.permission = permission;
        }

        @Override
        public boolean equals(Object obj) {
            // rule keys are equal iff they consist of the same class and
            // permission
            if (obj instanceof RuleKey) {
                RuleKey other = (RuleKey) obj;

                // role and permission are never null.
                Boolean result = role.getClass().getName().equals(
                        other.role.getClass().getName())
                        && permission.getName().equals(
                                other.permission.getName());

                return result;
            } else {
                // "this" cannot be null.
                return false;
            }
        }

        @Override
        public int hashCode() {
            // hash code must be consistent with equals.
            // role and permission are never null.
            int roleHashCode = role.getClass().getName().hashCode();
            int permissionHashCode = permission.getName().hashCode();

            int result = roleHashCode ^ permissionHashCode;

            return result;
        }
    }

    /**
     * The compound value of the ruleset which maps one role and one permission
     * to a set of asserters and an assert mode.
     * 
     * @author Markus Fischer
     * @author Daniel Huss
     * 
     * @version 0.1
     */
    class RuleConditions {
        Set<Asserter> asserters;
        AssertMode assertMode;

        /**
         * Constructor of the compound value.
         * 
         * @param asserters
         * @param assertMode
         */
        public RuleConditions(Asserter[] asserters, AssertMode assertMode) {
            super();
            this.assertMode = assertMode;
            this.asserters = asserters == null ? null : new HashSet<Asserter>(
                    Arrays.asList(asserters));
        }

        @Override
        public boolean equals(Object obj) {

            Boolean equal = false;

            if (obj instanceof RuleConditions) {
                RuleConditions value = (RuleConditions) obj;

                Boolean assertEqual = asserters == null ? value.asserters == null
                        : asserters.equals(value.asserters);

                Boolean modeEqual = assertMode == null ? value.assertMode == null
                        : assertMode.equals(value.assertMode);

                equal = assertEqual && modeEqual;
            }

            return equal;
        }

        @Override
        public int hashCode() {
            int assertersHash = asserters == null ? 0 : asserters.hashCode();
            int modeHash = assertMode == null ? 0 : assertMode.hashCode();
            return assertersHash ^ modeHash; // bitwise exclusive or
        }
    }

    /**
     * A long-lived reusable instance of the stateless asserter that always
     * returns true.
     */
    private static final Asserter[] alwaysTrueAsserter = { new AlwaysTrueAsserter() };

    /**
     * Singleton instance.
     */
    private static DefaultAccessControlList instance = new DefaultAccessControlList();

    /**
     * Map containing all rules.
     */
    protected Map<RuleKey, RuleConditions> ruleMap;

    /**
     * Singleton getter.
     * 
     * @return the ACL
     */
    public static DefaultAccessControlList getInstance() {
        return instance;
    }

    /**
     * Constructor, also initializes the ACL.
     */
    private DefaultAccessControlList() {
        super();
        this.ruleMap = new HashMap<RuleKey, RuleConditions>();
        this.init();
    }

    /**
     * Clears rule lis and sets default rules.
     */
    public void init() {
        clearRules();

        // The superadmin can do anything
        setRule(new SuperAdminRole(), new Permission("*"), SatisfyAny,
                new UserIsSuperAdminAsserter());

        /**
         * COMMAND PERMISSIONS
         */

        /**
         * Command Permissions System Facade
         * 
         * Nothing to do except one rule. All other commands are always allowed
         * by SuperAdmin. These commands are already covered by the first rule.
         */
        setRule(ServerLoadUpdaterRole.getInstance(), new CommandPermission(
                UpdateServerLoadCommand.class), SatisfyAll, alwaysTrueAsserter);

        setRule(ServerLoadUpdaterRole.getInstance(), new CommandPermission(
                GetServersCommand.class), SatisfyAll, alwaysTrueAsserter);

        setRule(ServerLoadUpdaterRole.getInstance(), new CommandPermission(
                GetServerCommand.class), SatisfyAll, alwaysTrueAsserter);

        setRule(ServerLoadUpdaterRole.getInstance(), new CommandPermission(
                LockServerCommand.class), SatisfyAll, alwaysTrueAsserter);

        /**
         * Command Permissions Tenant Facade
         */
        setRule(new TenantAdminRole(), new CommandPermission(
                AddTenantMemberCommand.class), SatisfyAll,
                new UserIsTenantAdminAsserter(), new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new SuperAdminRole(), new CommandPermission(
                GetAllTenantsCommand.class), SatisfyAll,
                new UserIsSuperAdminAsserter());

        setRule(new UserRole(),
                new CommandPermission(CreateTenantCommand.class), SatisfyAll,
                alwaysTrueAsserter);

        setRule(new TenantAdminRole(), new CommandPermission(
                GetTenantSettingsCommand.class), SatisfyAll,
                new UserIsTenantAdminAsserter(), new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                CreateWorkflowModelCommand.class), SatisfyAll,
                new UserIsTenantAdminAsserter(), new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new BasicRole(), new CommandPermission(
                GetCurrentColorSchemeCommand.class), SatisfyAll,
                alwaysTrueAsserter);

        setRule(new BasicRole(), new CommandPermission(
                GetTenantLogoCommand.class), SatisfyAll, alwaysTrueAsserter);

        setRule(new WorkflowAdminRole(), new CommandPermission(
                GetUsersOfTenantCommand.class), SatisfyAll,
                new UserIsWorkflowAdminWithinTenantAsserter(), new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new WorkflowAdminRole(), new CommandPermission(
                GetWorkflowInstancesCommand.class), SatisfyAll,
                new UserIsWorkflowAdminWithinTenantAsserter(), new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                GetWorkflowModelsCommand.class), SatisfyAll,
                new UserIsTenantAdminAsserter(), new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                ImportPublishedWorkflowModelsCommand.class), SatisfyAll,
                new UserIsTenantAdminAsserter(), new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new WorkflowAdminRole(), new CommandPermission(
                InviteUsersAsTenantMembersCommand.class), SatisfyAll,
                new UserAdministratesWorkflowInstanceAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                SetColorSchemeCommand.class), SatisfyAll,
                new UserIsTenantAdminAsserter(), new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                SetCurrentColorSchemeCommand.class), SatisfyAll,
                new UserIsTenantAdminAsserter(), new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                SetTenantDescriptionCommand.class), SatisfyAll,
                new UserIsTenantAdminAsserter(), new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                SetTenantLogoCommand.class), SatisfyAll,
                new UserIsTenantAdminAsserter(), new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new BasicRole(), new CommandPermission(GetTenantCommand.class),
                SatisfyAll, alwaysTrueAsserter);

        /**
         * Command Permissions User Facade
         */
        setRule(new BasicRole(), new CommandPermission(
                CreateNewUnregisteredUserCommand.class), SatisfyAll,
                alwaysTrueAsserter);

        setRule(new UserRole(),
                new CommandPermission(RegisterUserCommand.class), SatisfyAll,
                alwaysTrueAsserter);

        setRule(new BasicRole(), new CommandPermission(
                GetUserByLoginCommand.class), SatisfyAll, alwaysTrueAsserter);

        setRule(new UserRole(),
                new CommandPermission(CheckAuthKeyCommand.class), SatisfyAll,
                alwaysTrueAsserter);

        setRule(new UserRole(),
                new CommandPermission(SetPasswordCommand.class), SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new UserRole(), new CommandPermission(
                RequestPasswordResetCommand.class), SatisfyAll,
                alwaysTrueAsserter);

        setRule(new UserRole(),
                new CommandPermission(LeaveTenantCommand.class), SatisfyAll,
                new UserIsEnabledAsserter(),
                new UserNotParticipatingInAnyWorkflowAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new UserRole(), new CommandPermission(
                SetUserPropertyCommand.class), SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(),
                new UserIsEnabledAsserter());

        setRule(new UserRole(), new CommandPermission(
                GetUserPropertiesCommand.class), SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter());

        setRule(new UserRole(), new CommandPermission(
                SetCurrentTenantCommand.class), SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(),
                new UserIsEnabledAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                RemoveFromTenantCommand.class), SatisfyAll,
                new UserIsTenantAdminAsserter(), new UserIsEnabledAsserter(),
                new UserNotParticipatingInAnyWorkflowAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new UserRole(), new CommandPermission(
                ConfirmPasswordResetCommand.class), SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new UserRole(), new CommandPermission(
                ConfirmRegistrationCommand.class), SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(),
                new UserIsEnabledAsserter());

        setRule(new UserRole(), new CommandPermission(
                ConfirmChangeEmailRequestCommand.class), SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new UserRole(), new CommandPermission(
                ConfirmInvitationCommand.class), SatisfyAll,
                new UserIsInvitationReceiverAsserter());

        setRule(new UserRole(), new CommandPermission(
                RefuseInvitationCommand.class), SatisfyAll,
                new UserIsInvitationReceiverAsserter(),
                new IsRoleEqualToAccessedUserAsserter());

        setRule(new UserRole(), new CommandPermission(
                GetUserWithProfileCommand.class), SatisfyAll,
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new UserRole(), new CommandPermission(
                GetHighestUserRoleCommand.class), SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new UserRole(), new CommandPermission(
                GetUserRoleForTenantCommand.class), SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new WorkflowAdminRole(), new CommandPermission(
                GetAdministratedWorkflowInstancesCommand.class), SatisfyAll,
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new UserRole(), new CommandPermission(
                GetJoinedTenantsCommand.class), SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new WorkflowAdminRole(), new CommandPermission(
                GetAdministratedWorkflowModelsCommand.class), SatisfyAll,
                new UserIsWorkflowAdminWithinTenantAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new UserRole(),
                new CommandPermission(GetWorkItemsCommand.class), SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new UserRole(), new CommandPermission(
                GetInvitationCommand.class), SatisfyAll,
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new BasicRole(), new CommandPermission(
                IsUserRegisteredCommand.class), SatisfyAll, alwaysTrueAsserter);

        setRule(new BasicRole(), new CommandPermission(
                SetUserProfileCommand.class), SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new UserRole(), new CommandPermission(
                RequestChangeEmailCommand.class), SatisfyAll,
                new UserIsLoggedInAsserter());

        /**
         * Command Permissions WorkflowInstanceFacade
         */
        setRule(new WorkflowAdminRole(), new CommandPermission(
                StopWorkflowInstanceCommand.class), SatisfyAll,
                new UserAdministratesWorkflowInstanceAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new WorkflowAdminRole(), new CommandPermission(
                GetParticipatingUsersCommand.class), SatisfyAll,
                new UserAdministratesWorkflowInstanceAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new WorkflowAdminRole(), new CommandPermission(
                StartWorkflowInstanceCommand.class), SatisfyAll,
                new UserAdministratesWorkflowInstanceAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(HumanTaskRole.getInstance(), new CommandPermission(
                RemoveAllWorkItemsCommand.class), SatisfyAll,
                alwaysTrueAsserter);

        setRule(new WorkflowAdminRole(), new CommandPermission(
                GetAllWorkItemsCommand.class), SatisfyAll,
                new UserAdministratesWorkflowInstanceAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        /**
         * Command Permissions WorkflowModelFacade
         */
        setRule(new TenantAdminRole(), new CommandPermission(
                SaveWorkflowModelCommand.class), SatisfyAll,
                new UserOwnsWorkflowModelAsserter(),
                new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                GetWorkflowModelCommand.class), SatisfyAll,
                new UserOwnsWorkflowModelAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                PublishWorkflowModelsCommand.class), SatisfyAll,
                new UserOwnsWorkflowModelAsserter(),
                new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                MakeWorkflowModelExecutableCommand.class), SatisfyAll,
                new UserOwnsWorkflowModelAsserter(),
                new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                GetWorkflowAdministratorsCommand.class), SatisfyAll,
                new UserOwnsWorkflowModelAsserter(),
                new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                SetWorkflowAdministratorsCommand.class), SatisfyAll,
                new UserOwnsWorkflowModelAsserter(),
                new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                DeleteWorkflowModelCommand.class), SatisfyAll,
                new UserOwnsWorkflowModelAsserter(),
                new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                StartWorkflowInstanceCommand.class), SatisfyAll,
                new UserAdministratesWorkflowModelAsserter(),
                new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                StopWorkflowInstanceCommand.class), SatisfyAll,
                new UserAdministratesWorkflowModelAsserter(),
                new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                SaveStartConfigurationCommand.class), SatisfyAll,
                new UserOwnsWorkflowModelAsserter(),
               new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                GetLastStartConfigurationCommand.class), SatisfyAll,
                new UserOwnsWorkflowModelAsserter(),
                new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                GetPublishedWorkflowModelsCommand.class), SatisfyAll,
                new UserIsTenantAdminAsserter(), new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        /**
         * Command Permissions WorkItemFacade
         */
        setRule(new UserRole(),
                new CommandPermission(GetWorkItemCommand.class), SatisfyAll,
                new UserOwnsWorkItemAsserter(), new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(HumanTaskRole.getInstance(), new CommandPermission(
                GetWorkItemCommand.class), SatisfyAll, alwaysTrueAsserter);

        setRule(HumanTaskRole.getInstance(), new CommandPermission(
                CreateWorkItemCommand.class), SatisfyAll, alwaysTrueAsserter);

        setRule(new UserRole(), new CommandPermission(SetDataCommand.class),
                SatisfyAll, new UserOwnsWorkItemAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new UserRole(), new CommandPermission(SetStatusCommand.class),
                SatisfyAll, new UserOwnsWorkItemAsserter(),
                new UserIsEnabledAsserter(), new UserIsLoggedInAsserter());

        setRule(new WorkflowAdminRole(), new CommandPermission(
                DeleteWorkItemCommand.class), SatisfyAll,
                new UserOwnsWorkItemAsserter(), new UserIsEnabledAsserter(),
                new UserIsLoggedInAsserter());

        setRule(HumanTaskRole.getInstance(), new CommandPermission(
                DeleteWorkItemCommand.class), SatisfyAll, alwaysTrueAsserter);

        setRule(new BasicRole(), new CommandPermission(
                AssociateFileWithWorkItemCommand.class), SatisfyAny,
                alwaysTrueAsserter);

        /**
         * Command permissions FileFacade
         */
        setRule(new BasicRole(), new CommandPermission(GetFileCommand.class),
                SatisfyAll, alwaysTrueAsserter);

        setRule(new BasicRole(),
                new CommandPermission(GetFileDataCommand.class), SatisfyAll,
                alwaysTrueAsserter);

        setRule(new BasicRole(),
                new CommandPermission(DeleteFileCommand.class), SatisfyAll,
                alwaysTrueAsserter);

        setRule(new BasicRole(),
                new CommandPermission(ReplaceFileCommand.class), SatisfyAll,
                alwaysTrueAsserter);

        setRule(new BasicRole(),
                new CommandPermission(CreateFileCommand.class), SatisfyAll,
                alwaysTrueAsserter);

        /**
         * file permissions
         */
        Asserter[] fileAsserters = { new FileAccessAsserter(),
                new ImplicitFileAccessAsserter() };

        // File Delete Permissions
        setRule(new BasicRole(), new FileDeletePermission(null), SatisfyAny,
                fileAsserters);

        // File Read Permissions
        setRule(new BasicRole(), new FileReadPermission(null), SatisfyAny,
                fileAsserters);

        // File Replace Permissions
        setRule(new BasicRole(), new FileReplacePermission(null), SatisfyAny,
                fileAsserters);
    }

    /**
     * {@inheritDoc}
     */
    public void clearRules() {
        ruleMap.clear();
    }

    /**
     * {@inheritDoc}
     */
    public Boolean setRule(Role role, Permission permission, AssertMode mode,
            Asserter... asserters) {

        ruleMap.put(new RuleKey(role, permission), new RuleConditions(
                asserters, mode));
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean setRule(Role role, Permission permission, AssertMode mode,
            Asserter asserter) {
        Asserter[] asserters = { asserter };
        return setRule(role, permission, mode, asserters);
    }

    /**
     * {@inheritDoc}
     */
    public void allow(Role role, Permission permission) {
        setRule(role, permission, AssertMode.SatisfyAll,
                DefaultAccessControlList.alwaysTrueAsserter);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean isAllowed(Role role, Permission permission)
            throws TransactionException {
        Boolean allowed = true;
        RuleKey key = findRule(new RuleKey(role, permission));
        RuleConditions rule = key == null ? null : ruleMap.get(key);

        if (rule == null) {
            logger.debug("No rule conditions found for key " + key);
            return false;
        }

        switch (rule.assertMode) {

        case SatisfyAll:

            for (Asserter a : rule.asserters) {
                if (!a.assertRule(role, permission)) {
                    logger.debug(String.format(
                            "[SatisfyAll] %1$s DISAGREES, exiting loop.", a
                                    .getClass().getSimpleName()));
                    allowed = false;
                    break;
                } else {
                    logger.debug(String.format("[SatisfyAll] %1$s agrees.", a
                            .getClass().getSimpleName()));
                }
            }
            break;

        case SatisfyAny:
            allowed = false;
            for (Asserter a : rule.asserters) {
                if (a.assertRule(role, permission)) {
                    logger.debug(String.format(
                            "[SatisfyAny] %1$s AGREES, exiting loop.", a
                                    .getClass().getSimpleName()));
                    allowed = true;
                    break;
                } else {
                    logger.debug(String.format("[SatisfyAny] %1$s disagrees.",
                            a.getClass().getSimpleName()));
                }
            }
            break;

        default:
            allowed = false;
            break;
        }

        logger.debug("Final isAllowed() decision: "
                + (allowed ? "ALLOWED" : "DENIED"));

        return allowed;
    }

    /**
     * Finds an entry in the rule map that applies to the given {@link RuleKey}
     * by looking for implying roles / permissions if necessary. If no entry if
     * found, this method returns null.
     * <p>
     * <strong>Example: </strong>
     * <p>
     * The rule map contains a single rule whose rule key shall be callled
     * "key1":<br>
     * <code>RuleKey(SuperadminRole.class, new
     * Permission("WorkflowModel.*")) -> RuleConditions({alwaysTrueAsserter},
     * satisfyAll)</code>.
     * <p>
     * <ul>
     * <li>findRule(new RuleKey(SuperadminRole.class, new
     * Permission("WorkflowModel.1003")) returns key1.</li>
     * <li>findRule(new RuleKey(UserRole.class, new Permission("WorkflowModel.*)
     * returns null.</li>
     * <li>findRule(new RuleKey(SuperadminRole.class, new
     * Permission("WorkflowModel.*")) returns key1.
     * <li>findRule(new RuleKey(SuperadminRole.class, new
     * Permission("WorkflowModel.1003.someProperty") returns key1.</li>
     * </ul>
     * 
     * @param key
     *            rule key to search for.
     * @return the rule key that implies
     */
    protected RuleKey findRule(RuleKey key) {
        if (key == null) {
            throw new IllegalArgumentException("Rule key must not be null.");
        }

        if (ruleMap.containsKey(key)) {
            // since the rule map contains given key, we don't have to traverse
            // the hierarchy at all.
            return key;
        }

        // traverse the hierarchy from bottom to top.
        ArrayList<Permission> permissionHierarchy = new ArrayList<Permission>();
        ArrayList<Class<?>> roleHierarchy = new ArrayList<Class<?>>();

        // build permission hierarchy
        Permission permission;
        Permission nextPermission = key.permission;
        do {
            permission = nextPermission;
            permissionHierarchy.add(permission);
            nextPermission = permission.getNextImplyingPermission();
        } while (nextPermission != permission);

        // build role hierarchy
        Class<?> roleClass;
        Class<?> nextRoleClass = key.role.getClass();
        do {
            roleClass = nextRoleClass;
            roleHierarchy.add(roleClass);
            nextRoleClass = roleClass.getSuperclass();
        } while (nextRoleClass != null
                && Role.class.isAssignableFrom(nextRoleClass)
                && !nextRoleClass.getName().equals(roleClass.getName()));

        RuleKey result = null;
        RuleKey currentKey;
        for (Class<?> currentRoleClass : roleHierarchy) {
            for (Permission currentPermission : permissionHierarchy) {
                try {
                    currentKey = new RuleKey((Role) currentRoleClass
                            .newInstance(), currentPermission);
                    if (ruleMap.containsKey(currentKey)) {
                        result = currentKey;
                        // we do not break the for loops because there might be
                        // an implying key even higher up the permission
                        // hierarchy.
                    }
                } catch (InstantiationException e) {
                    // If the role is abstract or private we cannot instanciate
                    // it, but we also cannot define rules for abstract roles
                    // so this is ok.
                } catch (IllegalAccessException e) {
                    // see above
                }
            }
            /*
             * The role hierarchy is only traversed if no rule exists for the
             * current role and any permission.
             */
            if (result != null) {
                break;
            }
        }

        if (result == null) {
            logger.debug("No rule found for " + key.role.getClass().getName()
                    + ", " + key.permission.getName());
        }

        return result;
    }

    @Override
    public Boolean hasRule(Role role, Permission permission) {
        return findRule(new RuleKey(role, permission)) != null;
    }

}