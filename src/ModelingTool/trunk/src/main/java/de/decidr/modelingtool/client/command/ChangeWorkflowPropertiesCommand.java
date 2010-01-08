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

import de.decidr.modelingtool.client.model.workflow.WorkflowModel;
import de.decidr.modelingtool.client.model.workflow.WorkflowProperties;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.dialogs.workflow.WorkflowPropertyWindow;

/**
 * This command changes the properties of a {@link WorkflowModel}. The
 * properties are changed by the {@link WorkflowPropertyWindow}. The command
 * gets the workflow model by accessing the {@link Workflow} singleton instance.
 * 
 * @author Jonas Schlaak
 */
public class ChangeWorkflowPropertiesCommand implements UndoableCommand {

    private WorkflowProperties oldProperties;
    private WorkflowProperties newProperties;

    /**
     * Default constructor for the command.
     * 
     * @param newProperties
     *            the new workflow model properties stored in
     *            {@link WorkflowProperties}
     */
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
