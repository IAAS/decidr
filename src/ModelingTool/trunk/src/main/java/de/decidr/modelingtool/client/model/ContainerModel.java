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

import java.util.Collection;
import java.util.HashSet;

import de.decidr.modelingtool.client.ui.HasChildren;

/**
 * TODO: add comment
 * 
 * @author Johannes Engelhardt
 */
public class ContainerModel extends NodeModel implements HasChildModels {

    /**
     * The height of the assigned changelistener
     */
    protected int changeListenerWidth = 150;
    
    /**
     * The width of the assigned changelistener
     */
    protected int changeListenerHeight = 100;
    
    private Collection<NodeModel> childNodeModels = new HashSet<NodeModel>();
    
    private Collection<ConnectionModel> childConnectionModels = new HashSet<ConnectionModel>();

    private Collection<ConnectionModel> startConnections = new HashSet<ConnectionModel>();

    private Collection<ConnectionModel> endConnections = new HashSet<ConnectionModel>();

    public ContainerModel() {
        super();
    }

    /**
     * TODO: add comment
     * 
     * @param parentModel
     */
    public ContainerModel(HasChildModels parentModel) {
        super(parentModel);
    }

    public void addEndConnection(ConnectionModel model) {
        endConnections.add(model);
    }

    @Override
    public void addNodeModel(NodeModel model) {
        childNodeModels.add(model);
    }

    public void addStartConnection(ConnectionModel model) {
        startConnections.add(model);
    }

    public int getChangeListenerHeight() {
        return changeListenerHeight;
    }

    public int getChangeListenerWidth() {
        return changeListenerWidth;
    }

    @Override
    public Collection<NodeModel> getChildNodeModels() {
        return childNodeModels;
    }

    @Override
    public void addConnectionModel(ConnectionModel model) {
        childConnectionModels.add(model);
    }

    @Override
    public Collection<ConnectionModel> getChildConnectionModels() {
        return childConnectionModels;
    }

    @Override
    public void removeConnectionModel(ConnectionModel model) {
        childConnectionModels.remove(model);
    }

    @Override
    public void removeNodeModel(NodeModel model) {
        childNodeModels.remove(model);
    }

    @Override
    public HasChildren getHasChildrenChangeListener() {
        if (changeListener instanceof HasChildren) {
            return (HasChildren)changeListener;
        } else {
            return null;
        }
    }

    public void removeEndConnection(ConnectionModel model) {
        endConnections.remove(model);
    }

    public void removeStartConnection(ConnectionModel model) {
        startConnections.remove(model);
    }

    public void setChangeListenerSize(int width, int height) {
        this.changeListenerWidth = width;
        this.changeListenerHeight = height;
    }

}
