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

package de.decidr.modelingtool.client.command;

import com.google.gwt.user.client.Command;

import de.decidr.model.entities.WorkflowModel;
import de.decidr.modelingtool.client.model.NodeModel;

/**
 * TODO: add comment
 *
 * @author JE
 */
public class CreateWorkflowCommand implements Command {

    WorkflowModel model;
    
    CommandList cmdList = new CommandList();
    
    public CreateWorkflowCommand(WorkflowModel model) {
        this.model = model;
    }
    
    @Override
    public void execute() {
        // TODO Auto-generated method stub

    }
    
//    private UndoableCommand createNodeCommand(NodeModel nodeModel) {
//        CommandList nodeModelCmdList = new CommandList();
//        //nodeModelCmdList.addCommand(new CreateInvokeNodeCommand(nodeModel));
//    }

}
