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

package de.decidr.modelingtool.client.ui.dialogs.activitywindows.formdesigner;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.decidr.modelingtool.client.ModelingTool;
import de.decidr.modelingtool.client.model.Variable;
import de.decidr.modelingtool.client.model.VariablesFilter;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class FormDesignPanel extends ContentPanel {

    private ScrollPanel formScrollPanel;
    private FlexTable formTable;

    private List<TextField<String>> fields = new ArrayList<TextField<String>>();

    public FormDesignPanel() {
        // FIXME: panel not shown at first time
        super();

        this.setHeading(ModelingTool.messages.workItemForm());
        this.setLayout(new FitLayout());

        formTable = new FlexTable();
        formTable.setBorderWidth(0);
        formTable.setWidth("100%");
        formTable.setCellPadding(2);
        formTable.setCellSpacing(2);
        formScrollPanel = new ScrollPanel(formTable);
        this.add(formScrollPanel);

        createToolBar();

    }

    /**
     * TODO: add comment
     * 
     */
    private void createToolBar() {
        ToolBar toolBar = new ToolBar();

        TextToolItem addVar = new TextToolItem(ModelingTool.messages.addValue()); //$NON-NLS-1$
        addVar.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                addEntry(ModelingTool.messages.newStringValue());
            }
        });
        toolBar.add(addVar);

        TextToolItem delVar = new TextToolItem(ModelingTool.messages.delValue()); //$NON-NLS-1$
        delVar.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override
            public void componentSelected(ToolBarEvent ce) {
                removeEntry();
            }
        });
        toolBar.add(delVar);

        this.setBottomComponent(toolBar);
    }

    private void addEntry(String fieldContent) {
        TextField<String> text = new TextField<String>();
        text.setValue(fieldContent);
        text.setAutoWidth(true);
        fields.add(text);
        formTable.insertRow(formTable.getRowCount());
        formTable.setWidget(formTable.getRowCount() - 1, 0, new Label(
                ModelingTool.messages.workItemLabel()));
        formTable.setWidget(formTable.getRowCount() - 1, 1, text);

        ComboBox<Variable> combo = new ComboBox<Variable>();
        combo.setDisplayField(Variable.NAME);
        combo.setStore(VariablesFilter.getAllVariables());
        combo.setTypeAhead(true);
        formTable.setWidget(formTable.getRowCount() - 1, 2, new Label(
                ModelingTool.messages.workItemOutputVar()));
        formTable.setWidget(formTable.getRowCount() - 1, 3, combo);
    }

    private void removeEntry() {
        formTable.removeRow(formTable.getRowCount() - 1);
        fields.remove(formTable.getRowCount());
    }

    public void clearAllEntries() {
        if (formTable.getRowCount() > 0) {
            int start = formTable.getRowCount();
            for (int i = start; i > 0; i--) {
                formTable.removeRow(i - 1);
            }
        }
        if (fields.size() > 0) {
            fields.clear();
        }
    }

}
