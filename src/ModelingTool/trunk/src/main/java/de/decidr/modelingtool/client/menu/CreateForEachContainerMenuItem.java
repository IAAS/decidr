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

package de.decidr.modelingtool.client.menu;

import com.google.gwt.user.client.Command;

import de.decidr.modelingtool.client.command.CommandStack;
import de.decidr.modelingtool.client.command.CreateContainerCommand;
import de.decidr.modelingtool.client.ui.Container;
import de.decidr.modelingtool.client.ui.ForEachContainer;
import de.decidr.modelingtool.client.ui.Workflow;

/**
 * TODO: add comment
 *
 * @author JE
 */
public class CreateForEachContainerMenuItem implements Command {   
    
    @Override
    public void execute() {
        Container container = new ForEachContainer (Workflow.getInstance());
        Workflow.getInstance().addNode(container);
        container.setPixelSize(200, 150);
        
        CommandStack.getInstance().executeCommand(
                new CreateContainerCommand(container));
    }

}
