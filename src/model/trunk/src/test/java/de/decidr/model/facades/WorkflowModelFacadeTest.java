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
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.vaadin.data.Item;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Paginator;
import de.decidr.model.testing.DecidrDatabaseTest;

/**
 * Test case for <code>{@link WorkflowModelFacade}</code>. Some of the methods
 * can't be tested easily within the confines of a unit test, as they interact
 * with web services. These methods will be tested at a later point in time when
 * the most important test cases are written or during the system test.
 * 
 * @author Reinhold
 */
public class WorkflowModelFacadeTest extends DecidrDatabaseTest {

    static long wfmId;

    static WorkflowModelFacade adminFacade;
    static WorkflowModelFacade userFacade;
    static WorkflowModelFacade nullFacade;

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
        wfmId = tenantFacade.createWorkflowModel(tenantId,
                "WorkflowModelFacadeTestWFModel");
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
     * Test method for
     * {@link WorkflowModelFacade#publishWorkflowModels(List)}
     * {@link WorkflowModelFacade#unpublishWorkflowModels(List)}
     */
    @Test
    public void testPublishWorkflowModels() throws TransactionException {
        List<Long> wfmIds = new ArrayList<Long>();
        wfmIds.add(wfmId);
        
        adminFacade.publishWorkflowModels(wfmIds);
        Object po = adminFacade.getWorkflowModel(wfmId).getItemProperty("published").getValue();
        assertTrue((Boolean) po);
        
        adminFacade.unpublishWorkflowModels(wfmIds);
        po = adminFacade.getWorkflowModel(wfmId).getItemProperty("published").getValue();
        assertFalse((Boolean) po);
    }

    /**
     * Test method for {@link WorkflowModelFacade#setExecutable(Long, Boolean)}.
     */
    @Test
    public void testSetExecutable() throws TransactionException {
        adminFacade.setExecutable(wfmId, true);
    }

    /**
     * Test method for
     * {@link WorkflowModelFacade#getWorkflowAdministrators(Long)}.
     */
    @Test
    public void testGetWorkflowAdministrators() {
        fail("Not yet implemented"); // JE getWorkflowAdministrators
    }

    /**
     * Test method for {@link WorkflowModelFacade#deleteWorkflowModels(List)}.
     */
    @Test
    public void testDeleteWorkflowModels() {
        fail("Not yet implemented"); // JE deleteWorkflowModels
    }

    /**
     * Test method for
     * {@link WorkflowModelFacade#getWorkflowInstances(Long, Paginator)}.
     */
    @Test
    public void testGetWorkflowInstances() {
        fail("Not yet implemented"); // JE getWorkflowInstances
    }

    /**
     * Test method for
     * {@link WorkflowModelFacade#getAllPublishedWorkflowModels(List, Paginator)}
     * .
     */
    @Test
    public void testGetAllPublishedWorkflowModels() {
        fail("Not yet implemented"); // JE getAllPublishedWorkflowModels
    }

    /**
     * Test method for
     * {@link WorkflowModelFacade#getLastStartConfiguration(Long)}.
     */
    @Test
    public void testGetLastStartConfiguration() {
        fail("Not yet implemented"); // JE getLastStartConfiguration
    }
}
