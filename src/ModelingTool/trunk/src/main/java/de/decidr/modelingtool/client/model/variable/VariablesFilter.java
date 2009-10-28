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

package de.decidr.modelingtool.client.model.variable;

import com.extjs.gxt.ui.client.store.ListStore;

import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.ui.Workflow;

/**
 * This class provides some methods to filter the variables of a
 * {@link WorkflowModel}. The variables are returned in a special list format:
 * {@link ListStore}. The class is used for the model of the gxt widgets.
 * 
 * @author Jonas Schlaak
 */
public class VariablesFilter {

    // JS make variables filter obsolete

    /**
     * Return a {@link ListStore} of all variables of the {@link WorkflowModel}.
     * The returned variables are copies, not references.
     * 
     * @return the variables of the workflow model
     */
    public static ListStore<Variable> getAllVariablesAsStore() {
        ListStore<Variable> result = new ListStore<Variable>();
        for (Variable var : Workflow.getInstance().getModel().getVariables()) {
            result.add(var.copy());
        }
        return result;
    }

    /**
     * Returns all variables of the given type. The returned variables are
     * copies, not references.
     * 
     * @param type
     *            the type of variables to be returned
     * @return the variables that have the given type
     */
    public static ListStore<Variable> getVariablesOfTypeAsStore(
            VariableType type) {
        ListStore<Variable> result = new ListStore<Variable>();
        for (Variable var : Workflow.getInstance().getModel().getVariables()) {
            if (var.getType() == type) {
                result.add(var.copy());
            }
        }
        return result;
    }

}
