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

package de.decidr.model.testsuites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.decidr.model.acl.PasswordTest;
import de.decidr.model.acl.permissions.CommandPermissionTest;
import de.decidr.model.acl.permissions.FileDeletePermissionTest;
import de.decidr.model.acl.permissions.FileReadPermissionTest;
import de.decidr.model.acl.permissions.FileReplacePermissionTest;
import de.decidr.model.acl.permissions.PermissionTest;
import de.decidr.model.acl.roles.BasicRoleTest;
import de.decidr.model.acl.roles.EmailRoleTest;
import de.decidr.model.acl.roles.HumanTaskRoleTest;
import de.decidr.model.acl.roles.ServerLoadUpdaterRoleTest;
import de.decidr.model.acl.roles.TenantAdminRoleTest;
import de.decidr.model.acl.roles.UserRoleTest;
import de.decidr.model.acl.roles.WorkflowAdminRoleTest;
import de.decidr.model.testing.GlobalPreconditionsSuite;

@RunWith(Suite.class)
@SuiteClasses( { PasswordTest.class, PermissionTest.class,
        CommandPermissionTest.class, FileDeletePermissionTest.class,
        FileReadPermissionTest.class, FileReplacePermissionTest.class,
        BasicRoleTest.class, EmailRoleTest.class, HumanTaskRoleTest.class,
        ServerLoadUpdaterRoleTest.class, TenantAdminRoleTest.class,
        UserRoleTest.class, WorkflowAdminRoleTest.class,
// UserOwnsWorkItemAsserterTest.class,
// UserOwnsWorkflowModelAsserterTest.class
})
public class AclTestSuite extends GlobalPreconditionsSuite {
    private static boolean inSuite;

    /**
     * @return - <code>true</code>, if a {@link OtherTestsSuite} is currently
     *         running<br>
     *         - <code>false</code> if not
     */
    public static final boolean isInSuite() {
        return inSuite;
    }

    @AfterClass
    public static void deactivateSuite() {
        inSuite = false;
    }

    @BeforeClass
    public static void activateSuite() {
        inSuite = true;
    }
}
