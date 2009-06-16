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
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

import de.decidr.modelingtool.client.ModelingTool;
import de.decidr.modelingtool.client.ui.dialogs.Dialog;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.activitywindows.formdesigner.FormDesignPanel;
import de.decidr.modelingtool.client.ui.dialogs.activitywindows.formdesigner.TaskPanel;

/**
 * TODO: add comment
 * 
 * @author JS
 */
public class HumanTaskActivityWindow extends Dialog {

    private ContentPanel contentPanel;
    private ContentPanel taskPanel;
    private ContentPanel formPanel;

    public HumanTaskActivityWindow() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(400, 200);
        this.setResizable(true);
        createContentPanel();
        createButtons();
    }

    private void createContentPanel() {
        contentPanel = new ContentPanel();

        contentPanel.setHeading(ModelingTool.messages.humanTaskActivity());
        contentPanel.setLayout(new AccordionLayout());
        contentPanel.setBodyBorder(false);

        taskPanel = new TaskPanel();
        contentPanel.add(taskPanel);
        formPanel = new FormDesignPanel();
        contentPanel.add(formPanel);

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
                                HumanTaskActivityWindow.class.getName());
                    }
                }));
        addButton(new Button(ModelingTool.messages.cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().hideDialog(
                                HumanTaskActivityWindow.class.getName());
                    }
                }));
    }

    private void okButtonAction() {
        // TODO: write method
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub

    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }

}
