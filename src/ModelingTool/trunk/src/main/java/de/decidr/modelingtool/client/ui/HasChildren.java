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
 * TODO: add comment
 *
 * @author Johannes Engelhardt
 */
public interface HasChildren {

    public void addNode(Node node);
    
    public void addNode(Node node, int left, int top);
    
    public void removeNode(Node node);
    
    public Collection<Node> getNodes();
    
    public void addConnection(Connection connection);
    
    public void removeConnection(Connection connection);
    
    public Collection<Connection> getConnections();
    
    public HasChildModels getHasChildModelsModel() throws InvalidTypeException;
    
    public int getLeft();
    
    public int getTop();
    
    // widget operations
    //public void add(Widget w);
    
    //public void setWidgetPosition(Widget w, int left, int top);
    
}
