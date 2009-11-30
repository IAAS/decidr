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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.MenuBar;

import de.decidr.modelingtool.client.ModelingToolWidget;
import de.decidr.modelingtool.client.ui.resources.Messages;

/**
 * Menu for the modeling tool where all commands for modeling a workflow can be
 * selected.
 * 
 * @author Johannes Engelhardt
 */
public class Menu extends MenuBar {

    public Menu(ModelingToolWidget modelingToolWidget) {
        // argument false = non vertical
        super(false);

        this.addStyleName("menu-std");

        MenuImageBundle imgBundle = GWT.create(MenuImageBundle.class);

        addItem(imgBundle.clear().getHTML(), true, new ClearWorkflowMenuItem());
        addItem(imgBundle.save().getHTML(), true, new SaveMenuItem(modelingToolWidget));
        addItem(imgBundle.undo().getHTML(), true, new UndoMenuItem());
        addItem(imgBundle.redo().getHTML(), true, new RedoMenuItem());
        addItem(imgBundle.delete().getHTML(), true, new DeleteMenuItem());

        addSeparator();

        addItem(imgBundle.email().getHTML(), true,
                new CreateEmailInvokeNodeMenuItem());
        addItem(imgBundle.humantask().getHTML(), true,
                new CreateHumanTaskInvokeNodeMenuItem());

        addSeparator();

        addItem(imgBundle.flowcontainer().getHTML(), true,
                new CreateFlowContainerMenuItem());
        addItem(imgBundle.ifcontainer().getHTML(), true,
                new CreateIfContainerMenuItem());
        addItem(imgBundle.foreachcontainer().getHTML(), true,
                new CreateForEachContainerMenuItem());

        addSeparator();

        /*
         * Message class needs to be created here, accessing the
         * ModelingToolWidget creates null pointer exceptions
         */
        Messages msg = GWT.create(Messages.class);
        addItem(msg.variablesMenuItem(), new VariablesMenuItem());
        addItem(msg.propertiesMenuItem(), new PropertiesMenuItem());
    }

}
