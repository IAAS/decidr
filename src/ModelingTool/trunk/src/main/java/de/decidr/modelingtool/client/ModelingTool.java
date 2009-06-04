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
package main.java.de.decidr.modelingtool.client;

import main.java.de.decidr.modelingtool.client.ui.Container;
import main.java.de.decidr.modelingtool.client.ui.EmailInvokeNode;
import main.java.de.decidr.modelingtool.client.ui.EndNode;
import main.java.de.decidr.modelingtool.client.ui.Node;
import main.java.de.decidr.modelingtool.client.ui.StartNode;
import main.java.de.decidr.modelingtool.client.ui.Workflow;
import main.java.de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import main.java.de.decidr.modelingtool.client.ui.dialogs.variableeditor.VariableEditor;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ModelingTool implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
    	
		ButtonBar buttonBar = new ButtonBar();
		buttonBar.add(new Button("Test",
				new SelectionListener<ButtonEvent>() {

					@Override
					public void componentSelected(ButtonEvent ce) {
						DialogRegistry.getInstance().getDialog(VariableEditor.class.getName()).setVisible(true);
						
					}
				}));
		RootPanel.get().add(buttonBar);

        // create workflow and add to the root panel.
        final Workflow workflow = new Workflow();
        RootPanel.get("workflow").add(workflow);

        // create test elements
        Node startNode = new StartNode();
        Node endNode = new EndNode();
        Node emailInvokeNode = new EmailInvokeNode();

        Container con = new Container();

        workflow.add(startNode, 50, 50);
        workflow.add(emailInvokeNode, 50, 150);
        workflow.add(endNode, 50, 250);

        workflow.add(con, 200, 50);

    }
}
