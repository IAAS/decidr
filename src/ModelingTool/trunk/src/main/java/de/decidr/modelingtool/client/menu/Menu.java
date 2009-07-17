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

import com.google.gwt.user.client.ui.MenuBar;

/**
 * TODO: add comment
 * 
 * @author JE
 */
public class Menu extends MenuBar {

    public Menu() {
        // argument false = non vertical
        super(false);

        this.addStyleName("menu-std");

        // MenuBar createMenu = new MenuBar(true);
        // createMenu.addItem("Email", new CreateEmailInvokeNodeMenuItem());
        // createMenu.addItem("Human Task",
        // new CreateHumanTaskInvokeNodeMenuItem());

        addItem("Undo", new UndoMenuItem());
        addItem("Redo", new RedoMenuItem());
        addItem("Create Email", new CreateEmailInvokeNodeMenuItem());
        addItem("Create HumanTask", new CreateHumanTaskInvokeNodeMenuItem());
        addItem("Delete", new DeleteMenuItem());
        addItem("Edit Variables", new EditVariablesMenuItem());

    }

}
