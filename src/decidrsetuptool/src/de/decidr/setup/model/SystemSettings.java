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

package de.decidr.setup.model;

import de.decidr.setup.helpers.DateUtils;

public class SystemSettings {

    private String id = "'1'";
    private String modifiedDate;
    private String autoAcceptNewTenants;
    private String systemName;
    private String domain;
    private String systemEmailAddress;
    private String logLevel;
    private String superAdminId = "'1'";
    private String passwordResetRequestLifetimeSeconds;
    private String registrationRequestLifetimeSeconds;
    private String changeEmailRequestLifetimeSeconds;
    private String invitationLifetimeSeconds;
    private String mtaHostname;
    private String mtaPort;
    private String mtaUseTls;
    private String mtaUsername;
    private String mtaPassword;
    private String maxUploadFileSizeBytes;
    private String maxAttachmentsPerEmail;
    private String monitorUpdateIntervalSeconds;
    private String monitorAveragingPeriodSeconds;
    private String serverPoolInstances;
    private String minServerLoadForLock;
    private String maxServerLoadForUnlock;
    private String maxServerLoadForShutdown;
    private String minUnlockedServers;
    private String minWorkflowInstancesForLock;
    private String maxWorkflowInstancesForUnlock;
    private String maxWorkflowInstancesForShutdown;

    public SystemSettings() {
        String now = DateUtils.now();
        modifiedDate = now;
    }

    public String getAutoAcceptNewTenants() {
        return autoAcceptNewTenants;
    }

    public String getChangeEmailRequestLifetimeSeconds() {
        return changeEmailRequestLifetimeSeconds;
    }

    public String getDomain() {
        return domain;
    }

    public String getId() {
        return id;
    }

    public String getInvitationLifetimeSeconds() {
        return invitationLifetimeSeconds;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public String getMaxAttachmentsPerEmail() {
        return maxAttachmentsPerEmail;
    }

    public String getMaxServerLoadForShutdown() {
        return maxServerLoadForShutdown;
    }

    public String getMaxServerLoadForUnlock() {
        return maxServerLoadForUnlock;
    }

    public String getMaxUploadFileSizeBytes() {
        return maxUploadFileSizeBytes;
    }

    public String getMaxWorkflowInstancesForShutdown() {
        return maxWorkflowInstancesForShutdown;
    }

    public String getMaxWorkflowInstancesForUnlock() {
        return maxWorkflowInstancesForUnlock;
    }

    public String getMinServerLoadForLock() {
        return minServerLoadForLock;
    }

    public String getMinUnlockedServers() {
        return minUnlockedServers;
    }

    public String getMinWorkflowInstancesForLock() {
        return minWorkflowInstancesForLock;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public String getMonitorAveragingPeriodSeconds() {
        return monitorAveragingPeriodSeconds;
    }

    public String getMonitorUpdateIntervalSeconds() {
        return monitorUpdateIntervalSeconds;
    }

    public String getMtaHostname() {
        return mtaHostname;
    }

    public String getMtaPassword() {
        return mtaPassword;
    }

    public String getMtaPort() {
        return mtaPort;
    }

    public String getMtaUsername() {
        return mtaUsername;
    }

    public String getMtaUseTls() {
        return mtaUseTls;
    }

    public String getPasswordResetRequestLifetimeSeconds() {
        return passwordResetRequestLifetimeSeconds;
    }

    public String getRegistrationRequestLifetimeSeconds() {
        return registrationRequestLifetimeSeconds;
    }

    public String getServerPoolInstances() {
        return serverPoolInstances;
    }

    public String getSuperAdminId() {
        return superAdminId;
    }

    public String getSystemEmailAddress() {
        return systemEmailAddress;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setAutoAcceptNewTenants(String autoAcceptNewTenants) {
        this.autoAcceptNewTenants = autoAcceptNewTenants;
    }

    public void setChangeEmailRequestLifetimeSeconds(
            String changeEmailRequestLifetimeSeconds) {
        this.changeEmailRequestLifetimeSeconds = changeEmailRequestLifetimeSeconds;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setInvitationLifetimeSeconds(String invitationLifetimeSeconds) {
        this.invitationLifetimeSeconds = invitationLifetimeSeconds;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public void setMaxAttachmentsPerEmail(String maxAttachmentsPerEmail) {
        this.maxAttachmentsPerEmail = maxAttachmentsPerEmail;
    }

    public void setMaxServerLoadForShutdown(String maxServerLoadForShutdown) {
        this.maxServerLoadForShutdown = maxServerLoadForShutdown;
    }

    public void setMaxServerLoadForUnlock(String maxServerLoadForUnlock) {
        this.maxServerLoadForUnlock = maxServerLoadForUnlock;
    }

    public void setMaxUploadFileSizeBytes(String maxUploadFileSizeBytes) {
        this.maxUploadFileSizeBytes = maxUploadFileSizeBytes;
    }

    public void setMaxWorkflowInstancesForShutdown(
            String maxWorkflowInstancesForShutdown) {
        this.maxWorkflowInstancesForShutdown = maxWorkflowInstancesForShutdown;
    }

    public void setMaxWorkflowInstancesForUnlock(
            String maxWorkflowInstancesForUnlock) {
        this.maxWorkflowInstancesForUnlock = maxWorkflowInstancesForUnlock;
    }

    public void setMinServerLoadForLock(String minServerLoadForLock) {
        this.minServerLoadForLock = minServerLoadForLock;
    }

    public void setMinUnlockedServers(String minUnlockedServers) {
        this.minUnlockedServers = minUnlockedServers;
    }

    public void setMinWorkflowInstancesForLock(
            String minWorkflowInstancesForLock) {
        this.minWorkflowInstancesForLock = minWorkflowInstancesForLock;
    }

    public void setMonitorAveragingPeriodSeconds(
            String monitorAveragingPeriodSeconds) {
        this.monitorAveragingPeriodSeconds = monitorAveragingPeriodSeconds;
    }

    public void setMonitorUpdateIntervalSeconds(
            String monitorUpdateIntervalSeconds) {
        this.monitorUpdateIntervalSeconds = monitorUpdateIntervalSeconds;
    }

    public void setMtaHostname(String mtaHostname) {
        this.mtaHostname = mtaHostname;
    }

    public void setMtaPassword(String mtaPassword) {
        this.mtaPassword = mtaPassword;
    }

    public void setMtaPort(String mtaPort) {
        this.mtaPort = mtaPort;
    }

    public void setMtaUsername(String mtaUsername) {
        this.mtaUsername = mtaUsername;
    }

    public void setMtaUseTls(String mtaUseTls) {
        this.mtaUseTls = mtaUseTls;
    }

    public void setPasswordResetRequestLifetimeSeconds(
            String passwordResetRequestLifetimeSeconds) {
        this.passwordResetRequestLifetimeSeconds = passwordResetRequestLifetimeSeconds;
    }

    public void setRegistrationRequestLifetimeSeconds(
            String registrationRequestLifetimeSeconds) {
        this.registrationRequestLifetimeSeconds = registrationRequestLifetimeSeconds;
    }

    public void setServerPoolInstances(String serverPoolInstances) {
        this.serverPoolInstances = serverPoolInstances;
    }

    public void setSystemEmailAddress(String systemEmailAddress) {
        this.systemEmailAddress = systemEmailAddress;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}
