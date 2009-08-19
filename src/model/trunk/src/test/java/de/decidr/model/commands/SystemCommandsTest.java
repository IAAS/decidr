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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
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
import de.decidr.model.entities.File;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.entities.User;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
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

            badCommands.add(new AddServerCommand(null, serverType, null,
                    (byte) 0, false, false));
            badCommands.add(new AddServerCommand(null, serverType, "",
                    (byte) 0, false, false));
            badCommands.add(new AddServerCommand(null, serverType, null,
                    (byte) 0, false, true));
            badCommands.add(new AddServerCommand(null, serverType, "",
                    (byte) 0, true, true));
            badCommands.add(new AddServerCommand(null, serverType, null,
                    (byte) 0, true, false));
            badCommands.add(new AddServerCommand(null, serverType, "",
                    (byte) 0, true, false));
            badCommands.add(new AddServerCommand(null, serverType, null,
                    (byte) 0, true, true));
            badCommands.add(new AddServerCommand(null, serverType, "",
                    (byte) 0, true, true));

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
        GetServersCommand serversNullNull = new GetServersCommand(null,
                (ServerTypeEnum) null);

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
        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    serversNullNull);
            fail("getting a list of all servers as null user succeeded");
        } catch (TransactionException e) {
            // this is supposed to happen
        }

        for (ServerTypeEnum serverType : ServerTypeEnum.values()) {
            GetServersCommand serversSuperType = new GetServersCommand(
                    new SuperAdminRole(), serverType);
            GetServersCommand serversBasicType = new GetServersCommand(
                    new BasicRole(0L), serverType);
            GetServersCommand serversNullType = new GetServersCommand(null,
                    serverType);

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
            try {
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        serversNullType);
                fail("getting a list of servers as null user succeeded");
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
        try {
            stats = new GetServerStatisticsCommand(null);
            HibernateTransactionCoordinator.getInstance().runTransaction(stats);
            fail("Null user shouldn't be able to get server stats");
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
        LockServerCommand unlockerBasic = new LockServerCommand(null, serverID,
                false);
        LockServerCommand lockerNull = new LockServerCommand(new BasicRole(0L),
                serverID, true);
        LockServerCommand unlockerNull = new LockServerCommand(null, serverID,
                false);

        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    lockerBasic);
            fail("BasicRole shouldn't be able to lock servers.");
        } catch (TransactionException e) {
            // Exception is supposed to be thrown
        }
        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    lockerNull);
            fail("Null role shouldn't be able to lock servers.");
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
        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    unlockerNull);
            fail("Null role shouldn't be able to unlock servers.");
        } catch (TransactionException e) {
            // Exception is supposed to be thrown
        }

        HibernateTransactionCoordinator.getInstance().runTransaction(
                unlockerSuper);
        // DH & MF: gibt es eine Methode einen Server zu bekommen wenn man die
        // ID kennt, außer alle zu holen und durchzuiterieren? (ohne hibernate
        // direkt zu benutzen!) ~rr
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
            loader = new UpdateServerLoadCommand(null, serverID, b);
            try {
                HibernateTransactionCoordinator.getInstance().runTransaction(
                        loader);
                fail("Null role shouldn't be able to update server load.");
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
    public void testGetFileCommand() throws TransactionException {
        File decidrFileA = new File("testfile", "text/plain", true, 0);
        File decidrFileB = new File("testfile", "text/plain", true, 100);
        File invalidDecidrFile = new File("testfile", "text/plain", true, -1);

        // RR set files

        GetFileCommand getterA = new GetFileCommand(decidrFileA.getId());
        GetFileCommand getterB = new GetFileCommand(decidrFileB.getId());

        GetFileCommand getterInvalid = new GetFileCommand(invalidDecidrFile
                .getId());

        HibernateTransactionCoordinator.getInstance().runTransaction(getterA);
        HibernateTransactionCoordinator.getInstance().runTransaction(getterB);

        assertEquals(decidrFileA.getFileName(), getterA.getFile().getFileName());
        assertEquals(decidrFileA.getMimeType(), getterA.getFile().getMimeType());
        assertEquals(decidrFileA.isMayPublicRead(), getterA.getFile()
                .isMayPublicRead());
        assertEquals(decidrFileA.getFileSizeBytes(), getterA.getFile()
                .getFileSizeBytes());

        assertEquals(decidrFileB.getFileName(), getterB.getFile().getFileName());
        assertEquals(decidrFileB.getMimeType(), getterB.getFile().getMimeType());
        assertEquals(decidrFileB.isMayPublicRead(), getterB.getFile()
                .isMayPublicRead());
        assertEquals(decidrFileB.getFileSizeBytes(), getterB.getFile()
                .getFileSizeBytes());

        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    getterInvalid);
            fail("managed to get file with negative size");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
    }

    /**
     * Test method for
     * {@link GetLogCommand#GetLogCommand(Role, List, Paginator)}.
     */
    @Test
    public void testGetLogCommand() throws TransactionException {
        GetLogCommand getter = new GetLogCommand(new SuperAdminRole(),
                new ArrayList<Filter>(), new Paginator());
        HibernateTransactionCoordinator.getInstance().runTransaction(getter);
        assertNotNull(getter.getResult());

        // DH & MF: should any of the following three tests throw errors? ~rr
        getter = new GetLogCommand(new SuperAdminRole(),
                new ArrayList<Filter>(), null);
        HibernateTransactionCoordinator.getInstance().runTransaction(getter);
        assertNotNull(getter.getResult());

        getter = new GetLogCommand(new SuperAdminRole(), null, new Paginator());
        HibernateTransactionCoordinator.getInstance().runTransaction(getter);
        assertNotNull(getter.getResult());

        getter = new GetLogCommand(new SuperAdminRole(), null, null);
        HibernateTransactionCoordinator.getInstance().runTransaction(getter);
        assertNotNull(getter.getResult());

        try {
            getter = new GetLogCommand(null, new ArrayList<Filter>(),
                    new Paginator());
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(getter);
            fail("managed to get file with null user");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            getter = new GetLogCommand(new BasicRole(0L),
                    new ArrayList<Filter>(), new Paginator());
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(getter);
            fail("managed to get file with basic role");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
    }

    /**
     * Test method for
     * {@link SetSystemSettingsCommand#SetSystemSettingsCommand(Role, SystemSettings)}
     * and {@link GetSystemSettingsCommand#GetSystemSettingsCommand(Role)}.
     */
    @Test
    public void testSystemSettingsCommands() throws TransactionException {
        SystemSettings setterSettings = new SystemSettings();
        SystemSettings getterSettings;
        Date modDate;
        SetSystemSettingsCommand setter = new SetSystemSettingsCommand(
                new SuperAdminRole(), setterSettings);
        GetSystemSettingsCommand getter = new GetSystemSettingsCommand(
                new SuperAdminRole());
        SetSystemSettingsCommand userSetter = new SetSystemSettingsCommand(
                new BasicRole(0L), setterSettings);
        GetSystemSettingsCommand userGetter = new GetSystemSettingsCommand(
                new BasicRole(0L));
        SetSystemSettingsCommand nullSetter = new SetSystemSettingsCommand(
                null, setterSettings);
        GetSystemSettingsCommand nullGetter = new GetSystemSettingsCommand(null);

        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    userSetter);
            fail("User shouldn't be able to set settings");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    nullSetter);
            fail("Null user shouldn't be able to set settings");
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

        setterSettings.setAutoAcceptNewTenants(true);
        setterSettings.setChangeEmailRequestLifetimeSeconds(20);
        setterSettings.setDomain("decidr.de");
        // DH & MF: what are legal ID values
        setterSettings.setId(-1L);
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
        modDate = DecidrGlobals.getTime().getTime();
        setterSettings.setModifiedDate(modDate);
        setterSettings.setMonitorAveragingPeriodSeconds(60);
        setterSettings.setMonitorUpdateIntervalSeconds(10);
        setterSettings.setMtaHostname("localhost");
        setterSettings.setMtaPassword("asdfg");
        setterSettings.setMtaPort(-1);
        setterSettings.setMtaUsername("asd");
        setterSettings.setMtaUseTls(true);
        setterSettings.setPasswordResetRequestLifetimeSeconds(200);
        setterSettings.setRegistrationRequestLifetimeSeconds(2000);
        setterSettings.setServerPoolInstances(3);
        // DH & MF: how to properly initialise user? ~rr
        setterSettings.setSuperAdmin(new User());
        setterSettings.setSystemEmailAddress("decidr@decidr.biz");
        setterSettings.setSystemName("De Cidr");
        HibernateTransactionCoordinator.getInstance().runTransaction(setter);

        HibernateTransactionCoordinator.getInstance().runTransaction(getter);
        getterSettings = getter.getResult();
        assertEquals(true, getterSettings.isAutoAcceptNewTenants());
        assertEquals(true, getterSettings.isMtaUseTls());
        assertEquals(20, getterSettings.getChangeEmailRequestLifetimeSeconds());
        assertEquals("decidr.de", getterSettings.getDomain());
        assertEquals(-1L, (long) getterSettings.getId());
        assertEquals(10, getterSettings.getInvitationLifetimeSeconds());
        assertEquals("DEBUG", getterSettings.getLogLevel());
        assertEquals(10, getterSettings.getMaxAttachmentsPerEmail());
        assertEquals((byte) 100, getterSettings.getMaxServerLoadForShutdown());
        assertEquals((byte) 100, getterSettings.getMaxServerLoadForUnlock());
        assertEquals(1, getterSettings.getMaxUploadFileSizeBytes());
        assertEquals(1, getterSettings.getMaxWorkflowInstancesForShutdown());
        assertEquals(1, getterSettings.getMaxWorkflowInstancesForUnlock());
        assertEquals((byte) 100, getterSettings.getMinServerLoadForLock());
        assertEquals(1, getterSettings.getMinUnlockedServers());
        assertEquals(1, getterSettings.getMinWorkflowInstancesForLock());
        assertEquals(modDate, getterSettings.getModifiedDate());
        assertEquals(60, getterSettings.getMonitorAveragingPeriodSeconds());
        assertEquals(10, getterSettings.getMonitorUpdateIntervalSeconds());
        assertEquals("localhost", getterSettings.getMtaHostname());
        assertEquals("asdfg", getterSettings.getMtaPassword());
        assertEquals(-1, getterSettings.getMtaPort());
        assertEquals("asd", getterSettings.getMtaUsername());
        assertEquals(200, getterSettings
                .getPasswordResetRequestLifetimeSeconds());
        assertEquals(2000, getterSettings
                .getRegistrationRequestLifetimeSeconds());
        assertEquals(3, getterSettings.getServerPoolInstances());
        assertEquals(new User(), getterSettings.getSuperAdmin());
        assertEquals("decidr@decidr.biz", getterSettings
                .getSystemEmailAddress());
        assertEquals("De Cidr", getterSettings.getSystemName());

        setterSettings.setAutoAcceptNewTenants(false);
        setterSettings.setChangeEmailRequestLifetimeSeconds(150);
        setterSettings.setDomain("decidr.eu");
        setterSettings.setId(100L);
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
        modDate = new Date(DecidrGlobals.getTime().getTimeInMillis() - 1000000);
        setterSettings.setModifiedDate(modDate);
        setterSettings.setMonitorAveragingPeriodSeconds(600);
        setterSettings.setMonitorUpdateIntervalSeconds(1);
        setterSettings.setMtaHostname(null);
        setterSettings.setMtaPassword(null);
        setterSettings.setMtaPort(22);
        setterSettings.setMtaUsername(null);
        setterSettings.setMtaUseTls(false);
        setterSettings.setPasswordResetRequestLifetimeSeconds(20);
        setterSettings.setRegistrationRequestLifetimeSeconds(2);
        setterSettings.setServerPoolInstances(1);
        setterSettings
                .setSuperAdmin(new User(DecidrGlobals.getTime().getTime()));
        setterSettings.setSystemEmailAddress("dumbo@decidr.eu");
        setterSettings.setSystemName("Darth Vader");
        HibernateTransactionCoordinator.getInstance().runTransaction(setter);

        HibernateTransactionCoordinator.getInstance().runTransaction(getter);
        getterSettings = getter.getResult();
        assertEquals(false, getterSettings.isAutoAcceptNewTenants());
        assertEquals(false, getterSettings.isMtaUseTls());
        assertEquals(150, getterSettings.getChangeEmailRequestLifetimeSeconds());
        assertEquals("decidr.eu", getterSettings.getDomain());
        assertEquals(100L, (long) getterSettings.getId());
        assertEquals(1450, getterSettings.getInvitationLifetimeSeconds());
        assertEquals("ERROR", getterSettings.getLogLevel());
        assertEquals(0, getterSettings.getMaxAttachmentsPerEmail());
        assertEquals((byte) 10, getterSettings.getMaxServerLoadForShutdown());
        assertEquals((byte) 10, getterSettings.getMaxServerLoadForUnlock());
        assertEquals(100, getterSettings.getMaxUploadFileSizeBytes());
        assertEquals(100, getterSettings.getMaxWorkflowInstancesForShutdown());
        assertEquals(10, getterSettings.getMaxWorkflowInstancesForUnlock());
        assertEquals((byte) 10, getterSettings.getMinServerLoadForLock());
        assertEquals(10, getterSettings.getMinUnlockedServers());
        assertEquals(10, getterSettings.getMinWorkflowInstancesForLock());
        assertEquals(modDate, getterSettings.getModifiedDate());
        assertEquals(600, getterSettings.getMonitorAveragingPeriodSeconds());
        assertEquals(1, getterSettings.getMonitorUpdateIntervalSeconds());
        assertNull(getterSettings.getMtaHostname());
        assertNull(getterSettings.getMtaPassword());
        assertEquals(22, getterSettings.getMtaPort());
        assertNull(getterSettings.getMtaUsername());
        assertEquals(20, getterSettings
                .getPasswordResetRequestLifetimeSeconds());
        assertEquals(2, getterSettings.getRegistrationRequestLifetimeSeconds());
        assertEquals(1, getterSettings.getServerPoolInstances());
        assertEquals(new User(DecidrGlobals.getTime().getTime()),
                getterSettings.getSuperAdmin());
        assertEquals("dumbo@decidr.eu", getterSettings.getSystemEmailAddress());
        assertEquals("Darth Vader", getterSettings.getSystemName());

        setterSettings.setMtaHostname("");
        setterSettings.setMtaPort(-102);
        setterSettings.setMtaUsername("");
        setterSettings.setMtaPassword("");
        HibernateTransactionCoordinator.getInstance().runTransaction(setter);

        HibernateTransactionCoordinator.getInstance().runTransaction(getter);
        getterSettings = getter.getResult();
        assertEquals("", getterSettings.getMtaHostname());
        assertEquals("", getterSettings.getMtaPassword());
        assertEquals(-102, getterSettings.getMtaPort());
        assertEquals("", getterSettings.getMtaUsername());

        try {
            setterSettings.setChangeEmailRequestLifetimeSeconds(-1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid ChangeEmailRequestLifetimeSeconds succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setDomain(null);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("null domain succeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            // MF & DH: should this fail or return a default domain
            // (?localhost?) ~rr
            setterSettings.setDomain("");
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("empty domain succeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            // RR test as soon as legal values are known
            setterSettings.setId(-1L);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("illgal ID value succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setInvitationLifetimeSeconds(-1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid InvitationLifetimeSeconds succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setLogLevel("INVALID");
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid loglevel succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setMaxAttachmentsPerEmail(-1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid amount of attachments succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setMaxServerLoadForShutdown((byte) -1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid MaxServerLoadForShutdown succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setMaxServerLoadForUnlock((byte) -1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid MaxServerLoadForUnlock succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setMaxUploadFileSizeBytes(-1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid MaxUploadFileSizeBytes succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setMaxWorkflowInstancesForShutdown(-1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid MaxWorkflowInstancesForShutdown succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setMaxWorkflowInstancesForUnlock(-1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid MaxWorkflowInstancesForUnlock succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setMinServerLoadForLock((byte) -1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid MinServerLoadForLock succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setMinUnlockedServers(-1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid MinUnlockedServers succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setMinWorkflowInstancesForLock(-1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid MinWorkflowInstancesForLock succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setModifiedDate(new Date(DecidrGlobals.getTime()
                    .getTimeInMillis() + 10000000));
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid (future) ModifiedDate succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setMonitorAveragingPeriodSeconds(-1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid MonitorAveragingPeriodSeconds succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setMonitorUpdateIntervalSeconds(-1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid MonitorUpdateIntervalSeconds succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setPasswordResetRequestLifetimeSeconds(-1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid PasswordResetRequestLifetimeSeconds succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setRegistrationRequestLifetimeSeconds(-1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid RegistrationRequestLifetimeSeconds succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setServerPoolInstances(-1);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid ServerPoolInstances succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setSuperAdmin(null);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("null super admin succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setSystemEmailAddress("in@valid@email");
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("invalid email address ucceeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        // DH & MF: are the following two tests correct? ~rr
        try {
            setterSettings.setSystemName("");
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("empty system name succeeded");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            setterSettings.setSystemName(null);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(setter);
            fail("null system name succeeded");
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
        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    nullGetter);
            fail("Null user shouldn't be able to get settings");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
    }
}
