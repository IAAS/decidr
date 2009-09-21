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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.vaadin.data.Item;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.BasicRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.testsuites.DatabaseTestSuite;

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
        if (!DatabaseTestSuite.running()) {
            fail("Needs to run inside " + DatabaseTestSuite.class.getName());
        }

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
        SystemSettings setterSettings = new SystemSettings();
        Item getterSettings;
        Date modDate;

        getterSettings = adminFacade.getSettings();
        assertNotNull(getterSettings);

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
        setterSettings.setSystemEmailAddress("decidr@decidr.biz");
        setterSettings.setSystemName("De Cidr");
        adminFacade.setSettings(setterSettings);

        getterSettings = adminFacade.getSettings();
        assertEquals(true, getterSettings.getItemProperty(
                "autoAcceptNewTenants").getValue());
        assertEquals(true, getterSettings.getItemProperty("mtaUseTls")
                .getValue());
        assertEquals(20, getterSettings.getItemProperty(
                "changeEmailRequestLifetimeSeconds").getValue());
        assertEquals("decidr.de", getterSettings.getItemProperty("domain")
                .getValue());
        assertEquals(10, getterSettings.getItemProperty(
                "invitationLifetimeSeconds").getValue());
        assertEquals("DEBUG", getterSettings.getItemProperty("logLevel")
                .getValue());
        assertEquals(10, getterSettings.getItemProperty(
                "maxAttachmentsPerEmail").getValue());
        assertEquals((byte) 100, getterSettings.getItemProperty(
                "maxServerLoadForShutdown").getValue());
        assertEquals((byte) 100, getterSettings.getItemProperty(
                "maxServerLoadForUnlock").getValue());
        assertEquals(1, getterSettings.getItemProperty("maxUploadFileSizeByte")
                .getValue());
        assertEquals(1, getterSettings.getItemProperty(
                "maxWorkflowInstancesForShutdown").getValue());
        assertEquals(1, getterSettings.getItemProperty(
                "maxWorkflowInstancesForUnlock").getValue());
        assertEquals((byte) 100, getterSettings.getItemProperty(
                "minServerLoadForLock").getValue());
        assertEquals(1, getterSettings.getItemProperty("minUnlockedServers")
                .getValue());
        assertEquals(1, getterSettings.getItemProperty(
                "minWorkflowInstancesForLock").getValue());
        assertEquals(modDate, getterSettings.getItemProperty("modifiedDate")
                .getValue());
        assertEquals(60, getterSettings.getItemProperty(
                "monitorAveragingPeriodSeconds").getValue());
        assertEquals(10, getterSettings.getItemProperty(
                "monitorUpdateIntervalSeconds").getValue());
        assertEquals("localhost", getterSettings.getItemProperty("mtaHostname")
                .getValue());
        assertEquals("asdfg", getterSettings.getItemProperty("mtaPassword")
                .getValue());
        assertEquals(-1, getterSettings.getItemProperty("mtaPort").getValue());
        assertEquals("asd", getterSettings.getItemProperty("mtaUsername")
                .getValue());
        assertEquals(200, getterSettings.getItemProperty(
                "passwordResetRequestLifeTimeSeconds").getValue());
        assertEquals(2000, getterSettings.getItemProperty(
                "registrationRequestLifetimeSeconds").getValue());
        assertEquals(3, getterSettings.getItemProperty("serverPoolInstances")
                .getValue());
        assertEquals("decidr@decidr.biz", getterSettings.getItemProperty(
                "systemEmailAddress").getValue());
        assertEquals("De Cidr", getterSettings.getItemProperty("systemName")
                .getValue());

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
        setterSettings.setSystemEmailAddress("dumbo@decidr.eu");
        setterSettings.setSystemName("Darth Vader");
        adminFacade.setSettings(setterSettings);

        getterSettings = adminFacade.getSettings();
        assertEquals(false, getterSettings.getItemProperty(
                "autoAcceptNewTenants").getValue());
        assertEquals(false, getterSettings.getItemProperty("mtaUseTls")
                .getValue());
        assertEquals(150, getterSettings.getItemProperty(
                "changeEmailRequestLifetimeSeconds").getValue());
        assertEquals("decidr.eu", getterSettings.getItemProperty("domain")
                .getValue());
        assertEquals(1450, getterSettings.getItemProperty(
                "invitationLifetimeSeconds").getValue());
        assertEquals("ERROR", getterSettings.getItemProperty("logLevel")
                .getValue());
        assertEquals(0, getterSettings
                .getItemProperty("maxAttachmentsPerEmail").getValue());
        assertEquals((byte) 10, getterSettings.getItemProperty(
                "maxServerLoadForShutdown").getValue());
        assertEquals((byte) 10, getterSettings.getItemProperty(
                "maxServerLoadForUnlock").getValue());
        assertEquals(100, getterSettings.getItemProperty(
                "maxUploadFileSizeByte").getValue());
        assertEquals(100, getterSettings.getItemProperty(
                "maxWorkflowInstancesForShutdown").getValue());
        assertEquals(10, getterSettings.getItemProperty(
                "maxWorkflowInstancesForUnlock").getValue());
        assertEquals((byte) 10, getterSettings.getItemProperty(
                "minServerLoadForLock").getValue());
        assertEquals(10, getterSettings.getItemProperty("minUnlockedServers")
                .getValue());
        assertEquals(10, getterSettings.getItemProperty(
                "minWorkflowInstancesForLock").getValue());
        assertEquals(modDate, getterSettings.getItemProperty("modifiedDate")
                .getValue());
        assertEquals(600, getterSettings.getItemProperty(
                "monitorAveragingPeriodSeconds").getValue());
        assertEquals(1, getterSettings.getItemProperty(
                "monitorUpdateIntervalSeconds").getValue());
        assertNull(getterSettings.getItemProperty("mtaHostname").getValue());
        assertNull(getterSettings.getItemProperty("mtaPassword").getValue());
        assertEquals(22, getterSettings.getItemProperty("mtaPort").getValue());
        assertNull(getterSettings.getItemProperty("mtaUsername").getValue());
        assertEquals(20, getterSettings.getItemProperty(
                "passwordResetRequestLifeTimeSeconds").getValue());
        assertEquals(2, getterSettings.getItemProperty(
                "registrationRequestLifetimeSeconds").getValue());
        assertEquals(1, getterSettings.getItemProperty("serverPoolInstances")
                .getValue());
        assertEquals("dumbo@decidr.eu", getterSettings.getItemProperty(
                "systemEmailAddress").getValue());
        assertEquals("Darth Vader", getterSettings
                .getItemProperty("systemName").getValue());

        setterSettings.setMtaHostname("");
        setterSettings.setMtaPort(-102);
        setterSettings.setMtaUsername("");
        setterSettings.setMtaPassword("");
        setterSettings.setSystemName("");
        adminFacade.setSettings(setterSettings);

        getterSettings = adminFacade.getSettings();
        assertEquals("", getterSettings.getItemProperty("mtaHostname")
                .getValue());
        assertEquals("", getterSettings.getItemProperty("mtaPassword")
                .getValue());
        assertEquals(-102, getterSettings.getItemProperty("mtaPort").getValue());
        assertEquals("", getterSettings.getItemProperty("mtaUsername")
                .getValue());
        assertEquals("", getterSettings.getItemProperty("systemName")
                .getValue());

        setSettingsExceptionHelper("managed to set null settings", adminFacade,
                null);
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

        setterSettings.setDomain(null);
        setSettingsExceptionHelper("null domain succeded", adminFacade,
                setterSettings);
        setterSettings.setDomain("");
        setSettingsExceptionHelper("empty domain succeded", adminFacade,
                setterSettings);
        setterSettings.setDomain("decidr.de");

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

        setterSettings.setModifiedDate(new Date(DecidrGlobals.getTime()
                .getTimeInMillis() + 10000000));
        setSettingsExceptionHelper("invalid (future) ModifiedDate succeeded",
                adminFacade, setterSettings);
        setterSettings.setModifiedDate(DecidrGlobals.getTime().getTime());

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
        setSettingsExceptionHelper("invalid email address ucceeded",
                adminFacade, setterSettings);
        setterSettings.setSystemEmailAddress("");
        setSettingsExceptionHelper("empty email address ucceeded", adminFacade,
                setterSettings);
        setterSettings.setSystemEmailAddress(null);
        setSettingsExceptionHelper("null email address ucceeded", adminFacade,
                setterSettings);
        setterSettings.setSystemEmailAddress("invalid@email.de");

        setterSettings.setSystemName(null);
        setSettingsExceptionHelper("null system name succeeded", adminFacade,
                setterSettings);
        setterSettings.setSystemName("DecidR");
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
    private static Server getServer(Server testServer)
            throws TransactionException {
        List<Server> servers = adminFacade.getServers(ServerTypeEnum
                .valueOf(testServer.getServerType().getName()));
        for (Server server : servers) {
            if (server.getId() == testServer.getId()) {
                return server;
            }
        }
        return null;
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

    private static void updateServerLoadExceptionHelper(String failmsg,
            SystemFacade facade, long serverID, byte newLoad) {
        try {
            facade.updateServerLoad(serverID, newLoad);
            fail(failmsg);
        } catch (TransactionException e) {
            // should be thrown
        }
    }

    private static void addServerExceptionHelper(String failmsg,
            SystemFacade facade, ServerTypeEnum type, String location,
            byte initialLoad, boolean locked, boolean dynamicallyAdded) {
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

    private static void logExceptionHelper(String failmsg, SystemFacade facade,
            List<Filter> filters, Paginator paginator) {
        try {
            facade.getLog(filters, paginator);
            fail(failmsg);
        } catch (TransactionException e) {
            // should be thrown
        }
    }
}