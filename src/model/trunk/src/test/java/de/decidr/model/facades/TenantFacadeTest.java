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

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.vaadin.data.Item;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.UserRole;
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

    static long getInvalidLogoID() {
        long invalidID = Long.MIN_VALUE;

        for (long l = invalidID; session.createQuery(
                "FROM File WHERE id = :given").setLong("given", l)
                .uniqueResult() != null; l++)
            invalidID = l + 1;
        return invalidID;
    }

    Long testTenantID;
    private Long invalidTenantID;

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
    }

    @After
    public void deleteDefaultTenant() {
        invalidTenantID = testTenantID;

        if (testTenantID != null) {
            try {
                adminFacade.deleteTenant(testTenantID);
            } catch (TransactionException e) {
                // doesn't matter as long as it's gone
            }
        }
    }

    /**
     * Test method for {@link TenantFacade#addTenantMember(Long, Long)}.
     */
    @Test
    public void testAddTenantMember() throws TransactionException {
        assertEquals(2, adminFacade.getUsersOfTenant(testTenantID, null).size());
        fail("Not yet implemented"); // RR addTenantMember
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
     * Test method for {@link TenantFacade#getAllTenants(List, Paginator)}.
     */
    @Test
    public void testGetAllTenants() {
        fail("Not yet implemented"); // RR getAllTenants
    }

    /**
     * Test method for {@link TenantFacade#getTenantId(String)}.
     */
    @Test
    public void testGetTenantId() throws TransactionException {
        assertEquals(testTenantID, adminFacade.getTenantId(TEST_NAME));
        assertEquals(testTenantID, userFacade.getTenantId(TEST_NAME));

        fail("Not yet implemented"); // RR getTenantId
    }

    /**
     * Test method for {@link TenantFacade#getUsersOfTenant(Long, Paginator)}.
     */
    @Test
    public void testGetUsersOfTenant() throws TransactionException {
        TenantFacade userFacade = new TenantFacade(new UserRole(testAdminID));

        try {
            nullFacade.getUsersOfTenant(testTenantID, null);
            fail("managed to get users of tenant using null facade");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.getUsersOfTenant(null, null);
            fail("managed to get users of tenant using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            userFacade.getUsersOfTenant(null, null);
            fail("managed to get users of tenant using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        assertEquals(1, adminFacade.getUsersOfTenant(testTenantID, null).size());
        assertEquals(1, userFacade.getUsersOfTenant(testTenantID, null).size());

        Item user = adminFacade.getUsersOfTenant(testTenantID, null).get(0);
        assertNotNull(user.getItemProperty("username").getValue());
        assertNotNull(user.getItemProperty("id").getValue());
        assertNotNull(user.getItemProperty("email").getValue());
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
     * Test method for {@link TenantFacade#setLogo(Long, Long)} and
     * {@link TenantFacade#getLogo(Long)}.
     */
    @Test
    public void testLogo() throws TransactionException, IOException {
        TenantFacade userFacade = new TenantFacade(new UserRole(testAdminID));
        FileFacade fileFacade = new FileFacade(new SuperAdminRole(testAdminID));
        InputStream logoStream = TenantFacadeTest.class
                .getResourceAsStream("/decidr.jpg");
        Long logoID = fileFacade.createFile(logoStream, "decidr.jpg",
                "image/jpeg", false);
        Long invalidLogoID = getInvalidLogoID();

        try {
            nullFacade.setLogo(testTenantID, logoID);
            fail("managed to set tenant logo using null facade");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            nullFacade.getLogo(testTenantID);
            fail("managed to get tenant logo using null facade");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        try {
            adminFacade.setLogo(null, logoID);
            fail("managed to set tenant logo using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.setLogo(testTenantID, null);
            fail("managed to set tenant logo using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.setLogo(invalidTenantID, logoID);
            fail("managed to set tenant logo using invalid tenant ID");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.setLogo(testTenantID, invalidLogoID);
            fail("managed to set tenant logo using invalid logo ID");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.getLogo(null);
            fail("managed to get tenant logo using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        try {
            userFacade.setLogo(null, logoID);
            fail("managed to set tenant logo using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            userFacade.setLogo(testTenantID, null);
            fail("managed to set tenant logo using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            userFacade.setLogo(invalidTenantID, logoID);
            fail("managed to set tenant logo using invalid tenant ID");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            userFacade.setLogo(testTenantID, invalidLogoID);
            fail("managed to set tenant logo using invalid logo ID");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            userFacade.getLogo(null);
            fail("managed to get tenant logo using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        adminFacade.setLogo(testTenantID, logoID);
        long fileSizeBytes = fileFacade.getFileInfo(logoID).getFileSizeBytes();
        byte[] streamLogo = new byte[(int) fileSizeBytes];
        byte[] tenantLogo = new byte[(int) fileSizeBytes];
        assertEquals(fileSizeBytes, logoStream.read(streamLogo));
        assertEquals(fileSizeBytes, adminFacade.getLogo(logoID)
                .read(tenantLogo));
        assertTrue(Arrays.equals(streamLogo, tenantLogo));

        assertEquals(fileSizeBytes, userFacade.getLogo(logoID).read(tenantLogo));
        assertTrue(Arrays.equals(streamLogo, tenantLogo));

        fileFacade.deleteFile(logoID);
    }

    /**
     * Test method for {@link TenantFacade#setDescription(Long, String)}.
     */
    @Test
    public void testSetDescription() throws TransactionException {
        TenantFacade userFacade = new TenantFacade(new UserRole(testAdminID));

        try {
            nullFacade
                    .setDescription(testTenantID, TEST_DESC + " (testtttttt)");
            fail("managed to change description of tenant using null facade");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.setDescription(null, TEST_DESC + " (testtttttt)");
            fail("managed to change description of tenant using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.setDescription(testTenantID, null);
            fail("managed to change description of tenant using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.setDescription(invalidTenantID, TEST_DESC
                    + " (testtttttt)");
            fail("managed to change description of tenant using invalid tenant ID");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            userFacade.setDescription(null, TEST_DESC + " (testtttttt)");
            fail("managed to change description of tenant using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            userFacade.setDescription(testTenantID, null);
            fail("managed to change description of tenant using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            userFacade.setDescription(invalidTenantID, TEST_DESC
                    + " (testtttttt)");
            fail("managed to change description of tenant using invalid tenant ID");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        adminFacade.setDescription(testTenantID, TEST_DESC + " (testtttttt)");
        userFacade.setDescription(testTenantID, TEST_DESC + " (testtttttt)");
    }

    /**
     * Test method for {@link TenantFacade#createTenant(String, String, Long)}
     * and {@link TenantFacade#deleteTenant(Long)}.
     */
    @Test
    public void testTenant() throws TransactionException {
        try {
            nullFacade.createTenant(TEST_NAME + "foo", TEST_DESC, testAdminID);
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

        try {
            adminFacade.createTenant(null, TEST_DESC, testAdminID);
            fail("Managed to create tenant using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.createTenant(TEST_NAME + "foo", null, testAdminID);
            fail("Managed to create tenant using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.createTenant(TEST_NAME + "foo", TEST_DESC, null);
            fail("Managed to create tenant using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.createTenant(TEST_NAME + "foo", TEST_DESC,
                    UserFacadeTest.getInvalidUserID());
            fail("Managed to create tenant using invalid user id");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.createTenant(TEST_NAME, TEST_DESC, testAdminID);
            fail("Managed to create tenant twice");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        try {
            userFacade.createTenant(null, TEST_DESC, testAdminID);
            fail("Managed to create tenant using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            userFacade.createTenant(TEST_NAME + "foo", null, testAdminID);
            fail("Managed to create tenant using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            userFacade.createTenant(TEST_NAME + "foo", TEST_DESC, null);
            fail("Managed to create tenant using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        try {
            adminFacade.deleteTenant(null);
            fail("Managed to delete tenant using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            userFacade.deleteTenant(null);
            fail("Managed to delete tenant using null parameter");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        Long secondTenantID = adminFacade.createTenant(TEST_NAME + "blah",
                TEST_DESC, testAdminID);
        assertNotNull(secondTenantID);
        adminFacade.deleteTenant(secondTenantID);
        adminFacade.deleteTenant(secondTenantID);
        new TenantFacade(new UserRole(testAdminID))
                .deleteTenant(secondTenantID);
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
}
