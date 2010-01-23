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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Transaction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.entities.User;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.testing.LowLevelDatabaseTest;

/**
 * Test case for <code>{@link SystemFacade}</code>.
 * 
 * @author Reinhold
 */
public class SystemFacadeTest extends LowLevelDatabaseTest {

    static SystemFacade adminFacade;
    static SystemFacade userFacade;
    static SystemFacade nullFacade;

    public static String HOSTNAME_PREFIX = "127.0.0.1";

    private static int hostCounter = 0;

    private static void addServerExceptionHelper(String failmsg,
            SystemFacade facade, ServerTypeEnum type, String location,
            byte initialLoad, boolean locked, boolean dynamicallyAdded) {
        try {
            facade.addServer(type, location, initialLoad, locked,
                    dynamicallyAdded);
            fail(failmsg);
        } catch (TransactionException e) {
            // should be thrown
        } catch (IllegalArgumentException e) {
            // should be thrown
        }
    }

    public static String getHostname() {
        return HOSTNAME_PREFIX + ":" + (hostCounter++);
    }

    /**
     * Returns a new instance of the passed <code>{@link Server}</code> with
     * up-to-date data or <code>null</code> if the server doesn't exist anymore.<br>
     * <em>USAGE:</em> <code>server = getServer(server)</code>
     */
    private static Server getServer(Server testServer)
            throws TransactionException {
        List<Server> servers = adminFacade.getServers(ServerTypeEnum
                .valueOf(testServer.getServerType().getName()));
        for (Server server : servers) {
            if (testServer.getId().equals(server.getId())) {
                return server;
            }
        }

        fail("Couldn't find specified server in DB");
        return null;
    }

    private static void logExceptionHelper(String failmsg, SystemFacade facade,
            List<Filter> filters, Paginator paginator) {
        try {
            facade.getLog(filters, paginator);
            fail(failmsg);
        } catch (TransactionException e) {
            // should be thrown
        }
    }

    @BeforeClass
    @AfterClass
    public static void removeTestServers() throws TransactionException {
        if (adminFacade == null) {
            adminFacade = new SystemFacade(new SuperAdminRole(DecidrGlobals
                    .getSettings().getSuperAdmin().getId()));
        }

        Transaction trans = session.beginTransaction();
        for (Server server : adminFacade.getServers()) {
            if (server.getLocation().startsWith(HOSTNAME_PREFIX)) {
                session.delete(server);
            }
        }
        trans.commit();

        hostCounter = 0;
    }

    private static void setServerLockExceptionHelper(String failmsg,
            SystemFacade facade, long serverID, boolean lock) {
        try {
            facade.setServerLock(serverID, lock);
            fail(failmsg);
        } catch (TransactionException e) {
            // should be thrown
        }
    }

    private static void setSettingsExceptionHelper(String failmsg,
            SystemFacade facade, SystemSettings settings) {
        try {
            facade.setSettings(settings);
            fail(failmsg);
        } catch (TransactionException e) {
            // should be thrown
        }
    }

    /**
     * Initialises the facade instances.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        adminFacade = new SystemFacade(new SuperAdminRole(DecidrGlobals
                .getSettings().getSuperAdmin().getId()));
        userFacade = new SystemFacade(new BasicRole(0L));
        nullFacade = new SystemFacade(null);
    }

    private static void updateServerLoadExceptionHelper(String failmsg,
            SystemFacade facade, long serverID, byte newLoad) {
        try {
            facade.updateServerLoad(serverID, newLoad);
            fail(failmsg);
        } catch (IllegalArgumentException e) {
            // should be thrown
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

    /**
     * Test method for
     * {@link SystemFacade#addServer(ServerTypeEnum, String, byte, boolean, boolean)}
     * , {@link SystemFacade#getServers(ServerTypeEnum[])},
     * {@link SystemFacade#getServerStatistics()},
     * {@link SystemFacade#updateServerLoad(Long, byte)},
     * {@link SystemFacade#setServerLock(Long, boolean)}.
     */
    @Test
    public void testServer() throws TransactionException {
        Server testServer;

        for (ServerTypeEnum type : ServerTypeEnum.values()) {
            adminFacade.addServer(type, getHostname(), (byte) -1, true, true);
            adminFacade.addServer(type, getHostname(), (byte) 0, true, true);
            adminFacade.addServer(type, getHostname(), (byte) 50, true, true);
            adminFacade.addServer(type, getHostname(), (byte) 100, true, true);
            adminFacade.addServer(type, getHostname(), (byte) -1, false, true);
            adminFacade.addServer(type, getHostname(), (byte) 0, false, true);
            adminFacade.addServer(type, getHostname(), (byte) 50, false, true);
            adminFacade.addServer(type, getHostname(), (byte) 100, false, true);
            adminFacade.addServer(type, getHostname(), (byte) -1, true, false);
            adminFacade.addServer(type, getHostname(), (byte) 0, true, false);
            adminFacade.addServer(type, getHostname(), (byte) 50, true, false);
            adminFacade.addServer(type, getHostname(), (byte) 100, true, false);
            adminFacade.addServer(type, getHostname(), (byte) -1, false, false);
            adminFacade.addServer(type, getHostname(), (byte) 0, false, false);
            adminFacade.addServer(type, getHostname(), (byte) 50, false, false);
            adminFacade
                    .addServer(type, getHostname(), (byte) 100, false, false);

            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) -1, true, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, null, (byte) -1, true, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) 0, true, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, null, (byte) 0, true, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) 50, true, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, null, (byte) 50, true, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) 100, true, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, null, (byte) 100, true, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) -1, false, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, null, (byte) -1, false, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) 0, false, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, null, (byte) 0, false, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) 50, false, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, null, (byte) 50, false, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) 100, false, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, null, (byte) 100, false, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) -1, true, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, null, (byte) -1, true, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) 0, true, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, null, (byte) 0, true, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) 50, true, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, null, (byte) 50, true, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) 100, true, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, null, (byte) 100, true, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) -1, false, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, null, (byte) -1, false, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) 0, false, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, null, (byte) 0, false, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) 50, false, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, null, (byte) 50, false, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) 100, false, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, null, (byte) 100, false, false);

            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) -1, true, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) -1, false, true);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) -1, true, false);
            addServerExceptionHelper("invalid value succeeded", adminFacade,
                    type, "", (byte) -1, false, false);

            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) -1, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) -1, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) -1, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 0, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 0, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) 0, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 50, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 50, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) 50, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 100, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 100, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) 100, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 200, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 200, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) 200, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) -1, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) -1, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) -1, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 0, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 0, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) 0, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 50, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 50, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) 50, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 100, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 100, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) 100, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 200, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 200, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) 200, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) -1, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) -1, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) -1, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 0, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 0, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) 0, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 50, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 50, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) 50, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 100, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 100, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) 100, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 200, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 200, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) 200, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) -1, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) -1, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) -1, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 0, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 0, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) 0, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 50, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 50, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) 50, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 100, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 100, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) 100, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, "", (byte) 200, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, null, (byte) 200, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    nullFacade, type, getHostname(), (byte) 200, false, false);

            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) -1, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) -1, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) -1, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 0, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 0, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) 0, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 50, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 50, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) 50, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 100, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 100, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) 100, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 200, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 200, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) 200, true, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) -1, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) -1, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) -1, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 0, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 0, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) 0, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 50, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 50, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) 50, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 100, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 100, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) 100, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 200, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 200, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) 200, false, true);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) -1, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) -1, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) -1, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 0, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 0, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) 0, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 50, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 50, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) 50, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 100, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 100, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) 100, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 200, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 200, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) 200, true, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) -1, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) -1, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) -1, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 0, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 0, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) 0, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 50, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 50, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) 50, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 100, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 100, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) 100, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, "", (byte) 200, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, null, (byte) 200, false, false);
            addServerExceptionHelper("Null Facade action succeeded",
                    userFacade, type, getHostname(), (byte) 200, false, false);

            String hostname = getHostname();
            adminFacade.addServer(type, hostname, (byte) 50, true, true);
            adminFacade.addServer(type, hostname, (byte) 50, true, true);

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
            assertNotNull(testServer);

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
        }

    }

    /**
     * Test method for {@link SystemFacade#setSettings(SystemSettings)}.
     */
    @Test
    public void testSettings() throws TransactionException {
        SystemSettings setterSettings = new SystemSettings();
        SystemSettings getterSettings;
        Calendar modDate;

        getterSettings = DecidrGlobals.getSettings();
        assertNotNull(getterSettings);
        User admin = getterSettings.getSuperAdmin();

        setterSettings.setSuperAdmin(admin);
        setterSettings.setAutoAcceptNewTenants(true);
        setterSettings.setChangeEmailRequestLifetimeSeconds(20);
        setterSettings.setBaseUrl("decidr.de");
        setterSettings.setInvitationLifetimeSeconds(10);
        setterSettings.setLogLevel("DEBUG");
        setterSettings.setMaxAttachmentsPerEmail(10);
        setterSettings.setMaxServerLoadForShutdown((byte) 100);
        setterSettings.setMaxServerLoadForUnlock((byte) 100);
        setterSettings.setMaxUploadFileSizeBytes(1);
        setterSettings.setMaxWorkflowInstancesForShutdown(1);
        setterSettings.setMaxWorkflowInstancesForUnlock(1);
        setterSettings.setMinServerLoadForLock((byte) 100);
        setterSettings.setMinUnlockedServers(1);
        setterSettings.setMinWorkflowInstancesForLock(1);
        modDate = DecidrGlobals.getTime();
        modDate.set(Calendar.MILLISECOND, 0);
        setterSettings.setModifiedDate(modDate.getTime());
        setterSettings.setMonitorAveragingPeriodSeconds(60);
        setterSettings.setMonitorUpdateIntervalSeconds(10);
        setterSettings.setMtaHostname("localhost");
        setterSettings.setMtaPassword("asdfg");
        setterSettings.setMtaPort(0);
        setterSettings.setMtaUsername("asd");
        setterSettings.setMtaUseTls(true);
        setterSettings.setPasswordResetRequestLifetimeSeconds(200);
        setterSettings.setRegistrationRequestLifetimeSeconds(2000);
        setterSettings.setServerPoolInstances(3);
        setterSettings.setSystemEmailAddress("decidr@decidr.biz");
        setterSettings.setSystemName("De Cidr");
        adminFacade.setSettings(setterSettings);

        getterSettings = DecidrGlobals.getSettings();
        assertEquals(true, getterSettings.isAutoAcceptNewTenants());
        assertEquals(true, getterSettings.isMtaUseTls());
        assertEquals(20, getterSettings.getChangeEmailRequestLifetimeSeconds());
        assertEquals("decidr.de", getterSettings.getBaseUrl());
        assertEquals(10, getterSettings.getInvitationLifetimeSeconds());
        assertEquals("DEBUG", getterSettings.getLogLevel());
        assertEquals(10, getterSettings.getMaxAttachmentsPerEmail());
        assertEquals((byte) 100, getterSettings.getMaxServerLoadForShutdown());
        assertEquals((byte) 100, getterSettings.getMaxServerLoadForUnlock());
        assertEquals(1L, getterSettings.getMaxUploadFileSizeBytes());
        assertEquals(1, getterSettings.getMaxWorkflowInstancesForShutdown());
        assertEquals(1, getterSettings.getMaxWorkflowInstancesForUnlock());
        assertEquals((byte) 100, getterSettings.getMinServerLoadForLock());
        assertEquals(1, getterSettings.getMinUnlockedServers());
        assertEquals(1, getterSettings.getMinWorkflowInstancesForLock());
        assertEquals(60, getterSettings.getMonitorAveragingPeriodSeconds());
        assertEquals(10, getterSettings.getMonitorUpdateIntervalSeconds());
        assertEquals("localhost", getterSettings.getMtaHostname());
        assertEquals("asdfg", getterSettings.getMtaPassword());
        assertEquals(0, getterSettings.getMtaPort());
        assertEquals("asd", getterSettings.getMtaUsername());
        assertEquals(200, getterSettings
                .getPasswordResetRequestLifetimeSeconds());
        assertEquals(2000, getterSettings
                .getRegistrationRequestLifetimeSeconds());
        assertEquals(3, getterSettings.getServerPoolInstances());
        assertEquals("decidr@decidr.biz", getterSettings
                .getSystemEmailAddress());
        assertEquals("De Cidr", getterSettings.getSystemName());
        assertFalse(modDate.getTime().after(getterSettings.getModifiedDate()));

        setterSettings.setAutoAcceptNewTenants(false);
        setterSettings.setChangeEmailRequestLifetimeSeconds(150);
        setterSettings.setBaseUrl("decidr.eu");
        setterSettings.setInvitationLifetimeSeconds(1450);
        setterSettings.setLogLevel("ERROR");
        setterSettings.setMaxAttachmentsPerEmail(0);
        setterSettings.setMaxServerLoadForShutdown((byte) 10);
        setterSettings.setMaxServerLoadForUnlock((byte) 10);
        setterSettings.setMaxUploadFileSizeBytes(100);
        setterSettings.setMaxWorkflowInstancesForShutdown(100);
        setterSettings.setMaxWorkflowInstancesForUnlock(10);
        setterSettings.setMinServerLoadForLock((byte) 10);
        setterSettings.setMinUnlockedServers(10);
        setterSettings.setMinWorkflowInstancesForLock(10);
        modDate.add(Calendar.SECOND, -1000);
        setterSettings.setModifiedDate(modDate.getTime());
        setterSettings.setMonitorAveragingPeriodSeconds(600);
        setterSettings.setMonitorUpdateIntervalSeconds(1);
        setterSettings.setMtaPort(22);
        setterSettings.setMtaUseTls(false);
        setterSettings.setPasswordResetRequestLifetimeSeconds(20);
        setterSettings.setRegistrationRequestLifetimeSeconds(2);
        setterSettings.setServerPoolInstances(1);
        setterSettings.setSystemEmailAddress("dumbo@decidr.eu");
        setterSettings.setSystemName("Darth Vader");
        adminFacade.setSettings(setterSettings);

        SystemSettings oldSettings = getterSettings;
        // Notice: due to the way Hibernate works, DecidrGlobals.getSettings
        // might return the same SystemSettings object every time you call it
        // from within your current thread.
        getterSettings = DecidrGlobals.getSettings();
        assertNotSame(oldSettings, getterSettings);
        assertEquals(false, getterSettings.isAutoAcceptNewTenants());
        assertEquals(false, getterSettings.isMtaUseTls());
        assertEquals(150, getterSettings.getChangeEmailRequestLifetimeSeconds());
        assertEquals("decidr.eu", getterSettings.getBaseUrl());
        assertEquals(1450, getterSettings.getInvitationLifetimeSeconds());
        assertEquals("ERROR", getterSettings.getLogLevel());
        assertEquals(0, getterSettings.getMaxAttachmentsPerEmail());
        assertEquals((byte) 10, getterSettings.getMaxServerLoadForShutdown());
        assertEquals((byte) 10, getterSettings.getMaxServerLoadForUnlock());
        assertEquals(100L, getterSettings.getMaxUploadFileSizeBytes());
        assertEquals(100, getterSettings.getMaxWorkflowInstancesForShutdown());
        assertEquals(10, getterSettings.getMaxWorkflowInstancesForUnlock());
        assertEquals((byte) 10, getterSettings.getMinServerLoadForLock());
        assertEquals(10, getterSettings.getMinUnlockedServers());
        assertEquals(10, getterSettings.getMinWorkflowInstancesForLock());
        assertEquals(600, getterSettings.getMonitorAveragingPeriodSeconds());
        assertEquals(1, getterSettings.getMonitorUpdateIntervalSeconds());
        assertEquals(22, getterSettings.getMtaPort());
        assertEquals(20, getterSettings
                .getPasswordResetRequestLifetimeSeconds());
        assertEquals(2, getterSettings.getRegistrationRequestLifetimeSeconds());
        assertEquals(1, getterSettings.getServerPoolInstances());
        assertEquals("dumbo@decidr.eu", getterSettings.getSystemEmailAddress());
        assertEquals("Darth Vader", getterSettings.getSystemName());
        assertFalse(modDate.getTime().after(getterSettings.getModifiedDate()));

        setterSettings.setMtaHostname("");
        setterSettings.setMtaUsername("");
        setterSettings.setMtaPassword("");
        setterSettings.setSystemName("");
        adminFacade.setSettings(setterSettings);

        getterSettings = DecidrGlobals.getSettings();
        assertEquals("", getterSettings.getMtaHostname());
        assertEquals("", getterSettings.getMtaPassword());
        assertEquals("", getterSettings.getMtaUsername());
        assertEquals("", getterSettings.getSystemName());

        try {
            adminFacade.setSettings(null);
            fail("managed to set null settings");
        } catch (IllegalArgumentException e) {
            // should be thrown
        }
        setSettingsExceptionHelper("managed to set null settings", adminFacade,
                new SystemSettings());

        setSettingsExceptionHelper(
                "setting settings with null facade succeeded", nullFacade,
                setterSettings);
        setSettingsExceptionHelper(
                "setting settings with normal user facade succeeded",
                userFacade, setterSettings);

        setterSettings.setChangeEmailRequestLifetimeSeconds(-1);
        setSettingsExceptionHelper(
                "invalid ChangeEmailRequestLifetimeSeconds succeeded",
                adminFacade, setterSettings);
        setterSettings.setChangeEmailRequestLifetimeSeconds(1);

        setterSettings.setBaseUrl(null);
        setSettingsExceptionHelper("null domain succeeded", adminFacade,
                setterSettings);
        setterSettings.setBaseUrl("");
        setSettingsExceptionHelper("empty domain succeeded", adminFacade,
                setterSettings);
        setterSettings.setBaseUrl("decidr.de");

        setterSettings.setInvitationLifetimeSeconds(-1);
        setSettingsExceptionHelper(
                "invalid InvitationLifetimeSeconds succeeded", adminFacade,
                setterSettings);
        setterSettings.setInvitationLifetimeSeconds(1);

        setterSettings.setLogLevel("INVALID");
        setSettingsExceptionHelper("invalid loglevel succeeded", adminFacade,
                setterSettings);
        setterSettings.setLogLevel("ERROR");

        setterSettings.setMaxAttachmentsPerEmail(-1);
        setSettingsExceptionHelper("invalid amount of attachments succeeded",
                adminFacade, setterSettings);
        setterSettings.setMaxAttachmentsPerEmail(1);

        setterSettings.setMaxServerLoadForShutdown((byte) -1);
        setSettingsExceptionHelper(
                "invalid MaxServerLoadForShutdown succeeded", adminFacade,
                setterSettings);
        setterSettings.setMaxServerLoadForShutdown((byte) 1);

        setterSettings.setMaxServerLoadForUnlock((byte) -1);
        setSettingsExceptionHelper("invalid MaxServerLoadForUnlock succeeded",
                adminFacade, setterSettings);
        setterSettings.setMaxServerLoadForUnlock((byte) 1);

        setterSettings.setMaxUploadFileSizeBytes(-1);
        setSettingsExceptionHelper("invalid MaxUploadFileSizeBytes succeeded",
                adminFacade, setterSettings);
        setterSettings.setMaxUploadFileSizeBytes(1);

        setterSettings.setMaxWorkflowInstancesForShutdown(-1);
        setSettingsExceptionHelper(
                "invalid MaxWorkflowInstancesForShutdown succeeded",
                adminFacade, setterSettings);
        setterSettings.setMaxWorkflowInstancesForShutdown(1);

        setterSettings.setMaxWorkflowInstancesForUnlock(-1);
        setSettingsExceptionHelper(
                "invalid MaxWorkflowInstancesForUnlock succeeded", adminFacade,
                setterSettings);
        setterSettings.setMaxWorkflowInstancesForUnlock(1);

        setterSettings.setMinServerLoadForLock((byte) -1);
        setSettingsExceptionHelper("invalid MinServerLoadForLock succeeded",
                adminFacade, setterSettings);
        setterSettings.setMinServerLoadForLock((byte) 1);

        setterSettings.setMinUnlockedServers(-1);
        setSettingsExceptionHelper("invalid MinUnlockedServers succeeded",
                adminFacade, setterSettings);
        setterSettings.setMinUnlockedServers(1);

        setterSettings.setMinWorkflowInstancesForLock(-1);
        setSettingsExceptionHelper(
                "invalid MinWorkflowInstancesForLock succeeded", adminFacade,
                setterSettings);
        setterSettings.setMinWorkflowInstancesForLock(1);

        setterSettings.setMonitorAveragingPeriodSeconds(-1);
        setSettingsExceptionHelper(
                "invalid MonitorAveragingPeriodSeconds succeeded", adminFacade,
                setterSettings);
        setterSettings.setMonitorAveragingPeriodSeconds(1);

        setterSettings.setMonitorUpdateIntervalSeconds(-1);
        setSettingsExceptionHelper(
                "invalid MonitorUpdateIntervalSeconds succeeded", adminFacade,
                setterSettings);
        setterSettings.setMonitorUpdateIntervalSeconds(1);

        setterSettings.setPasswordResetRequestLifetimeSeconds(-1);
        setSettingsExceptionHelper(
                "invalid PasswordResetRequestLifetimeSeconds succeeded",
                adminFacade, setterSettings);
        setterSettings.setPasswordResetRequestLifetimeSeconds(1);

        setterSettings.setRegistrationRequestLifetimeSeconds(-1);
        setSettingsExceptionHelper(
                "invalid RegistrationRequestLifetimeSeconds succeeded",
                adminFacade, setterSettings);
        setterSettings.setRegistrationRequestLifetimeSeconds(1);

        setterSettings.setServerPoolInstances(-1);
        setSettingsExceptionHelper("invalid ServerPoolInstances succeeded",
                adminFacade, setterSettings);
        setterSettings.setServerPoolInstances(1);

        setterSettings.setSystemEmailAddress("in@valid@email");
        setSettingsExceptionHelper("invalid email address succeeded",
                adminFacade, setterSettings);
        setterSettings.setSystemEmailAddress("");
        setSettingsExceptionHelper("empty email address succeeded",
                adminFacade, setterSettings);
        setterSettings.setSystemEmailAddress(null);
        setSettingsExceptionHelper("null email address succeeded", adminFacade,
                setterSettings);
        setterSettings.setSystemEmailAddress("invalid@email.de");

        setterSettings.setSystemName(null);
        setSettingsExceptionHelper("null system name succeeded", adminFacade,
                setterSettings);
        setterSettings.setSystemName("DecidR");

        adminFacade.setSettings(setterSettings);
    }
}
