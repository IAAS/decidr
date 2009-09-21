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

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.JAXBException;

import com.vaadin.ui.Form;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.exceptions.UserDisabledException;
import de.decidr.model.exceptions.UserUnavailableException;
import de.decidr.model.exceptions.UsernameNotFoundException;
import de.decidr.model.exceptions.WorkflowModelNotStartableException;
import de.decidr.model.facades.WorkflowModelFacade;
import de.decidr.model.workflowmodel.dwdl.translator.TransformUtil;
import de.decidr.model.workflowmodel.wsc.TAssignment;
import de.decidr.model.workflowmodel.wsc.TConfiguration;
import de.decidr.model.workflowmodel.wsc.TRole;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * TODO: add comment
 * 
 * @author AT
 */
public class SaveStartConfigurationAction implements ClickListener {

    private Form form = null;

    private Tree tree = null;

    private TConfiguration tConfiguration = null;

    private Long workflowModelId = null;

    private boolean checked = false;

    private WorkflowModelFacade workflowModelFacade = new WorkflowModelFacade(
            new UserRole((Long) Main.getCurrent().getSession().getAttribute(
                    "userId")));

    /**
     * TODO: add comment
     * 
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
    @Override
    public void buttonClick(ClickEvent event) {
        for (TRole role : tConfiguration.getRoles().getRole()) {
            ArrayList<TRole> collect = (ArrayList<TRole>) tree.getChildren(role
                    .getName());
            for (TRole tRole : collect) {
                this.tConfiguration.getRoles().getRole().add(tRole);
            }
        }
        for (TAssignment assignment : tConfiguration.getAssignment()) {
            assignment.getValue().add(
                    form.getField(assignment.getKey()).getValue().toString());
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
                    new TransactionErrorDialogComponent());
        }

    }

}
