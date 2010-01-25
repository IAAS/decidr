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

import de.decidr.modelingtool.client.ui.ModelChangeListener;

/**
 * This abstract model provides basic functionality of all models; id, name,
 * description, change listener.
 * 
 * @author Johannes Engelhardt, Jonas Schlaak
 */
public abstract class AbstractModel implements Model {

    /** The change listener of this model. Null, if none is registered. */
    protected ModelChangeListener changeListener = null;

    /** The unique id of the model. */
    private Long id;

    /** The name of the model. */
    protected String name;

    /** The description of the model. */
    String description;

    /**
     * Notifies the change listener (if present) that any data in the model has
     * changed. Has to be called manually.
     */
    public void fireDataChanged() {
        if (changeListener != null) {
            changeListener.onModelChange();
        }
    }

    @Override
    public ModelChangeListener getChangeListener() {
        return changeListener;
    }

    public String getDescription() {
        if (description == null) {
            description = "";
        }
        return description;
    }

    /**
     * 
     * Gets the id of a model. If no id has been assigned to the model, a new id
     * equal to the time stamp will be assigned. However, if the time stamp is
     * smaller than the last assigned id, the assigned id to the model will be
     * lastAssignedId + 1.
     * 
     * @return the id of the model
     */
    public Long getId() {
        if (this.id == null) {
            id = IdGenerator.generateId();
        }
        return id;
    }

    public String getName() {
        if (name == null) {
            name = "";
        }
        return name;
    }

    @Override
    public void setChangeListener(ModelChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the id of the model. The uniqueness of the id will not be tested,
     * i.e. the caller of this method has to be sure that the id given to this
     * method is not assigned elsewhere.
     * 
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
        if (id > IdGenerator.generateId()) {
            IdGenerator.setHighestId(id);
        }
    }

    public void setName(String name) {
        this.name = name;
    }
}