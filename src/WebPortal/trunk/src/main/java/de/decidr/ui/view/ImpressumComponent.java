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
 * The legal information is stored in this component.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2160", currentReviewState = State.PassedWithComments)
public class ImpressumComponent extends CustomComponent {

    private VerticalLayout verticalLayout = null;
    private Label labelImpressum = null;

    /**
     * TODO document
     */
    public ImpressumComponent() {
        init();
    }

    /**
     * This method initializes the components of the {@link ImpressumComponent}.
     */
    private void init() {
        verticalLayout = new VerticalLayout();
        this.setCompositionRoot(verticalLayout);

        labelImpressum = new Label(
                "<h5>Diese Seite wird betrieben von</h5>"
                        + "Universität Stuttgart<br/>"
                        + "Keplerstrasse 7<br/>"
                        + "70174 Stuttgart<br/>"
                        + "Deutschland<br/><br/>"
                        + "<h5>Telefon</h5>"
                        + "++49 (0)711/685-0<br/><br/>"
                        + "<h5>Fax</h5>"
                        + "++49 (0)711/685-82113<br/><br/>"
                        + "<h5>E-Mail</h5>"
                        + "<a href=\"mailto:decidr@decidr.com\">decidr@decidr.com</a><br/><br/>"
                        + "<h5>Internet</h5>"
                        + "<a href=\"http://www.decidr.de\">http://www.decidr.de</a><br/><br/>"
                        + "<h5>Verantwortlich</h5>"
                        + "Die Universität Stuttgart ist eine Körperschaft des Öffentlichen Rechts."
                        + " Sie wird durch den Rektor Prof. Dr.-Ing. Wolfram Ressel gesetzlich"
                        + " vertreten.</br></br>"
                        + "DecidR wurde im Rahmen des Studienprojektes A 2008/2009 an der "
                        + "Universität Stuttgart am Institut IAAS implementiert."
                        + " Die Gruppe aus neun Studenten bestehend aus: "
                        + "Modood Alvi, Johannes Engelhardt, Markus Fischer, Geoffrey Heinze, "
                        + "Daniel Huss, Thomas Karsten, Reinhold Rumberger, Jonas Schlaak und "
                        + "Aleksandar Tolev haben sich ein Jahr lang bemüht das Programm so zu "
                        + "gestalten wie es der Kunde - Ralph Mietzner - gewünscht hat.",
                Label.CONTENT_XHTML);

        verticalLayout.addComponent(labelImpressum);
    }
}