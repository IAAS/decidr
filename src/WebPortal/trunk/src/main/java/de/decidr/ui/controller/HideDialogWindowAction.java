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

package de.decidr.ui.controller;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.ui.view.Main;

/**
 * This action hides a sub window (i.e. ChangeEmailComponent)
 * 
 * @author Geoffrey-Alexeij Heinze
 * @reviewed ~tk, ~dh
 */
public class HideDialogWindowAction implements ClickListener {

    /**
     * GH: what..!? ~tk,dh
     * Overrides default buttonClick(ClickEvent event) of ClickListener to
     * implement desired functionality
     */
    @Override
    public void buttonClick(ClickEvent event) {
        Main.getCurrent().getMainWindow().removeWindow(
                event.getButton().getWindow());
    }
}
