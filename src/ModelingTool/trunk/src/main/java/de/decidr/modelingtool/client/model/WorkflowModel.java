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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.extjs.gxt.ui.client.store.ListStore;

import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;
import de.decidr.modelingtool.client.ui.HasChildren;
import de.decidr.modelingtool.client.ui.Workflow;

/**
 * The model class for {@link Workflow}. The model data is stored separately in
 * a propertied class, {@link WorkflowProperties}.
 * 
 * @author Jonas Schlaak
 */
public class WorkflowModel extends AbstractModel implements HasChildModels {

    /*
     * (non-Javadoc)
     * 
     * @seede.decidr.modelingtool.client.model.HasChildModels#
     * getHasChildrenChangeListener()
     */
    @Override
    public HasChildren getHasChildrenChangeListener() {
        if (changeListener instanceof HasChildren) {
            return (HasChildren) changeListener;
        } else {
            return null;
        }
    }

    private Collection<NodeModel> childNodeModels = new HashSet<NodeModel>();
    private Collection<ConnectionModel> childConnectionModels = new HashSet<ConnectionModel>();

    private WorkflowProperties properties;
    private List<Variable> variables;

    /**
     * Constructs a new workflow model. Initializes the variables list and the
     * properties container.
     */
    public WorkflowModel() {
        properties = new WorkflowProperties();
        variables = new ArrayList<Variable>();
    }

    @Override
    public void addConnectionModel(ConnectionModel model) {
        childConnectionModels.add(model);
    }

    @Override
    public void addNodeModel(NodeModel model) {
        childNodeModels.add(model);
    }

    @Override
    public Collection<ConnectionModel> getChildConnectionModels() {
        return childConnectionModels;
    }

    @Override
    public Collection<NodeModel> getChildNodeModels() {
        return childNodeModels;
    }

    @Override
    public void removeConnectionModel(ConnectionModel model) {
        childConnectionModels.remove(model);
    }

    @Override
    public void removeNodeModel(NodeModel model) {
        childNodeModels.remove(model);
    }

    /**
     * Returns a list of all variables of this workflow model.
     * 
     * @return the variables
     */
    public List<Variable> getVariables() {
        return variables;
    }

    /**
     * Returns a list of all variables of a certain type
     * 
     * @param type
     *            the type of variables that shall be returned
     * @return the list of variables
     */
    public List<Variable> getVariablesOfType(VariableType type) {
        List<Variable> list = new ArrayList<Variable>();
        for (Variable variable : variables) {
            if (variable.getType() == type) {
                list.add(variable);
            }
        }
        return list;
    }

    /**
     * Returns the variable that has the given id.
     * 
     * @param id
     *            the id
     * @return the variable
     */
    public Variable getVariable(Long id) {
        Variable result = null;
        for (Variable var : variables) {
            if (var.getId().equals(id)) {
                result = var;
            }
        }
        return result;
    }

    /**
     * Return a {@link ListStore} of all variables of the {@link WorkflowModel}.
     * The returned variables are copies, not references.
     * 
     * @return the variables of the workflow model
     */
    public ListStore<Variable> getAllVariablesAsStore() {
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
    public ListStore<Variable> getVariablesOfTypeAsStore(VariableType type) {
        ListStore<Variable> result = new ListStore<Variable>();
        for (Variable var : Workflow.getInstance().getModel().getVariables()) {
            if (var.getType() == type) {
                result.add(var.copy());
            }
        }
        return result;
    }

    /**
     * Returns the container for the properties of this workflow
     * 
     * @return the properties
     */
    public WorkflowProperties getProperties() {
        return properties;
    }

    /**
     * Sets the variables of this workflow
     * 
     * @param variables
     *            the variables
     */
    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    /**
     * Sets the proerties continer for this workflow
     * 
     * @param properties
     *            the properties container
     */
    public void setProperties(WorkflowProperties properties) {
        this.properties = properties;
    }

}
