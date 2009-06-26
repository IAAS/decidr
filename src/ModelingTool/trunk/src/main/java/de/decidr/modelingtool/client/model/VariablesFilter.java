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

package de.decidr.modelingtool.client.model;

import com.extjs.gxt.ui.client.store.ListStore;

import de.decidr.modelingtool.client.ui.Workflow;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class VariablesFilter {

    public static ListStore<Variable> getVariablesOfType(VariableType type) {
        ListStore<Variable> result = new ListStore<Variable>();
        for (Variable var : Workflow.getInstance().getModel().getVariables()) {
            if (var.getType() == type) {
                Variable targetVar = new Variable();
                targetVar.setName(var.getName());
                targetVar.setType(var.getType());
                targetVar.setValues(var.getValues());
                targetVar.setConfig(var.isConfig());
                result.add(targetVar);
            }
        }
        return result;
    }

    public static ListStore<Variable> getAllVariables() {
        ListStore<Variable> result = new ListStore<Variable>();
        for (Variable var : Workflow.getInstance().getModel().getVariables()) {
            Variable targetVar = new Variable();
            targetVar.setName(var.getName());
            targetVar.setType(var.getType());
            targetVar.setValues(var.getValues());
            targetVar.setConfig(var.isConfig());
            result.add(targetVar);
        }
        return result;
    }

    // JS: rewrite method
    public static Variable getVariableByName(String name) {
        for (Variable var : Workflow.getInstance().getModel().getVariables()) {
            if (var.getName() == name) {
                return var;
            }
        }
        return null;
    }
}
