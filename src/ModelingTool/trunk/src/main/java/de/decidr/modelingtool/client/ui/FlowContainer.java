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

/**
 * This node represents a flow container.
 * 
 * @author Johannes Engelhardt
 */
public class FlowContainer extends Container {

    public FlowContainer(HasChildren parentPanel) {
        super(parentPanel);

        getGraphic().addStyleName("flow-container-std");

        getContainerStartPort().setMultipleConnectionsAllowed(true);
        getContainerExitPort().setMultipleConnectionsAllowed(true);
    }

    @Override
    public void showPropertyWindow() throws NoPropertyWindowException {
        throw new NoPropertyWindowException("This node has no properties.");
    }

}
