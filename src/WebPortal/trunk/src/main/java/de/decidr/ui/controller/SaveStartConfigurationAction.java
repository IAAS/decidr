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

import java.util.Collection;

import com.vaadin.ui.Form;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.exceptions.UserDisabledException;
import de.decidr.model.exceptions.UserUnavailableException;
import de.decidr.model.exceptions.UsernameNotFoundException;
import de.decidr.model.exceptions.WorkflowModelNotStartableException;
import de.decidr.model.facades.WorkflowModelFacade;
import de.decidr.model.workflowmodel.wsc.TActor;
import de.decidr.model.workflowmodel.wsc.TAssignment;
import de.decidr.model.workflowmodel.wsc.TConfiguration;
import de.decidr.model.workflowmodel.wsc.TRole;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This actions saves the start configuration. It goes through the window and
 * set the entered value in the start configuration object
 * 
 * @author AT
 * @reviewed ~tk, ~dh
 */
public class SaveStartConfigurationAction implements ClickListener {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;

	private Form form = null;

	private Tree tree = null;

	private TConfiguration tConfiguration = null;

	private Long workflowModelId = null;

	private boolean checked = false;
	
	private Role role = (Role) Main.getCurrent().getSession().getAttribute("role");

	private WorkflowModelFacade workflowModelFacade = new WorkflowModelFacade(
			role);

	/**
	 * AT the constructor does not actually perform the save action~dh
	 * 
	 * Constructor which saves the role tree, the form of the start
	 * configuration window. Also the start configuration object,
	 * tConfiguration, is saved with the given workflow model id. And a boolean
	 * value is saved if the user wants to start the workflow instance
	 * immediately or not.
	 * 
	 * @param tree
	 * @param form
	 * @param tConfiguation
	 * @param workflowModelId
	 * @param checked
	 */
	public SaveStartConfigurationAction(Tree tree, Form form,
			TConfiguration tConfiguation, Long workflowModelId, boolean checked) {
		this.tree = tree;
		this.form = form;
		this.tConfiguration = tConfiguation;
		this.workflowModelId = workflowModelId;
		this.checked = checked;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
	 * ClickEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void buttonClick(ClickEvent event) {		
		for (TRole role : tConfiguration.getRoles().getRole()) {
			Collection<TActor> collect = tree.getChildren(role.getName());
			if (collect.size() > 0) {
				for (TActor tActor : collect) {
					role.getActor().add(tActor);
				}
			}
		}
		//AT bitte checken, ob es überhaupt den ValueType "File" gibt ~tk,dh
		for (TAssignment assignment : tConfiguration.getAssignment()) {
			if (assignment.getValueType().equals("File")) {
				assignment.getValue().add(
						String.valueOf(Main.getCurrent().getMainWindow()
								.getData()));
			} else {
				assignment.getValue().add(
						form.getField(assignment.getKey()).getValue()
								.toString());
			}

		}
		try {
			workflowModelFacade.startWorkflowInstance(workflowModelId,
					tConfiguration, checked);
		} catch (UsernameNotFoundException expception) {
			Main.getCurrent().getMainWindow().showNotification(
					"Username not found!");
		} catch (UserDisabledException expception) {
			Main.getCurrent().getMainWindow().showNotification(
					"Username disabled!");
		} catch (UserUnavailableException exceptions) {
			Main.getCurrent().getMainWindow().showNotification(
					"User unavailable!");
		} catch (WorkflowModelNotStartableException exception) {
			Main.getCurrent().getMainWindow().showNotification(
					"Workflow model is not startable!");
		} catch (TransactionException exception) {
			Main.getCurrent().getMainWindow().addWindow(
					new TransactionErrorDialogComponent(exception));
		}
		new HideDialogWindowAction();
	}

}
