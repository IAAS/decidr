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

import de.decidr.modelingtool.client.model.variable.Variable;
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
     * Return a list of all variables of this workflow model.
     * 
     * @return the variables
     */
    public List<Variable> getVariables() {
        return variables;
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
