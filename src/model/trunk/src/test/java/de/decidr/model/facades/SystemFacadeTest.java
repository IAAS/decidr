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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
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
     * Test method for {@link SystemFacade#getSettings()} and
     * {@link SystemFacade#setSettings(SystemSettings)}.
     */
    @Test
    public void testSettings() throws TransactionException {
        fail("Not yet implemented"); // RR setSettings
        fail("Not yet implemented"); // RR getSettings
    }

    /**
     * Test method for
     * {@link SystemFacade#addServer(ServerTypeEnum, String, Byte, Boolean, Boolean)}
     * , {@link SystemFacade#getServers(ServerTypeEnum[])},
     * {@link SystemFacade#getServerStatistics()},
     * {@link SystemFacade#updateServerLoad(Long, byte)},
     * {@link SystemFacade#setServerLock(Long, Boolean)} and
     * {@link SystemFacade#removeServer(Long)}.
     */
    @Test
    public void testServer() throws TransactionException {
        Server testServer;

        for (ServerTypeEnum type : ServerTypeEnum.values()) {
            // DH what should happen here (empty location)?
            adminFacade.addServer(type, "", (byte) -1, true, true);
            adminFacade.addServer(type, null, (byte) -1, true, true);
            adminFacade.addServer(type, "127.0.0.1", (byte) -1, true, true);
            adminFacade.addServer(type, "", (byte) 0, true, true);
            adminFacade.addServer(type, null, (byte) 0, true, true);
            adminFacade.addServer(type, "127.0.0.1", (byte) 0, true, true);
            adminFacade.addServer(type, "", (byte) 50, true, true);
            adminFacade.addServer(type, null, (byte) 50, true, true);
            adminFacade.addServer(type, "127.0.0.1", (byte) 50, true, true);
            adminFacade.addServer(type, "", (byte) 100, true, true);
            adminFacade.addServer(type, null, (byte) 100, true, true);
            adminFacade.addServer(type, "127.0.0.1", (byte) 100, true, true);
            adminFacade.addServer(type, "", (byte) -1, false, true);
            adminFacade.addServer(type, null, (byte) -1, false, true);
            adminFacade.addServer(type, "127.0.0.1", (byte) -1, false, true);
            adminFacade.addServer(type, "", (byte) 0, false, true);
            adminFacade.addServer(type, null, (byte) 0, false, true);
            adminFacade.addServer(type, "127.0.0.1", (byte) 0, false, true);
            adminFacade.addServer(type, "", (byte) 50, false, true);
            adminFacade.addServer(type, null, (byte) 50, false, true);
            adminFacade.addServer(type, "127.0.0.1", (byte) 50, false, true);
            adminFacade.addServer(type, "", (byte) 100, false, true);
            adminFacade.addServer(type, null, (byte) 100, false, true);
            adminFacade.addServer(type, "127.0.0.1", (byte) 100, false, true);
            adminFacade.addServer(type, "", (byte) -1, true, false);
            adminFacade.addServer(type, null, (byte) -1, true, false);
            adminFacade.addServer(type, "127.0.0.1", (byte) -1, true, false);
            adminFacade.addServer(type, "", (byte) 0, true, false);
            adminFacade.addServer(type, null, (byte) 0, true, false);
            adminFacade.addServer(type, "127.0.0.1", (byte) 0, true, false);
            adminFacade.addServer(type, "", (byte) 50, true, false);
            adminFacade.addServer(type, null, (byte) 50, true, false);
            adminFacade.addServer(type, "127.0.0.1", (byte) 50, true, false);
            adminFacade.addServer(type, "", (byte) 100, true, false);
            adminFacade.addServer(type, null, (byte) 100, true, false);
            adminFacade.addServer(type, "127.0.0.1", (byte) 100, true, false);
            adminFacade.addServer(type, "", (byte) -1, false, false);
            adminFacade.addServer(type, null, (byte) -1, false, false);
            adminFacade.addServer(type, "127.0.0.1", (byte) -1, false, false);
            adminFacade.addServer(type, "", (byte) 0, false, false);
            adminFacade.addServer(type, null, (byte) 0, false, false);
            adminFacade.addServer(type, "127.0.0.1", (byte) 0, false, false);
            adminFacade.addServer(type, "", (byte) 50, false, false);
            adminFacade.addServer(type, null, (byte) 50, false, false);
            adminFacade.addServer(type, "127.0.0.1", (byte) 50, false, false);
            adminFacade.addServer(type, "", (byte) 100, false, false);
            adminFacade.addServer(type, null, (byte) 100, false, false);
            adminFacade.addServer(type, "127.0.0.1", (byte) 100, false, false);

            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) -1, true, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) -1, false, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) -1, true, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) -1, false, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "localhost", (byte) -2, true, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "localhost", (byte) -200, true, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "localhost", (byte) 101, true, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "localhost", (byte) 200, true, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "localhost", (byte) -2, false, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "localhost", (byte) -200, false, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "localhost", (byte) 101, false, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "localhost", (byte) 200, false, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "localhost", (byte) -2, true, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "localhost", (byte) -200, true, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "localhost", (byte) 101, true, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "localhost", (byte) 200, true, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "localhost", (byte) -2, false, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "localhost", (byte) -200, false, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "localhost", (byte) 101, false, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "localhost", (byte) 200, false, false);

            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) -1, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) -1, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) -1, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 0, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 0, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) 0, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 50, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 50, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) 50, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 100, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 100, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) 100, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 200, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 200, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) 200, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) -1, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) -1, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) -1, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 0, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 0, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) 0, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 50, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 50, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) 50, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 100, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 100, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) 100, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 200, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 200, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) 200, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) -1, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) -1, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) -1, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 0, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 0, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) 0, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 50, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 50, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) 50, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 100, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 100, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) 100, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 200, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 200, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) 200, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) -1, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) -1, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) -1, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 0, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 0, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) 0, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 50, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 50, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) 50, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 100, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 100, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) 100, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 200, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 200, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "127.0.0.1", (byte) 200, false, false);

            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) -1, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) -1, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) -1, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 0, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 0, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) 0, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 50, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 50, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) 50, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 100, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 100, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) 100, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 200, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 200, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) 200, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) -1, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) -1, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) -1, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 0, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 0, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) 0, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 50, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 50, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) 50, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 100, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 100, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) 100, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 200, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 200, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) 200, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) -1, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) -1, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) -1, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 0, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 0, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) 0, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 50, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 50, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) 50, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 100, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 100, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) 100, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 200, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 200, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) 200, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) -1, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) -1, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) -1, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 0, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 0, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) 0, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 50, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 50, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) 50, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 100, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 100, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) 100, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 200, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 200, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "127.0.0.1", (byte) 200, false, false);

            assertNotNull(adminFacade.getServers(type));
            assertFalse(adminFacade.getServers(type).isEmpty());
            assertNotNull(adminFacade.getServers((ServerTypeEnum) null));
            assertFalse(adminFacade.getServers((ServerTypeEnum) null).isEmpty());
            try {
                nullFacade.getServers(type);
                fail("null facade suceeded");
            } catch (TransactionException e) {
                // should be thrown
            }
            try {
                nullFacade.getServers((ServerTypeEnum) null);
                fail("null facade suceeded");
            } catch (TransactionException e) {
                // should be thrown
            }
            try {
                userFacade.getServers(type);
                fail("normal user facade suceeded");
            } catch (TransactionException e) {
                // should be thrown
            }
            try {
                userFacade.getServers((ServerTypeEnum) null);
                fail("normal user facade suceeded");
            } catch (TransactionException e) {
                // should be thrown
            }

            testServer = adminFacade.getServers(type).get(0);

            assertNotNull(adminFacade.getServerStatistics());
            assertFalse(adminFacade.getServerStatistics().isEmpty());
            try {
                nullFacade.getServerStatistics();
                fail("null facade suceeded");
            } catch (TransactionException e) {
                // should be thrown
            }
            try {
                userFacade.getServerStatistics();
                fail("normal user facade suceeded");
            } catch (TransactionException e) {
                // should be thrown
            }

            Long testServerID = testServer.getId();
            adminFacade.updateServerLoad(testServerID, (byte) 20);
            testServer = getServer(testServer);
            assertEquals((byte) 20, testServer.getLoad());
            adminFacade.updateServerLoad(testServerID, (byte) 82);
            testServer = getServer(testServer);
            assertEquals((byte) 82, testServer.getLoad());
            adminFacade.updateServerLoad(testServerID, (byte) 0);
            testServer = getServer(testServer);
            assertEquals((byte) 0, testServer.getLoad());
            adminFacade.updateServerLoad(testServerID, (byte) 100);
            testServer = getServer(testServer);
            assertEquals((byte) 100, testServer.getLoad());
            adminFacade.updateServerLoad(testServerID, (byte) -1);
            testServer = getServer(testServer);
            assertEquals((byte) -1, testServer.getLoad());

            updateServerLoadExceptionHelper(
                    "updating server load with invalid value succeeded.",
                    adminFacade, testServerID, (byte) -2);
            updateServerLoadExceptionHelper(
                    "updating server load with invalid value succeeded.",
                    adminFacade, testServerID, (byte) -200);
            updateServerLoadExceptionHelper(
                    "updating server load with invalid value succeeded.",
                    adminFacade, testServerID, (byte) 101);
            updateServerLoadExceptionHelper(
                    "updating server load with invalid value succeeded.",
                    adminFacade, testServerID, (byte) 200);

            updateServerLoadExceptionHelper(
                    "updating server load with null facade succeeded.",
                    nullFacade, testServerID, (byte) 0);
            updateServerLoadExceptionHelper(
                    "updating server load with null facade succeeded.",
                    nullFacade, testServerID, (byte) -10);
            updateServerLoadExceptionHelper(
                    "updating server load with null facade succeeded.",
                    nullFacade, testServerID, (byte) -1);
            updateServerLoadExceptionHelper(
                    "updating server load with null facade succeeded.",
                    nullFacade, testServerID, (byte) 50);
            updateServerLoadExceptionHelper(
                    "updating server load with null facade succeeded.",
                    nullFacade, testServerID, (byte) 100);
            updateServerLoadExceptionHelper(
                    "updating server load with null facade succeeded.",
                    nullFacade, testServerID, (byte) 101);
            updateServerLoadExceptionHelper(
                    "updating server load with null facade succeeded.",
                    nullFacade, testServerID, (byte) 200);

            updateServerLoadExceptionHelper(
                    "updating server load with normal user facade succeeded.",
                    userFacade, testServerID, (byte) 0);
            updateServerLoadExceptionHelper(
                    "updating server load with normal user facade succeeded.",
                    userFacade, testServerID, (byte) -10);
            updateServerLoadExceptionHelper(
                    "updating server load with normal user facade succeeded.",
                    userFacade, testServerID, (byte) -1);
            updateServerLoadExceptionHelper(
                    "updating server load with normal user facade succeeded.",
                    userFacade, testServerID, (byte) 50);
            updateServerLoadExceptionHelper(
                    "updating server load with normal user facade succeeded.",
                    userFacade, testServerID, (byte) 100);
            updateServerLoadExceptionHelper(
                    "updating server load with normal user facade succeeded.",
                    userFacade, testServerID, (byte) 101);
            updateServerLoadExceptionHelper(
                    "updating server load with normal user facade succeeded.",
                    userFacade, testServerID, (byte) 200);

            adminFacade.setServerLock(testServerID, true);
            testServer = getServer(testServer);
            assertTrue(testServer.isLocked());
            adminFacade.setServerLock(testServerID, false);
            testServer = getServer(testServer);
            assertFalse(testServer.isLocked());

            setServerLockExceptionHelper(
                    "setting server lock with null facade succeeded.",
                    nullFacade, testServerID, true);
            setServerLockExceptionHelper(
                    "unsetting server lock with null facade succeeded.",
                    nullFacade, testServerID, false);
            setServerLockExceptionHelper(
                    "setting server lock with normal user facade succeeded.",
                    userFacade, testServerID, true);
            setServerLockExceptionHelper(
                    "unsetting server lock with normal user facade succeeded.",
                    userFacade, testServerID, false);
            
            List<Server> servers = adminFacade.getServers(ServerTypeEnum
                    .valueOf(testServer.getServerType().getName()));
            for (Server server : servers) {
                try {
                    nullFacade.removeServer(server.getId());
                    fail("shouldn't be able to remove server with null facade");
                } catch (TransactionException e) {
                    // supposed to be thrown
                }
                try {
                    userFacade.removeServer(server.getId());
                    fail("shouldn't be able to remove server with normal user facade");
                } catch (TransactionException e) {
                    // supposed to be thrown
                }

                adminFacade.removeServer(server.getId());
            }
            assertTrue(adminFacade.getServerStatistics().isEmpty());
            assertTrue(adminFacade.getServers(type).isEmpty());
            assertTrue(adminFacade.getServers((ServerTypeEnum) null).isEmpty());
        }
    }

    /**
     * Returns a new instance of the passed <code>{@link Server}</code> with
     * up-to-date data or <code>null</code> if the server doesn't exist anymore.<br>
     * <em>USAGE:</em> <code>server = getServer(server)</code>
     */
    private Server getServer(Server testServer) throws TransactionException {
        List<Server> servers = adminFacade.getServers(ServerTypeEnum
                .valueOf(testServer.getServerType().getName()));
        for (Server server : servers) {
            if (server.getId() == testServer.getId()) {
                return server;
            }
        }
        return null;
    }

    private void setServerLockExceptionHelper(String failmsg,
            SystemFacade facade, long serverID, boolean lock) {
        try {
            facade.setServerLock(serverID, lock);
            fail(failmsg);
        } catch (TransactionException e) {
            // should be thrown
        }
    }

    private void updateServerLoadExceptionHelper(String failmsg,
            SystemFacade facade, long serverID, byte newLoad) {
        try {
            facade.updateServerLoad(serverID, newLoad);
            fail(failmsg);
        } catch (TransactionException e) {
            // should be thrown
        }
    }

    private void addServerExceptionHelper(String failmsg, SystemFacade facade,
            ServerTypeEnum type, String location, byte initialLoad,
            boolean locked, boolean dynamicallyAdded) {
        try {
            facade.addServer(type, location, initialLoad, locked,
                    dynamicallyAdded);
            fail(failmsg);
        } catch (TransactionException e) {
            // should be thrown
        }
    }

    /**
     * Test method for {@link SystemFacade#getLog(List, Paginator)}.
     */
    @Test
    public void testGetLog() throws TransactionException {
        Paginator emptyPaginator = new Paginator();
        ArrayList<Filter> emptyList = new ArrayList<Filter>();

        adminFacade.getLog(null, null);
        adminFacade.getLog(emptyList, null);
        adminFacade.getLog(null, emptyPaginator);
        adminFacade.getLog(emptyList, emptyPaginator);

        logExceptionHelper(
                "getting log (null, null) with null facade succeeded",
                nullFacade, null, null);
        logExceptionHelper(
                "getting log (obj, null) with null facade succeeded",
                nullFacade, emptyList, null);
        logExceptionHelper(
                "getting log (null, obj) with null facade succeeded",
                nullFacade, null, emptyPaginator);
        logExceptionHelper("getting log (obj, obj) with null facade succeeded",
                nullFacade, emptyList, emptyPaginator);

        logExceptionHelper(
                "getting log (null, null) with normal user facade succeeded",
                userFacade, null, null);
        logExceptionHelper(
                "getting log (obj, null) with normal user facade succeeded",
                userFacade, emptyList, null);
        logExceptionHelper(
                "getting log (null, obj) with normal user facade succeeded",
                userFacade, null, emptyPaginator);
        logExceptionHelper(
                "getting log (obj, obj) with normal user facade succeeded",
                userFacade, emptyList, emptyPaginator);
    }

    private void logExceptionHelper(String failmsg, SystemFacade facade,
            List<Filter> filters, Paginator paginator) {
        try {
            facade.getLog(filters, paginator);
            fail(failmsg);
        } catch (TransactionException e) {
            // should be thrown
        }
    }
}
