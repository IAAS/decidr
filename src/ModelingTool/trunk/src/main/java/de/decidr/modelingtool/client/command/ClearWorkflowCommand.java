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

import de.decidr.modelingtool.client.exception.OperationNotAllowedException;
import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.Workflow;

/**
 * This command clears the workflow, except the start and exit node.
 * 
 * @author Johannes Engelhardt
 */
public class ClearWorkflowCommand implements Command {

    @Override
    public void execute() {
        CommandList cmdList = new CommandList();

        for (Node node : Workflow.getInstance().getNodes()) {
            if (node.isDeletable()) {
                try {
                    cmdList.addCommand(new RemoveNodeCommand(node));
                } catch (OperationNotAllowedException e) {
                    // do nothing, node can not be deleted
                    // JE: debug:
                    e.printStackTrace();
                }
            }
        }

        cmdList.execute();

        // flush command stack
        CommandStack.getInstance().flush();
    }

}
