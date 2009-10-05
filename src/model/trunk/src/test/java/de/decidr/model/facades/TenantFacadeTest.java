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

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.filters.Paginator;
import de.decidr.model.testsuites.DatabaseTestSuite;

/**
 * Test case for <code>{@link TenantFacade}</code>. Some of the methods can't be
 * tested easily within the confines of a unit test, as they interact with web
 * services. These methods will be tested at a later point in time when the most
 * important test cases are written or during the system test.
 * 
 * @author Reinhold
 */
public class TenantFacadeTest {

    static TenantFacade adminFacade;
    static TenantFacade userFacade;
    static TenantFacade nullFacade;

    @BeforeClass
    public static void disable() {
        fail("This test class has not yet been implemented");
    }

    /**
     * Initialises the facade instances.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        if (!DatabaseTestSuite.running()) {
            fail("Needs to run inside " + DatabaseTestSuite.class.getName());
        }

        adminFacade = new TenantFacade(new SuperAdminRole(DecidrGlobals
                .getSettings().getSuperAdmin().getId()));
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
     * {@link TenantFacade#setLogo(Long, Long)} and
     * {@link TenantFacade#getLogo(Long)}.
     */
    @Test
    public void testLogo() {
        fail("Not yet implemented"); // RR setLogo
        fail("Not yet implemented"); // RR getLogo
    }

    /**
     * Test method for
     * {@link TenantFacade#setSimpleColorScheme(Long, Long)}
     * .
     */
    @Test
    public void testSetSimpleColorScheme() {
        fail("Not yet implemented"); // RR setSimpleColorScheme
    }

    /**
     * Test method for
     * {@link TenantFacade#setAdvancedColorScheme(Long, Long)}
     * .
     */
    @Test
    public void testSetAdvancedColorScheme() {
        fail("Not yet implemented"); // RR setAdvancedColorScheme
    }

    /**
     * Test method for
     * {@link TenantFacade#setCurrentColorScheme(Long, Boolean)}
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
     * {@link TenantFacade#importPublishedWorkflowModels(Long, List)}.
     */
    @Test
    public void testImportPublishedWorkflowModels() {
        fail("Not yet implemented"); // RR importPublishedWorkflowModels
    }
}
