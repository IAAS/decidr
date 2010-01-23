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

package de.decidr.modelingtool.client.ui.dialogs.canvas;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog;

/**
 * A dialog for changing the size of the workflow canvas.
 * 
 * @author Jonas Schlaak
 */
public class CanvasSizeWindow extends ModelingToolDialog {

    private Workflow workflow;

    private ContentPanel contentPanel;
    private ScrollPanel scrollPanel;
    private FlexTable table;

    private Spinner heigthSpinner;
    private Spinner widthSpinner;

    /**
     * Default constructor.
     */
    public CanvasSizeWindow() {
        super();
        this.setLayout(new FitLayout());
        this.setSize(300, 200);
        this.setResizable(true);
        createContentPanel();
        createButtons();
    }

    private void changeSize() {
        int width = widthSpinner.getValue();
        int height = heigthSpinner.getValue();
        workflow.setSize(width, height);
    }

    private void createButtons() {
        setButtonAlign(HorizontalAlignment.CENTER);
        addButton(new Button(ModelingToolWidget.getMessages().okButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        changeSize();
                        DialogRegistry.getInstance().hideDialog(
                                CanvasSizeWindow.class.getName());
                    }
                }));
        addButton(new Button(ModelingToolWidget.getMessages().cancelButton(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DialogRegistry.getInstance().hideDialog(
                                CanvasSizeWindow.class.getName());
                    }
                }));
    }

    private void createContentPanel() {
        contentPanel = new ContentPanel();

        contentPanel.setHeading(ModelingToolWidget.getMessages()
                .editCanvasSize());
        contentPanel.setLayout(new FitLayout());

        table = new FlexTable();
        table.setBorderWidth(0);
        table.setWidth("100%");
        table.setCellPadding(2);
        table.setCellSpacing(2);
        scrollPanel = new ScrollPanel(table);
        contentPanel.add(scrollPanel);

        this.add(contentPanel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.modelingtool.client.ui.dialogs.ModelingToolDialog#initialize()
     */
    @Override
    public Boolean initialize() {
        widthSpinner = new Spinner(100, Workflow.MAX_SIZE, workflow.getWidth());
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.getMessages().width()));
        table.setWidget(table.getRowCount() - 1, 1, widthSpinner);

        heigthSpinner = new Spinner(100, Workflow.MAX_SIZE, workflow
                .getHeight());
        table.insertRow(table.getRowCount());
        table.setWidget(table.getRowCount() - 1, 0, new Label(
                ModelingToolWidget.getMessages().height()));
        table.setWidget(table.getRowCount() - 1, 1, heigthSpinner);

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

    public void setModel(Workflow workflow) {
        this.workflow = workflow;
    }

}
