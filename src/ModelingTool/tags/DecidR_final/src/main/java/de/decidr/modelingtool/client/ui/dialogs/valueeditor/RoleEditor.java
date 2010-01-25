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

package de.decidr.modelingtool.client.ui.dialogs.valueeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.dnd.ListViewDragSource;
import com.extjs.gxt.ui.client.dnd.ListViewDropTarget;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.command.ChangeVariablesCommand;
import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog;
import de.decidr.modelingtool.client.ui.dialogs.variableeditor.VariableEditorWindow;

/**
 * A dialog for selecting users for a {@link Variable} of the type
 * {@link VariableType#ROLE}.
 * 
 * @author Jonas Schlaak
 */
public class RoleEditor extends ModelingToolDialog {

    private ContentPanel contentPanel;
    private ListView<RoleEditorUser> tenantUsersView;
    private ListView<RoleEditorUser> roleUsersView;

    private HashMap<Long, String> tenantUsers;
    private HashMap<Long, String> tenantUsersNotInRole;
    private List<Long> roleUserIds;

    private Variable variable;

    /**
     * Default constructor.
     */
    public RoleEditor() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(400, 200);
        this.setResizable(true);
        createContentPanel();
        createButtons();
    }

    private void changeWorkflowModel(List<String> newValues) {
        /*
         * Check if the variable is already in the workflow model. If that is
         * the case, it means the value editor was called outside of the
         * variable editor (for example, from an email activity. Therefore any
         * changes have to be pushed in to the command stack. If the variable is
         * not in the workflow model, it means that the variable is a reference
         * to an element in the list store of the variable editor.
         */
        if (Workflow.getInstance().getModel().getVariables().contains(variable)) {
            if (variable.getValues().equals(newValues)) {
                Variable newVariable = variable.copy();
                newVariable.setValues(newValues);
                CommandStack.getInstance().executeCommand(
                        new ChangeVariablesCommand(newVariable));
            }
        } else {
            variable.setValues(newValues);
        }
    }

    private void clearAllEntries() {
        tenantUsers.clear();
        tenantUsersView.getStore().removeAll();
        tenantUsersNotInRole.clear();
        roleUserIds.clear();
        roleUsersView.getStore().removeAll();
    }

    /**
     * Create ok and cancel button.
     */
    private void createButtons() {
        setButtonAlign(HorizontalAlignment.CENTER);
        addButton(new Button(ModelingToolWidget.getMessages().okButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        List<String> newValues = new ArrayList<String>();
                        for (RoleEditorUser user : roleUsersView.getStore()
                                .getModels()) {
                            newValues.add(user.getUserId().toString());
                        }

                        changeWorkflowModel(newValues);
                        /*
                         * Refresh of the variable editor needed so that the
                         * displayed values are updated
                         */
                        ((VariableEditorWindow) DialogRegistry
                                .getInstance()
                                .getDialog(VariableEditorWindow.class.getName()))
                                .refresh();
                        DialogRegistry.getInstance().hideDialog(
                                RoleEditor.class.getName());

                    }
                }));
        addButton(new Button(ModelingToolWidget.getMessages().cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().hideDialog(
                                RoleEditor.class.getName());
                    }
                }));
    }

    /**
     * Creates the {@link ContentPanel} which holds two {@link ListView}s. One
     * to select users from, the other holds the selected users.
     */
    private void createContentPanel() {
        contentPanel = new ContentPanel();
        contentPanel
                .setHeading(ModelingToolWidget.getMessages().editVariable());
        contentPanel.setFrame(true);
        contentPanel.setLayout(new RowLayout(Orientation.HORIZONTAL));

        tenantUsersView = new ListView<RoleEditorUser>();
        tenantUsersView.setDisplayProperty(RoleEditorUser.DISPLAYNAME);
        tenantUsersView.setStyleAttribute("backgroundColor", "white");

        roleUsersView = new ListView<RoleEditorUser>();
        roleUsersView.setDisplayProperty(RoleEditorUser.DISPLAYNAME);
        roleUsersView.setStyleAttribute("backgroundColor", "white");

        new ListViewDragSource(tenantUsersView);
        new ListViewDragSource(roleUsersView);

        new ListViewDropTarget(tenantUsersView);
        new ListViewDropTarget(roleUsersView);

        RowData data = new RowData(.5, 1);
        data.setMargins(new Margins(5));

        contentPanel.add(tenantUsersView, data);
        contentPanel.add(roleUsersView, data);

        this.add(contentPanel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.Dialog#initialize()
     */
    @Override
    public Boolean initialize() {
        tenantUsers = new HashMap<Long, String>();
        for (Long userId : Workflow.getInstance().getUsers().keySet()) {
            tenantUsers.put(new Long(userId), new String(Workflow.getInstance()
                    .getUsers().get(userId)));
        }

        roleUserIds = new ArrayList<Long>();
        for (String userId : variable.getValues()) {
            try {
                roleUserIds.add(new Long(userId));
            } catch (NumberFormatException e) {
                MessageBox.alert(ModelingToolWidget.getMessages()
                        .warningTitle(), ModelingToolWidget.getMessages()
                        .invalidUserIds(), null);
                return false;
            }
        }

        setStores(tenantUsersView, roleUsersView);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.Dialog#reset()
     */
    @Override
    public void reset() {
        clearAllEntries();
    }

    /**
     * Sets the store of the two list views. The first list
     * (tenantUsersNotInRole) serves as "source" for users from which the users
     * are selected for the second list (roleUsers) , the "target". The second
     * list contains all the users selected for the role. The "tenantUsers" list
     * serves as reference, that means every user id in "roleUsers" has to be in
     * "tenantUsers", or else it will be deleted (to prevent obsolete user names
     * from appearing in the list view). User ids from "roleUsers" that are in
     * "tenantUsersNotInRole" are deleted from "tenantUsersNotInRole" to keep
     * the lists consistent.
     * 
     * @param tenantUsersView
     *            the first list - source
     * @param roleUsersView
     *            the second list - target
     */
    private void setStores(ListView<RoleEditorUser> tenantUsersView,
            ListView<RoleEditorUser> roleUsersView) {
        tenantUsersNotInRole = new HashMap<Long, String>();

        /* Copy tenant user list */
        for (Long userid : tenantUsers.keySet()) {
            tenantUsersNotInRole.put(userid, tenantUsers.get(userid));
        }

        /* Check lists for consistency */
        for (Long userId : roleUserIds) {
            if (tenantUsersNotInRole.containsKey(userId)) {
                /*
                 * The user is in the role and therefore has to be removed from
                 * the "source" list
                 */
                tenantUsersNotInRole.remove(userId);
            } else {
                /*
                 * The user id cannot be found in the tenant user list, e.g. the
                 * user was deleted and no longer is in the role.
                 */
                roleUserIds.remove(userId);
            }
        }

        /* Create store for tenantUsersList */
        ListStore<RoleEditorUser> tenantUsersStore = new ListStore<RoleEditorUser>();
        tenantUsersStore.setStoreSorter(new StoreSorter<RoleEditorUser>(
                new RoleEditorUserComparator()));
        for (Long userId : tenantUsersNotInRole.keySet()) {
            tenantUsersStore.add(new RoleEditorUser(userId, tenantUsers
                    .get(userId)));
        }
        tenantUsersView.setStore(tenantUsersStore);

        /* Create store for roleUsersList */
        ListStore<RoleEditorUser> roleUsersStore = new ListStore<RoleEditorUser>();
        roleUsersStore.setStoreSorter(new StoreSorter<RoleEditorUser>(
                new RoleEditorUserComparator()));
        for (Long userId : roleUserIds) {
            roleUsersStore.add(new RoleEditorUser(userId, tenantUsers
                    .get(userId)));
        }
        roleUsersView.setStore(roleUsersStore);
        GWT.log("Finished setting stores of user lists.", null);
    }

    /**
     * Sets the "role" variable that is to be modeled with this dialog.
     * 
     * @param variable
     *            the variable
     */
    public void setVariable(Variable variable) {
        this.variable = variable;
    }

}