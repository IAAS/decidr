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

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;

/**
 * The terms of service are represented in this component.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "TK", "JS" }, lastRevision = "2377", currentReviewState = State.Passed)
public class TermsOfServiceWindow extends CustomComponent {

    /**
     * Serial Uid
     */
    private static final long serialVersionUID = 1067396863067502885L;
    
    private VerticalLayout verticalLayout = null;
    private Label labelTerms = null;

    /**
     * Default constructor
     * 
     */
    public TermsOfServiceWindow() {
        init();
    }

    /**
     * This method initializes the components of the terms of service component
     * 
     */
    private void init() {
        verticalLayout = new VerticalLayout();
        this.setCompositionRoot(verticalLayout);

        labelTerms = new Label(
                "<h2>Terms of Service</h2><br/>This service is licensed under the Apache License, Version 2.0, January 2004.<br>The license can be found under http://www.apache.org/licenses/",
                Label.CONTENT_XHTML);

        verticalLayout.addComponent(labelTerms);
    }

}
