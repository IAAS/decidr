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
 * TODO: add comment
 * 
 * @author Johannes Engelhardt
 */
public class SaveMenuItem implements Command {

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        // JS replace with real implementation
//        WorkflowIO io = new WorkflowIOStub();
//        try {
//            io.saveWorkflow(Workflow.getInstance().getModel());
//        } catch (SaveDWDLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        WorkflowParser parser = new WorkflowParserImpl();
        String dwdl = parser.parse(Workflow.getInstance().getModel());
        ModelingToolWidget.getInstance().sendDWDLtoServer(dwdl);
    }

}
