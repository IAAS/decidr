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

package de.decidr.modelingtool.client.ui.dialogs.variableeditor;

import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;

import de.decidr.modelingtool.client.model.variable.Variable;

/**
 * Listens for changes made to the variables in the {@link VariableEditorWindow}
 * .
 * 
 * @author Jonas Schlaak
 */
public class VariableEditorStoreListener extends StoreListener<Variable> {

    private boolean dataChanged = false;

    /**
     * Returns whether the variables have changed or not.
     * 
     * @return the dataChanged
     */
    public boolean hasDataChanged() {
        return dataChanged;
    }

    /**
     * Reset the listener to a value.
     * 
     * @param dataChanged
     *            the dataChanged to set
     */
    public void setDataChanged(boolean dataChanged) {
        this.dataChanged = dataChanged;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.extjs.gxt.ui.client.store.StoreListener#storeAdd(com.extjs.gxt.ui
     * .client.store.StoreEvent)
     */
    @Override
    public void storeAdd(StoreEvent<Variable> se) {
        super.storeAdd(se);
        dataChanged = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.extjs.gxt.ui.client.store.StoreListener #
     * storeDataChanged(com.extjs.gxt.ui.client. store.StoreEvent)
     */
    @Override
    public void storeDataChanged(StoreEvent<Variable> se) {
        super.storeBeforeDataChanged(se);
        dataChanged = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.extjs.gxt.ui.client.store.StoreListener#storeRemove(com.extjs.gxt
     * .ui.client.store.StoreEvent)
     */
    @Override
    public void storeRemove(StoreEvent<Variable> se) {
        super.storeRemove(se);
        dataChanged = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.extjs.gxt.ui.client.store.StoreListener#storeUpdate(com.extjs.gxt
     * .ui.client.store.StoreEvent)
     */
    @Override
    public void storeUpdate(StoreEvent<Variable> se) {
        super.storeUpdate(se);
        dataChanged = true;
    }

}