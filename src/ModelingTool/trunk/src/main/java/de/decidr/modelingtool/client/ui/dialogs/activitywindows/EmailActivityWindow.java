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

package de.decidr.modelingtool.client.ui.dialogs.activitywindows;

import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.FlexTable;

import de.decidr.modelingtool.client.ModelingTool;
import de.decidr.modelingtool.client.model.Variable;
import de.decidr.modelingtool.client.model.VariableType;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.ui.dialogs.Dialog;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;

/**
 * TODO: add comment
 * 
 * @author JS
 */
public class EmailActivityWindow extends Dialog {

    private ListStore<Variable> variables = new ListStore<Variable>();

    private ContentPanel contentPanel;
    private FlexTable table;

    private ComboBox<Variable> toField;
    private ComboBox<Variable> ccField;
    private ComboBox<Variable> bccField;
    private ComboBox<Variable> subjectField;
    private ComboBox<Variable> messageField;
    private FileUploadField attachmentField;

    // TODO: private ComboBox<VariableType> sent;

    public EmailActivityWindow() {
        super();
        this.setLayout(new FitLayout());
        this.setResizable(true);
        createcontentPanel();
        createButtons();
    }

    private void createcontentPanel() {
        contentPanel = new ContentPanel();

        contentPanel.setHeading("Email Activity");
        contentPanel.setLayout(new FitLayout());

        // TODO: fix layout
        table = new FlexTable();
        table.setBorderWidth(4);
        table.setWidth("100%");
        contentPanel.add(table);

        this.add(contentPanel);
    }

    private ListStore<Variable> getStringVariables(ListStore<Variable> list) {
        ListStore<Variable> result = new ListStore<Variable>();
        for (int i = 0; i < list.getCount(); i++) {
            if (list.getAt(i).getType() == VariableType.STRING) {
                result.add(list.getAt(i));
            }
        }
        return result;
    }

    private void createButtons() {
        setButtonAlign(HorizontalAlignment.CENTER);
        addButton(new Button(ModelingTool.messages.okButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        okButtonAction();
                        DialogRegistry.getInstance().hideDialog(
                                EmailActivityWindow.class.getName());
                    }
                }));
        addButton(new Button(ModelingTool.messages.cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().hideDialog(
                                EmailActivityWindow.class.getName());
                    }
                }));
    }

    private void okButtonAction() {
        // TODO: write method
    }

    /**
     * 
     * TODO: add comment
     * 
     */
    private void getVariablesFromModel() {
        variables.removeAll();
        List<Variable> variablesModel = WorkflowModel.getInstance()
                .getVariables();
        for (Variable v : variablesModel) {
            Variable targetVar = new Variable();
            targetVar.setName(v.getName());
            targetVar.setType(v.getType());
            targetVar.setValues(v.getValues());
            targetVar.setConfig(v.isConfig());
            variables.add(targetVar);
        }
    }

    private void addToField() {
        toField = new ComboBox<Variable>();
        toField.setDisplayField(Variable.NAME);
        toField.setStore(getStringVariables(variables));
        toField.setTypeAhead(true);
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, toField);
    }

    private void addCcField() {
        ccField = new ComboBox<Variable>();
        ccField.setDisplayField(Variable.NAME);
        ccField.setStore(getStringVariables(variables));
        ccField.setTypeAhead(true);
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, ccField);
    }

    private void addBccField() {
        bccField = new ComboBox<Variable>();
        bccField.setDisplayField(Variable.NAME);
        bccField.setStore(getStringVariables(variables));
        bccField.setTypeAhead(true);
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, bccField);
    }

    private void clearAllEntries() {
        if (table.getRowCount() > 0) {
            int start = table.getRowCount();
            for (int i = start; i > 0; i--) {
                table.removeRow(i - 1);
            }
        }
    }

    @Override
    public void initialize() {
        getVariablesFromModel();
        addToField();
        addCcField();
        addBccField();
    }

    @Override
    public void reset() {
        clearAllEntries();
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }
}
