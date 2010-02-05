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

import com.vaadin.terminal.ThemeResource;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;

/**
 * Represents the resources which are used in the web portal to have a central
 * point where all resources are handled.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2355", currentReviewState = State.PassedWithComments)
public class Resources {

    private static Resources resources = null;

    /**
     * Returns a singleton instance of this class.
     * 
     * @return resources TODO document
     */
    public static Resources getInstance() {
        if (resources == null) {
            resources = new Resources();
        }
        return resources;
    }

    private ThemeResource imgDecidrLogo = null;

    /**
     * Returns the DecidR logo as a theme resource.
     * 
     * @return imgDecidrLogo TODO document
     */
    public ThemeResource getDecidrLogo() {
        if (imgDecidrLogo == null) {
            imgDecidrLogo = new ThemeResource("img/decidrlogo.png");
        }
        return imgDecidrLogo;
    }
}
