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

package de.decidr.modelingtool.client.model.foreach;

import de.decidr.modelingtool.client.model.ContainerModel;
import de.decidr.modelingtool.client.model.HasChildModels;
import de.decidr.modelingtool.client.ui.ForEachContainer;

/**
 * This class holds all properties of a {@link ForEachContainer}.
 * 
 * @author Johannes Engelhardt, Jonas Schlaak
 */
public class ForEachContainerModel extends ContainerModel {

    private final String iterationVariableFieldName = "iteration";
    private final String exitConditionFieldName = "exit";
    private final String parallelFieldName = "parallel";

    /**
     * Default constructor. No properties are set by default.
     * 
     * @param parentModel
     *            the model of the parent node
     */
    public ForEachContainerModel(HasChildModels parentModel) {
        super(parentModel);
        this.name = this.getClass().getName();
    }

    public Long getIterationVariableId() {
        return properties.get(iterationVariableFieldName);
    }

    public ExitCondition getExitCondition() {
        return properties.get(exitConditionFieldName);
    }

    public Boolean isParallel() {
        return properties.get(parallelFieldName);
    }

    public void setIterationVariableId(Long iterationVariableId) {
        this.properties.set(this.iterationVariableFieldName,
                iterationVariableId);
    }

    public void setExitCondition(ExitCondition exitCondition) {
        this.properties.set(this.exitConditionFieldName, exitCondition);
    }

    public void setParallel(Boolean parallel) {
        this.properties.set(this.parallelFieldName, parallel);
    }
}
