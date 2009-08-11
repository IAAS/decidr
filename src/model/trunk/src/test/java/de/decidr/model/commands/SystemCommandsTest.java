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

package de.decidr.model.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.commands.system.AddServerCommand;
import de.decidr.model.commands.system.GetFileCommand;
import de.decidr.model.commands.system.GetLogCommand;
import de.decidr.model.commands.system.GetServerStatisticsCommand;
import de.decidr.model.commands.system.GetServersCommand;
import de.decidr.model.commands.system.GetSystemSettingsCommand;
import de.decidr.model.commands.system.LockServerCommand;
import de.decidr.model.commands.system.RemoveServerCommand;
import de.decidr.model.commands.system.SetSystemSettingsCommand;
import de.decidr.model.commands.system.UpdateServerLoadCommand;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

/**
 * This class tests the commands in <code>de.decidr.model.commands.system</code>
 * .
 * 
 * @author Reinhold
 */
public class SystemCommandsTest {

    /**
     * Test method for
     * {@link AddServerCommand#AddServerCommand(Role, ServerTypeEnum, String, Byte, Boolean, Boolean)}
     * , {@link GetServersCommand#GetServersCommand(Role, ServerTypeEnum[])},
     * {@link GetServerStatisticsCommand#GetServerStatisticsCommand(Role)},
     * {@link LockServerCommand#LockServerCommand(Role, Long, Boolean)},
     * {@link UpdateServerLoadCommand#UpdateServerLoadCommand(Role, Long, byte)}
     * and {@link RemoveServerCommand#RemoveServerCommand(Role, Long)} .
     */
    @Test
    public void testServerCommands() throws TransactionException {
        Set<AddServerCommand> goodCommands = new HashSet<AddServerCommand>();
        Set<AddServerCommand> badCommands = new HashSet<AddServerCommand>();
        for (ServerTypeEnum serverType : ServerTypeEnum.values()) {
            goodCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, null, (byte) 0, false, false));
            goodCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, "", (byte) 0, false, false));
            goodCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, null, (byte) 0, false, true));
            goodCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, "", (byte) 0, true, true));
            goodCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, null, (byte) 0, true, false));
            goodCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, "", (byte) 0, true, false));
            goodCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, null, (byte) 0, true, true));
            goodCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, "", (byte) 0, true, true));

            goodCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, null, (byte) -1, false, false));
            goodCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, "", (byte) -1, false, false));
            goodCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, null, (byte) -1, false, true));
            goodCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, "", (byte) -1, true, true));
            goodCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, null, (byte) -1, true, false));
            goodCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, "", (byte) -1, true, false));
            goodCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, null, (byte) -1, true, true));
            goodCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, "", (byte) -1, true, true));

            badCommands.add(new AddServerCommand(new BasicRole(0L), serverType,
                    null, (byte) 0, false, false));
            badCommands.add(new AddServerCommand(new BasicRole(0L), serverType,
                    "", (byte) 0, false, false));
            badCommands.add(new AddServerCommand(new BasicRole(0L), serverType,
                    null, (byte) 0, false, true));
            badCommands.add(new AddServerCommand(new BasicRole(0L), serverType,
                    "", (byte) 0, true, true));
            badCommands.add(new AddServerCommand(new BasicRole(0L), serverType,
                    null, (byte) 0, true, false));
            badCommands.add(new AddServerCommand(new BasicRole(0L), serverType,
                    "", (byte) 0, true, false));
            badCommands.add(new AddServerCommand(new BasicRole(0L), serverType,
                    null, (byte) 0, true, true));
            badCommands.add(new AddServerCommand(new BasicRole(0L), serverType,
                    "", (byte) 0, true, true));

            badCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, null, (byte) -10, false, false));
            badCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, "", (byte) -10, false, false));
            badCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, null, (byte) -10, false, true));
            badCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, "", (byte) -10, true, true));
            badCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, null, (byte) -10, true, false));
            badCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, "", (byte) -10, true, false));
            badCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, null, (byte) -10, true, true));
            badCommands.add(new AddServerCommand(new SuperAdminRole(),
                    serverType, "", (byte) -10, true, true));
        }

        for (AddServerCommand addServerCommand : goodCommands) {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    addServerCommand);
        }

        for (AddServerCommand addServerCommand : badCommands) {
            try {
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        addServerCommand);
                fail("a bad command succeeded");
            } catch (TransactionException e) {
                // this is supposed to happen
            }
        }

        GetServersCommand serversSuperNull = new GetServersCommand(
                new SuperAdminRole(), (ServerTypeEnum) null);
        GetServersCommand serversBasicNull = new GetServersCommand(
                new BasicRole(0L), (ServerTypeEnum) null);

        HibernateTransactionCoordinator.getInstance().runTransaction(
                serversSuperNull);

        assertEquals(serversSuperNull.getResult().size(), goodCommands.size());

        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    serversBasicNull);
            fail("getting a list of all servers as basic user succeeded");
        } catch (TransactionException e) {
            // this is supposed to happen
        }

        for (ServerTypeEnum serverType : ServerTypeEnum.values()) {
            GetServersCommand serversSuperType = new GetServersCommand(
                    new SuperAdminRole(), serverType);
            GetServersCommand serversBasicType = new GetServersCommand(
                    new BasicRole(0L), serverType);

            HibernateTransactionCoordinator.getInstance().runTransaction(
                    serversSuperType);

            assertEquals(serversSuperType.getResult().size(), goodCommands
                    .size()
                    / ServerTypeEnum.values().length);

            try {
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        serversBasicType);
                fail("getting a list of servers as basic user succeeded");
            } catch (TransactionException e) {
                // this is supposed to happen
            }
        }

        GetServerStatisticsCommand stats = new GetServerStatisticsCommand(
                new SuperAdminRole());
        HibernateTransactionCoordinator.getInstance().runTransaction(stats);
        for (ServerLoadView serverLoadView : stats.getResult()) {
            // DH was für Werte darf so eine ID haben? ~rr
            assertTrue(serverLoadView.getId() >= 0);
            assertTrue(serverLoadView.getLastLoadUpdate() == null
                    || serverLoadView.getLastLoadUpdate().before(
                            DecidrGlobals.getTime().getTime()));
            assertTrue(serverLoadView.getLoad() >= -1);
            assertTrue(serverLoadView.getNumInstances() >= 0);
            // DH what's this meant to be?
            assertTrue(serverLoadView.getServerTypeId() > -1);
            try {
                ServerTypeEnum.valueOf(serverLoadView.getServerType());
            } catch (IllegalArgumentException e) {
                fail("Invalid server type");
            }
        }

        try {
            stats = new GetServerStatisticsCommand(new BasicRole(0L));
            HibernateTransactionCoordinator.getInstance().runTransaction(stats);
            fail("Normal user shouldn't be able to get server stats");
        } catch (TransactionException e) {
            // is supposed to be thrown
        }

        // RR do LockServerCommand

        // RR do UpdateServerLoadCommand

        GetServersCommand getAllServers = new GetServersCommand(
                new SuperAdminRole(), (ServerTypeEnum) null);
        HibernateTransactionCoordinator.getInstance().runTransaction(
                getAllServers);

        Set<RemoveServerCommand> delete = new HashSet<RemoveServerCommand>();
        for (Server server : getAllServers.getResult()) {
            delete.add(new RemoveServerCommand(new SuperAdminRole(), server
                    .getId()));
        }
        HibernateTransactionCoordinator.getInstance().runTransaction(delete);

        HibernateTransactionCoordinator.getInstance().runTransaction(
                getAllServers);
        assertTrue(getAllServers.getResult() == null
                || getAllServers.getResult().isEmpty());
    }

    /**
     * Test method for {@link GetFileCommand#GetFileCommand(Long)}.
     */
    @Test
    public void testGetFileCommand() {
        fail("Not yet implemented"); // RR GetFileCommand
    }

    /**
     * Test method for
     * {@link GetLogCommand#GetLogCommand(Role, List, Paginator)}.
     */
    @Test
    public void testGetLogCommand() {
        fail("Not yet implemented"); // RR GetLogCommand
    }

    /**
     * Test method for
     * {@link SetSystemSettingsCommand#SetSystemSettingsCommand(Role, SystemSettings)}
     * and {@link GetSystemSettingsCommand#GetSystemSettingsCommand(Role)}.
     */
    @Test
    public void testSystemSettingsCommands() {
        fail("Not yet implemented"); // RR SetSystemSettingsCommand
        fail("Not yet implemented"); // RR GetSystemSettingsCommand
    }
}
