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

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.w3c.dom.DOMException;

import com.vaadin.ui.Form;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
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
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This actions saves the start configuration. It goes through the window and
 * set the entered value in the start configuration object
 * 
 * @author AT
 */
@Reviewed(reviewers = { "TK", "DH", "RR" }, lastRevision = "2343", currentReviewState = State.PassedWithComments)
public class SaveStartConfigurationAction implements ClickListener {

    private static final long serialVersionUID = 1L;

    private Form form = null;

    private Tree tree = null;

    private TConfiguration tConfiguration = null;

    private Long workflowModelId = null;

    private boolean checked = false;

    private Role role = (Role) Main.getCurrent().getSession().getAttribute(
            "role");

    private WorkflowModelFacade workflowModelFacade = new WorkflowModelFacade(
            role);

    /**
     * Aleks the constructor does not actually perform the save action ~dh
     * 
     * Constructor which saves the role tree, the form of the start
     * configuration window. Also the start configuration object,
     * {@link TConfiguration}, is saved with the given workflow model ID. And a
     * boolean value is saved, indicating whether the user wants to start the
     * workflow instance immediately or not.
     * 
     * @param tree
     *            TODO document
     * @param form
     *            TODO document
     * @param tConfiguation
     *            TODO document
     * @param workflowModelId
     *            TODO document
     * @param checked
     *            TODO document
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
        if (form.isValid()) {
            Collection<Object> itemIds = tree.getItemIds();
            for (Object id : itemIds) {
                if (tree.isRoot(id)) {
                    for (TRole role : tConfiguration.getRoles().getRole()) {
                        Collection<Object> collect = tree.getChildren(id);
                        if (collect != null) {
                            for (Object childId : collect) {
                                TActor tActor = new TActor();
                                String actorOrEmail = tree.getItem(childId)
                                        .getItemProperty("actor").getValue()
                                        .toString();
                                if (actorOrEmail.contains("@")) {
                                    tActor.setEmail(actorOrEmail);
                                } else {
                                    tActor.setName(actorOrEmail);
                                }
                                role.getActor().add(tActor);
                            }
                        }
                    }
                }
            }

            for (TAssignment assignment : tConfiguration.getAssignment()) {
                Object value = form.getField(assignment.getKey()).getValue();
                if (assignment.getValueType().equals("date")) {
                    SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = (Date) value;
                        value = dfs.format(date);
                    } catch (DOMException e) {
                        Main.getCurrent().getMainWindow().addWindow(
                                new TransactionErrorDialogComponent(e));
                    }
                }
                assignment.getValue().add(value.toString());
            }

            try {
                //AT :Commented code is just for testing as long as no ODE is running
                /*
                 * AbstractTransactionalCommand cmd = new
                 * AbstractTransactionalCommand() {
                 * 
                 * 
                 * (non-Javadoc)
                 * 
                 * 
                 * 
                 * 
                 * @seede.decidr.model.commands.AbstractTransactionalCommand#
                 * transactionStarted
                 * (de.decidr.model.transactions.TransactionEvent)
                 * 
                 * @Override public void transactionStarted(TransactionEvent
                 * evt) throws TransactionException { File file = new File("",
                 * "", true, true, true, 10L, new Date(), true); try {
                 * file.setData(XmlTools.getBytes(tConfiguration)); } catch
                 * (JAXBException e) { throw new TransactionException(e); }
                 * evt.getSession().save(file); } };
                 */

                /*
                 * SaveStartConfigurationCommand saveCmd = new
                 * SaveStartConfigurationCommand( role, workflowModelId,
                 * tConfiguration);
                 */
                // HibernateTransactionCoordinator.getInstance().runTransaction(
                // cmd);
                workflowModelFacade.startWorkflowInstance(workflowModelId,
                        tConfiguration, checked);
                Main
                        .getCurrent()
                        .getMainWindow()
                        .addWindow(
                                new InformationDialogComponent(
                                        "Start configuration successfully saved and workflow instance started",
                                        "Success"));
            } catch (UsernameNotFoundException e) {
                Main.getCurrent().getMainWindow().showNotification(
                        "Username not found!");
            } catch (UserDisabledException e) {
                Main.getCurrent().getMainWindow().showNotification(
                        "User disabled!");
            } catch (UserUnavailableException e) {
                Main.getCurrent().getMainWindow().showNotification(
                        "User unavailable!");
            } catch (WorkflowModelNotStartableException e) {
                Main.getCurrent().getMainWindow().showNotification(
                        "Workflow model is not startable!");
            } catch (TransactionException e) {
                Main.getCurrent().getMainWindow().addWindow(
                        new TransactionErrorDialogComponent(e));
            }
        } else {
            Main
                    .getCurrent()
                    .getMainWindow()
                    .addWindow(
                            new InformationDialogComponent(
                                    "If you haven't added actors to the role(s) please add some actors and fill out the form",
                                    "Information"));
        }

        Main.getCurrent().getMainWindow().removeWindow(
                event.getButton().getWindow());
    }
}
