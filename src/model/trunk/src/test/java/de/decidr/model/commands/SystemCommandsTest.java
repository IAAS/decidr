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

import java.util.Date;
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
import de.decidr.model.entities.User;
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
            // DH what are legal values for this?
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

        HibernateTransactionCoordinator.getInstance().runTransaction(
                serversSuperNull);
        Long serverID = serversSuperNull.getResult().get(0).getId();
        LockServerCommand lockerSuper = new LockServerCommand(
                new SuperAdminRole(), serverID, true);
        LockServerCommand unlockerSuper = new LockServerCommand(
                new SuperAdminRole(), serverID, false);
        LockServerCommand lockerBasic = new LockServerCommand(
                new BasicRole(0L), serverID, true);
        LockServerCommand unlockerBasic = new LockServerCommand(new BasicRole(
                0L), serverID, false);

        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    lockerBasic);
            fail("BasicRole shouldn't be able to lock servers.");
        } catch (TransactionException e) {
            // Exception is supposed to be thrown
        }

        HibernateTransactionCoordinator.getInstance().runTransaction(
                lockerSuper);
        // RR check that the server was locked

        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    unlockerBasic);
            fail("BasicRole shouldn't be able to unlock servers.");
        } catch (TransactionException e) {
            // Exception is supposed to be thrown
        }

        HibernateTransactionCoordinator.getInstance().runTransaction(
                unlockerSuper);
        // RR gibt es eine Methode einen Server zu bekommen wenn man die
        // ID kennt, außer alle zu holen und durchzuiterieren? (ohne hibernate
        // direkt zu benutzen!)
        // RR check that the server was unlocked

        UpdateServerLoadCommand loader;
        byte[] goodLoads = new byte[] { 0, -1, 100, 50 };
        byte[] badLoads = new byte[] { -2, -100, 101, 127 };
        for (byte b : goodLoads) {
            loader = new UpdateServerLoadCommand(new SuperAdminRole(),
                    serverID, b);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(loader);
            // RR check that the server got the correct load
        }

        for (byte b : goodLoads) {
            loader = new UpdateServerLoadCommand(new BasicRole(0L), serverID, b);
            try {
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        loader);
                fail("BasicRole shouldn't be able to update server load.");
            } catch (TransactionException e) {
                // is supposed to be thrown
            }
        }

        for (byte b : badLoads) {
            loader = new UpdateServerLoadCommand(new SuperAdminRole(),
                    serverID, b);
            try {
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        loader);
                fail("shouldn't be able to set this value.");
            } catch (TransactionException e) {
                // is supposed to be thrown
            }
        }

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
    public void testSystemSettingsCommands() throws TransactionException {
        SystemSettings settings = new SystemSettings();
        SetSystemSettingsCommand setter = new SetSystemSettingsCommand(
                new SuperAdminRole(), settings);
        GetSystemSettingsCommand getter = new GetSystemSettingsCommand(
                new SuperAdminRole());
        SetSystemSettingsCommand userSetter = new SetSystemSettingsCommand(
                new SuperAdminRole(), settings);
        GetSystemSettingsCommand userGetter = new GetSystemSettingsCommand(
                new SuperAdminRole());

        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    userSetter);
            fail("User shouldn't be able to set settings");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        try {
            // DH & MF: should this throw an error, set all values to defaults
            // or change nothing? ~rr
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("Shouldn't be able to set empty settings");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        settings.setAutoAcceptNewTenants(true);
        settings.setChangeEmailRequestLifetimeSeconds(20);
        settings.setDomain("decidr.de");
        settings.setId(-1L);
        settings.setInvitationLifetimeSeconds(10);
        settings.setLogLevel("DEBUG");
        settings.setMaxAttachmentsPerEmail(10);
        settings.setMaxServerLoadForShutdown((byte) 100);
        settings.setMaxServerLoadForUnlock((byte) 100);
        settings.setMaxUploadFileSizeBytes(1);
        settings.setMaxWorkflowInstancesForShutdown(1);
        settings.setMaxWorkflowInstancesForUnlock(1);
        settings.setMinServerLoadForLock((byte) 100);
        settings.setMinUnlockedServers(1);
        settings.setMinWorkflowInstancesForLock(1);
        settings.setModifiedDate(DecidrGlobals.getTime().getTime());
        settings.setMonitorAveragingPeriodSeconds(60);
        settings.setMonitorUpdateIntervalSeconds(10);
        settings.setMtaHostname("localhost");
        settings.setMtaPassword("asdfg");
        settings.setMtaPort(-1);
        settings.setMtaUsername("asd");
        settings.setMtaUseTls(true);
        settings.setPasswordResetRequestLifetimeSeconds(200);
        settings.setRegistrationRequestLifetimeSeconds(2000);
        settings.setServerPoolInstances(3);
        settings.setSuperAdmin(new User());
        settings.setSystemEmailAddress("decidr@decidr.biz");
        settings.setSystemName("De Cidr");
        HibernateTransactionCoordinator.getInstance().runTransaction(setter);

        // RR check for equality (i.e. test getter)

        settings.setAutoAcceptNewTenants(false);
        settings.setChangeEmailRequestLifetimeSeconds(150);
        settings.setDomain("decidr.eu");
        settings.setId(100L);
        settings.setInvitationLifetimeSeconds(1450);
        settings.setLogLevel("ERROR");
        settings.setMaxAttachmentsPerEmail(2);
        settings.setMaxServerLoadForShutdown((byte) 10);
        settings.setMaxServerLoadForUnlock((byte) 10);
        settings.setMaxUploadFileSizeBytes(100);
        settings.setMaxWorkflowInstancesForShutdown(100);
        settings.setMaxWorkflowInstancesForUnlock(10);
        settings.setMinServerLoadForLock((byte) 10);
        settings.setMinUnlockedServers(10);
        settings.setMinWorkflowInstancesForLock(10);
        settings.setModifiedDate(new Date(DecidrGlobals.getTime()
                .getTimeInMillis() - 1000000));
        // RR change values
        settings.setMonitorAveragingPeriodSeconds(600);
        settings.setMonitorUpdateIntervalSeconds(1);
        settings.setMtaHostname("example.com");
        settings.setMtaPassword("wewewe");
        settings.setMtaPort(22);
        settings.setMtaUsername(null);
        settings.setMtaUseTls(false);
        settings.setPasswordResetRequestLifetimeSeconds(20);
        settings.setRegistrationRequestLifetimeSeconds(2);
        settings.setServerPoolInstances(1);
        settings.setSuperAdmin(new User(DecidrGlobals.getTime().getTime()));
        settings.setSystemEmailAddress("dumbo@decidr.eu");
        settings.setSystemName("Darth Vader");
        HibernateTransactionCoordinator.getInstance().runTransaction(setter);

        // RR check for equality (i.e. test getter)

        try {
            settings.setChangeEmailRequestLifetimeSeconds(-1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid ChangeEmailRequestLifetimeSeconds succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setDomain(null);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("null domain succeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            // MF & DH: should this fail or return a default domain
            // (?localhost?) ~rr
            settings.setDomain("");
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("empty domain succeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        // RR test illegal values
        try {
            settings.setId(-1L);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setInvitationLifetimeSeconds(10);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setLogLevel("DEBUG");
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setMaxAttachmentsPerEmail(10);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setMaxServerLoadForShutdown((byte) 100);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setMaxServerLoadForUnlock((byte) 100);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setMaxUploadFileSizeBytes(1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setMaxWorkflowInstancesForShutdown(1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setMaxWorkflowInstancesForUnlock(1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setMinServerLoadForLock((byte) 100);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setMinUnlockedServers(1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setMinWorkflowInstancesForLock(1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setModifiedDate(DecidrGlobals.getTime().getTime());
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setMonitorAveragingPeriodSeconds(60);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setMonitorUpdateIntervalSeconds(10);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setMtaHostname("localhost");
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setMtaPassword("asdfg");
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setMtaPort(-1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setMtaUsername("asd");
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setPasswordResetRequestLifetimeSeconds(200);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setRegistrationRequestLifetimeSeconds(2000);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setServerPoolInstances(3);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setSuperAdmin(new User());
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setSystemEmailAddress("decidr@decidr.biz");
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            settings.setSystemName("De Cidr");
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    userGetter);
            fail("User shouldn't be able to get settings");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
    }
}
