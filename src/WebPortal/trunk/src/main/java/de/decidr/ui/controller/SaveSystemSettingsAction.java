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

package de.decidr.ui.controller;

/**
 * This action saves changes of the system settings
 *
 * @author Geoffrey-Alexeij Heinze
 * @reviewed ~tk, ~dh
 */

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.SystemFacade;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.SystemSettingComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

public class SaveSystemSettingsAction implements ClickListener {

	private HttpSession session = Main.getCurrent().getSession();

	private Long userId = (Long) session.getAttribute("userId");
	private SystemFacade systemFacade = new SystemFacade(new SuperAdminRole(
			userId));
	private UserFacade userFacade = new UserFacade(new SuperAdminRole(userId));

	private SystemSettingComponent content = null;
	private Item item = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
	 * ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		SystemSettings settings = DecidrGlobals.getSettings();

		content = (SystemSettingComponent) Main.getCurrent().getUIDirector()
				.getTemplateView().getContent();
		content.saveSettingsItem();
		item = content.getSettingsItem();

		if(item.getItemProperty("autoAcceptNewTenants").getValue() != null){
			settings
			.setAutoAcceptNewTenants(Boolean.parseBoolean(item
					.getItemProperty("autoAcceptNewTenants").getValue()
					.toString()));
		}
		
		if (item.getItemProperty("logLevel").getValue() != null) {
			settings.setLogLevel(item.getItemProperty("logLevel").getValue()
					.toString());
		}
		if (item
					.getItemProperty("changeEmailRequestLifetimeSeconds")
					.getValue() != null) {
			settings.setChangeEmailRequestLifetimeSeconds(Integer.parseInt(item
					.getItemProperty("changeEmailRequestLifetimeSeconds")
					.getValue().toString()));
		}
		if (item.getItemProperty("domain").getValue() != null) {
			settings.setDomain(item.getItemProperty("domain").getValue()
					.toString());
		}
		if (item
					.getItemProperty("invitationLifetimeSeconds").getValue() != null) {
			settings.setInvitationLifetimeSeconds(Integer.parseInt(item
					.getItemProperty("invitationLifetimeSeconds").getValue()
					.toString()));
		}
		if (item
					.getItemProperty("maxAttachmentsPerEmail").getValue() != null) {
			settings.setMaxAttachmentsPerEmail(Integer.parseInt(item
					.getItemProperty("maxAttachmentsPerEmail").getValue()
					.toString()));
		}
		if (item
					.getItemProperty("maxUploadFileSizeBytes").getValue() != null) {
			settings.setMaxUploadFileSizeBytes(Long.parseLong(item
					.getItemProperty("maxUploadFileSizeBytes").getValue()
					.toString()));
		}
		if (item.getItemProperty("mtaHostname")
					.getValue() != null) {
			settings.setMtaHostname(item.getItemProperty("mtaHostname")
					.getValue().toString());
		}
		if (item.getItemProperty("mtaPassword")
					.getValue() != null) {
			settings.setMtaPassword(item.getItemProperty("mtaPassword")
					.getValue().toString());
		}
		if (item
					.getItemProperty("mtaPort").getValue() != null) {
			settings.setMtaPort(Integer.parseInt(item
					.getItemProperty("mtaPort").getValue().toString()));
		}
		if (item.getItemProperty("mtaUsername")
					.getValue() != null) {
			settings.setMtaUsername(item.getItemProperty("mtaUsername")
					.getValue().toString());
		}
		if (item.getItemProperty(
					"mtaUseTls").getValue() != null) {
			settings.setMtaUseTls(Boolean.parseBoolean(item.getItemProperty(
					"mtaUseTls").getValue().toString()));
		}
		if (item.getItemProperty(
							"passwordResetRequestLifetimeSeconds").getValue() != null) {
			settings.setPasswordResetRequestLifetimeSeconds(Integer
					.parseInt(item.getItemProperty(
							"passwordResetRequestLifetimeSeconds").getValue()
							.toString()));
		}
		if (item.getItemProperty(
		"registrationRequestLifetimeSeconds").getValue() != null) {
			settings.setRegistrationRequestLifetimeSeconds(Integer
					.parseInt(item.getItemProperty(
							"registrationRequestLifetimeSeconds").getValue()
							.toString()));
		}
		//settings.setSuperAdmin((User) item.getItemProperty("superAdmin")
				//.getValue());
		if (item.getItemProperty("systemName")
					.getValue() != null) {
			settings.setSystemName(item.getItemProperty("systemName")
					.getValue().toString());
		}
		if (item.getItemProperty(
					"systemEmailAddress").getValue() != null) {
			settings.setSystemEmailAddress(item.getItemProperty(
					"systemEmailAddress").getValue().toString());
		}
		if (item.getItemProperty(
							"monitorUpdateIntervalSeconds").getValue() != null) {
			settings.setPasswordResetRequestLifetimeSeconds(Integer
					.parseInt(item.getItemProperty(
							"monitorUpdateIntervalSeconds").getValue()
							.toString()));
		}
		if (item
					.getItemProperty("monitorAveragingPeriodSeconds")
					.getValue() != null) {
			settings.setMonitorAveragingPeriodSeconds(Integer.parseInt(item
					.getItemProperty("monitorAveragingPeriodSeconds")
					.getValue().toString()));
		}
		if (item
					.getItemProperty("serverPoolInstances").getValue() != null) {
			settings.setServerPoolInstances(Integer.parseInt(item
					.getItemProperty("serverPoolInstances").getValue()
					.toString()));
		}
		if (item
					.getItemProperty("minServerLoadForLock").getValue() != null) {
			settings.setMinServerLoadForLock(Byte.parseByte(item
					.getItemProperty("minServerLoadForLock").getValue()
					.toString()));
		}
		if (item
					.getItemProperty("maxServerLoadForUnlock").getValue() != null) {
			settings.setMaxServerLoadForUnlock(Byte.parseByte(item
					.getItemProperty("maxServerLoadForUnlock").getValue()
					.toString()));
		}
		if (item
					.getItemProperty("maxServerLoadForShutdown").getValue() != null) {
			settings.setMaxServerLoadForShutdown(Byte.parseByte(item
					.getItemProperty("maxServerLoadForShutdown").getValue()
					.toString()));
		}
		if (item
					.getItemProperty("minUnlockedServers").getValue() != null) {
			settings.setMinUnlockedServers(Integer.parseInt(item
					.getItemProperty("minUnlockedServers").getValue()
					.toString()));
		}
		if (item
					.getItemProperty("minWorkflowInstancesForLock").getValue() != null) {
			settings.setMinWorkflowInstancesForLock(Integer.parseInt(item
					.getItemProperty("minWorkflowInstancesForLock").getValue()
					.toString()));
		}
		if (item
					.getItemProperty("maxWorkflowInstancesForUnlock").getValue() != null) {
			settings.setMaxWorkflowInstancesForUnlock(Integer.parseInt(item
					.getItemProperty("maxWorkflowInstancesForUnlock").getValue()
					.toString()));
		}
		if (item
					.getItemProperty("maxWorkflowInstancesForShutdown")
					.getValue() != null) {
			settings.setMaxWorkflowInstancesForShutdown(Integer.parseInt(item
					.getItemProperty("maxWorkflowInstancesForShutdown")
					.getValue().toString()));
		}
		try {
			systemFacade.setSettings(settings);
		} catch (TransactionException e) {
			Main.getCurrent().getMainWindow().addWindow(
					new TransactionErrorDialogComponent(e));
		}
	}
}
