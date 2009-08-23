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

package de.decidr.model.commands;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.util.List;

import org.junit.Test;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.tenant.AddTenantMemberCommand;
import de.decidr.model.commands.tenant.ApproveTenantsCommand;
import de.decidr.model.commands.tenant.CreateTenantCommand;
import de.decidr.model.commands.tenant.CreateWorkflowModelCommand;
import de.decidr.model.commands.tenant.DeleteTenantCommand;
import de.decidr.model.commands.tenant.DisapproveTenantsCommand;
import de.decidr.model.commands.tenant.GetAllTenantsCommand;
import de.decidr.model.commands.tenant.GetCurrentColorSchemeCommand;
import de.decidr.model.commands.tenant.GetTenantIdCommand;
import de.decidr.model.commands.tenant.GetTenantLogoCommand;
import de.decidr.model.commands.tenant.GetTenantsToApproveCommand;
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
import de.decidr.model.commands.tenant.TenantCommand;
import de.decidr.model.filters.Paginator;

/**
 * RR: add comment
 * 
 * @author Reinhold
 */
public class TenantCommandsTest {

    /**
     * Test method for
     * {@link AddTenantMemberCommand#AddTenantMemberCommand(Role, Long, Long)}.
     */
    @Test
    public void testAddTenantMemberCommand() {
        fail("Not yet implemented"); // RR AddTenantMemberCommand
    }

    /**
     * Test method for
     * {@link ApproveTenantsCommand#ApproveTenantsCommand(Role, List)}.
     */
    @Test
    public void testApproveTenantsCommand() {
        fail("Not yet implemented"); // RR ApproveTenantsCommand
    }

    /**
     * Test method for
     * {@link CreateTenantCommand#CreateTenantCommand(Role, String, String, Long)}
     * .
     */
    @Test
    public void testCreateTenantCommand() {
        fail("Not yet implemented"); // RR CreateTenantCommand
    }

    /**
     * Test method for
     * {@link CreateWorkflowModelCommand#CreateWorkflowModelCommand(Role, Long, String)}
     * .
     */
    @Test
    public void testCreateWorkflowModelCommand() {
        fail("Not yet implemented"); // RR CreateWorkflowModelCommand
    }

    /**
     * Test method for
     * {@link DeleteTenantCommand#DeleteTenantCommand(Role, Long)}.
     */
    @Test
    public void testDeleteTenantCommand() {
        fail("Not yet implemented"); // RR DeleteTenantCommand
    }

    /**
     * Test method for
     * {@link DisapproveTenantsCommand#DisapproveTenantsCommand(Role, List)}.
     */
    @Test
    public void testDisapproveTenantsCommand() {
        fail("Not yet implemented"); // RR DisapproveTenantsCommand
    }

    /**
     * Test method for
     * {@link GetAllTenantsCommand#GetAllTenantsCommand(Role, List, Paginator)}.
     */
    @Test
    public void testGetAllTenantsCommand() {
        fail("Not yet implemented"); // RR GetAllTenantsCommand
    }

    /**
     * Test method for
     * {@link GetCurrentColorSchemeCommand#GetCurrentColorSchemeCommand(Role, Long)}
     * .
     */
    @Test
    public void testGetCurrentColorSchemeCommand() {
        fail("Not yet implemented"); // RR GetCurrentColorSchemeCommand
    }

    /**
     * Test method for
     * {@link GetTenantIdCommand#GetTenantIdCommand(Role, String)}.
     */
    @Test
    public void testGetTenantIdCommand() {
        fail("Not yet implemented"); // RR GetTenantIdCommand
    }

    /**
     * Test method for
     * {@link GetTenantLogoCommand#GetTenantLogoCommand(Role, Long)}.
     */
    @Test
    public void testGetTenantLogoCommand() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for
     * {@link GetTenantsToApproveCommand#GetTenantsToApproveCommand(Role, List, Paginator)}
     * .
     */
    @Test
    public void testGetTenantsToApproveCommand() {
        fail("Not yet implemented"); // RR GetTenantsToApproveCommand
    }

    /**
     * Test method for
     * {@link GetUsersOfTenantCommand#GetUsersOfTenantCommand(Role, Long, Paginator)}
     * .
     */
    @Test
    public void testGetUsersOfTenantCommand() {
        fail("Not yet implemented"); // RR GetUsersOfTenantCommand
    }

    /**
     * Test method for
     * {@link GetWorkflowInstancesCommand#GetWorkflowInstancesCommand(Role, Long, Paginator)}
     * .
     */
    @Test
    public void testGetWorkflowInstancesCommand() {
        fail("Not yet implemented"); // RR GetWorkflowInstancesCommand
    }

    /**
     * Test method for
     * {@link GetWorkflowModelsCommand#GetWorkflowModelsCommand(Role, Long, List, Paginator)}
     * .
     */
    @Test
    public void testGetWorkflowModelsCommand() {
        fail("Not yet implemented"); // RR GetWorkflowModelsCommand
    }

    /**
     * Test method for
     * {@link ImportPublishedWorkflowModelsCommand#ImportPublishedWorkflowModelsCommand(Role, Long, List)}
     * .
     */
    @Test
    public void testImportPublishedWorkflowModelsCommand() {
        fail("Not yet implemented"); // RR ImportPublishedWorkflowModelsCommand
    }

    /**
     * Test method for
     * {@link InviteUsersAsTenantMembersCommand#InviteUsersAsTenantMembersCommand(Role, Long, List, List)}
     * .
     */
    @Test
    public void testInviteUsersAsTenantMembersCommand() {
        fail("Not yet implemented"); // RR InviteUsersAsTenantMembersCommand
    }

    /**
     * Test method for
     * {@link RemoveWorkflowModelCommand#RemoveWorkflowModelCommand(Role, Long)}
     * .
     */
    @Test
    public void testRemoveWorkflowModelCommand() {
        fail("Not yet implemented"); // RR RemoveWorkflowModelCommand
    }

    /**
     * Test method for
     * {@link SetAdvancedColorSchemeCommand#SetAdvancedColorSchemeCommand(Role, Long, FileInputStream, String, String)}
     * .
     */
    @Test
    public void testSetAdvancedColorSchemeCommand() {
        fail("Not yet implemented"); // RR SetAdvancedColorSchemeCommand
    }

    /**
     * Test method for
     * {@link SetCurrentColorSchemeCommand#SetCurrentColorSchemeCommand(Role, Long, FileInputStream, String, String)}
     * .
     */
    @Test
    public void testSetCurrentColorSchemeCommand() {
        fail("Not yet implemented"); // RR SetCurrentColorSchemeCommand
    }

    /**
     * Test method for
     * {@link SetSimpleColorSchemeCommand#SetSimpleColorSchemeCommand(Role, Long, FileInputStream, String, String)}
     * .
     */
    @Test
    public void testSetSimpleColorSchemeCommand() {
        fail("Not yet implemented"); // RR SetSimpleColorSchemeCommand
    }

    /**
     * Test method for
     * {@link SetTenantDescriptionCommand#SetTenantDescriptionCommand(Role, Long, String)}
     * .
     */
    @Test
    public void testSetTenantDescriptionCommand() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for
     * {@link SetTenantLogoCommand#SetTenantLogoCommand(Role, Long, FileInputStream, String, String)}
     * .
     */
    @Test
    public void testSetTenantLogoCommand() {
        fail("Not yet implemented"); // RR SetTenantLogoCommand
    }

    /**
     * Test method for {@link TenantCommand#TenantCommand(Role, Long)}.
     */
    @Test
    public void testTenantCommand() {
        fail("Not yet implemented"); // RR TenantCommand
    }
}
