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

package de.decidr.model.facades;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.filters.Paginator;

/**
 * Test case for <code>{@link TenantFacade}</code>.
 * 
 * @author Reinhold
 */
public class TenantFacadeTest {

    static TenantFacade adminFacade;
    static TenantFacade userFacade;
    static TenantFacade nullFacade;

    /**
     * Initialises the facade instances.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        adminFacade = new TenantFacade(new SuperAdminRole());
        userFacade = new TenantFacade(new BasicRole(0L));
        nullFacade = new TenantFacade(null);
    }

    /**
     * Test method for {@link TenantFacade#createTenant(String, String, Long)}.
     */
    @Test
    public void testCreateTenant() {
        fail("Not yet implemented"); // RR createTenant
    }

    /**
     * Test method for {@link TenantFacade#setDescription(Long, String)}.
     */
    @Test
    public void testSetDescription() {
        fail("Not yet implemented"); // RR setDescription
    }

    /**
     * Test method for
     * {@link TenantFacade#setLogo(Long, FileInputStream, String, String)} and
     * {@link TenantFacade#getLogo(Long)}.
     */
    @Test
    public void testLogo() {
        fail("Not yet implemented"); // RR setLogo
        fail("Not yet implemented"); // RR getLogo
    }

    /**
     * Test method for
     * {@link TenantFacade#setSimpleColorScheme(Long, FileInputStream, String, String)}
     * .
     */
    @Test
    public void testSetSimpleColorScheme() {
        fail("Not yet implemented"); // RR setSimpleColorScheme
    }

    /**
     * Test method for
     * {@link TenantFacade#setAdvancedColorScheme(FileInputStream, Long, String, String)}
     * .
     */
    @Test
    public void testSetAdvancedColorScheme() {
        fail("Not yet implemented"); // RR setAdvancedColorScheme
    }

    /**
     * Test method for
     * {@link TenantFacade#setCurrentColorScheme(FileInputStream, Long, String, String)}
     * and {@link TenantFacade#getCurrentColorScheme(Long)}.
     */
    @Test
    public void testCurrentColorScheme() {
        fail("Not yet implemented"); // RR setCurrentColorScheme
        fail("Not yet implemented"); // RR getCurrentColorScheme
    }

    /**
     * Test method for {@link TenantFacade#addTenantMember(Long, Long)}.
     */
    @Test
    public void testAddTenantMember() {
        fail("Not yet implemented"); // RR addTenantMember
    }

    /**
     * Test method for {@link TenantFacade#createWorkflowModel(Long, String)}.
     */
    @Test
    public void testCreateWorkflowModel() {
        fail("Not yet implemented"); // RR createWorkflowModel
    }

    /**
     * Test method for {@link TenantFacade#removeWorkflowModel(Long)}.
     */
    @Test
    public void testRemoveWorkflowModel() {
        fail("Not yet implemented"); // RR removeWorkflowModel
    }

    /**
     * Test method for {@link TenantFacade#approveTenants(List)}.
     */
    @Test
    public void testApproveTenants() {
        fail("Not yet implemented"); // RR approveTenants
    }

    /**
     * Test method for {@link TenantFacade#disapproveTenants(List)}.
     */
    @Test
    public void testDisapproveTenants() {
        fail("Not yet implemented"); // RR disapproveTenants
    }

    /**
     * Test method for {@link TenantFacade#deleteTenant(Long)}.
     */
    @Test
    public void testDeleteTenant() {
        fail("Not yet implemented"); // RR deleteTenant
    }

    /**
     * Test method for {@link TenantFacade#getTenantId(String)}.
     */
    @Test
    public void testGetTenantId() {
        fail("Not yet implemented"); // RR getTenantId
    }

    /**
     * Test method for {@link TenantFacade#getUsersOfTenant(Long, Paginator)}.
     */
    @Test
    public void testGetUsersOfTenant() {
        fail("Not yet implemented"); // RR getUsersOfTenant
    }

    /**
     * Test method for
     * {@link TenantFacade#getWorkflowInstances(Long, Paginator)}.
     */
    @Test
    public void testGetWorkflowInstances() {
        fail("Not yet implemented"); // RR getWorkflowInstances
    }

    /**
     * Test method for {@link TenantFacade#getTenantsToApprove(List, Paginator)}
     * .
     */
    @Test
    public void testGetTenantsToApprove() {
        fail("Not yet implemented"); // RR getTenantsToApprove
    }

    /**
     * Test method for {@link TenantFacade#getAllTenants(List, Paginator)}.
     */
    @Test
    public void testGetAllTenants() {
        fail("Not yet implemented"); // RR getAllTenants
    }

    /**
     * Test method for
     * {@link TenantFacade#getWorkflowModels(Long, List, Paginator)}.
     */
    @Test
    public void testGetWorkflowModels() {
        fail("Not yet implemented"); // RR getWorkflowModels
    }

    /**
     * Test method for
     * {@link TenantFacade#inviteUsersAsMembers(Long, List, List)}.
     */
    @Test
    public void testInviteUsersAsMembers() {
        fail("Not yet implemented"); // RR inviteUsersAsMembers
    }

    /**
     * Test method for
     * {@link TenantFacade#importPublishedWorkflowModels(Long, List)}.
     */
    @Test
    public void testImportPublishedWorkflowModels() {
        fail("Not yet implemented"); // RR importPublishedWorkflowModels
    }
}
