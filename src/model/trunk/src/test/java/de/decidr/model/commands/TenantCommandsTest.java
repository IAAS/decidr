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

}
