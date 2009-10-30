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

package de.decidr.modelingtool.client.ui.dialogs.variableeditor;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog;

/**
 * Whenever a new {@link Variable} in the {@link VariableEditorWindow} is added,
 * this dialog is called. The user must specify a name, a {@link VariableType}
 * for the variable and whether it is a configuration variable or not.
 * 
 * @author Jonas Schlaak
 */
public class NewVariableWindow extends ModelingToolDialog {

    private ContentPanel contentPanel;
    private FlexTable table;
    private ScrollPanel scrollPanel;

    private TextField<String> labelField;
    private SimpleComboBox<String> typeField;
    private CheckBox configField;

    private ListStore<Variable> variables;

    public NewVariableWindow() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(350, 200);
        this.setResizable(true);
        createContentPanel();
        createButtons();
    }

    private void createContentPanel() {
        contentPanel = new ContentPanel();
        contentPanel.setHeading(ModelingToolWidget.getMessages().addVariable());
        contentPanel.setLayout(new FitLayout());

        table = new FlexTable();
        table.setBorderWidth(0);
        table.setWidth("100%");
        scrollPanel = new ScrollPanel(table);
        contentPanel.add(scrollPanel);

        this.add(contentPanel);
    }

    private void createButtons() {
        setButtonAlign(HorizontalAlignment.CENTER);
        addButton(new Button(ModelingToolWidget.getMessages().okButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        addVariable();
                        DialogRegistry.getInstance().hideDialog(
                                NewVariableWindow.class.getName());
                    }

                }));
        addButton(new Button(ModelingToolWidget.getMessages().cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().hideDialog(
                                NewVariableWindow.class.getName());
                    }
                }));
    }

    private void addVariable() {
        String label = labelField.getValue();
        VariableType type = VariableType.getTypeFromLocalName(typeField
                .getSimpleValue());
        Boolean config = configField.getValue();
        Variable variable = new Variable(label, type, config);
        variables.insert(variable, variables.getCount());
    }

    public void setVariables(ListStore<Variable> variables) {
        this.variables = variables;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog#initialize()
     */
    @Override
    public Boolean initialize() {
        /* Create text field for the variable label */
        labelField = new TextField<String>();
        labelField.setAllowBlank(false);
        labelField.setWidth("200px");
        labelField.setAutoValidate(false);
        labelField.setValue(ModelingToolWidget.getMessages()
                .enterVariableName());
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.getMessages().nameLabel()));
        table.setWidget(table.getRowCount() - 1, 1, labelField);

        /* Create combobox for type selection */
        typeField = new SimpleComboBox<String>();
        for (VariableType type : VariableType.values()) {
            typeField.add(type.getLocalName());
        }
        typeField.setEditable(false);
        typeField.setSimpleValue(VariableType.STRING.getLocalName());
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.getMessages().typeLabel()));
        table.setWidget(table.getRowCount() - 1, 1, typeField);

        /* Create checkbox for configuration variable status */
        configField = new CheckBox();
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.getMessages().configLabel()));
        table.setWidget(table.getRowCount() - 1, 1, configField);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog#reset()
     */
    @Override
    public void reset() {
        if (table.getRowCount() > 0) {
            int start = table.getRowCount();
            for (int i = start; i > 0; i--) {
                table.removeRow(i - 1);
            }
        }
    }

}
