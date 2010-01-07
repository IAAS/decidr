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

package de.decidr.modelingtool.client.menu;

import com.google.gwt.user.client.Command;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.io.WorkflowParser;
import de.decidr.modelingtool.client.io.WorkflowParserImpl;
import de.decidr.modelingtool.client.ui.Workflow;

/**
 * This command invokes the {@link WorkflowParser} which created a dwdl xml file
 * from the workflow. The xml file is sent to the server via the vaadin objects.
 * 
 * @author Johannes Engelhardt, Jonas Schlaak
 */
public class SaveMenuItem implements Command {

    private ModelingToolWidget modelingToolWidget;

    /**
     * Default constructor for the save menu item.
     * 
     * @param modelingToolWidget
     *            {@link ModelingToolWidget} which provides a
     *            "sent dwdl to server" method.
     */
    public SaveMenuItem(ModelingToolWidget modelingToolWidget) {
        super();
        this.modelingToolWidget = modelingToolWidget;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

        /* Invoke parser and send resulting dwdl document to server */
        WorkflowParser parser = new WorkflowParserImpl();
        // JS make this nicer -> create class as collection of constants
        String doc = parser.parse(Workflow.getInstance().getModel());
        modelingToolWidget.sendDWDLtoServer(header + doc);
    }

}
