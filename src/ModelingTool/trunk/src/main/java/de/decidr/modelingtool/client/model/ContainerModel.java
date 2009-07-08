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
 * @author JE
 */
public class ContainerModel extends NodeModel implements HasChildModels {

    /**
     * The height of the assigned changelistener
     */
    private int changeListenerWidth = 0;
    
    /**
     * The width of the assigned changelistener
     */
    private int changeListenerHeight = 0;
    
    public int getChangeListenerWidth() {
        return changeListenerWidth;
    }

    public void setChangeListenerWidth(int changeListenerWidth) {
        this.changeListenerWidth = changeListenerWidth;
    }

    public int getChangeListenerHeight() {
        return changeListenerHeight;
    }

    public void setChangeListenerHeight(int changeListenerHeight) {
        this.changeListenerHeight = changeListenerHeight;
    }

    @Override
    public Collection<Model> getChildModels() {
        return childModels;
    }

    @Override
    public HasChildren getHasChildrenChangeListener() {
        if (changeListener instanceof HasChildren) {
            return (HasChildren)changeListener;
        } else {
            return null;
        }
    }

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

    private Collection<Model> childModels = new HashSet<Model>();

    private Collection<ConnectionModel> startConnections = new HashSet<ConnectionModel>();

    private Collection<ConnectionModel> endConnections = new HashSet<ConnectionModel>();

    public void addModel(Model model) {
        childModels.add(model);
    }

    public void removeModel(Model model) {
        childModels.remove(model);
    }

    public void addStartConnection(ConnectionModel model) {
        startConnections.add(model);
    }

    public void removeStartConnection(ConnectionModel model) {
        startConnections.remove(model);
    }

    public void addEndConnection(ConnectionModel model) {
        endConnections.add(model);
    }

    public void removeEndConnection(ConnectionModel model) {
        endConnections.remove(model);
    }

}
