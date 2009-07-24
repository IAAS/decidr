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

/**
 * TODO: add comment
 * 
 * @author Johannes Engelhardt
 */
public class IfContainerModel extends ContainerModel {

    public IfContainerModel() {
        super();
    }

    /**
     * TODO: add comment
     * 
     * @param parentModel
     */
    public IfContainerModel(HasChildModels parentModel) {
        super(parentModel);
    }

    @Override
    public NodePropertyData getProperties() {
        NodePropertyData result = new NodePropertyData();
        for (ConnectionModel con : getChildConnectionModels()) {
            if (con instanceof Condition) {
                // JS this is not finished
                result.set(((Condition) con).getName(), con);
            }
        }
        return result;
    }

    @Override
    public void setProperties(NodePropertyData properties) {
        /* remove old objects */
        for (ConnectionModel con : getChildConnectionModels()) {
            if (con instanceof Condition) {
                // JS coverthink complete method
                getChildConnectionModels().remove(con);
            }
        }
        for (Object ob : properties.getValues()) {
            getChildConnectionModels().add((Condition) ob);
        }
    }

    private void setConditions(List<Condition> conditions) {
        /*
         * delete the old conditions before the new ones are added.
         */
        for (ConnectionModel con : getChildConnectionModels()) {
            if (con instanceof Condition) {
                // JS check if this is safe to do
                getChildConnectionModels().remove(con);
            }
        }
        /* add the new conditions */
        for (Condition con : conditions) {
            getChildConnectionModels().add(con);
        }
    }

    public void addCondition(Condition con) {
        getChildConnectionModels().add(con);
    }

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
