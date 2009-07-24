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

/**
 * Listener interface which provides a callback method which can be called by
 * a model class registered to the implementing class to indicate that any model
 * data has changed.
 *
 * @author Johannes Engelhardt
 */
public interface ModelChangeListener {

    /**
     * Callback method for the registered model class. Called by the model if
     * any model data has changed.
     */
    public void onModelChange();
    
}
