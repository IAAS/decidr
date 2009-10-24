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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.vaadin.data.Item;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.entities.UserProfile;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.exceptions.UserDisabledException;
import de.decidr.model.exceptions.UserUnavailableException;
import de.decidr.model.exceptions.UsernameNotFoundException;
import de.decidr.model.filters.Paginator;
import de.decidr.model.testing.LowLevelDatabaseTest;

/**
 * Test case for <code>{@link WorkflowModelFacade}</code>. Some of the methods
 * can't be tested easily within the confines of a unit test, as they interact
 * with web services. These methods will be tested at a later point in time when
 * the most important test cases are written or during the system test.
 * 
 * @author Reinhold
 */
public class WorkflowModelFacadeTest extends LowLevelDatabaseTest {

    static long wfmId;
    static String username;
    static long userId;

    static WorkflowModelFacade adminFacade;
    static WorkflowModelFacade userFacade;
    static WorkflowModelFacade nullFacade;

    static UserFacade adminUserFacade;

    /**
     * Initialises the facade instances.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        adminFacade = new WorkflowModelFacade(new SuperAdminRole(DecidrGlobals
                .getSettings().getSuperAdmin().getId()));
        userFacade = new WorkflowModelFacade(new BasicRole(0L));
        nullFacade = new WorkflowModelFacade(null);
    }

    @BeforeClass
    public static void createWorkflowModel() throws TransactionException {
        // create test tenant
        final String NAME = "WorkflowModelFacadeTestTenant";
        final String DESCRIPTION = "TenantDescription";

        Role role = new SuperAdminRole(DecidrGlobals.getSettings()
                .getSuperAdmin().getId());
        TenantFacade tenantFacade = new TenantFacade(role);

        try {
            long id = tenantFacade.getTenantId(NAME);
            tenantFacade.deleteTenant(id);
        } catch (EntityNotFoundException e) {
            // tenant does not exist - good!
        }

        long tenantId = tenantFacade.createTenant(NAME, DESCRIPTION,
                DecidrGlobals.getSettings().getSuperAdmin().getId());

        // create test workflow model
        wfmId = tenantFacade.createWorkflowModel(tenantId,
                "WorkflowModelFacadeTestWFModel");

        // create test user for workflow admin test purpose
        UserFacadeTest.deleteTestUsers();
        adminUserFacade = new UserFacade(role);

        UserProfile userProfile = new UserProfile();
        username = UserFacadeTest.USERNAME_PREFIX + "WMFTestUser";
        userProfile.setUsername(username);
        userId = adminUserFacade.registerUser(UserFacadeTest.getTestEmail(0),
                "ads", userProfile);
    }

    @AfterClass
    public static void tearDownTestCase() {
        UserFacadeTest.deleteTestUsers();
    }

    /**
     * Test method for
     * {@link WorkflowModelFacade#saveWorkflowModel(Long, String, String, byte[])}
     * {@link WorkflowModelFacade#getWorkflowModel(Long)}
     */
    @Test
    public void testSaveWorkflowModel() throws TransactionException {
        final String NAME = "WorkflowModelFacadeTestWorkflowModel";
        final String DESCRIPTION = "UnitTest Model for WorkflowModelFacade UnitTest";
        final byte[] DWDL = "<dwdl>DWDLBLA</dwdl>".getBytes();

        adminFacade.saveWorkflowModel(wfmId, NAME, DESCRIPTION, DWDL);
        Item wfm = adminFacade.getWorkflowModel(wfmId);

        long id = (Long) wfm.getItemProperty("id").getValue();
        String name = (String) wfm.getItemProperty("name").getValue();
        String description = (String) wfm.getItemProperty("description")
                .getValue();
        byte[] dwdl = (byte[]) wfm.getItemProperty("dwdl").getValue();

        assertEquals(id, wfmId);
        assertEquals(name, NAME);
        assertEquals(description, DESCRIPTION);
        assertArrayEquals(dwdl, DWDL);
    }

    /**
     * Test method for {@link WorkflowModelFacade#publishWorkflowModels(List)}
     * {@link WorkflowModelFacade#unpublishWorkflowModels(List)}
     * {@link WorkflowModelFacade#getAllPublishedWorkflowModels(List, Paginator)}
     */
    @Test
    public void testPublishWorkflowModels() throws TransactionException {
        List<Long> wfmIds = new ArrayList<Long>();
        wfmIds.add(wfmId);

        adminFacade.publishWorkflowModels(wfmIds);
        Object po = adminFacade.getWorkflowModel(wfmId).getItemProperty(
                "published").getValue();
        assertTrue((Boolean) po);

        List<Item> pwfms = adminFacade
                .getAllPublishedWorkflowModels(null, null);
        assertTrue(itemListContainsWfmId(pwfms, wfmId));

        adminFacade.unpublishWorkflowModels(wfmIds);
        po = adminFacade.getWorkflowModel(wfmId).getItemProperty("published")
                .getValue();
        assertFalse((Boolean) po);

        pwfms = adminFacade.getAllPublishedWorkflowModels(null, null);
        assertFalse(itemListContainsWfmId(pwfms, wfmId));
    }

    private boolean itemListContainsWfmId(List<Item> items, long wfmId) {
        for (Item item : items) {
            if (wfmId == (Long) item.getItemProperty("id").getValue()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Test method for {@link WorkflowModelFacade#setExecutable(Long, Boolean)}.
     */
    @Test
    public void testSetExecutable() throws TransactionException {
        // JE: how to test?
        // impossible without a running ODE and properly configured web services
        // and esb ~dh
        adminFacade.setExecutable(wfmId, true);
    }

    /**
     * Test method for
     * {@link WorkflowModelFacade#setWorkflowAdministrators(Long)}.
     * {@link WorkflowModelFacade#getWorkflowAdministrators(Long)}.
     */
    @Test
    public void testGetWorkflowAdministrators() throws UserDisabledException,
            UserUnavailableException, UsernameNotFoundException,
            TransactionException {

        final String INVALID_USERNAME = "InvalidUserName";

        List<String> emails = new ArrayList<String>();
        List<String> unames = new ArrayList<String>();

        unames.add(INVALID_USERNAME);
        try {
            adminFacade.setWorkflowAdministrators(wfmId, emails, unames);
            fail("Username expected to be invalid, but is not.");
        } catch (UsernameNotFoundException e) {
            // expected
        }

        unames.clear();
        unames.add(username);
        adminUserFacade.setDisabledSince(userId, new Date());
        try {
            adminFacade.setWorkflowAdministrators(wfmId, emails, unames);
            fail("Username expected to be disabled, but is not.");
        } catch (UserDisabledException e) {
            // expected
        }

        adminUserFacade.setDisabledSince(userId, null);
        adminUserFacade.setUnavailableSince(userId, new Date());
        try {
            adminFacade.setWorkflowAdministrators(wfmId, emails, unames);
            fail("Username expected to be unavailable, but is not.");
        } catch (UserUnavailableException e) {
            // expected
        }

        adminUserFacade.setUnavailableSince(userId, null);
        adminFacade.setWorkflowAdministrators(wfmId, emails, unames);

        Object un = adminFacade.getWorkflowAdministrators(wfmId).get(0)
                .getItemProperty("username");
        assertEquals(un, username);
    }

    /**
     * Test method for
     * {@link WorkflowModelFacade#getWorkflowInstances(Long, Paginator)}.
     */
    @Test
    public void testGetWorkflowInstances() throws TransactionException {
        // JE: how to test with existing items?
        List<Item> items = adminFacade.getWorkflowInstances(wfmId, null);
        assertTrue(items.isEmpty());
    }

    /**
     * Test method for
     * {@link WorkflowModelFacade#getLastStartConfiguration(Long)}.
     */
    @Test
    public void testGetLastStartConfiguration() throws TransactionException {
        // JE: how to test?
        adminFacade.getLastStartConfiguration(wfmId);
    }

    /**
     * Test method for {@link WorkflowModelFacade#deleteWorkflowModels(List)}.
     */
    @Test
    public void testDeleteWorkflowModels() throws TransactionException {
        List<Long> wfmIds = new ArrayList<Long>();
        wfmIds.add(wfmId);
        adminFacade.deleteWorkflowModels(wfmIds);

        try {
            adminFacade.getWorkflowModel(wfmId);
            fail("Model to be deleted is still in the database.");
        } catch (EntityNotFoundException e) {
            // expected
        }
    }
}
