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

/**
 * TODO: add comment
 * 
 * @author Johannes Engelhardt
 */
public class Menu extends MenuBar {

    public Menu() {
        // argument false = non vertical
        super(false);

        this.addStyleName("menu-std");

        MenuImageBundle imgBundle = GWT.create(MenuImageBundle.class);

        addItem(imgBundle.undo().getHTML(), true, new UndoMenuItem());
        addItem(imgBundle.redo().getHTML(), true, new RedoMenuItem());
        addItem(imgBundle.save().getHTML(), true, new SaveMenuItem());
        addItem(imgBundle.delete().getHTML(), true, new DeleteMenuItem());

        addSeparator();

        addItem(imgBundle.email().getHTML(), true,
                new CreateEmailInvokeNodeMenuItem());
        addItem(imgBundle.humantask().getHTML(), true,
                new CreateHumanTaskInvokeNodeMenuItem());

        // JS externalize or replace with Icons
        addItem("Variables", new VariablesMenuItem());
        addItem("Properties", new PropertiesMenuItem());

    }

}
