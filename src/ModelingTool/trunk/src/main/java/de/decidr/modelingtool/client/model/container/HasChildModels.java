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

package de.decidr.modelingtool.client.model.container;

import java.util.Collection;

import de.decidr.modelingtool.client.model.connections.ConnectionModel;
import de.decidr.modelingtool.client.model.nodes.NodeModel;
import de.decidr.modelingtool.client.ui.HasChildren;

/**
 * This interface provides operations to add child models to the implementing
 * class. This interface is implemented by workflow and container models.
 * 
 * @author Johannes Engelhardt
 */
public interface HasChildModels {

    public void addConnectionModel(ConnectionModel model);

    public void addNodeModel(NodeModel model);

    public Collection<ConnectionModel> getChildConnectionModels();

    public Collection<NodeModel> getChildNodeModels();

    /**
     * Returns the change listener of the implementing class as type
     * HasChildren.
     * 
     * @return The change listener as type HasChildren. Null, if the change
     *         listener is not of type HasChildren.
     */
    public HasChildren getHasChildrenChangeListener();

    public void removeConnectionModel(ConnectionModel model);

    public void removeNodeModel(NodeModel model);

}
