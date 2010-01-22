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

package de.decidr.setup.input;

import de.decidr.setup.helpers.BooleanRequest;
import de.decidr.setup.helpers.EmailRequest;
import de.decidr.setup.helpers.LoglevelRequest;
import de.decidr.setup.helpers.NumberRequest;
import de.decidr.setup.helpers.StringRequest;
import de.decidr.setup.model.SystemSettings;

/**
 * Retrieves information about the system settings from the user and generates
 * an SQL script.
 * 
 * @author Johannes Engelhardt
 */
public class InputSystemSettings {

    private static String createSql(SystemSettings sys) {
        StringBuilder sql = new StringBuilder();
        sql.append("REPLACE INTO `system_settings` (`id`,`modifiedDate`,"
                + "`autoAcceptNewTenants`,`systemName`,`baseUrl`,"
                + "`systemEmailAddress`,`logLevel`,`superAdminId`,"
                + "`passwordResetRequestLifetimeSeconds`,"
                + "`registrationRequestLifetimeSeconds`,"
                + "`changeEmailRequestLifetimeSeconds`,"
                + "`invitationLifetimeSeconds`,"
                + "`mtaHostname`,`mtaPort`,`mtaUseTls`,`mtaUsername`,"
                + "`mtaPassword`,`maxUploadFileSizeBytes`,"
                + "`maxAttachmentsPerEmail`,"
                + "`monitorUpdateIntervalSeconds`,"
                + "`monitorAveragingPeriodSeconds`,"
                + "`serverPoolInstances`,`minServerLoadForLock`,"
                + "`maxServerLoadForUnlock`,`maxServerLoadForShutdown`,"
                + "`minUnlockedServers`,`minWorkflowInstancesForLock`,"
                + "`maxWorkflowInstancesForUnlock`,"
                + "`maxWorkflowInstancesForShutdown`)\n");

        sql.append("VALUES (" + sys.getId() + "," + sys.getModifiedDate() + ","
                + sys.getAutoAcceptNewTenants() + "," + sys.getSystemName()
                + "," + sys.getBaseUrl() + "," + sys.getSystemEmailAddress()
                + "," + sys.getLogLevel() + "," + sys.getSuperAdminId() + ","
                + sys.getPasswordResetRequestLifetimeSeconds() + ","
                + sys.getRegistrationRequestLifetimeSeconds() + ","
                + sys.getChangeEmailRequestLifetimeSeconds() + ","
                + sys.getInvitationLifetimeSeconds() + ","
                + sys.getMtaHostname() + "," + sys.getMtaPort() + ","
                + sys.getMtaUseTls() + "," + sys.getMtaUsername() + ","
                + sys.getMtaPassword() + "," + sys.getMaxUploadFileSizeBytes()
                + "," + sys.getMaxAttachmentsPerEmail() + ","
                + sys.getMonitorUpdateIntervalSeconds() + ","
                + sys.getMonitorAveragingPeriodSeconds() + ","
                + sys.getServerPoolInstances() + ","
                + sys.getMinServerLoadForLock() + ","
                + sys.getMaxServerLoadForUnlock() + ","
                + sys.getMaxServerLoadForShutdown() + ","
                + sys.getMinUnlockedServers() + ","
                + sys.getMinWorkflowInstancesForLock() + ","
                + sys.getMaxWorkflowInstancesForUnlock() + ","
                + sys.getMaxWorkflowInstancesForShutdown() + ");\n\n");

        return sql.toString();
    }

    public static String getSql() {
        SystemSettings sys = getSystemSettings();
        return createSql(sys);
    }

    public static SystemSettings getSystemSettings() {
        System.out.println("------------------------------------------------");
        System.out.println("Set up System Settings");
        System.out.println("------------------------------------------------");

        SystemSettings sys = new SystemSettings();

        sys.setAutoAcceptNewTenants(BooleanRequest.getResult(
                "Auto accept new tenants", "yes"));
        sys.setSystemName(StringRequest.getResult("System name", "DecidR"));
        sys.setBaseUrl(StringRequest.getResult("Base URL",
                "decidr.de:8080/WebPortal"));
        sys.setSystemEmailAddress(EmailRequest
                .getResult("System email address"));
        sys.setLogLevel(LoglevelRequest.getResult("Loglevel", "WARN"));
        sys.setPasswordResetRequestLifetimeSeconds(NumberRequest.getResult(
                "Password request lifetime in seconds", "259200"));
        sys.setRegistrationRequestLifetimeSeconds(NumberRequest.getResult(
                "Registration request lifetime in seconds", "259200"));
        sys.setChangeEmailRequestLifetimeSeconds(NumberRequest.getResult(
                "Change email request lifetime in seconds", "259200"));
        sys.setInvitationLifetimeSeconds(NumberRequest.getResult(
                "Invitation lifetime in seconds", "259200"));
        sys
                .setMtaHostname(StringRequest.getResult("MTA hostname",
                        "localhost"));
        sys.setMtaPort(NumberRequest.getResult("MTA port", "0"));
        sys.setMtaUseTls(BooleanRequest.getResult("Use TLS?", "yes"));
        sys.setMtaUsername(StringRequest.getResult("MTA username"));
        sys.setMtaPassword(StringRequest.getResult("MTA password"));
        sys.setMaxUploadFileSizeBytes(NumberRequest.getResult(
                "Max. upload filesize in bytes", "10485760"));
        sys.setMaxAttachmentsPerEmail(NumberRequest.getResult(
                "Max. attachments per email", "10"));
        sys.setMonitorUpdateIntervalSeconds(NumberRequest.getResult(
                "Monitor update interval in seconds", "60"));
        sys.setMonitorAveragingPeriodSeconds(NumberRequest.getResult(
                "Monitor averaging period in seconds", "300"));
        sys.setServerPoolInstances(NumberRequest.getResult(
                "Server pool instances", "5"));
        sys.setMinServerLoadForLock(NumberRequest.getResult(
                "Min. server load for lock in %", "80"));
        sys.setMaxServerLoadForUnlock(NumberRequest.getResult(
                "Max. server load for unlock in %", "0"));
        sys.setMaxServerLoadForShutdown(NumberRequest.getResult(
                "Max. server load for shutdown in %", "20"));
        sys.setMinUnlockedServers(NumberRequest.getResult(
                "Min. unlocked servers", "1"));
        sys.setMinWorkflowInstancesForLock(NumberRequest.getResult(
                "Min. workflow instances for lock", "10"));
        sys.setMaxWorkflowInstancesForUnlock(NumberRequest.getResult(
                "Max. workflow instances for unlock", "8"));
        sys.setMaxWorkflowInstancesForShutdown(NumberRequest.getResult(
                "Max. workflow instances for shutdown", "1"));

        return sys;
    }
}
