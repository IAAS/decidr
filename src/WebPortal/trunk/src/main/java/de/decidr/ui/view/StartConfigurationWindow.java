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

import java.util.Collection;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.Action;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Button.ClickEvent;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.workflowmodel.wsc.TAssignment;
import de.decidr.model.workflowmodel.wsc.TConfiguration;
import de.decidr.model.workflowmodel.wsc.TRole;
import de.decidr.ui.controller.HideDialogWindowAction;
import de.decidr.ui.controller.SaveStartConfigurationAction;
import de.decidr.ui.controller.UploadFileHumanTaskAction;

/**
 * This window represents the start configuration xml file. In this window the
 * user can add actors for roles and can enter his value for assignment
 * variables. These values and actors will be stored in the xml file, so the
 * user can start a workflow instance with the given configuration file.
 * 
 * @author AT
 */
public class StartConfigurationWindow extends Window {

    private HorizontalLayout horizontalLayout = null;

    private VerticalLayout verticalLayout = null;

    private VerticalLayout mainVerticalLayout = null;

    private HorizontalLayout buttonHorizontalLayout = null;

    private SplitPanel splitPanel = null;

    private Tree rolesTree = null;

    private Form assignmentForm = null;

    private TextField emailTextField = null;

    private Button applyButton = null;

    private Button okButton = null;

    private Button cancelButton = null;

    private CheckBox checkBox = null;

    private ComboBox comboBox = null;

    private Upload upload = null;

    private TConfiguration tConfiguration = null;

    private Long workflowModelId = null;

    private static final Action ACTION_ADD = new Action("Add child item");
    private static final Action ACTION_DELETE = new Action("Delete");

    private static final Action[] ACTIONS = new Action[] { ACTION_ADD,
            ACTION_DELETE };

    /**
     * Default constructor
     * 
     */
    public StartConfigurationWindow() {
        init(this.tConfiguration, this.workflowModelId);
    }

    /**
     * Default constructor with TConfiguration as parameter.
     * 
     */
    public StartConfigurationWindow(TConfiguration tConfiguration,
            Long workflowModelId) {
        this.tConfiguration = tConfiguration;
        this.workflowModelId = workflowModelId;
        init(tConfiguration, workflowModelId);
    }


    /**
     * Initializes the components for the start configuration window. The
     * Tconfiguration which represents the start configuration xml file is given
     * as parametrer.
     * 
     * @param tconfiguration
     */
    private void init(TConfiguration tconfiguration, Long workflowModelId) {
        splitPanel = new SplitPanel();
        horizontalLayout = new HorizontalLayout();
        verticalLayout = new VerticalLayout();
        mainVerticalLayout = new VerticalLayout();
        buttonHorizontalLayout = new HorizontalLayout();
        rolesTree = new Tree("Rollen");
        assignmentForm = new Form();
        emailTextField = new TextField("E-Mail: ");
        applyButton = new Button("Apply");
        okButton = new Button("OK", new SaveStartConfigurationAction(rolesTree,
                assignmentForm, tconfiguration, workflowModelId, checkBox
                        .booleanValue()));
        cancelButton = new Button("Cancel", new HideDialogWindowAction());
        checkBox = new CheckBox();
        comboBox = new ComboBox("Wählen sie einen User aus!");
        upload = new Upload("Upload", new UploadFileHumanTaskAction());

        rolesTree.setItemCaptionPropertyId("caption");
        rolesTree.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
        rolesTree.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                // holt das ausgewählte item aus dem tree
                Item item = rolesTree.getItem(rolesTree.getValue());
                // holt die property ids aus dem ausgewählten item
                Collection<?> collect = item.getItemPropertyIds();
                // Geht die property ids durch, schaut ob es property ids
                // userId,
                // username oder email
                // gibt. Wenn ja wird der Wert dieser Property, in diesem Fall
                // ein
                // TextField, ausgelesen,
                // sodass der Wert des TextField in das entsprechende TextField
                // gesetzt
                // werden kann, damit
                // der User die schon eingegeben Daten des Actors wieder
                // einsehen kann.
                for (int i = 0; i < collect.size(); i++) {
                    if (collect.contains(item.getItemProperty("combobox"))) {
                        ComboBox cb = (ComboBox) item.getItemProperty(
                                "combobox").getValue();
                        comboBox.setValue(cb.getValue());
                    }
                    if (collect.contains(item.getItemProperty("email"))) {
                        TextField tf = (TextField) item
                                .getItemProperty("email").getValue();
                        emailTextField.setValue(tf.getValue().toString());
                    }
                    if (!collect.contains(item.getItemProperty("combobox"))
                            && !collect.contains(item.getItemProperty("email"))) {
                        showNotification("Please enter a value so they can be shown");
                    }
                }

            }

        });
        rolesTree.addActionHandler(new Action.Handler() {

            @Override
            public Action[] getActions(Object target, Object sender) {

                return ACTIONS;
            }

            @Override
            public void handleAction(Action action, Object sender, Object target) {
                if (action == ACTION_ADD) {
                    Object itemId = rolesTree.addItem();
                    // Setzt den Vaterknoten des neuhinzugefügten Kindes und
                    // erlaubt es
                    // dem Kind nicht,
                    // weitere Kinder zu haben. Und setzt die Caption im Tree.
                    rolesTree.setParent(itemId, target);
                    rolesTree.setChildrenAllowed(itemId, false);

                    Item item = rolesTree.getItem(itemId);
                    Property caption = item.getItemProperty("caption");
                    caption.setValue("New Actor");
                } else if (action == ACTION_DELETE) {
                    rolesTree.removeItem(target);
                }

            }

        });

        comboBox.setWidth(175, UNITS_PIXELS);
        comboBox.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);
        comboBox.setImmediate(true);
        comboBox.setMultiSelect(false);
        comboBox.setNullSelectionAllowed(true);
        comboBox.setNewItemsAllowed(false);
        fillContainer();

        // The edited value should be taken and set as caption in the roles tree
        applyButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Item item = rolesTree.getItem(rolesTree.getValue());
                Property caption = item.getItemProperty("caption");
                if (!comboBox.getValue().equals("")) {
                    caption.setValue(comboBox.getValue());
                    item.addItemProperty("comboBox", comboBox);
                } else if (!emailTextField.getValue().equals("")) {
                    caption.setValue(emailTextField.getValue());
                    item.addItemProperty("email", emailTextField);
                } else {
                    showNotification("Please fill in a value in one of the three fields!");
                }

            }

        });

        this.setContent(mainVerticalLayout);

        this.setModal(true);

        horizontalLayout.setSpacing(true);
        horizontalLayout.addComponent(rolesTree);
        addRolesToTree(tconfiguration);

        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(emailTextField);
        verticalLayout.setMargin(false, true, false, false);

        horizontalLayout.addComponent(verticalLayout);
        horizontalLayout.addComponent(applyButton);

        splitPanel.setFirstComponent(horizontalLayout);

        assignmentForm.setCaption("Liste der Konfigurationsvariablen");
        assignmentForm.setWriteThrough(false);
        assignmentForm.setInvalidCommitted(false);
        addAssignmentToForm(tconfiguration);

        splitPanel.setSecondComponent(assignmentForm);

        mainVerticalLayout.addComponent(splitPanel);

        buttonHorizontalLayout.setSpacing(true);
        buttonHorizontalLayout.addComponent(checkBox);
        checkBox.setCaption("Start Immediately");
        buttonHorizontalLayout.setComponentAlignment(checkBox,
                Alignment.MIDDLE_RIGHT);
        buttonHorizontalLayout.addComponent(okButton);
        buttonHorizontalLayout.addComponent(cancelButton);

        mainVerticalLayout.addComponent(buttonHorizontalLayout);
    }
    
    /**
     * Adds a field for every assignment which is in the start configuration xml
     * file. So for every assignment the user can enter his desired value.
     * 
     * @param tConfiguration
     */
    private void addAssignmentToForm(TConfiguration tConfiguration) {
        for (TAssignment assignment : tConfiguration.getAssignment()) {

            if (assignment.getValueType().equals("String")) {
                assignmentForm.addField(assignment.getKey(), new TextField(
                        assignment.getKey()));
                assignmentForm.getField(assignment.getKey()).addValidator(
                        new RegexpValidator("[a-zA-Z]",
                                "Please enter a value of the type string"));
            } else if (assignment.getValueType().equals("Integer")) {
                assignmentForm.addField(assignment.getKey(), new TextField(
                        assignment.getKey()));
                assignmentForm.getField(assignment.getKey()).addValidator(
                        new IntegerValidator(
                                "Please enter a value of the type integer"));
            } else if (assignment.getValueType().equals("File")) {
                assignmentForm.getLayout().addComponent(upload);

            }
        }
    }

    /**
     * Adds a child to the tree which will be a parent node and which represents
     * the roles which are stored in the start configuration xml file. Also the
     * nodes can contain children and they will be expanded.
     * 
     * @param tConfiguration
     */
    private void addRolesToTree(TConfiguration tConfiguration) {
        for (TRole role : tConfiguration.getRoles().getRole()) {
            rolesTree.addItem(role.getName());
            rolesTree.setChildrenAllowed(role.getName(), true);
            rolesTree.expandItem(role.getName());

        }

    }

    /**
     * Fills the combo box with the current usernames of the tenant, so the user
     * can choose from the usernames and add them as actors to a role.
     * 
     */
    private void fillContainer() {
        HttpSession session = Main.getCurrent().getSession();
        String tenantName = (String) session.getAttribute("tenant");
        Long userId = (Long) session.getAttribute("userId");
        TenantFacade tenantFacade = new TenantFacade(new UserRole(userId));
        try {
            Long tenantId = tenantFacade.getTenantId(tenantName);
            for (Item item : tenantFacade.getUsersOfTenant(tenantId, null)) {
                comboBox.addItem(item.getItemProperty("username").getValue());
            }
        } catch (TransactionException exception) {
            Main.getCurrent().addWindow(new TransactionErrorDialogComponent());
        }

    }

    public Form getAssignmentForm() {
        return assignmentForm;
    }

    /**
     * Returns the check box
     * 
     * @return checkBox
     */
    public CheckBox getCheckBox() {
        return checkBox;
    }

    /**
     * Returns the upload field
     * 
     * @return uploadField
     */
    public Upload getUpload() {
        return upload;
    }

}
