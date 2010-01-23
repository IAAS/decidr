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

import de.decidr.modelingtool.client.exception.NoPropertyWindowException;
import de.decidr.modelingtool.client.model.Model;

/**
 * Classes which implement this interface are selectable and can be selected
 * through the selection handler.
 * 
 * @author Johannes Engelhardt
 */
public interface Selectable {

    /**
     * Returns the model of the implementing element.
     * 
     * @return The model of the element.
     */
    public Model getModel();

    /**
     * Returns the selected state.
     * 
     * @return True, if selected.
     */
    public boolean isSelected();

    /**
     * Sets the selected state.
     * 
     * @param selected
     *            The selected state.
     */
    public void setSelected(boolean selected);

    public void showPropertyWindow() throws NoPropertyWindowException;

}
