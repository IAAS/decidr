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

package de.decidr.modelingtool.client.model.connections;

import de.decidr.modelingtool.client.model.AbstractModel;
import de.decidr.modelingtool.client.model.container.HasChildModels;
import de.decidr.modelingtool.client.model.nodes.NodeModel;

/**
 * This is the basic model of all connections in the workflow. It contains
 * pointers to its connection nodes and the parent model.
 * 
 * @author Johannes Engelhardt
 */
public class ConnectionModel extends AbstractModel {

    /** The parent model of the connection. */
    private HasChildModels parentModel = null;

    /** The source node of the connection. */
    private NodeModel source = null;

    /** The target node of the connection. */
    private NodeModel target = null;

    public ConnectionModel() {
        this.name = this.getClass().getName();
    }

    public HasChildModels getParentModel() {
        return parentModel;
    }

    public NodeModel getSource() {
        return source;
    }

    public NodeModel getTarget() {
        return target;
    }

    public void setParentModel(HasChildModels parentModel) {
        this.parentModel = parentModel;
    }

    public void setSource(NodeModel source) {
        this.source = source;
    }

    public void setTarget(NodeModel target) {
        this.target = target;
    }
}
