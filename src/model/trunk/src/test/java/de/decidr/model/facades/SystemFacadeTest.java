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

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.TransactionTest;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;
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
public class SystemFacadeTest extends TransactionTest {

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
        fail("Not yet implemented"); // RR addServer
        fail("Not yet implemented"); // RR getServers
        fail("Not yet implemented"); // RR getServerStatistics
        fail("Not yet implemented"); // RR updateServerLoad
        fail("Not yet implemented"); // RR setServerLock
        fail("Not yet implemented"); // RR removeServer
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
