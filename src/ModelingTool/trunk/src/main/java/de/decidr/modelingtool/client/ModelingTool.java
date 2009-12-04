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

package de.decidr.modelingtool.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>. Only needed for test
 * purposes. This enables the Modeling Tool to run as stand-alone widget.
 */
public class ModelingTool implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        // Some test user data
        String users = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><userlist><user id=\"23\" name=\"Clark Kent\"/><user id=\"42\" name=\"Bruce Wayne\"/><user id=\"113\" name=\"Peter Parker\"/></userlist>";

        // A very basic workflow
        String dwdl = "<workflow name=\"Simple Workflow\" id=\"3141\" targetNamespace=\"namespace\" xmlns=\"schema\"><description></description><variables /><roles /><faultHandler><setProperty name=\"message\" /><recipient><setProperty name=\"name\" /></recipient></faultHandler><nodes><startNode name=\"de.decidr.modelingtool.client.model.StartNodeModel\" id=\"4815162342\"><description></description><graphics x=\"20\" y=\"20\" width=\"50\" height=\"30\" /></startNode><endNode name=\"de.decidr.modelingtool.client.model.EndNodeModel\" id=\"108\"><description></description><graphics x=\"200\" y=\"200\" width=\"50\" height=\"30\" /><notificationOfSuccess><setProperty name=\"successMessage\" /><setProperty name=\"recipient\" /></notificationOfSuccess></endNode></nodes><arcs /></workflow>";

        // Create widget
        ModelingToolWidget modelingToolWidget = new ModelingToolWidget();
        RootPanel.get("panel").add(modelingToolWidget);

        // Set test data
        modelingToolWidget.setUsers(users);
        modelingToolWidget.setDWDL(dwdl);
    }

}
