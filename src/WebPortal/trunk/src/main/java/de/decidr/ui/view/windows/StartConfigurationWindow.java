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

package de.decidr.ui.view.windows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.event.Action;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Button.ClickEvent;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.dwdl.Variable;
import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.wsc.TAssignment;
import de.decidr.model.workflowmodel.wsc.TConfiguration;
import de.decidr.ui.beans.RoleBean;
import de.decidr.ui.controller.HideDialogWindowAction;
import de.decidr.ui.controller.SaveStartConfigurationAction;
import de.decidr.ui.data.FloatValidator;
import de.decidr.ui.data.TreeRoleContainer;
import de.decidr.ui.view.Main;

/**
 * This window represents the start configuration XML file. In this window the
 * user can add actors to roles and can enter values for assignment variables.
 * These values and actors will be stored in the XML file, so that the user can
 * start a workflow instance with the given configuration file.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2560", currentReviewState = State.PassedWithComments)
public class StartConfigurationWindow extends Window {

    private static final long serialVersionUID = 1L;

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

    private TConfiguration tConfiguration = null;

    private Long workflowModelId = null;

    private TreeRoleContainer treeRoleContainer = null;

    private static final Action ACTION_ADD = new Action("Add actor");
    private static final Action ACTION_DELETE = new Action("Delete");

    private static final Action[] ACTIONS = new Action[] { ACTION_ADD,
            ACTION_DELETE };

    private static int count = 0;

    private Workflow workflow = null;

    /**
     * Default constructor with TConfiguration as parameter.<br>
     * Aleks: If you want me to tell you what a default constructor is, don't
     * hesitate to ask ~rr
     */
    public StartConfigurationWindow(TConfiguration tConfiguration,
            Long workflowModelId, Workflow workflow) {
        this.tConfiguration = tConfiguration;
        this.workflowModelId = workflowModelId;
        this.workflow = workflow;
        
        DefaultLogger.getLogger(StartConfigurationWindow.class).
            debug("StartConfigurationWindow initialized, setting up ui components");
        init();
    }

    /**
     * Initializes the components for the {@link StartConfigurationWindow}. The
     * {@link TConfiguration} which represents the start configuration XML file
     * is passed as a parameter.
     */
    private void init() {
        splitPanel = new SplitPanel();
        horizontalLayout = new HorizontalLayout();
        verticalLayout = new VerticalLayout();
        mainVerticalLayout = new VerticalLayout();
        buttonHorizontalLayout = new HorizontalLayout();
        rolesTree = new Tree("Roles");
        treeRoleContainer = new TreeRoleContainer(tConfiguration, rolesTree);
        assignmentForm = new Form();
        emailTextField = new TextField("Email: ");
        applyButton = new Button("Apply");
        checkBox = new CheckBox();
        okButton = new Button("OK", new SaveStartConfigurationAction(rolesTree,
                assignmentForm, tConfiguration, workflowModelId, checkBox
                        .booleanValue()));
        cancelButton = new Button("Cancel", new HideDialogWindowAction());

        comboBox = new ComboBox();

        rolesTree.setItemCaptionPropertyId("actor");
        rolesTree.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
        rolesTree.setContainerDataSource(treeRoleContainer);
        rolesTree.setImmediate(true);

        emailTextField
                .setDescription("Enter a valid email address if you don't know the username");
        emailTextField.addValidator(new EmailValidator(
                "Please enter a valid email address"));
        emailTextField.setImmediate(true);

        comboBox.setWidth(175, UNITS_PIXELS);
        comboBox.setInputPrompt("Please choose a user");
        comboBox.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);
        comboBox.setImmediate(true);
        comboBox.setMultiSelect(false);
        comboBox.setNullSelectionAllowed(true);
        comboBox.setNewItemsAllowed(false);
        comboBox.setImmediate(true);

        DefaultLogger.getLogger(StartConfigurationWindow.class).
            debug("StartConfigurationWindow filling up containers...");
        fillContainer();

        DefaultLogger.getLogger(StartConfigurationWindow.class).
            debug("StartConfigurationWindow filling up containers...done");

        this.setContent(mainVerticalLayout);

        this.setCaption("Start configuration window");
        this.setModal(true);
        this.setWidth("700px");
        this.setHeight("400px");
        this.setResizable(false);

        mainVerticalLayout.setSpacing(true);
        mainVerticalLayout.addComponent(splitPanel);
        mainVerticalLayout.setMargin(true);

        splitPanel.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
        splitPanel.setSplitPosition(350, Sizeable.UNITS_PIXELS);
        splitPanel.setLocked(true);
        splitPanel.setFirstComponent(horizontalLayout);

        horizontalLayout.setHeight("300px");
        horizontalLayout.setSpacing(true);
        horizontalLayout.addComponent(rolesTree);
        horizontalLayout.addComponent(verticalLayout);

        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(comboBox);
        verticalLayout.addComponent(emailTextField);
        verticalLayout.addComponent(applyButton);

        splitPanel.setSecondComponent(assignmentForm);

        assignmentForm.setHeight("300px");
        assignmentForm.setCaption("Configuration variables");
        assignmentForm.setWriteThrough(false);
        assignmentForm.setInvalidCommitted(false);

        DefaultLogger.getLogger(StartConfigurationWindow.class).
            debug("StartConfigurationWindow adding assignment form...");
        addAssignmentToForm(tConfiguration);

        DefaultLogger.getLogger(StartConfigurationWindow.class).
            debug("StartConfigurationWindow adding assignemnt form...done");
        
        mainVerticalLayout.addComponent(buttonHorizontalLayout);

        buttonHorizontalLayout.setSpacing(true);
        buttonHorizontalLayout.addComponent(checkBox);
        checkBox.setCaption("Start Immediately");
        buttonHorizontalLayout.setComponentAlignment(checkBox,
                Alignment.MIDDLE_RIGHT);
        buttonHorizontalLayout.addComponent(okButton);
        buttonHorizontalLayout.addComponent(cancelButton);


        DefaultLogger.getLogger(StartConfigurationWindow.class).
            debug("StartConfigurationWindow initializing handler...");
        initializeHandler();

        DefaultLogger.getLogger(StartConfigurationWindow.class).
            debug("StartConfigurationWindow initializing handler...done");
    }

    private void initializeHandler() {
        rolesTree.addListener(new Property.ValueChangeListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void valueChange(ValueChangeEvent event) {
                emailTextField.setValue("");
                comboBox.setValue(null);
                if (event.getProperty().getValue() != null) {
                    String name = rolesTree.getItem(
                            event.getProperty().getValue()).getItemProperty(
                            "actor").toString();
                    if (name.indexOf("@") != -1) {
                        emailTextField.setValue(name);
                        emailTextField.requestRepaint();
                    } else if (comboBox.containsId(name)) {
                        comboBox.setValue(name);
                    }
                }
            }
        });

        rolesTree.addActionHandler(new Action.Handler() {

            private static final long serialVersionUID = 1L;

            @Override
            public Action[] getActions(Object target, Object sender) {
                return ACTIONS;
            }

            @Override
            public void handleAction(Action action, Object sender, Object target) {

                if (action == ACTION_ADD && rolesTree.isRoot(target)) {
                    RoleBean roleBean = new RoleBean("New Actor " + count);
                    Item newChildItem = new BeanItem(roleBean);
                    Object itemId = rolesTree.addItem(newChildItem);
                    // Sets the parent node. The children can't have
                    // children.
                    // Also, the caption is set.
                    rolesTree.setParent(itemId, target);
                    rolesTree.setChildrenAllowed(itemId, false);
                    rolesTree.expandItem(target);
                    count++;
                } else if (action == ACTION_DELETE && !rolesTree.isRoot(target)) {
                    rolesTree.removeItem(target);
                } else {
                    Main.getCurrent().getMainWindow().addWindow(
                            new InformationDialogComponent(
                                    "You can't delete a role or "
                                            + "add an actor to an actor",
                                    "Operation not allowed"));
                }
            }
        });

        comboBox.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (event.getProperty().getValue() != null
                        && !rolesTree.isRoot(rolesTree.getValue())) {
                    Item item = rolesTree.getItem(rolesTree.getValue());
                    Property actor = item.getItemProperty("actor");
                    actor.setValue(comboBox.getValue());
                    emailTextField.setValue("");
                } else if (event.getProperty().getValue() != null
                        && rolesTree.isRoot(event.getProperty().getValue())) {
                    Main.getCurrent().getMainWindow().addWindow(
                            new InformationDialogComponent(
                                    "You can't modify the role."
                                            + " Add an actor first.",
                                    "Information"));
                } else if (event.getProperty().getValue() == null
                        && rolesTree.getValue() == null
                        && comboBox.getValue() != null) {
                    Main.getCurrent().getMainWindow().addWindow(
                            new InformationDialogComponent(
                                    "Please select an actor "
                                            + "to assign the user to!",
                                    "Information"));
                }
            }
        });

        // The edited value should be taken and set as caption in the roles tree
        applyButton.addListener(new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                if (rolesTree.getValue() != null
                        && !rolesTree.isRoot(rolesTree.getValue())
                        && emailTextField.isValid()) {
                    Item item = rolesTree.getItem(rolesTree.getValue());
                    Property actor = item.getItemProperty("actor");
                    actor.setValue(emailTextField.getValue());
                    comboBox.setValue(null);
                } else if (rolesTree.isRoot(rolesTree.getValue())) {
                    Main.getCurrent().getMainWindow().addWindow(
                            new InformationDialogComponent(
                                    "You can't modify the role."
                                            + " Add an actor first.",
                                    "Information"));
                } else {
                    Main.getCurrent().getMainWindow().addWindow(
                            new InformationDialogComponent(
                                    "Please select an actor "
                                            + "to assign the email addresss "
                                            + "to or enter a valid "
                                            + "email address!", "Information"));
                }
            }
        });
    }

    /**
     * Adds a field for every assignment which is in the start configuration XML
     * file. This allows the user to enter a value for every field.
     * 
     * @param tConfiguration
     *            TODO document
     */
    private void addAssignmentToForm(TConfiguration tConfiguration) {
        for (TAssignment assignment : tConfiguration.getAssignment()) {
            // Aleks, GH: unused variable ~rr
            String label = getLabel(assignment.getKey());
            if (assignment.getValue().size() > 0) {
                for (String string : assignment.getValue()) {
                    if (assignment.getValueType().equals("string")) {
                        assignmentForm.addField(assignment.getKey(),
                                new TextField(assignment.getKey()));
                    } else if (assignment.getValueType().equals("integer")) {
                        assignmentForm.addField(assignment.getKey(),
                                new TextField(assignment.getKey()));
                        assignmentForm
                                .getField(assignment.getKey())
                                .addValidator(
                                        new IntegerValidator(
                                                "Please enter an integer value!"));
                    } else if (assignment.getValueType().equals("date")) {
                        assignmentForm.addField(assignment.getKey(),
                                new DateField(assignment.getKey()));
                        assignmentForm.getField(assignment.getKey()).setValue(
                                new Date());
                    } else if (assignment.getValueType().equals("float")) {
                        assignmentForm.addField(assignment.getKey(),
                                new TextField(assignment.getKey()));
                        assignmentForm
                                .getField(assignment.getKey())
                                .addValidator(
                                        new FloatValidator(
                                                "Please enter a floating point value!"));
                    }
                    if (assignmentForm.getField(assignment.getKey()) instanceof DateField) {
                        // Temporary solution. Should be improved.
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        Date date;
                        try {
                            date = simpleDateFormat.parse(string);
                        } catch (ParseException e) {
                            date = new Date();
                        }
                        assignmentForm.getField(assignment.getKey()).setValue(
                                date);
                    } else if (!string.isEmpty()) {
                        assignmentForm.getField(assignment.getKey()).setValue(
                                string);
                    }
                    assignmentForm.getField(assignment.getKey()).setRequired(
                            true);
                    assignmentForm.setImmediate(true);
                }
            } else {
                if (assignment.getValueType().equals("string")) {
                    assignmentForm.addField(assignment.getKey(), new TextField(
                            assignment.getKey()));
                } else if (assignment.getValueType().equals("integer")) {
                    assignmentForm.addField(assignment.getKey(), new TextField(
                            assignment.getKey()));
                    assignmentForm.getField(assignment.getKey()).addValidator(
                            new IntegerValidator(
                                    "Please enter an integer value!"));
                } else if (assignment.getValueType().equals("date")) {
                    assignmentForm.addField(assignment.getKey(), new DateField(
                            assignment.getKey()));
                    assignmentForm.getField(assignment.getKey()).setValue(
                            new Date());
                } else if (assignment.getValueType().equals("float")) {
                    assignmentForm.addField(assignment.getKey(), new TextField(
                            assignment.getKey()));
                    assignmentForm.getField(assignment.getKey()).addValidator(
                            new FloatValidator(
                                    "Please enter a floating point value!"));
                }
                assignmentForm.getField(assignment.getKey()).setRequired(true);
                assignmentForm.setImmediate(true);
            }
        }
    }

    /**
     * Returns the label from the dwdl to show in the start configuration window
     * 
     * @param key
     *            TODO document
     * @return label - The label of the specified component, or null, if it can't be found.
     */
    private String getLabel(String key) {
        for (Variable variable : workflow.getVariables().getVariable()) {
            if (variable.equals(key)) {
                return variable.getLabel();
            }
        }
        return null;
    }

    /**
     * Fills the combo box with the usernames currently contained in the tenant,
     * so that the user can choose from the usernames and add them as actors to
     * a role.
     */
    private void fillContainer() {
        HttpSession session = Main.getCurrent().getSession();
        TenantFacade tenantFacade = new TenantFacade((Role) Main.getCurrent()
                .getSession().getAttribute("role"));
        try {
            Long tenantId = (Long) session.getAttribute("tenantId");
            List<User> users = tenantFacade.getUsersOfTenant(tenantId, null);
            for (User user : users) {
                if (user.getUserProfile() != null) {
                    comboBox.addItem(user.getUserProfile().getUsername());
                }

            }
        } catch (TransactionException exception) {
            Main.getCurrent().addWindow(
                    new TransactionErrorDialogComponent(exception));
        }
    }

    public Form getAssignmentForm() {
        return assignmentForm;
    }

    /**
     * Returns the check box.<br>
     * Aleks: Why? What is it good for? Why would anyone need this method? ~rr
     * 
     * @return checkBox TODO document
     */
    public CheckBox getCheckBox() {
        return checkBox;
    }
}