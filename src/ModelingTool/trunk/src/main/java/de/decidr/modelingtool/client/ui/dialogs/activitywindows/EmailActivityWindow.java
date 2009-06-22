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

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.decidr.modelingtool.client.ModelingTool;
import de.decidr.modelingtool.client.model.Variable;
import de.decidr.modelingtool.client.model.VariableType;
import de.decidr.modelingtool.client.model.VariablesFilter;
import de.decidr.modelingtool.client.ui.dialogs.Dialog;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.SelectionBoxListener;

/**
 * TODO: add comment
 * 
 * @author JS
 */
public class EmailActivityWindow extends Dialog {

    private ContentPanel contentPanel;
    private ScrollPanel scrollPanel;
    private FlexTable table;

    private ComboBox<Variable> toField;
    private ComboBox<Variable> ccField;
    private ComboBox<Variable> bccField;
    private ComboBox<Variable> subjectField;
    private ComboBox<Variable> messageField;
    private ComboBox<Variable> attachmentField;

    public EmailActivityWindow() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(400, 200);
        this.setResizable(true);
        createcontentPanel();
        createButtons();
    }

    private void createcontentPanel() {
        contentPanel = new ContentPanel();

        contentPanel.setHeading(ModelingTool.messages.emailActivity());
        contentPanel.setLayout(new FitLayout());

        // TODO: fix layout
        table = new FlexTable();
        table.setBorderWidth(0);
        table.setWidth("100%");
        table.setCellPadding(2);
        table.setCellSpacing(2);
        scrollPanel = new ScrollPanel(table);
        contentPanel.add(scrollPanel);

        this.add(contentPanel);
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

    private void getEmailNodeModel() {
        // TODO: write method
    }

    private void putEmailNodeModel() {
        // TODO: write method
    }

    private void addComboField(ComboBox<Variable> field, String label,
            VariableType type) {
        field = new ComboBox<Variable>();
        field.setDisplayField(Variable.NAME);
        field.setStore(VariablesFilter.getVariablesOfType(type));
        field.setTypeAhead(true);
        field.setWidth("200px");
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(label));
        table.setWidget(table.getRowCount() - 1, 1, field);
        // TODO: externalize String

        table.setWidget(table.getRowCount() - 1, 2, new Button("Change Value",
                new SelectionBoxListener(field)));
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
        getEmailNodeModel();
        addComboField(toField, ModelingTool.messages.toFieldLabel(),
                VariableType.STRING);
        addComboField(ccField, ModelingTool.messages.ccFieldLabel(),
                VariableType.STRING);
        addComboField(bccField, ModelingTool.messages.bccFieldLabel(),
                VariableType.STRING);
        addComboField(subjectField, ModelingTool.messages.subjectFieldLabel(),
                VariableType.STRING);
        addComboField(messageField, ModelingTool.messages.messageFieldLabel(),
                VariableType.STRING);
        addComboField(attachmentField, ModelingTool.messages
                .attachmentFieldLabel(), VariableType.FILE);
    }

    @Override
    public void reset() {
        putEmailNodeModel();
        clearAllEntries();
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }
}
