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

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.tenant.AddTenantMemberCommand;
import de.decidr.model.commands.tenant.ApproveTenantsCommand;
import de.decidr.model.commands.tenant.CreateTenantCommand;
import de.decidr.model.commands.tenant.CreateWorkflowModelCommand;
import de.decidr.model.commands.tenant.DeleteTenantCommand;
import de.decidr.model.commands.tenant.GetAllTenantsCommand;
import de.decidr.model.commands.tenant.GetCurrentColorSchemeCommand;
import de.decidr.model.commands.tenant.GetTenantIdCommand;
import de.decidr.model.commands.tenant.GetTenantLogoCommand;
import de.decidr.model.commands.tenant.GetTenantsToApproveCommand;
import de.decidr.model.commands.tenant.GetUsersOfTenantCommand;
import de.decidr.model.commands.tenant.GetWorkflowInstancesCommand;
import de.decidr.model.commands.tenant.GetWorkflowModelsCommand;
import de.decidr.model.commands.tenant.ImportPublishedWorkflowModelsCommand;
import de.decidr.model.commands.tenant.SetAdvancedColorSchemeCommand;
import de.decidr.model.commands.tenant.SetCurrentColorSchemeCommand;
import de.decidr.model.commands.tenant.SetSimpleColorSchemeCommand;
import de.decidr.model.commands.tenant.SetTenantDescriptionCommand;
import de.decidr.model.commands.tenant.SetTenantLogoCommand;
import de.decidr.model.commands.tenant.TenantCommand;
import de.decidr.model.filters.Paginator;

/**
 * This class tests the commands in <code>de.decidr.model.commands.tenant</code>
 * . Some of the methods can't be tested properly within the confines of a unit
 * test, as they interact with web services.
 * 
 * @author Reinhold
 */
public class TenantCommandsTest {

    @BeforeClass
    public static void disable() {
        fail("This test class has not yet been implemented");
    }

    /**
     * Test method for
     * {@link AddTenantMemberCommand#AddTenantMemberCommand(Role, Long, Long)}.
     */
    @Test
    public void testAddTenantMemberCommand() {
        fail("Not yet implemented"); // TODO AddTenantMemberCommand
    }

    /**
     * Test method for
     * {@link ApproveTenantsCommand#ApproveTenantsCommand(Role, List)}.
     */
    @Test
    public void testApproveTenantsCommand() {
        fail("Not yet implemented"); // TODO ApproveTenantsCommand
    }

    /**
     * Test method for
     * {@link CreateTenantCommand#CreateTenantCommand(Role, String, String, Long)}
     * .
     */
    @Test
    public void testCreateTenantCommand() {
        fail("Not yet implemented"); // TODO CreateTenantCommand
    }

    /**
     * Test method for
     * {@link CreateWorkflowModelCommand#CreateWorkflowModelCommand(Role, Long, String)}
     * .
     */
    @Test
    public void testCreateWorkflowModelCommand() {
        fail("Not yet implemented"); // TODO CreateWorkflowModelCommand
    }

    /**
     * Test method for
     * {@link DeleteTenantCommand#DeleteTenantCommand(Role, Long)}.
     */
    @Test
    public void testDeleteTenantCommand() {
        fail("Not yet implemented"); // TODO DeleteTenantCommand
    }

    /**
     * Test method for
     * {@link GetAllTenantsCommand#GetAllTenantsCommand(Role, List, Paginator)}.
     */
    @Test
    public void testGetAllTenantsCommand() {
        fail("Not yet implemented"); // TODO GetAllTenantsCommand
    }

    /**
     * Test method for
     * {@link GetCurrentColorSchemeCommand#GetCurrentColorSchemeCommand(Role, Long)}
     * .
     */
    @Test
    public void testGetCurrentColorSchemeCommand() {
        fail("Not yet implemented"); // TODO GetCurrentColorSchemeCommand
    }

    /**
     * Test method for
     * {@link GetTenantIdCommand#GetTenantIdCommand(Role, String)}.
     */
    @Test
    public void testGetTenantIdCommand() {
        fail("Not yet implemented"); // TODO GetTenantIdCommand
    }

    /**
     * Test method for
     * {@link GetTenantLogoCommand#GetTenantLogoCommand(Role, Long)}.
     */
    @Test
    public void testGetTenantLogoCommand() {
        fail("Not yet implemented"); // TODO GetTenantLogoCommand
    }

    /**
     * Test method for
     * {@link GetTenantsToApproveCommand#GetTenantsToApproveCommand(Role, List, Paginator)}
     * .
     */
    @Test
    public void testGetTenantsToApproveCommand() {
        fail("Not yet implemented"); // TODO GetTenantsToApproveCommand
    }

    /**
     * Test method for
     * {@link GetUsersOfTenantCommand#GetUsersOfTenantCommand(Role, Long, Paginator)}
     * .
     */
    @Test
    public void testGetUsersOfTenantCommand() {
        fail("Not yet implemented"); // TODO GetUsersOfTenantCommand
    }

    /**
     * Test method for
     * {@link GetWorkflowInstancesCommand#GetWorkflowInstancesCommand(Role, Long, Paginator)}
     * .
     */
    @Test
    public void testGetWorkflowInstancesCommand() {
        fail("Not yet implemented"); // TODO GetWorkflowInstancesCommand
    }

    /**
     * Test method for
     * {@link GetWorkflowModelsCommand#GetWorkflowModelsCommand(Role, Long, List, Paginator)}
     * .
     */
    @Test
    public void testGetWorkflowModelsCommand() {
        fail("Not yet implemented"); // TODO GetWorkflowModelsCommand
    }

    /**
     * Test method for
     * {@link ImportPublishedWorkflowModelsCommand#ImportPublishedWorkflowModelsCommand(Role, Long, List)}
     * .
     */
    @Test
    public void testImportPublishedWorkflowModelsCommand() {
        fail("Not yet implemented"); // TODO ImportPublishedWorkflowModelsCommand
    }

    /**
     * Test method for
     * {@link SetAdvancedColorSchemeCommand#SetAdvancedColorSchemeCommand(Role, Long, FileInputStream, String, String)}
     * .
     */
    @Test
    public void testSetAdvancedColorSchemeCommand() {
        fail("Not yet implemented"); // TODO SetAdvancedColorSchemeCommand
    }

    /**
     * Test method for
     * {@link SetCurrentColorSchemeCommand#SetCurrentColorSchemeCommand(Role, Long, FileInputStream, String, String)}
     * .
     */
    @Test
    public void testSetCurrentColorSchemeCommand() {
        fail("Not yet implemented"); // TODO SetCurrentColorSchemeCommand
    }

    /**
     * Test method for
     * {@link SetSimpleColorSchemeCommand#SetSimpleColorSchemeCommand(Role, Long, FileInputStream, String, String)}
     * .
     */
    @Test
    public void testSetSimpleColorSchemeCommand() {
        fail("Not yet implemented"); // TODO SetSimpleColorSchemeCommand
    }

    /**
     * Test method for
     * {@link SetTenantDescriptionCommand#SetTenantDescriptionCommand(Role, Long, String)}
     * .
     */
    @Test
    public void testSetTenantDescriptionCommand() {
        fail("Not yet implemented"); // TODO SetTenantDescriptionCommand
    }

    /**
     * Test method for
     * {@link SetTenantLogoCommand#SetTenantLogoCommand(Role, Long, FileInputStream, String, String)}
     * .
     */
    @Test
    public void testSetTenantLogoCommand() {
        fail("Not yet implemented"); // TODO SetTenantLogoCommand
    }

    /**
     * Test method for {@link TenantCommand#TenantCommand(Role, Long)}.
     */
    @Test
    public void testTenantCommand() {
        fail("Not yet implemented"); // TODO TenantCommand
    }
}
