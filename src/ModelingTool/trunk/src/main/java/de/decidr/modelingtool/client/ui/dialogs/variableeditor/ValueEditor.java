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
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

import de.decidr.modelingtool.client.ModelingTool;
import de.decidr.modelingtool.client.model.Variable;
import de.decidr.modelingtool.client.model.VariableType;
import de.decidr.modelingtool.client.ui.dialogs.Dialog;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;

/**
 * TODO: add comment
 * 
 * @author JS
 */
public class ValueEditor extends Dialog {

    private ContentPanel contentPanel;
    SimpleComboBox<String> typeComboBox;

    private Variable variable = new Variable();

    /**
     * TODO: add comment
     * 
     */
    public ValueEditor() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(400, 200);
        this.setResizable(true);
        createContentPanel();
        createButtons();
    }

    /**
     * 
     * TODO: add comment
     * 
     */
    private void createContentPanel() {
        /* Check, whether a content panel was previously created */
        if (this.getItem(0) != null) {
            this.remove(contentPanel);
        }

        contentPanel = new ContentPanel();
        contentPanel.setHeading(ModelingTool.messages.editVar());
        contentPanel.setLayout(new FitLayout());

        typeComboBox = new SimpleComboBox<String>();
        for (VariableType t : VariableType.values()) {
            typeComboBox.add(t.getLocalName());
        }
        typeComboBox.setEditable(false);
        contentPanel.add(typeComboBox);

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
                        okButtonAction();
                        DialogRegistry.getInstance().hideDialog(
                                ValueEditor.class.getName());
                    }

                }));
        addButton(new Button(ModelingTool.messages.cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().hideDialog(
                                ValueEditor.class.getName());
                    }
                }));
    }

    private void okButtonAction() {
        variable.setType(VariableType.getTypeFromLocalName(typeComboBox
                .getValue().getValue()));
        DialogRegistry.getInstance().getDialog(VariableEditor.class.getName())
                .refresh();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.Dialog#initialize()
     */
    @Override
    public void initialize() {
        variable = ((VariableEditor) DialogRegistry.getInstance().getDialog(
                VariableEditor.class.getName())).getSelectedVariable();
        typeComboBox.setSimpleValue(variable.getType().getLocalName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.Dialog#reset()
     */
    @Override
    public void reset() {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.ui.dialogs.Dialog#refresh()
     */
    @Override
    public void refresh() {
        // TODO Auto-generated method stub
    }
}
