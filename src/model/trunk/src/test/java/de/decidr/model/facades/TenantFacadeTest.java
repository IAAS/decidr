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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Paginator;
import de.decidr.model.testing.LowLevelDatabaseTest;

/**
 * Test case for <code>{@link TenantFacade}</code>. Some of the methods can't be
 * tested easily within the confines of a unit test, as they interact with web
 * services. These methods will be tested at a later point in time when the most
 * important test cases are written or during the system test.
 * 
 * @author Reinhold
 */
public class TenantFacadeTest extends LowLevelDatabaseTest {

    private static final String TEST_NAME = "Doofus";
    private static final String TEST_DESC = "Доофус";
    static TenantFacade adminFacade;
    static TenantFacade userFacade;
    static TenantFacade nullFacade;

    Long testTenantID;
    private static Long testAdminID;

    /**
     * Initialises the facade instances.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws TransactionException {
        adminFacade = new TenantFacade(new SuperAdminRole(DecidrGlobals
                .getSettings().getSuperAdmin().getId()));
        userFacade = new TenantFacade(new BasicRole(0L));
        nullFacade = new TenantFacade(null);

        testAdminID = DecidrGlobals.getSettings().getSuperAdmin().getId();

        try {
            adminFacade.deleteTenant(((Tenant) session.createQuery(
                    "FROM Tenant WHERE name = '" + TEST_NAME + "'")
                    .uniqueResult()).getId());
        } catch (NullPointerException e) {
            // doesn't matter
        }
    }

    @Before
    public void createDefaultTenant() throws TransactionException {
        testTenantID = adminFacade.createTenant(TEST_NAME, TEST_DESC,
                testAdminID);
        assertNotNull(testTenantID);
        System.out.println("findme " + testTenantID);
    }

    /**
     * Test method for {@link TenantFacade#createTenant(String, String, Long)}
     * and {@link TenantFacade#deleteTenant(Long)}.
     */
    @Test
    public void testTenant() throws TransactionException {
        try {
            nullFacade.createTenant(TEST_NAME, TEST_DESC, testAdminID);
            fail("Managed to create tenant using null facade");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            nullFacade.deleteTenant(testTenantID);
            fail("Managed to delete tenant using null facade");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

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
     * Test method for {@link TenantFacade#setLogo(Long, Long)} and
     * {@link TenantFacade#getLogo(Long)}.
     */
    @Test
    public void testLogo() {
        fail("Not yet implemented"); // RR setLogo
        fail("Not yet implemented"); // RR getLogo
    }

    /**
     * Test method for {@link TenantFacade#setSimpleColorScheme(Long, Long)},
     * {@link TenantFacade#setAdvancedColorScheme(Long, Long)},
     * {@link TenantFacade#setCurrentColorScheme(Long, Boolean)} and
     * {@link TenantFacade#getCurrentColorScheme(Long)}.
     */
    @Test
    public void testColorScheme() {
        fail("Not yet implemented"); // RR setSimpleColorScheme
        fail("Not yet implemented"); // RR setAdvancedColorScheme
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
     * Test method for {@link TenantFacade#createWorkflowModel(Long, String)},
     * {@link TenantFacade#getWorkflowModels(Long, List, Paginator)},
     * {@link TenantFacade#importPublishedWorkflowModels(Long, List)} and
     * {@link TenantFacade#removeWorkflowModel(Long)}.
     */
    @Test
    public void testWorkflowModel() {
        fail("Not yet implemented"); // RR createWorkflowModel
        fail("Not yet implemented"); // RR getWorkflowModels
        fail("Not yet implemented"); // RR importPublishedWorkflowModels
        fail("Not yet implemented"); // RR removeWorkflowModel
    }

    /**
     * Test method for {@link TenantFacade#getTenantsToApprove(List, Paginator)}
     * and {@link TenantFacade#approveTenants(List)}.
     */
    @Test
    public void testApproveTenants() {
        fail("Not yet implemented"); // RR getTenantsToApprove
        fail("Not yet implemented"); // RR approveTenants
    }

    @After
    public void deleteDefaultTenant() throws TransactionException {
        if (testTenantID != null) {
            adminFacade.deleteTenant(testTenantID);
        }
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
     * Test method for {@link TenantFacade#getAllTenants(List, Paginator)}.
     */
    @Test
    public void testGetAllTenants() {
        fail("Not yet implemented"); // RR getAllTenants
    }
}
