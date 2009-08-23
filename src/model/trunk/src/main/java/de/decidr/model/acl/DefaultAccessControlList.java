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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.decidr.model.acl.asserters.AlwaysTrueAsserter;
import de.decidr.model.acl.asserters.AssertMode;
import de.decidr.model.acl.asserters.Asserter;
import de.decidr.model.acl.asserters.IsRoleEqualToAccessedUserAsserter;
import de.decidr.model.acl.asserters.UserIsSuperAdminAsserter;
import de.decidr.model.acl.asserters.UserIsTenantAdminAsserter;
import de.decidr.model.acl.asserters.UserIsEnabledAsserter;
import de.decidr.model.acl.asserters.UserAdministratesWorkflowInstanceAsserter;
import de.decidr.model.acl.asserters.UserIsLoggedInAsserter;
import de.decidr.model.acl.asserters.UserAdministratesWorkflowModelAsserter;
import de.decidr.model.acl.asserters.UserHasAccessToFileAsserter;
import de.decidr.model.acl.asserters.UserIsInvitationReceiverAsserter;
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
import de.decidr.model.commands.tenant.RemoveWorkflowModelCommand;
import de.decidr.model.commands.tenant.SetAdvancedColorSchemeCommand;
import de.decidr.model.commands.tenant.SetCurrentColorSchemeCommand;
import de.decidr.model.commands.tenant.SetSimpleColorSchemeCommand;
import de.decidr.model.commands.tenant.SetTenantDescriptionCommand;
import de.decidr.model.commands.tenant.SetTenantLogoCommand;
import de.decidr.model.commands.user.CheckAuthKeyCommand;
import de.decidr.model.commands.user.ConfirmChangeEmailRequestCommand;
import de.decidr.model.commands.user.ConfirmInviationCommand;
import de.decidr.model.commands.user.ConfirmPasswordResetCommand;
import de.decidr.model.commands.user.ConfirmRegistrationCommand;
import de.decidr.model.commands.user.GetAdministratedWorkflowModelCommand;
import de.decidr.model.commands.user.GetAdminstratedWorkflowInstancesCommand;
import de.decidr.model.commands.user.GetHighestUserRoleCommand;
import de.decidr.model.commands.user.GetInvitationCommand;
import de.decidr.model.commands.user.GetJoinedTenantsCommand;
import de.decidr.model.commands.user.GetUserByLoginCommand;
import de.decidr.model.commands.user.GetUserWithProfileCommand;
import de.decidr.model.commands.user.GetUserRoleForTenantCommand;
import de.decidr.model.commands.user.GetWorkitemsCommand;
import de.decidr.model.commands.user.IsRegisteredCommand;
import de.decidr.model.commands.user.LeaveTenantCommand;
import de.decidr.model.commands.user.RefuseInviationCommand;
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
import de.decidr.model.commands.workflowmodel.GetWorkflowAdminstratorsCommand;
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

/**
 * Provides a centralized mechanism for permissions checking. The default ACL
 * acts as a whitelist that grants or denies roles access to permissions based
 * on a set of conditions that must be met. These conditions are represented by
 * asserters.
 * 
 * The ruleset is currently hardcoded into the init method. In the future these
 * rules could potentially be externalized to a configuration file or the
 * database.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class DefaultAccessControlList implements AccessControlList {

    /**
     * The compound key of the ruleset which maps one role and one permission to
     * a set of asserters and an assert mode.
     * 
     * @author Markus Fischer
     * @author Daniel Huss
     * 
     * @version 0.1
     */
    class Key {

        private Role role;
        private Permission permission;

        /**
         * Constructor of the compound key.
         * 
         * @param role
         * @param permission
         */
        public Key(Role role, Permission permission) {
            super();
            this.role = role;
            this.permission = permission;
        }

        @Override
        public boolean equals(Object obj) {

            Boolean equal = false;

            if (obj instanceof Key) {
                Key key = (Key) obj;

                Boolean roleEqual = role == null ? key.role == null : (role
                        .equals(key.role));

                Boolean permissionEqual = permission == null ? key.permission == null
                        : permission.equals(key.permission);

                equal = roleEqual && permissionEqual;
            }

            return equal;
        }

        @Override
        public int hashCode() {
            int roleHash = role == null ? 0 : role.hashCode();
            int permissionHash = permission == null ? 0 : permission.hashCode();
            return roleHash ^ permissionHash; // bitwise exclusive or
        }

        /**
         * Compares this key to the given key and returns true iff the given key
         * is implied by this key. This is the case iff all of the following
         * conditions are true:
         * <ul>
         * <li>The role of this key is a superclass of or the same as key.role</li>
         * <li>The permission of this key implies key.permission</li>
         * </ul>
         * 
         * @param key
         * @return true iff the given key is implied by this key.
         */
        public Boolean implies(Key key) {
            if (key == null) {
                return false;
            } else {
                Boolean isSuperClass = (key.role == null || role == null) ? false
                        : role.getClass().isAssignableFrom(key.role.getClass());
                return (isSuperClass && permission.implies(key.permission));

            }
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
    class Value {
        Set<Asserter> asserters;
        AssertMode assertMode;

        /**
         * Constructor of the compound value.
         * 
         * @param asserters
         * @param assertMode
         */
        public Value(Asserter[] asserters, AssertMode assertMode) {
            super();
            this.assertMode = assertMode;
            this.asserters = asserters == null ? null : new HashSet<Asserter>(
                    Arrays.asList(asserters));
        }

        @Override
        public boolean equals(Object obj) {

            Boolean equal = false;

            if (obj instanceof Value) {
                Value value = (Value) obj;

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
    protected Map<Key, Value> ruleMap;

    /**
     * Singleton getter.
     * 
     * @return the ACL
     */
    public static DefaultAccessControlList getInstance() {
        return instance;
    }

    /**
     * Constructor.
     */
    private DefaultAccessControlList() {
        super();
        this.ruleMap = new HashMap<Key, Value>();
        this.init();
    }

    /**
     * clears list and sets default rules
     * 
     */
    public void init() {
        clearRules();
      
        // The superadmin can do anything
        setRule(new SuperAdminRole(), new Permission("*"), AssertMode.SatisfyAny,
                new UserIsSuperAdminAsserter(),new UserIsLoggedInAsserter());

        /**
         * COMMAND PERMISSIONS
         */
        
        /**
         * Command Permissions System Facade
         * 
         * Nothing to do except one rule. All other commands are always allowed
         * by SuperAdmin. These commands are already covered by the first rule.
         * 
         */
        setRule(ServerLoadUpdaterRole.getInstance(), new CommandPermission(
                UpdateServerLoadCommand.class), AssertMode.SatisfyAll,
                alwaysTrueAsserter);
        
        setRule(ServerLoadUpdaterRole.getInstance(), new CommandPermission(
                GetServersCommand.class), AssertMode.SatisfyAll,
                alwaysTrueAsserter);
        

        
        
        /**
         *  Command Permissions Tenant Facade
         */
        setRule(new TenantAdminRole(), new CommandPermission(
                AddTenantMemberCommand.class), AssertMode.SatisfyAll,
                new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());

        setRule(new UserRole(), new CommandPermission(
                CreateTenantCommand.class), AssertMode.SatisfyAll,
                alwaysTrueAsserter);
        
        setRule(new TenantAdminRole(), new CommandPermission(
                CreateWorkflowModelCommand.class), AssertMode.SatisfyAll,
                new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new BasicRole(null), new CommandPermission(
                GetCurrentColorSchemeCommand.class), AssertMode.SatisfyAll,
                alwaysTrueAsserter);
         
        setRule(new BasicRole(null), new CommandPermission(
                GetTenantLogoCommand.class), AssertMode.SatisfyAll,
                alwaysTrueAsserter);
        
        setRule(new WorkflowAdminRole(), new CommandPermission(
                GetUsersOfTenantCommand.class), AssertMode.SatisfyAll,
                new UserIsTenantAdminAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new WorkflowAdminRole(), new CommandPermission(
                GetWorkflowInstancesCommand.class), AssertMode.SatisfyAll,
                new UserIsTenantAdminAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new TenantAdminRole(), new CommandPermission(
                GetWorkflowModelsCommand.class), AssertMode.SatisfyAll,
                new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new TenantAdminRole(), new CommandPermission(
                ImportPublishedWorkflowModelsCommand.class), AssertMode.SatisfyAll,
                new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new WorkflowAdminRole(), new CommandPermission(
                InviteUsersAsTenantMembersCommand.class), AssertMode.SatisfyAll,
                new UserAdministratesWorkflowInstanceAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());

        setRule(new TenantAdminRole(), new CommandPermission(
                RemoveWorkflowModelCommand.class), AssertMode.SatisfyAll,
                new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());    
        
        setRule(new TenantAdminRole(), new CommandPermission(
                SetAdvancedColorSchemeCommand.class), AssertMode.SatisfyAll,
                new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter()); 
        
        setRule(new TenantAdminRole(), new CommandPermission(
                SetCurrentColorSchemeCommand.class), AssertMode.SatisfyAll,
                new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter()); 
        
        setRule(new TenantAdminRole(), new CommandPermission(
                SetTenantDescriptionCommand.class), AssertMode.SatisfyAll,
                new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter()); 
        
        setRule(new TenantAdminRole(), new CommandPermission(
                SetTenantLogoCommand.class), AssertMode.SatisfyAll,
                new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new TenantAdminRole(), new CommandPermission(
                SetSimpleColorSchemeCommand.class), AssertMode.SatisfyAll,
                new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        /**
         *  Command Permissions User Facade
         */
        setRule(new UserRole(), new CommandPermission(
                RegisterUserCommand.class), AssertMode.SatisfyAll,
                alwaysTrueAsserter); 
        
        setRule(new BasicRole(null), new CommandPermission(
                GetUserByLoginCommand.class), AssertMode.SatisfyAll,
                alwaysTrueAsserter); 
        
        setRule(new UserRole(), new CommandPermission(
                CheckAuthKeyCommand.class), AssertMode.SatisfyAll,
                alwaysTrueAsserter);
        
        setRule(new UserRole(), new CommandPermission(
                SetPasswordCommand.class), AssertMode.SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new UserRole(), new CommandPermission(
                RequestPasswordResetCommand.class), AssertMode.SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new UserRole(), new CommandPermission(
                LeaveTenantCommand.class), AssertMode.SatisfyAll,
                new UserIsEnabledAsserter(), new UserNotParticipatingInAnyWorkflowAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new UserRole(), new CommandPermission(
                SetUserPropertyCommand.class), AssertMode.SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new TenantAdminRole(), new CommandPermission(
                RemoveFromTenantCommand.class), AssertMode.SatisfyAll,
                new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserNotParticipatingInAnyWorkflowAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new UserRole(), new CommandPermission(
                ConfirmPasswordResetCommand.class), AssertMode.SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new UserRole(), new CommandPermission(
                ConfirmRegistrationCommand.class), AssertMode.SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(), new UserIsEnabledAsserter());
        
        setRule(new UserRole(), new CommandPermission(
                ConfirmChangeEmailRequestCommand.class), AssertMode.SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new UserRole(), new CommandPermission(
                ConfirmInviationCommand.class), AssertMode.SatisfyAll,
                new UserIsInvitationReceiverAsserter());
        
        setRule(new UserRole(), new CommandPermission(
                RefuseInviationCommand.class), AssertMode.SatisfyAll,
                new UserIsInvitationReceiverAsserter(), new IsRoleEqualToAccessedUserAsserter());
        
        setRule(new UserRole(), new CommandPermission(
                GetUserWithProfileCommand.class), AssertMode.SatisfyAll,
                new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new BasicRole(null), new CommandPermission(
                GetHighestUserRoleCommand.class), AssertMode.SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new BasicRole(null), new CommandPermission(
                GetUserRoleForTenantCommand.class), AssertMode.SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new WorkflowAdminRole(), new CommandPermission(
                GetAdminstratedWorkflowInstancesCommand.class), AssertMode.SatisfyAll,
                new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new UserRole(), new CommandPermission(
                GetJoinedTenantsCommand.class), AssertMode.SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new TenantAdminRole(), new CommandPermission(
                GetAdministratedWorkflowModelCommand.class), AssertMode.SatisfyAll,
                new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new UserRole(), new CommandPermission(
                GetWorkitemsCommand.class), AssertMode.SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(), new UserOwnsWorkItemAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new UserRole(), new CommandPermission(
                GetInvitationCommand.class), AssertMode.SatisfyAll,
                new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new BasicRole(null), new CommandPermission(
                IsRegisteredCommand.class), AssertMode.SatisfyAll,
                alwaysTrueAsserter);
        
        setRule(new BasicRole(null), new CommandPermission(
                SetUserProfileCommand.class), AssertMode.SatisfyAll,
                new IsRoleEqualToAccessedUserAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        /**
         *  Command Permissions WorkflowInstanceFacade
         */
        setRule(new WorkflowAdminRole(), new CommandPermission(
                StopWorkflowInstanceCommand.class), AssertMode.SatisfyAll,
                new UserAdministratesWorkflowInstanceAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new WorkflowAdminRole(), new CommandPermission(
                GetParticipatingUsersCommand.class), AssertMode.SatisfyAll,
                new UserAdministratesWorkflowInstanceAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new WorkflowAdminRole(), new CommandPermission(
                StartWorkflowInstanceCommand.class), AssertMode.SatisfyAll,
                new UserAdministratesWorkflowInstanceAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(HumanTaskRole.getInstance(), new CommandPermission(
                RemoveAllWorkItemsCommand.class), AssertMode.SatisfyAll,
                new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new WorkflowAdminRole(), new CommandPermission(
                GetAllWorkitemsCommand.class), AssertMode.SatisfyAll,
                new UserAdministratesWorkflowInstanceAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        /**
         *  Command Permissions WorkflowModelFacade
         */
        setRule(new TenantAdminRole(), new CommandPermission(
                SaveWorkflowModelCommand.class), AssertMode.SatisfyAll,
                new UserOwnsWorkflowModelAsserter(), new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new TenantAdminRole(), new CommandPermission(
                GetWorkflowModelCommand.class), AssertMode.SatisfyAll,
                new UserOwnsWorkflowModelAsserter(), new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new TenantAdminRole(), new CommandPermission(
                PublishWorkflowModelsCommand.class), AssertMode.SatisfyAll,
                new UserOwnsWorkflowModelAsserter(), new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new TenantAdminRole(), new CommandPermission(
                MakeWorkflowModelExecutableCommand.class), AssertMode.SatisfyAll,
                new UserOwnsWorkflowModelAsserter(), new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new TenantAdminRole(), new CommandPermission(
                GetWorkflowAdminstratorsCommand.class), AssertMode.SatisfyAll,
                new UserOwnsWorkflowModelAsserter(), new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new TenantAdminRole(), new CommandPermission(
                SetWorkflowAdministratorsCommand.class), AssertMode.SatisfyAll,
                new UserOwnsWorkflowModelAsserter(), new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new TenantAdminRole(), new CommandPermission(
                DeleteWorkflowModelCommand.class), AssertMode.SatisfyAll,
                new UserOwnsWorkflowModelAsserter(), new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new TenantAdminRole(), new CommandPermission(
                StartWorkflowInstanceCommand.class), AssertMode.SatisfyAll,
                new UserAdministratesWorkflowModelAsserter(), new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new TenantAdminRole(), new CommandPermission(
                StopWorkflowInstanceCommand.class), AssertMode.SatisfyAll,
                new UserAdministratesWorkflowModelAsserter(), new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
    
        setRule(new TenantAdminRole(), new CommandPermission(
                SaveStartConfigurationCommand.class), AssertMode.SatisfyAll,
                new UserOwnsWorkflowModelAsserter(), new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new TenantAdminRole(), new CommandPermission(
                GetLastStartConfigurationCommand.class), AssertMode.SatisfyAll,
                new UserOwnsWorkflowModelAsserter(), new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new TenantAdminRole(), new CommandPermission(
                GetPublishedWorkflowModelsCommand.class), AssertMode.SatisfyAll,
                new UserIsTenantAdminAsserter(),new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        /**
         *  Command Permissions WorkitemFacade
         */
        
        setRule(new UserRole(), new CommandPermission(
                GetWorkItemCommand.class), AssertMode.SatisfyAll,
                new UserOwnsWorkItemAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(HumanTaskRole.getInstance(), new CommandPermission(
                CreateWorkItemCommand.class), AssertMode.SatisfyAll,
                new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new UserRole(), new CommandPermission(
                SetDataCommand.class), AssertMode.SatisfyAll,
                new UserOwnsWorkItemAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        setRule(new UserRole(), new CommandPermission(
                SetStatusCommand.class), AssertMode.SatisfyAll,
                new UserOwnsWorkItemAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());      
        
        setRule(new WorkflowAdminRole(), new CommandPermission(
                DeleteWorkItemCommand.class), AssertMode.SatisfyAll,
                new UserOwnsWorkItemAsserter(), new UserIsEnabledAsserter(),new UserIsLoggedInAsserter());
        
        
        /**
         * file permissions
         */
        
        // File Delete Permissions
        setRule(new BasicRole(null), new FileDeletePermission(null), AssertMode.SatisfyAll,
                new UserHasAccessToFileAsserter());
        
        // File Read Permissions
        setRule(new BasicRole(null), new FileReadPermission(null), AssertMode.SatisfyAll,
                new UserHasAccessToFileAsserter());
        
        // File Replace Permissions
        setRule(new BasicRole(null), new FileReplacePermission(null), AssertMode.SatisfyAll,
                new UserHasAccessToFileAsserter());
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
    public Boolean setRule(Role role, Permission permission,
            AssertMode mode, Asserter... asserters) {

        Boolean isAlreadyImplied = hasRule(role, permission);

        if (!isAlreadyImplied) {
            Key newKey = new Key(role, permission);
            removeImpliedRules(newKey);
            ruleMap.put(newKey, new Value(asserters, mode));
        }

        return !isAlreadyImplied;
    }

    /**
     * Removes the rules that key implies from the ruleset. Be aware that this
     * will include key itself if it is currently present in the ruleset.
     */
    private void removeImpliedRules(Key key) {
        for (Key impliedKey : ruleMap.keySet()) {
            if (!key.equals(impliedKey) && key.implies(impliedKey)) {
                ruleMap.remove(impliedKey);
            }
        }
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
        Value rule = ruleMap.get(new Key(role, permission));

        if (rule == null) {
            allowed = false;
            return allowed;
        }

        switch (rule.assertMode) {

        case SatisfyAll:

            for (Asserter a : rule.asserters) {
                if (!a.assertRule(role, permission)) {
                    allowed = false;
                    break;
                }
            }
            break;

        case SatisfyAny:
            allowed = false;
            for (Asserter a : rule.asserters) {
                if (a.assertRule(role, permission)) {
                    allowed = true;
                    break;
                }
            }
            break;

        default:
            allowed = false;
            break;
        }

        return allowed;
    }

    @Override
    public Boolean hasRule(Role role, Permission permission) {
        Key key = new Key(role, permission);

        // Try to find implying rule
        Boolean isAlreadyImplied = false;
        for (Key oldKey : ruleMap.keySet()) {
            if (oldKey.implies(key)) {
                isAlreadyImplied = true;
                break;
            }
        }

        return isAlreadyImplied;
    }

}