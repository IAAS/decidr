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

    private String id = "1";
    private String modifiedDate;
    private String autoAcceptNewTenants;
    private String systemName;
    private String domain;
    private String systemEmailAddress;
    private String logLevel;
    private String superAdminId = "1";
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

    public void setAutoAcceptNewTenants(String autoAcceptNewTenants) {
	this.autoAcceptNewTenants = autoAcceptNewTenants;
    }

    public String getSystemName() {
	return systemName;
    }

    public void setSystemName(String systemName) {
	this.systemName = systemName;
    }

    public String getDomain() {
	return domain;
    }

    public void setDomain(String domain) {
	this.domain = domain;
    }

    public String getSystemEmailAddress() {
	return systemEmailAddress;
    }

    public void setSystemEmailAddress(String systemEmailAddress) {
	this.systemEmailAddress = systemEmailAddress;
    }

    public String getLogLevel() {
	return logLevel;
    }

    public void setLogLevel(String logLevel) {
	this.logLevel = logLevel;
    }

    public String getPasswordResetRequestLifetimeSeconds() {
	return passwordResetRequestLifetimeSeconds;
    }

    public void setPasswordResetRequestLifetimeSeconds(
	    String passwordResetRequestLifetimeSeconds) {
	this.passwordResetRequestLifetimeSeconds = passwordResetRequestLifetimeSeconds;
    }

    public String getRegistrationRequestLifetimeSeconds() {
	return registrationRequestLifetimeSeconds;
    }

    public void setRegistrationRequestLifetimeSeconds(
	    String registrationRequestLifetimeSeconds) {
	this.registrationRequestLifetimeSeconds = registrationRequestLifetimeSeconds;
    }

    public String getChangeEmailRequestLifetimeSeconds() {
	return changeEmailRequestLifetimeSeconds;
    }

    public void setChangeEmailRequestLifetimeSeconds(
	    String changeEmailRequestLifetimeSeconds) {
	this.changeEmailRequestLifetimeSeconds = changeEmailRequestLifetimeSeconds;
    }

    public String getInvitationLifetimeSeconds() {
	return invitationLifetimeSeconds;
    }

    public void setInvitationLifetimeSeconds(String invitationLifetimeSeconds) {
	this.invitationLifetimeSeconds = invitationLifetimeSeconds;
    }

    public String getMtaHostname() {
	return mtaHostname;
    }

    public void setMtaHostname(String mtaHostname) {
	this.mtaHostname = mtaHostname;
    }

    public String getMtaPort() {
	return mtaPort;
    }

    public void setMtaPort(String mtaPort) {
	this.mtaPort = mtaPort;
    }

    public String getMtaUseTls() {
	return mtaUseTls;
    }

    public void setMtaUseTls(String mtaUseTls) {
	this.mtaUseTls = mtaUseTls;
    }

    public String getMtaUsername() {
	return mtaUsername;
    }

    public void setMtaUsername(String mtaUsername) {
	this.mtaUsername = mtaUsername;
    }

    public String getMtaPassword() {
	return mtaPassword;
    }

    public void setMtaPassword(String mtaPassword) {
	this.mtaPassword = mtaPassword;
    }

    public String getMaxUploadFileSizeBytes() {
	return maxUploadFileSizeBytes;
    }

    public void setMaxUploadFileSizeBytes(String maxUploadFileSizeBytes) {
	this.maxUploadFileSizeBytes = maxUploadFileSizeBytes;
    }

    public String getMaxAttachmentsPerEmail() {
	return maxAttachmentsPerEmail;
    }

    public void setMaxAttachmentsPerEmail(String maxAttachmentsPerEmail) {
	this.maxAttachmentsPerEmail = maxAttachmentsPerEmail;
    }

    public String getMonitorUpdateIntervalSeconds() {
	return monitorUpdateIntervalSeconds;
    }

    public void setMonitorUpdateIntervalSeconds(
	    String monitorUpdateIntervalSeconds) {
	this.monitorUpdateIntervalSeconds = monitorUpdateIntervalSeconds;
    }

    public String getMonitorAveragingPeriodSeconds() {
	return monitorAveragingPeriodSeconds;
    }

    public void setMonitorAveragingPeriodSeconds(
	    String monitorAveragingPeriodSeconds) {
	this.monitorAveragingPeriodSeconds = monitorAveragingPeriodSeconds;
    }

    public String getServerPoolInstances() {
	return serverPoolInstances;
    }

    public void setServerPoolInstances(String serverPoolInstances) {
	this.serverPoolInstances = serverPoolInstances;
    }

    public String getMinServerLoadForLock() {
	return minServerLoadForLock;
    }

    public void setMinServerLoadForLock(String minServerLoadForLock) {
	this.minServerLoadForLock = minServerLoadForLock;
    }

    public String getMaxServerLoadForUnlock() {
	return maxServerLoadForUnlock;
    }

    public void setMaxServerLoadForUnlock(String maxServerLoadForUnlock) {
	this.maxServerLoadForUnlock = maxServerLoadForUnlock;
    }

    public String getMaxServerLoadForShutdown() {
	return maxServerLoadForShutdown;
    }

    public void setMaxServerLoadForShutdown(String maxServerLoadForShutdown) {
	this.maxServerLoadForShutdown = maxServerLoadForShutdown;
    }

    public String getMinUnlockedServers() {
	return minUnlockedServers;
    }

    public void setMinUnlockedServers(String minUnlockedServers) {
	this.minUnlockedServers = minUnlockedServers;
    }

    public String getMinWorkflowInstancesForLock() {
	return minWorkflowInstancesForLock;
    }

    public void setMinWorkflowInstancesForLock(
	    String minWorkflowInstancesForLock) {
	this.minWorkflowInstancesForLock = minWorkflowInstancesForLock;
    }

    public String getMaxWorkflowInstancesForUnlock() {
	return maxWorkflowInstancesForUnlock;
    }

    public void setMaxWorkflowInstancesForUnlock(
	    String maxWorkflowInstancesForUnlock) {
	this.maxWorkflowInstancesForUnlock = maxWorkflowInstancesForUnlock;
    }

    public String getMaxWorkflowInstancesForShutdown() {
	return maxWorkflowInstancesForShutdown;
    }

    public void setMaxWorkflowInstancesForShutdown(
	    String maxWorkflowInstancesForShutdown) {
	this.maxWorkflowInstancesForShutdown = maxWorkflowInstancesForShutdown;
    }

    public String getId() {
	return id;
    }

    public String getModifiedDate() {
	return modifiedDate;
    }

    public String getSuperAdminId() {
	return superAdminId;
    }
}
