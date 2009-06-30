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

package de.decidr.modelingtool.client.ui.dialogs;

import com.extjs.gxt.ui.client.widget.Window;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
abstract public class Dialog extends Window {
    
    /**
     * TODO: add comment
     * 
     */
    public Dialog() {
        super();
    }

    /**
     * TODO: add comment will be called, when window is closed
     */
    abstract public void reset();

    /**
     * TODO: add comment will be called, when window is displayed
     */
    abstract public void initialize();

    /**
     * 
     * TODO: add comment provide a method which updates the view when a model
     * change occured
     */
    abstract public void refresh();

}
