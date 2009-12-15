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

package de.decidr.ui.data;

import com.vaadin.data.validator.AbstractStringValidator;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;

/**
 * TODO: add comment
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2453", currentReviewState = State.PassedWithComments)
public class FloatValidator extends AbstractStringValidator {

    /**
     * TODO: add comment
     */
    public FloatValidator(String errorMessage) {
        super(errorMessage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.data.validator.AbstractStringValidator#isValidString(java.
     * lang.String)
     */
    @Override
    protected boolean isValidString(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }
}
