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

package de.decidr.ui.view;

import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;

/**
 * This class builds a button panel which can be added to a component.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2355", currentReviewState = State.Passed)
public class ButtonPanel extends Panel {

    private static final long serialVersionUID = 1L;

    private List<Button> buttonList;

    private HorizontalLayout horizontalLayout;

    /**
     * Constructor which saves the given button list and calls the init method.
     * 
     * @param buttonList
     *            - A list with buttons.
     */
    public ButtonPanel(List<Button> buttonList) {
        this.buttonList = buttonList;
        init();
    }

    /**
     * Adds buttons to the panel.
     */
    private void init() {
        horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        for (Button button : buttonList) {
            horizontalLayout.addComponent(button);
        }

        this.setContent(horizontalLayout);
    }
}