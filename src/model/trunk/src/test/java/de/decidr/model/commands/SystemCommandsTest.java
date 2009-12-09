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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Transaction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.commands.system.AddServerCommand;
import de.decidr.model.commands.system.GetLogCommand;
import de.decidr.model.commands.system.GetServerStatisticsCommand;
import de.decidr.model.commands.system.GetServersCommand;
import de.decidr.model.commands.system.GetSystemSettingsCommand;
import de.decidr.model.commands.system.LockServerCommand;
import de.decidr.model.commands.system.SetSystemSettingsCommand;
import de.decidr.model.commands.system.UpdateServerLoadCommand;
import de.decidr.model.commands.user.CreateNewUnregisteredUserCommand;
import de.decidr.model.commands.user.GetAllUsersCommand;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.entities.User;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.SystemFacadeTest;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.testing.CommandsTest;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

/**
 * This class tests the commands in <code>de.decidr.model.commands.system</code>
 * .
 * <p>
 * <em>NOTE: due to time constraints, {@link SystemFacadeTest} may be more complete in places!</em>
 * 
 * RR the system settings test breaks the system settings (MTA, email port set to 22...) ~dh
 * 
 * @author Reinhold
 */
public class SystemCommandsTest extends CommandsTest {

    private static Long SUPER_ADMIN_ID = DecidrGlobals.getSettings()
            .getSuperAdmin().getId();

    @AfterClass
    @BeforeClass
    public static void deleteServers() throws TransactionException {
        SystemFacadeTest.removeTestServers();
    }

    private Server getServer(long ID) throws TransactionException {
        GetServersCommand getAllServers = new GetServersCommand(
                new SuperAdminRole(SUPER_ADMIN_ID), (ServerTypeEnum) null);
        HibernateTransactionCoordinator.getInstance().runTransaction(
                getAllServers);

        for (Server s : getAllServers.getResult()) {
            if (s.getId() == ID) {
                return s;
            }
        }

        return null;
    }

    /**
     * Test method for
     * {@link GetLogCommand#GetLogCommand(Role, List, Paginator)}.
     */
    @Test
    public void testGetLogCommand() throws TransactionException {
        SuperAdminRole superAdminRole = new SuperAdminRole(SUPER_ADMIN_ID);

        GetLogCommand getter = new GetLogCommand(superAdminRole,
                new ArrayList<Filter>(), new Paginator());
        HibernateTransactionCoordinator.getInstance().runTransaction(getter);
        assertNotNull(getter.getResult());

        getter = new GetLogCommand(superAdminRole, new ArrayList<Filter>(),
                null);
        HibernateTransactionCoordinator.getInstance().runTransaction(getter);
        assertNotNull(getter.getResult());

        getter = new GetLogCommand(superAdminRole, null, new Paginator());
        HibernateTransactionCoordinator.getInstance().runTransaction(getter);
        assertNotNull(getter.getResult());

        getter = new GetLogCommand(superAdminRole, null, null);
        HibernateTransactionCoordinator.getInstance().runTransaction(getter);
        assertNotNull(getter.getResult());

        getter = new GetLogCommand(null, null, null);
        assertTransactionException("managed to get log with null user", getter);

        getter = new GetLogCommand(new BasicRole(0L), new ArrayList<Filter>(),
                new Paginator());
        assertTransactionException("managed to get file with basic role",
                getter);
    }

    /**
     * Test method for
     * {@link AddServerCommand#AddServerCommand(Role, ServerTypeEnum, String, Byte, Boolean, Boolean)}
     * , {@link GetServersCommand#GetServersCommand(Role, ServerTypeEnum[])},
     * {@link GetServerStatisticsCommand#GetServerStatisticsCommand(Role)},
     * {@link LockServerCommand#LockServerCommand(Role, Long, Boolean)},
     * {@link UpdateServerLoadCommand#UpdateServerLoadCommand(Role, Long, byte)}
     * .
     */
    @Test
    public void testServerCommands() throws TransactionException {
        Set<AddServerCommand> goodCommands = new HashSet<AddServerCommand>();
        Set<AddServerCommand> badCommands = new HashSet<AddServerCommand>();

        SuperAdminRole superAdminRole = new SuperAdminRole(SUPER_ADMIN_ID);

        for (ServerTypeEnum serverType : ServerTypeEnum.values()) {
            goodCommands.add(new AddServerCommand(superAdminRole, serverType,
                    SystemFacadeTest.getHostname(), (byte) 0, false, false));
            goodCommands.add(new AddServerCommand(superAdminRole, serverType,
                    SystemFacadeTest.getHostname(), (byte) 0, true, true));
            goodCommands.add(new AddServerCommand(superAdminRole, serverType,
                    SystemFacadeTest.getHostname(), (byte) 0, true, false));
            goodCommands.add(new AddServerCommand(superAdminRole, serverType,
                    SystemFacadeTest.getHostname(), (byte) 0, true, true));

            goodCommands.add(new AddServerCommand(superAdminRole, serverType,
                    SystemFacadeTest.getHostname(), (byte) -1, false, false));
            goodCommands.add(new AddServerCommand(superAdminRole, serverType,
                    SystemFacadeTest.getHostname(), (byte) -1, true, true));
            goodCommands.add(new AddServerCommand(superAdminRole, serverType,
                    SystemFacadeTest.getHostname(), (byte) -1, true, false));
            goodCommands.add(new AddServerCommand(superAdminRole, serverType,
                    SystemFacadeTest.getHostname(), (byte) -1, true, true));

            String hostname = SystemFacadeTest.getHostname();
            goodCommands.add(new AddServerCommand(superAdminRole, serverType,
                    hostname, (byte) 10, true, true));
            goodCommands.add(new AddServerCommand(superAdminRole, serverType,
                    hostname, (byte) 10, true, true));

            badCommands.add(new AddServerCommand(new BasicRole(0L), serverType,
                    SystemFacadeTest.getHostname(), (byte) 0, false, false));
            badCommands.add(new AddServerCommand(new BasicRole(0L), serverType,
                    SystemFacadeTest.getHostname(), (byte) 0, true, true));
            badCommands.add(new AddServerCommand(new BasicRole(0L), serverType,
                    SystemFacadeTest.getHostname(), (byte) 0, true, false));
            badCommands.add(new AddServerCommand(new BasicRole(0L), serverType,
                    SystemFacadeTest.getHostname(), (byte) 0, true, true));

            badCommands.add(new AddServerCommand(null, serverType,
                    SystemFacadeTest.getHostname(), (byte) 0, false, false));
            badCommands.add(new AddServerCommand(null, serverType,
                    SystemFacadeTest.getHostname(), (byte) 0, true, true));
            badCommands.add(new AddServerCommand(null, serverType,
                    SystemFacadeTest.getHostname(), (byte) 0, true, false));
            badCommands.add(new AddServerCommand(null, serverType,
                    SystemFacadeTest.getHostname(), (byte) 0, true, true));

            try {
                badCommands.add(new AddServerCommand(superAdminRole,
                        serverType, null, (byte) 0, false, false));
                fail("managed to set a null server location");
            } catch (IllegalArgumentException e) {
                // supposed to be thrown
            }
            try {
                badCommands.add(new AddServerCommand(superAdminRole,
                        serverType, "", (byte) 0, false, false));
                fail("managed to set an empty server location");
            } catch (IllegalArgumentException e) {
                // supposed to be thrown
            }
        }

        for (AddServerCommand addServerCommand : goodCommands) {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    addServerCommand);
        }

        for (AddServerCommand addServerCommand : badCommands) {
            assertTransactionException("a bad command succeeded",
                    addServerCommand);
        }

        GetServersCommand serversSuperNull = new GetServersCommand(
                superAdminRole, (ServerTypeEnum) null);
        GetServersCommand serversBasicNull = new GetServersCommand(
                new BasicRole(0L), (ServerTypeEnum) null);
        GetServersCommand serversNullNull = new GetServersCommand(null,
                (ServerTypeEnum) null);

        HibernateTransactionCoordinator.getInstance().runTransaction(
                serversSuperNull);

        assertTrue(serversSuperNull.getResult().size() >= goodCommands.size());

        assertTransactionException(
                "getting a list of all servers as basic user succeeded",
                serversBasicNull);
        assertTransactionException(
                "getting a list of all servers as null user succeeded",
                serversNullNull);

        for (ServerTypeEnum serverType : ServerTypeEnum.values()) {
            GetServersCommand serversSuperType = new GetServersCommand(
                    superAdminRole, serverType);
            GetServersCommand serversBasicType = new GetServersCommand(
                    new BasicRole(0L), serverType);
            GetServersCommand serversNullType = new GetServersCommand(null,
                    serverType);

            HibernateTransactionCoordinator.getInstance().runTransaction(
                    serversSuperType);

            assertTrue(serversSuperType.getResult().size() >= goodCommands
                    .size()
                    / ServerTypeEnum.values().length);

            assertTransactionException(
                    "getting a list of servers as basic user succeeded",
                    serversBasicType);
            assertTransactionException(
                    "getting a list of servers as null user succeeded",
                    serversNullType);
        }

        GetServerStatisticsCommand stats = new GetServerStatisticsCommand(
                superAdminRole);
        HibernateTransactionCoordinator.getInstance().runTransaction(stats);
        for (ServerLoadView serverLoadView : stats.getResult()) {
            assertTrue(serverLoadView.getLastLoadUpdate() == null
                    || serverLoadView.getLastLoadUpdate().before(
                            DecidrGlobals.getTime().getTime()));
            assertTrue(serverLoadView.getNumInstances() >= 0);
            try {
                ServerTypeEnum.valueOf(serverLoadView.getServerType());
            } catch (IllegalArgumentException e) {
                fail("Invalid server type");
            }
        }

        stats = new GetServerStatisticsCommand(new BasicRole(0L));
        assertTransactionException(
                "Normal user shouldn't be able to get server stats", stats);
        stats = new GetServerStatisticsCommand(null);
        assertTransactionException(
                "Null user shouldn't be able to get server stats", stats);

        HibernateTransactionCoordinator.getInstance().runTransaction(
                serversSuperNull);
        Long serverID = serversSuperNull.getResult().get(0).getId();
        LockServerCommand lockerSuper = new LockServerCommand(superAdminRole,
                serverID, true);
        LockServerCommand unlockerSuper = new LockServerCommand(superAdminRole,
                serverID, false);
        LockServerCommand lockerBasic = new LockServerCommand(
                new BasicRole(0L), serverID, true);
        LockServerCommand unlockerBasic = new LockServerCommand(null, serverID,
                false);
        LockServerCommand lockerNull = new LockServerCommand(new BasicRole(0L),
                serverID, true);
        LockServerCommand unlockerNull = new LockServerCommand(null, serverID,
                false);

        assertTransactionException(
                "BasicRole shouldn't be able to lock servers.", lockerBasic);
        assertTransactionException(
                "Null role shouldn't be able to lock servers.", lockerNull);

        HibernateTransactionCoordinator.getInstance().runTransaction(
                lockerSuper);
        assertTrue(getServer(serverID).isLocked());

        assertTransactionException(
                "BasicRole shouldn't be able to unlock servers.", unlockerBasic);
        assertTransactionException(
                "Null role shouldn't be able to unlock servers.", unlockerNull);

        HibernateTransactionCoordinator.getInstance().runTransaction(
                unlockerSuper);
        assertFalse(getServer(serverID).isLocked());

        UpdateServerLoadCommand loader;
        byte[] goodLoads = new byte[] { 0, -1, 100, 50 };
        for (byte b : goodLoads) {
            loader = new UpdateServerLoadCommand(superAdminRole, serverID, b);
            HibernateTransactionCoordinator.getInstance()
                    .runTransaction(loader);
            assertEquals(b, getServer(serverID).getLoad());

            loader = new UpdateServerLoadCommand(new BasicRole(0L), serverID, b);
            assertTransactionException(
                    "BasicRole shouldn't be able to update server load.",
                    loader);
            loader = new UpdateServerLoadCommand(null, serverID, b);
            assertTransactionException(
                    "Null role shouldn't be able to update server load.",
                    loader);
        }

        byte[] badLoads = new byte[] { -2, -100, 127, 101 };
        for (byte b : badLoads) {
            try {
                loader = new UpdateServerLoadCommand(superAdminRole, serverID,
                        b);
                fail("Shouldn't be able to update with invalid server load.");
            } catch (IllegalArgumentException e) {
                // supposed to happen
            }
        }

        GetServersCommand getAllServers = new GetServersCommand(superAdminRole,
                (ServerTypeEnum) null);
        HibernateTransactionCoordinator.getInstance().runTransaction(
                getAllServers);

        assertNotNull(getAllServers.getResult());

        SystemFacadeTest.removeTestServers();
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
        Calendar modDate;
        SuperAdminRole superRole = new SuperAdminRole(SUPER_ADMIN_ID);
        SetSystemSettingsCommand setter = new SetSystemSettingsCommand(
                superRole, setterSettings);
        GetSystemSettingsCommand getter = new GetSystemSettingsCommand(
                superRole);
        SetSystemSettingsCommand userSetter = new SetSystemSettingsCommand(
                new BasicRole(0L), setterSettings);
        GetSystemSettingsCommand userGetter = new GetSystemSettingsCommand(
                new BasicRole(0L));
        SetSystemSettingsCommand nullSetter = new SetSystemSettingsCommand(
                null, setterSettings);
        GetSystemSettingsCommand nullGetter = new GetSystemSettingsCommand(null);

        assertTransactionException("User shouldn't be able to set settings",
                userSetter);
        assertTransactionException(
                "Null user shouldn't be able to set settings", nullSetter);
        assertTransactionException("Shouldn't be able to set empty settings",
                setter);

        Transaction trans = session.beginTransaction();
        session.createQuery("delete from User WHERE email LIKE 'ab@c.de'")
                .executeUpdate();
        trans.commit();

        User admin = new User("ab@c.de", DecidrGlobals.getTime().getTime());
        CreateNewUnregisteredUserCommand userCreator = new CreateNewUnregisteredUserCommand(
                superRole, admin);
        GetAllUsersCommand userFetcher = new GetAllUsersCommand(superRole,
                null, null);
        HibernateTransactionCoordinator.getInstance().runTransaction(
                userCreator, userFetcher);
        admin = userFetcher.getResult().get(0);

        setterSettings.setAutoAcceptNewTenants(true);
        setterSettings.setChangeEmailRequestLifetimeSeconds(20);
        setterSettings.setDomain("decidr.de");
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
        setterSettings.setSuperAdmin(admin);
        setterSettings.setSystemEmailAddress("decidr@decidr.biz");
        setterSettings.setSystemName("De Cidr");
        HibernateTransactionCoordinator.getInstance().runTransaction(setter);

        HibernateTransactionCoordinator.getInstance().runTransaction(getter);
        getterSettings = getter.getResult();
        assertEquals(true, getterSettings.isAutoAcceptNewTenants());
        assertEquals(true, getterSettings.isMtaUseTls());
        assertEquals(20, getterSettings.getChangeEmailRequestLifetimeSeconds());
        assertEquals("decidr.de", getterSettings.getDomain());
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
        assertEquals(admin.getId(), getterSettings.getSuperAdmin().getId());
        assertEquals("decidr@decidr.biz", getterSettings
                .getSystemEmailAddress());
        assertEquals("De Cidr", getterSettings.getSystemName());
        assertFalse(modDate.getTime().after(getterSettings.getModifiedDate()));

        setterSettings.setAutoAcceptNewTenants(false);
        setterSettings.setChangeEmailRequestLifetimeSeconds(150);
        setterSettings.setDomain("decidr.eu");
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
        setterSettings.setModifiedDate(new Date(
                modDate.getTimeInMillis() - 1000000));
        setterSettings.setMonitorAveragingPeriodSeconds(600);
        setterSettings.setMonitorUpdateIntervalSeconds(1);
        setterSettings.setMtaPort(22);
        setterSettings.setMtaUseTls(false);
        setterSettings.setPasswordResetRequestLifetimeSeconds(20);
        setterSettings.setRegistrationRequestLifetimeSeconds(2);
        setterSettings.setServerPoolInstances(1);
        setterSettings.setSystemEmailAddress("dumbo@decidr.eu");
        setterSettings.setSystemName("Darth Vader");
        HibernateTransactionCoordinator.getInstance().runTransaction(setter);

        HibernateTransactionCoordinator.getInstance().runTransaction(getter);
        getterSettings = getter.getResult();
        assertEquals(false, getterSettings.isAutoAcceptNewTenants());
        assertEquals(false, getterSettings.isMtaUseTls());
        assertEquals(150, getterSettings.getChangeEmailRequestLifetimeSeconds());
        assertEquals("decidr.eu", getterSettings.getDomain());
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
        HibernateTransactionCoordinator.getInstance().runTransaction(setter);

        HibernateTransactionCoordinator.getInstance().runTransaction(getter);
        getterSettings = getter.getResult();
        assertEquals("", getterSettings.getMtaHostname());
        assertEquals("", getterSettings.getMtaPassword());
        assertEquals("", getterSettings.getMtaUsername());
        assertEquals("", getterSettings.getSystemName());

        setterSettings.setChangeEmailRequestLifetimeSeconds(-1);
        assertTransactionException(
                "invalid ChangeEmailRequestLifetimeSeconds succeeded", setter);
        setterSettings.setChangeEmailRequestLifetimeSeconds(1);

        setterSettings.setDomain(null);
        assertTransactionException("null domain succeeded", setter);
        setterSettings.setDomain("");

        assertTransactionException("empty domain succeeded", setter);
        setterSettings.setDomain("decidr.de");

        setterSettings.setInvitationLifetimeSeconds(-1);
        assertTransactionException(
                "invalid InvitationLifetimeSeconds succeeded", setter);
        setterSettings.setInvitationLifetimeSeconds(1);

        setterSettings.setLogLevel("INVALID");
        assertTransactionException("invalid loglevel succeeded", setter);
        setterSettings.setLogLevel("ERROR");

        setterSettings.setMaxAttachmentsPerEmail(-1);
        assertTransactionException("invalid amount of attachments succeeded",
                setter);
        setterSettings.setMaxAttachmentsPerEmail(1);

        setterSettings.setMaxServerLoadForShutdown((byte) -1);
        assertTransactionException(
                "invalid MaxServerLoadForShutdown succeeded", setter);
        setterSettings.setMaxServerLoadForShutdown((byte) 1);

        setterSettings.setMaxServerLoadForUnlock((byte) -1);
        assertTransactionException("invalid MaxServerLoadForUnlock succeeded",
                setter);
        setterSettings.setMaxServerLoadForUnlock((byte) 1);

        setterSettings.setMaxUploadFileSizeBytes(-1);
        assertTransactionException("invalid MaxUploadFileSizeBytes succeeded",
                setter);
        setterSettings.setMaxUploadFileSizeBytes(1);

        setterSettings.setMaxWorkflowInstancesForShutdown(-1);
        assertTransactionException(
                "invalid MaxWorkflowInstancesForShutdown succeeded", setter);
        setterSettings.setMaxWorkflowInstancesForShutdown(1);

        setterSettings.setMaxWorkflowInstancesForUnlock(-1);
        assertTransactionException(
                "invalid MaxWorkflowInstancesForUnlock succeeded", setter);
        setterSettings.setMaxWorkflowInstancesForUnlock(1);

        setterSettings.setMinServerLoadForLock((byte) -1);
        assertTransactionException("invalid MinServerLoadForLock succeeded",
                setter);
        setterSettings.setMinServerLoadForLock((byte) 1);

        setterSettings.setMinUnlockedServers(-1);
        assertTransactionException("invalid MinUnlockedServers succeeded",
                setter);
        setterSettings.setMinUnlockedServers(1);

        setterSettings.setMinWorkflowInstancesForLock(-1);
        assertTransactionException(
                "invalid MinWorkflowInstancesForLock succeeded", setter);
        setterSettings.setMinWorkflowInstancesForLock(1);

        setterSettings.setMonitorAveragingPeriodSeconds(-1);
        assertTransactionException(
                "invalid MonitorAveragingPeriodSeconds succeeded", setter);
        setterSettings.setMonitorAveragingPeriodSeconds(1);

        setterSettings.setMonitorUpdateIntervalSeconds(-1);
        assertTransactionException(
                "invalid MonitorUpdateIntervalSeconds succeeded", setter);
        setterSettings.setMonitorUpdateIntervalSeconds(1);

        setterSettings.setPasswordResetRequestLifetimeSeconds(-1);
        assertTransactionException(
                "invalid PasswordResetRequestLifetimeSeconds succeeded", setter);
        setterSettings.setPasswordResetRequestLifetimeSeconds(1);

        setterSettings.setRegistrationRequestLifetimeSeconds(-1);
        assertTransactionException(
                "invalid RegistrationRequestLifetimeSeconds succeeded", setter);
        setterSettings.setRegistrationRequestLifetimeSeconds(1);

        setterSettings.setServerPoolInstances(-1);
        assertTransactionException("invalid ServerPoolInstances succeeded",
                setter);
        setterSettings.setServerPoolInstances(1);

        setterSettings.setSuperAdmin(null);
        assertTransactionException("null super admin succeeded", setter);
        setterSettings.setSuperAdmin(new User());
        assertTransactionException("empty super admin succeeded", setter);
        setterSettings.setSuperAdmin(admin);

        setterSettings.setSystemEmailAddress("in@valid@email");
        assertTransactionException("invalid email address ucceeded", setter);
        setterSettings.setSystemEmailAddress("");
        assertTransactionException("empty email address ucceeded", setter);
        setterSettings.setSystemEmailAddress(null);
        assertTransactionException("null email address ucceeded", setter);
        setterSettings.setSystemEmailAddress("invalid@email.de");

        setterSettings.setSystemName(null);
        assertTransactionException("null system name succeeded", setter);
        setterSettings.setSystemName("DecidR");

        assertTransactionException("User shouldn't be able to get settings",
                userGetter);
        assertTransactionException(
                "Null user shouldn't be able to get settings", nullGetter);
    }
}
