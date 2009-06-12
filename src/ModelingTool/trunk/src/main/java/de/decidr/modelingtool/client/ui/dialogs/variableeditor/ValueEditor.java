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
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

import de.decidr.modelingtool.client.ModelingTool;
import de.decidr.modelingtool.client.model.Variable;
import de.decidr.modelingtool.client.ui.dialogs.Dialog;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;

/**
 * TODO: add comment
 * 
 * @author JS
 */
public class ValueEditor extends Dialog {

    private ContentPanel contentPanel;

    private Variable variable;
    private ListStore<Variable> variables;

    /**
     * TODO: add comment
     * 
     */
    public ValueEditor() {
        super();
        this.setLayout(new FitLayout());
        this.setResizable(true);
        createButtons();

    }

    public void setContent(Variable variable) {
        this.variable = variable;
        createContentPanel();
        this.variables = ((VariableEditor) DialogRegistry.getInstance()
                .getDialog(VariableEditor.class.getName())).getVariables();
    }

    private void createContentPanel() {
        /* Check, whether a content panel was previously created */
        if (this.getItem(0) != null) {
            this.remove(contentPanel);
        }

        contentPanel = new FormPanel();
        contentPanel.setHeading(ModelingTool.messages.editValue());
        switch (variable.getType()) {
        case STRING:
            TextField<String> stringField = new TextField<String>();
            stringField.setFieldLabel(variable.getType().getLocalName());
            stringField.setValue(variable.getValues().get(0));
            stringField.setAllowBlank(false);
            contentPanel.add(stringField);
            break;
        case DATE:
            DateField dateField = new DateField();
            dateField.setFieldLabel(variable.getType().getLocalName());
//            Integer datev = new Integer(variable.getValues().get(0));
//            Date date = new Date(datev);
//            dateField.setValue(date);
            contentPanel.add(dateField);
        default:
            break;
        }
        this.add(contentPanel);
    }

    /**
     * TODO: add comment
     * 
     */
    private void createButtons() {
        setButtonAlign(HorizontalAlignment.CENTER);
        addButton(new Button(ModelingTool.messages.okButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        // TODO: write method
//                        DialogRegistry.getInstance().getDialog(
//                                ValueEditor.class.getName()).setVisible(false);
//                        int temp = variables.indexOf(variable);
//                        variables.remove(variable);

//                        variable.setValue(((TextField<String>) contentPanel
//                                .getWidget(0)).getValue().toString());
//                        variables.insert(variable, temp);

                    }
                }));
        addButton(new Button(ModelingTool.messages.cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        // TODO:write method
                        DialogRegistry.getInstance().getDialog(
                                ValueEditor.class.getName()).setVisible(false);
                    }
                }));
    }
}
