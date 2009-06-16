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

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;

import de.decidr.modelingtool.client.ModelingTool;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class FormDesignPanel extends ContentPanel {

    private ScrollPanel formScrollPanel;
    private FlexTable formTable;

    public FormDesignPanel() {
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


    }

}
