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

import java.util.ArrayList;
import java.util.List;

import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.ui.Workflow;

/**
 * This command changes the variables of a {@link WorkflowModel}.
 * 
 * @author Jonas Schlaak
 */
public class ChangeVariablesCommand implements UndoableCommand {

    private List<Variable> oldVariables;
    private List<Variable> newVariables;

    /**
     * This constructor should be used when the variables are changed by the
     * variable editor.
     * 
     * @param newVariables
     *            the new variables list which shall replace the old ones
     */
    public ChangeVariablesCommand(List<Variable> newVariables) {
        this.oldVariables = Workflow.getInstance().getModel().getVariables();
        this.newVariables = newVariables;
    }

    /**
     * This constructor should be used when the values of a single variable were
     * changed by the value editor. The value editor was not called within the
     * variable editor.
     * 
     * @param newVariable
     *            the variable with the new values
     * 
     */
    public ChangeVariablesCommand(Variable newVariable) {
        /*
         * In oder to properly prepare for the execute and undo commands, which
         * only handle variables lists, it is necessary to create a "new"
         * variables list which contains the new variable. This is done by
         * copying variable by variable from the old list.If the loop gets to
         * the variable which is to be replaced, the new variable is inserted
         * instead of the old one.
         */
        this.oldVariables = Workflow.getInstance().getModel().getVariables();
        this.newVariables = new ArrayList<Variable>();
        for (Variable var : oldVariables) {
            if (var.getId() == newVariable.getId()) {
                newVariables.add(newVariable);
            } else {
                newVariables.add(var);
            }
        }
    }

    @Override
    public void execute() {
        Workflow.getInstance().getModel().setVariables(newVariables);
    }

    @Override
    public void undo() {
        Workflow.getInstance().getModel().setVariables(oldVariables);
    }

}
