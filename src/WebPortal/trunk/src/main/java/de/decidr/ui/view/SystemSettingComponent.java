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
package de.decidr.ui.view;

import com.vaadin.data.Item;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.SaveSystemSettingsAction;

public class SystemSettingComponent extends CustomComponent {

	/**
	 * The user can change the system settings. 
	 * 
	 * @author AT
	 */
	private static final long serialVersionUID = 3389525551936631625L;

	private Item settingsItem = null;

	private VerticalLayout verticalLayout = null;

	private Form settingsForm = null;

	private static final String[] logLevel = new String[] { "INFO", "TRACE",
			"DEBUG", "WARN", "ERROR", "FATAL" };

	private Button saveButton = null;

	/**
	 * Default constructor
	 * 
	 */
	public SystemSettingComponent(Item settingsItem) {
		this.settingsItem = settingsItem;
		init();
	}


	public Form getSettingsForm() {
		return settingsForm;
	}

	/**
	 * Returns the settings item.
	 * 
	 * @return settingsItem
	 */
	public Item getSettingsItem() {
		return settingsItem;
	}

	/**
	 * This method initializes the components of the system settings component
	 * 
	 */

	private void init() {
		settingsForm = new Form();
		//settingsForm.setFormFieldFactory(new SystemSettingsFieldFactory());
		settingsForm.setWriteThrough(true);
		settingsForm.setImmediate(true);
		settingsForm.setItemDataSource(settingsItem);

		verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);


		saveButton = new Button("Save", new SaveSystemSettingsAction());

		setCompositionRoot(verticalLayout);
		setCaption("System Setting");

		verticalLayout.addComponent(settingsForm);
		verticalLayout.addComponent(saveButton);
		verticalLayout.setComponentAlignment(saveButton, Alignment.BOTTOM_LEFT);
	}

	/**
	 * Updates all changes since the previous commit to the data source.
	 * 
	 */
	public void saveSettingsItem() {
		try {
			settingsForm.commit();

		} catch (Exception e) {
			Main.getCurrent().getMainWindow().showNotification(e.getMessage());
		}
	}

	private class SystemSettingsFieldFactory extends DefaultFieldFactory {

		/**
		 * Serial version uid
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public Field createField(Item item, Object propertyId,
				Component uiContext) {
			Field field = super.createField(item, propertyId, uiContext);
			if ("logLevel".equals(propertyId)) {
				NativeSelect nativeSelect = (NativeSelect) field;
				nativeSelect.setCaption("Log Level");
				for (int i = 0; i < logLevel.length; i++) {
					nativeSelect.addItem(logLevel[i]);
				}
				nativeSelect.setNullSelectionAllowed(false);
				nativeSelect.setValue(logLevel[0]);
			}else if("superAdmin".equals(propertyId)){
				TextField tf = (TextField) field;
				tf.setCaption("Super admin");
				tf.setColumns(30);
			}
			else if ("autoAcceptNewTenants".equals(propertyId)) {
				CheckBox checkbox = (CheckBox) field;
				checkbox.setCaption("Automatically approve all new tenants");
			} else if ("systemName".equals(propertyId)) {
				TextField tf = (TextField) field;
				tf.setCaption("System Name");
				tf.setColumns(30);
			} else if ("domain".equals(propertyId)) {
				TextField tf = (TextField) field;
				tf.setCaption("Domain Name");
				tf.setColumns(30);
			} else if ("systemEmailAddress".equals(propertyId)) {
				TextField tf = (TextField) field;
				tf.setCaption("System email address");
				tf.setColumns(30);
				tf.addValidator(new EmailValidator(
						"Please insert a valid email address"));
			} else if ("passwordResetRequestLifeTimeSeconds".equals(propertyId)) {
				Slider slider = (Slider) field;
				slider.setCaption("Password reset request lifetime");
				slider.setMax(259200.00);
				slider.setMin(0.00);
			} else if ("registrationRequestLifetimeSeconds".equals(propertyId)) {
				Slider slider = (Slider) field;
				slider.setCaption("Registration request lifetime");
				slider.setMax(259200);
				slider.setMin(0);
			} /*else if ("changeEmailRequestLifetimeSeconds".equals(propertyId)) {
				Slider slider = (Slider) field;
				slider.setCaption("Change email request lifetime");
				slider.setMax(259200);
				slider.setMin(0);
			} */else if ("invitationLifetimeSeconds".equals(propertyId)) {
				Slider slider = (Slider) field;
				slider.setCaption("Invitation lifetime");
				slider.setMax(259200);
				slider.setMin(0);
			} else if ("mtaHostname".equals(propertyId)) {
				TextField tf = (TextField) field;
				tf.setCaption("MTA hostname");
				tf.setColumns(30);
			} else if ("mtaPort".equals(propertyId)) {
				TextField tf = (TextField) field;
				tf.setCaption("MTA port");
				tf.setColumns(30);
				tf.addValidator(new IntegerValidator(
						"A valid port number does contain only numbers"));
			} else if ("mtaUseTls".equals(propertyId)) {
				CheckBox checkbox = (CheckBox) field;
				checkbox.setCaption("Use MTA transport layer");
			} else if ("mtaUsername".equals(propertyId)) {
				TextField tf = (TextField) field;
				tf.setCaption("MTA username");
				tf.setColumns(30);
			} else if ("mtaPassword".equals(propertyId)) {
				TextField tf = (TextField) field;
				tf.setCaption("MTA password");
				tf.setColumns(30);
				tf.setSecret(true);
			} else if ("maxUploadFileSizeByte".equals(propertyId)) {
				Slider slider = (Slider) field;
				slider.setCaption("Max upload size");
				slider.setMax(10000);
				slider.setMin(0);
			}else if("maxAttachmentSlider".equals(propertyId)){
				Slider slider = (Slider) field;
				slider.setCaption("Max attachments per email");
				slider.setMax(10000);
				slider.setMin(0);
			}else if("monitorUpdateIntervalSeconds".equals(propertyId)){
				Slider slider = (Slider) field;
				slider.setCaption("Monitor update interval seconds");
				slider.setMax(10000);
				slider.setMin(0);
			}else if("monitorAveragingPeriodSeconds".equals(propertyId)){
				Slider slider = (Slider) field;
				slider.setCaption("Monitor average period seconds");
				slider.setMax(10000);
				slider.setMin(0);
			}else if("serverPoolInstances".equals(propertyId)){
				Slider slider = (Slider) field;
				slider.setCaption("Server pool instances");
				slider.setMax(10);
				slider.setMin(0);
			}else if("minServerLoadForLock".equals(propertyId)){
				Slider slider = (Slider) field;
				slider.setCaption("Min server load for locking");
				slider.setMax(10000);
				slider.setMin(0);
			}else if("minServerLoadForUnlock".equals(propertyId)){
				Slider slider = (Slider) field;
				slider.setCaption("Min server load for unlocking");
				slider.setMax(10000);
				slider.setMin(0);
			}else if("maxServerLoadForShutdown".equals(propertyId)){
				Slider slider = (Slider) field;
				slider.setCaption("Max server load for shutdown");
				slider.setMax(10000);
				slider.setMin(0);
			}else if("minUnlockedServers".equals(propertyId)){
				Slider slider = (Slider) field;
				slider.setCaption("Min unlocked servers");
				slider.setMax(10);
				slider.setMin(0);
			}else if("minWorkflowInstancesForLock".equals(propertyId)){
				Slider slider = (Slider) field;
				slider.setCaption("Min workflow instances for locking");
				slider.setMax(100);
				slider.setMin(0);
			}else if("maxWorkflowInstanceForUnlock".equals(propertyId)){
				Slider slider = (Slider) field;
				slider.setCaption("Max workflow instances for locking");
				slider.setMax(100);
				slider.setMin(0);
			}else if("maxWorkflowInstancesForShutdown".equals(propertyId)){
				Slider slider = (Slider) field;
				slider.setCaption("Max workflow instances for shutdown");
				slider.setMax(100);
				slider.setMin(0);
			}
			return field;
		}
	}

}
