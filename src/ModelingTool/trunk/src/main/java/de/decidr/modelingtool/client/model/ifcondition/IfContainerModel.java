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

package de.decidr.modelingtool.client.model.ifcondition;

import java.util.ArrayList;
import java.util.List;

import de.decidr.modelingtool.client.model.ConnectionModel;
import de.decidr.modelingtool.client.model.ContainerModel;
import de.decidr.modelingtool.client.model.HasChildModels;
import de.decidr.modelingtool.client.model.NodePropertyData;
import de.decidr.modelingtool.client.ui.IfContainer;

/**
 * This class holds all properties of a {@link IfContainer}.
 * 
 * @author Johannes Engelhardt, Jonas Schlaak
 */
public class IfContainerModel extends ContainerModel {

    /**
     * Default constructor. No properties are set by default.
     * 
     * @param parentModel
     *            the model of the parent node
     */
    public IfContainerModel(HasChildModels parentModel) {
        super(parentModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.model.NodeModel#getProperties()
     */
    @Override
    public NodePropertyData getProperties() {
        /*
         * Check every connection model in the container. Only connection models
         * which are containers are model data.
         */
        for (ConnectionModel connectionModel : getChildConnectionModels()) {
            if (connectionModel instanceof Condition) {
                Condition con = (Condition) connectionModel;
                properties.set(con.getName(), con);
            }
        }
        return properties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.modelingtool.client.model.NodeModel#setProperties(de.decidr
     * .modelingtool.client.model.NodePropertyData)
     */
    @Override
    public void setProperties(NodePropertyData properties) {
        /* if there are any, remove old objects */
        if (this.getChildConnectionModels() != null) {
            for (ConnectionModel con : this.getChildConnectionModels()) {
                if (con instanceof Condition) {
                    getChildConnectionModels().remove(con);
                }
            }
        }
        for (Object value : properties.getValues()) {
            if (value instanceof Condition) {
                getChildConnectionModels().add((Condition) value);
            }
        }
        super.setProperties(properties);
    }

    /**
     * Adds a {@link Condition} the the child connection models of this
     * container.
     * 
     * @param condition
     *            the condition to add
     */
    public void addCondition(Condition condition) {
        getChildConnectionModels().add(condition);
        this.properties.set(condition.getName(), condition);
    }

    /**
     * Returns all conditions of this model.
     * 
     * @return the conditions
     */
    public List<Condition> getConditions() {
        List<Condition> result = new ArrayList<Condition>();
        for (ConnectionModel con : getChildConnectionModels()) {
            if (con instanceof Condition) {
                result.add((Condition) con);
            }
        }
        return result;
    }

}
