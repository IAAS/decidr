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

import de.decidr.modelingtool.client.ui.InputPort;
import de.decidr.modelingtool.client.ui.OutputPort;
import de.decidr.modelingtool.client.ui.StartNode;

/**
 * This class is the model of a {@link StartNode}. The node has no model data
 * (aka {@link NodePropertyData}), only an {@link InputPort} and no
 * {@link OutputPort}.
 * 
 * @author Johannes Engelhardt
 */
public class StartNodeModel extends NodeModel {

    /**
     * Default constructor. No properties are set by default.
     * 
     * @param parentModel
     *            the model of the parent node
     */
    public StartNodeModel(HasChildModels parentModel) {
        super(parentModel);
        this.name = this.getClass().getName();
    }

    @Override
    public ConnectionModel getInput() {
        return null;
    }

}
