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

/**
 * TODO: add comment
 * 
 * @author Johannes Engelhardt
 */
public class ForEachContainerModel extends ContainerModel {

    // JS rename
    private final String iterationVariableId = "iteration";
    private final String exitCondition = "exit";
    private final String parallel = "parallel";

    public ForEachContainerModel() {
        super();
    }

    /**
     * TODO: add comment
     * 
     * @param parentModel
     */
    public ForEachContainerModel(HasChildModels parentModel) {
        super(parentModel);
    }

    public Long getIterationVariableId() {
        return properties.get(iterationVariableId);
    }

    public ExitCondition getExitCondition() {
        return properties.get(exitCondition);
    }

    public Boolean isParallel() {
        return properties.get(parallel);
    }

    public void setIterationVariableId(Long iterationVariableId) {
        this.properties.set(this.iterationVariableId, iterationVariableId);
    }

    public void setExitCondition(ExitCondition exitCondition) {
        this.properties.set(this.exitCondition, exitCondition);
    }

    public void setParallel(Boolean parallel) {
        this.properties.set(this.parallel, parallel);
    }
}
