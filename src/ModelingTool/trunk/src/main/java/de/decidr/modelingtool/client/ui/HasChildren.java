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

package de.decidr.modelingtool.client.ui;

import java.util.Collection;

import de.decidr.modelingtool.client.exception.InvalidTypeException;
import de.decidr.modelingtool.client.model.HasChildModels;

/**
 * Interface with methods needed for nodes which have children
 * (workflows and containers).
 *
 * @author Johannes Engelhardt
 */
public interface HasChildren {

    /**
     * Adds a connection.
     *
     * @param connection The connections to add.
     */
    public void addConnection(Connection connection);
    
    /**
     * Adds a node.
     *
     * @param node The node to add.
     */
    public void addNode(Node node);
    
    /**
     * Adds a node in a specified position.
     * 
     * @param node The node to add.
     * @param x Desired x coordinate of the node relative to the container
     * @param y Desired y coordinate of the node relative to the container
     */
    public void addNode(Node node, int left, int top);
    
    /**
     * Returns all connections.
     *
     * @return A collection containing all connections.
     */
    public Collection<Connection> getConnections();
    
    /**
     * Returns the model as type HasChildModels.
     *
     * @return The model as HasChildModels
     * @throws InvalidTypeException if the model isn't an instance of
     * HasChildModels
     */
    public HasChildModels getHasChildModelsModel() throws InvalidTypeException;
    
    /**
     * Returns the x coordinate of the upper left cordner.
     *
     * @return The x coordinate in pixels.
     */
    public int getLeft();
    
    /**
     * Returns all nodes.
     *
     * @return A collection containing all nodes.
     */
    public Collection<Node> getNodes();
    
    /**
     * Returns the y coordinate of the upper left cordner.
     *
     * @return The y coordinate in pixels.
     */
    public int getTop();
    
    /**
     * Removes a connection.
     *
     * @param connection The connection to remove.
     */
    public void removeConnection(Connection connection);
    
    /**
     * Removes a node.
     *
     * @param node The node to remove
     */
    public void removeNode(Node node);
    
}
