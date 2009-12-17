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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.permissions.FileDeletePermission;
import de.decidr.model.acl.permissions.FilePermission;
import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.acl.permissions.FileReplacePermission;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.acl.roles.TenantAdminRole;
import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.acl.roles.WorkflowAdminRole;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.TenantSummaryView;
import de.decidr.model.entities.TenantWithAdminView;
import de.decidr.model.entities.User;
import de.decidr.model.entities.WorkflowModel;
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

    private Long testTenantID;
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
        new UserFacade(new SuperAdminRole(DecidrGlobals.getSettings()
                .getSuperAdmin().getId())).setDisabledSince(testAdminID, null);

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

        if (invalidTenantID == null) {
            long invalidID = Long.MIN_VALUE;

            for (long l = invalidID; session.createQuery(
                    "FROM User WHERE id = :given").setLong("given", l)
                    .uniqueResult() != null; l++)
                invalidID = l + 1;

            invalidTenantID = invalidID;
        }
        assertNotNull(invalidTenantID);
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
        // UserFacade adminUserFacade = new UserFacade(new SuperAdminRole(
        // DecidrGlobals.getSettings().getSuperAdmin().getId()));
        UserFacadeTest.deleteTestUsers();

        // UserProfile userProfile = new UserProfile();
        // userProfile.setUsername("testname");
        // Long secondUserID = adminUserFacade.registerUser(UserFacadeTest
        // .getTestEmail(0), "ads", userProfile);

        TenantFacade userFacade = new TenantFacade(new TenantAdminRole(
                testAdminID));

        try {
            nullFacade.addTenantMember(testTenantID, testAdminID);
            fail("managed to add a tenant member using null facade");
        } catch (TransactionException e) {
            // supposed to happen
        }
        try {
            adminFacade.addTenantMember(null, testAdminID);
            fail("managed to add a tenant member using null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to happen
        }
        try {
            adminFacade.addTenantMember(testTenantID, null);
            fail("managed to add a tenant member using null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to happen
        }
        try {
            adminFacade.addTenantMember(testTenantID, UserFacadeTest
                    .getInvalidUserID());
            fail("managed to add a tenant member using invalid user ID");
        } catch (TransactionException e) {
            // supposed to happen
        }
        try {
            adminFacade.addTenantMember(invalidTenantID, testAdminID);
            fail("managed to add a tenant member using invalid tenant ID");
        } catch (TransactionException e) {
            // supposed to happen
        }

        try {
            userFacade.addTenantMember(null, testAdminID);
            fail("managed to add a tenant member using null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to happen
        }
        try {
            userFacade.addTenantMember(testTenantID, null);
            fail("managed to add a tenant member using null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to happen
        }
        try {
            userFacade.addTenantMember(testTenantID, UserFacadeTest
                    .getInvalidUserID());
            fail("managed to add a tenant member using invalid user ID");
        } catch (TransactionException e) {
            // supposed to happen
        }
        try {
            userFacade.addTenantMember(invalidTenantID, testAdminID);
            fail("managed to add a tenant member using invalid tenant ID");
        } catch (TransactionException e) {
            // supposed to happen
        }

        // adminFacade.addTenantMember(testTenantID, secondUserID);
        // assertEquals(2, adminFacade.getUsersOfTenant(testTenantID,
        // null).size());
        // adminFacade.addTenantMember(testTenantID, secondUserID);
        // assertEquals(2, adminFacade.getUsersOfTenant(testTenantID,
        // null).size());
        //
        // assertEquals(2, adminFacade.getUsersOfTenant(testTenantID,
        // null).size());
        // userFacade.addTenantMember(testTenantID, secondUserID);
        // assertEquals(2, adminFacade.getUsersOfTenant(testTenantID,
        // null).size());
        // userFacade.addTenantMember(testTenantID, secondUserID);
        // assertEquals(2, adminFacade.getUsersOfTenant(testTenantID,
        // null).size());

        UserFacadeTest.deleteTestUsers();
    }

    /**
     * Test method for {@link TenantFacade#getTenantsToApprove(List, Paginator)}
     * and {@link TenantFacade#approveTenants(List)}.
     */
    @Test
    public void testApproveTenants() throws TransactionException {
        List<TenantWithAdminView> tenants = adminFacade.getTenantsToApprove(
                null, null);
        assertNotNull(tenants);
        assertFalse(tenants.isEmpty());
        List<Long> tenantIDs = new ArrayList<Long>(tenants.size());

        for (TenantWithAdminView tenant : tenants) {
            assertNotNull(tenant.getId());
            assertNotNull(tenant.getName());
            tenant.getAdminFirstName();
            tenant.getAdminLastName();
            assertNotNull(tenant.getAdminId());

            tenantIDs.add((Long) tenant.getId());
        }

        try {
            userFacade.getTenantsToApprove(null, null);
            fail("managed to get a list of tenants to approve using basic user facade");
        } catch (TransactionException e) {
            // supposed to happen
        }
        try {
            nullFacade.getTenantsToApprove(null, null);
            fail("managed to get a list of tenants to approve using null facade");
        } catch (TransactionException e) {
            // supposed to happen
        }

        try {
            adminFacade.approveTenants(null);
            fail("managed to approve tenants without specifying them");
        } catch (IllegalArgumentException e) {
            // supposed to happen
        }
        try {
            userFacade.approveTenants(tenantIDs);
            fail("managed to approve tenants using basic user facade");
        } catch (TransactionException e) {
            // supposed to happen
        }
        try {
            nullFacade.approveTenants(tenantIDs);
            fail("managed to approve tenants using null facade");
        } catch (TransactionException e) {
            // supposed to happen
        }

        adminFacade.approveTenants(tenantIDs);
        adminFacade.approveTenants(tenantIDs);
        adminFacade.approveTenants(new ArrayList<Long>(1));
        assertTrue(adminFacade.getTenantsToApprove(null, null).isEmpty());
    }

    /**
     * Test method for {@link TenantFacade#setColorScheme(Long, Long, Boolean)},
     * {@link TenantFacade#setCurrentColorScheme(Long, Boolean)} and
     * {@link TenantFacade#getCurrentColorScheme(Long)}.
     */
    @Test
    public void testColorScheme() throws TransactionException, IOException {
        Set<Class<? extends FilePermission>> publicPermissions = new HashSet<Class<? extends FilePermission>>();
        publicPermissions.add(FileReadPermission.class);
        publicPermissions.add(FileDeletePermission.class);
        publicPermissions.add(FileReplacePermission.class);
        FileFacade fileFacade = new FileFacade(new SuperAdminRole(DecidrGlobals
                .getSettings().getSuperAdmin().getId()));
        Long simpleSize = FileFacadeTest
                .getInputStreamSize(TenantFacadeTest.class
                        .getResourceAsStream("/test_simple_cs.css"));
        Long advSize = FileFacadeTest.getInputStreamSize(TenantFacadeTest.class
                .getResourceAsStream("/test_adv_cs.css"));
        Long simpleID = fileFacade.createFile(TenantFacadeTest.class
                .getResourceAsStream("/test_simple_cs.css"), simpleSize,
                "test_simple_cs.css", "text/plain", false, publicPermissions);
        Long advancedID = fileFacade.createFile(TenantFacadeTest.class
                .getResourceAsStream("/test_adv_cs.css"), advSize,
                "test_adv_cs.css", "text/plain", false, publicPermissions);

        assertNull(adminFacade.getCurrentColorScheme(testTenantID));
        assertNull(userFacade.getCurrentColorScheme(testTenantID));

        adminFacade.setCurrentColorScheme(testTenantID, true);
        adminFacade.setCurrentColorScheme(testTenantID, false);

        try {
            adminFacade.getCurrentColorScheme(invalidTenantID);
            fail("managed getting invalid facade's color scheme");
        } catch (TransactionException e) {
            // supposed to happen
        }
        try {
            userFacade.getCurrentColorScheme(invalidTenantID);
            fail("managed getting invalid facade's color scheme");
        } catch (TransactionException e) {
            // supposed to happen
        }

        try {
            adminFacade.setCurrentColorScheme(invalidTenantID, true);
            fail("managed setting invalid facade's color scheme");
        } catch (TransactionException e) {
            // supposed to happen
        }
        try {
            adminFacade.setCurrentColorScheme(invalidTenantID, false);
            fail("managed setting invalid facade's color scheme");
        } catch (TransactionException e) {
            // supposed to happen
        }

        adminFacade.setColorScheme(testTenantID, simpleID, false);
        adminFacade.setColorScheme(testTenantID, advancedID, true);

        adminFacade.setCurrentColorScheme(testTenantID, true);
        assertEquals(advSize, (Long) FileFacadeTest
                .getInputStreamSize(adminFacade
                        .getCurrentColorScheme(testTenantID)));
        assertTrue(FileFacadeTest.compareInputStreams(TenantFacadeTest.class
                .getResourceAsStream("/test_adv_cs.css"), adminFacade
                .getCurrentColorScheme(testTenantID)));
        assertEquals(advSize, (Long) FileFacadeTest
                .getInputStreamSize(userFacade
                        .getCurrentColorScheme(testTenantID)));
        assertTrue(FileFacadeTest.compareInputStreams(TenantFacadeTest.class
                .getResourceAsStream("/test_adv_cs.css"), userFacade
                .getCurrentColorScheme(testTenantID)));

        adminFacade.setCurrentColorScheme(testTenantID, false);
        assertEquals(simpleSize, (Long) FileFacadeTest
                .getInputStreamSize(adminFacade
                        .getCurrentColorScheme(testTenantID)));
        assertTrue(FileFacadeTest.compareInputStreams(TenantFacadeTest.class
                .getResourceAsStream("/test_simple_cs.css"), adminFacade
                .getCurrentColorScheme(testTenantID)));
        assertEquals(simpleSize, (Long) FileFacadeTest
                .getInputStreamSize(userFacade
                        .getCurrentColorScheme(testTenantID)));
        assertTrue(FileFacadeTest.compareInputStreams(TenantFacadeTest.class
                .getResourceAsStream("/test_simple_cs.css"), userFacade
                .getCurrentColorScheme(testTenantID)));
    }

    /**
     * Test method for {@link TenantFacade#getAllTenants(List, Paginator)}.
     */
    @Test
    public void testGetAllTenants() throws TransactionException {
        try {
            userFacade.getAllTenants(null, null);
            fail("managed to get a list of all tenants using basic user facade");
        } catch (TransactionException e) {
            // supposed to happen
        }
        try {
            nullFacade.getAllTenants(null, null);
            fail("managed to get a list of all tenants using null facade");
        } catch (TransactionException e) {
            // supposed to happen
        }

        List<TenantSummaryView> tenants = adminFacade.getAllTenants(null, null);
        List<TenantSummaryView> compare = adminFacade.getAllTenants(null, null);
        assertNotNull(tenants);
        assertFalse(tenants.isEmpty());
        assertNotNull(compare);
        assertFalse(compare.isEmpty());
        assertEquals(tenants.size(), compare.size());
        try {
            for (int i = 0; i < tenants.size(); i++) {
                for (String property : new String[] { "adminFirstName", "id",
                        "adminLastName", "numDeployedWorkflowModels",
                        "numMembers", "numWorkflowInstances", "tenantName",
                        "numWorkflowModels" }) {
                    assertEquals(BeanUtils
                            .getProperty(tenants.get(i), property), BeanUtils
                            .getProperty(compare.get(i), property));
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link TenantFacade#getTenant(String)}.
     */
    @Test
    public void testGetTenantId() throws TransactionException {
        String invalidName = TEST_NAME + "invalid";

        assertEquals(testTenantID, adminFacade.getTenant(TEST_NAME).getId());
        assertEquals(testTenantID, userFacade.getTenant(TEST_NAME).getId());

        try {
            Long id = adminFacade.getTenant(invalidName).getId();
            adminFacade.deleteTenant(id);
            fail("getting invalid tenant ID succeeded");
        } catch (TransactionException e) {
            // indicates the state we want
        }

        try {
            nullFacade.getTenant(TEST_NAME);
            fail("managed to get tenant ID with null facade");
        } catch (TransactionException e) {
            // supposed to happen
        }

        try {
            adminFacade.getTenant((String) null);
            fail("managed to get tenant ID with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to happen
        }
        try {
            adminFacade.getTenant("");
            fail("managed to get tenant ID with empty parameter");
        } catch (IllegalArgumentException e) {
            // supposed to happen
        }
        try {
            adminFacade.getTenant(invalidName);
            fail("managed to get tenant ID with invalid parameter");
        } catch (TransactionException e) {
            // supposed to happen
        }

        try {
            userFacade.getTenant((Long) null);
            fail("managed to get tenant ID with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to happen
        }
        try {
            userFacade.getTenant("");
            fail("managed to get tenant ID with empty parameter");
        } catch (IllegalArgumentException e) {
            // supposed to happen
        }
        try {
            userFacade.getTenant(invalidName);
            fail("managed to get tenant ID with invalid parameter");
        } catch (TransactionException e) {
            // supposed to happen
        }
    }

    /**
     * Test method for {@link TenantFacade#getUsersOfTenant(Long, Paginator)}.
     */
    @Test
    public void testGetUsersOfTenant() throws TransactionException {
        TenantFacade userFacade = new TenantFacade(new WorkflowAdminRole(
                testAdminID));

        try {
            nullFacade.getUsersOfTenant(testTenantID, null);
            fail("managed to get users of tenant using null facade");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            adminFacade.getUsersOfTenant(null, null);
            fail("managed to get users of tenant using null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            userFacade.getUsersOfTenant(null, null);
            fail("managed to get users of tenant using null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }

        assertEquals(1, adminFacade.getUsersOfTenant(testTenantID, null).size());
        assertEquals(1, userFacade.getUsersOfTenant(testTenantID, null).size());

        User user = adminFacade.getUsersOfTenant(testTenantID, null).get(0);
        assertNotNull(user.getId());
        assertNotNull(user.getEmail());
    }

    /**
     * Test method for
     * {@link TenantFacade#getWorkflowInstances(Long, Paginator)}.
     */
    @Test
    public void testGetWorkflowInstances() throws TransactionException {
        assertTrue(adminFacade.getWorkflowInstances(testTenantID, null)
                .isEmpty());

        try {
            adminFacade.getWorkflowInstances(invalidTenantID, null);
            fail("managed to get workflow instances for invalid tenant ID");
        } catch (TransactionException e) {
            // supposed to happen
        }

        try {
            adminFacade.getWorkflowInstances(null, null);
            fail("managed to get workflow instances with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to happen
        }
        try {
            nullFacade.getWorkflowInstances(testTenantID, null);
            fail("managed to get workflow instances with null facade");
        } catch (TransactionException e) {
            // supposed to happen
        }
        try {
            userFacade.getWorkflowInstances(testTenantID, null);
            fail("managed to get workflow instances with normal user facade");
        } catch (TransactionException e) {
            // supposed to happen
        }
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
        assertNotNull(logoStream);
        Set<Class<? extends FilePermission>> publicPermissions = new HashSet<Class<? extends FilePermission>>();
        publicPermissions.add(FileReadPermission.class);
        publicPermissions.add(FileDeletePermission.class);
        publicPermissions.add(FileReplacePermission.class);
        Long logoID = fileFacade.createFile(logoStream, FileFacadeTest
                .getInputStreamSize(TenantFacadeTest.class
                        .getResourceAsStream("/decidr.jpg")), "decidr.jpg",
                "image/jpeg", false, publicPermissions);
        Long invalidLogoID = FileFacadeTest.getInvalidFileID();

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
        } catch (IllegalArgumentException e) {
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
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }

        try {
            userFacade.setLogo(null, logoID);
            fail("managed to set tenant logo using null parameter");
        } catch (IllegalArgumentException e) {
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
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }

        assertNull(adminFacade.getLogo(testTenantID));

        adminFacade.setLogo(testTenantID, logoID);
        assertNotNull(adminFacade.getLogo(testTenantID));

        long fileSizeBytes = fileFacade.getFileInfo(logoID).getFileSizeBytes();
        byte[] streamLogo = new byte[(int) fileSizeBytes];
        byte[] tenantLogo = new byte[(int) fileSizeBytes];
        assertEquals(fileSizeBytes, TenantFacadeTest.class.getResourceAsStream(
                "/decidr.jpg").read(streamLogo));
        assertEquals(fileSizeBytes, adminFacade.getLogo(testTenantID).read(
                tenantLogo));
        assertTrue(Arrays.equals(streamLogo, tenantLogo));

        assertEquals(fileSizeBytes, userFacade.getLogo(testTenantID).read(
                tenantLogo));
        assertTrue(Arrays.equals(streamLogo, tenantLogo));

        adminFacade.setLogo(testTenantID, null);
        assertNull(adminFacade.getLogo(testTenantID));

        fileFacade.deleteFile(logoID);
    }

    /**
     * Test method for {@link TenantFacade#setDescription(Long, String)}.
     */
    @Test
    public void testSetDescription() throws TransactionException {
        TenantFacade userFacade = new TenantFacade(new TenantAdminRole(
                testAdminID));

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
        } catch (IllegalArgumentException e) {
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
        } catch (IllegalArgumentException e) {
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
        } catch (IllegalArgumentException e) {
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
        } catch (IllegalArgumentException e) {
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
        } catch (IllegalArgumentException e) {
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
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }

        try {
            adminFacade.deleteTenant(null);
            fail("Managed to delete tenant using null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }
        try {
            userFacade.deleteTenant(null);
            fail("Managed to delete tenant using null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }

        Long secondTenantID = adminFacade.createTenant(TEST_NAME + "blah",
                TEST_DESC, testAdminID);
        assertNotNull(secondTenantID);
        adminFacade.deleteTenant(secondTenantID);
        adminFacade.deleteTenant(secondTenantID);
    }

    /**
     * Test method for {@link TenantFacade#createWorkflowModel(Long, String)},
     * {@link TenantFacade#getWorkflowModels(Long, List, Paginator)} and
     * {@link TenantFacade#importPublishedWorkflowModels(Long, List)}.
     */
    @Test
    public void testWorkflowModel() throws TransactionException {
        assertTrue(adminFacade.getWorkflowModels(testTenantID, null, null)
                .isEmpty());

        Long wfmID = adminFacade.createWorkflowModel(testTenantID, "testWFM");
        assertEquals(1, adminFacade.getWorkflowModels(testTenantID, null, null)
                .size());
        WorkflowModel WFM = adminFacade.getWorkflowModels(testTenantID, null,
                null).get(0);
        assertEquals("testWFM", WFM.getName());
        assertEquals(false, WFM.isPublished());
        // The dates must be less than 60s apart to count as equal
        assertTrue(Math.abs(DecidrGlobals.getTime().getTimeInMillis()
                - ((Date) WFM.getCreationDate()).getTime()) < 60000);

        ArrayList<Long> myWFM = new ArrayList<Long>(2);
        myWFM.add(wfmID);
        new WorkflowModelFacade(new SuperAdminRole(DecidrGlobals.getSettings()
                .getSuperAdmin().getId())).publishWorkflowModels(myWFM);
        adminFacade.importPublishedWorkflowModels(testTenantID, myWFM);
        assertEquals(1, adminFacade.getWorkflowModels(testTenantID, null, null)
                .size());

        session.beginTransaction().commit();
        session.createQuery("delete from WorkflowModel where id = :id")
                .setLong("id", wfmID).executeUpdate();

        try {
            adminFacade.createWorkflowModel(null, "testWFM");
            fail("Managed to create workflow model with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to happen
        }
        try {
            adminFacade.createWorkflowModel(testTenantID, null);
            fail("Managed to create workflow model with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to happen
        }
        try {
            adminFacade.createWorkflowModel(invalidTenantID, "testWFM");
            fail("Managed to create workflow model with invalid parameter");
        } catch (TransactionException e) {
            // supposed to happen
        }
        try {
            nullFacade.createWorkflowModel(testTenantID, "testWFM");
            fail("Managed to create workflow model with null facade");
        } catch (TransactionException e) {
            // supposed to happen
        }
        try {
            userFacade.createWorkflowModel(testTenantID, "testWFM");
            fail("Managed to create workflow model with normal user facade");
        } catch (TransactionException e) {
            // supposed to happen
        }

        try {
            adminFacade.getWorkflowModels(null, null, null);
            fail("Managed to get workflow models with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to happen
        }
        try {
            adminFacade.getWorkflowModels(invalidTenantID, null, null);
            fail("Managed to get workflow models with invalid parameter");
        } catch (TransactionException e) {
            // supposed to happen
        }
        try {
            nullFacade.getWorkflowModels(testTenantID, null, null);
            fail("Managed to get workflow models with null facade");
        } catch (TransactionException e) {
            // supposed to happen
        }
        try {
            userFacade.getWorkflowModels(testTenantID, null, null);
            fail("Managed to get workflow models with normal user facade");
        } catch (TransactionException e) {
            // supposed to happen
        }

        try {
            adminFacade.importPublishedWorkflowModels(null,
                    new ArrayList<Long>(1));
            fail("Managed to import workflow models with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to happen
        }
        try {
            adminFacade.importPublishedWorkflowModels(testTenantID, null);
            fail("Managed to import workflow models with null parameter");
        } catch (IllegalArgumentException e) {
            // supposed to happen
        }
        try {
            adminFacade.importPublishedWorkflowModels(invalidTenantID,
                    new ArrayList<Long>(1));
            fail("Managed to import workflow models with invalid parameter");
        } catch (TransactionException e) {
            // supposed to happen
        }
        try {
            nullFacade.importPublishedWorkflowModels(testTenantID,
                    new ArrayList<Long>(1));
            fail("Managed to import workflow models with null facade");
        } catch (TransactionException e) {
            // supposed to happen
        }
        try {
            userFacade.importPublishedWorkflowModels(testTenantID,
                    new ArrayList<Long>(1));
            fail("Managed to import workflow models with normal user facade");
        } catch (TransactionException e) {
            // supposed to happen
        }
    }
}
