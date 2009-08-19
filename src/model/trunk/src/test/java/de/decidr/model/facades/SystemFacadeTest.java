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

import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.filters.Paginator;

/**
 * Test case for <code>{@link SystemFacade}</code>.
 * 
 * @author Reinhold
 */
public class SystemFacadeTest {

    static SystemFacade adminFacade;
    static SystemFacade userFacade;
    static SystemFacade nullFacade;

    /**
     * Initialises the facade instances.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        adminFacade = new SystemFacade(new SuperAdminRole());
        userFacade = new SystemFacade(new BasicRole(0L));
        nullFacade = new SystemFacade(null);
    }

    /**
     * Test method for {@link SystemFacade#getSettings()}.
     */
    @Test
    public void testGetSettings() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link SystemFacade#setSettings(SystemSettings)}.
     */
    @Test
    public void testSetSettings() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for
     * {@link SystemFacade#addServer(ServerTypeEnum, String, Byte, Boolean, Boolean)}
     * .
     */
    @Test
    public void testAddServer() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link SystemFacade#removeServer(Long)}.
     */
    @Test
    public void testRemoveServer() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link SystemFacade#updateServerLoad(Long, byte)}.
     */
    @Test
    public void testUpdateServerLoad() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link SystemFacade#setServerLock(Long, Boolean)}.
     */
    @Test
    public void testSetServerLock() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link SystemFacade#getLog(List, Paginator)}.
     */
    @Test
    public void testGetLog() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link SystemFacade#getServerStatistics()}.
     */
    @Test
    public void testGetServerStatistics() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link SystemFacade#getServers(ServerTypeEnum[])}.
     */
    @Test
    public void testGetServers() {
        fail("Not yet implemented"); // RR
    }
}
