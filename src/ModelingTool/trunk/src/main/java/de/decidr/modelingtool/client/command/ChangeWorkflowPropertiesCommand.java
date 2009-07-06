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

import de.decidr.modelingtool.client.model.WorkflowProperties;
import de.decidr.modelingtool.client.ui.Workflow;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class ChangeWorkflowPropertiesCommand implements UndoableCommand {

    private WorkflowProperties oldProperties;
    private WorkflowProperties newProperties;

    public ChangeWorkflowPropertiesCommand(WorkflowProperties newProperties) {
        this.oldProperties = Workflow.getInstance().getModel().getProperties();
        this.newProperties = newProperties;
    }

    @Override
    public void execute() {
        Workflow.getInstance().getModel().setProperties(newProperties);
    }

    @Override
    public void undo() {
        Workflow.getInstance().getModel().setProperties(oldProperties);
    }
}
