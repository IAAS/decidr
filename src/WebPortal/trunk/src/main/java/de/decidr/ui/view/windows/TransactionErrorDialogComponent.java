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

package de.decidr.ui.view.windows;

/**
 * This component displays an error message. Should be used if an Action failed.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class TransactionErrorDialogComponent extends InformationDialogComponent {

    private static final long serialVersionUID = 1L;

    /**
     * Calls the super constructor with the given text and shows a notification
     * to the user.
     */
    public TransactionErrorDialogComponent(Exception e) {
        super(
                "An error occured while performing your request. Please try again."
                        + "<br/><br/>If this error occurs repeatedly please inform the system administrator."
                        + "<br/><br/>Error description:<br/>"
                        + e.getClass().getSimpleName() + ": " + e.getMessage(),
                "Transaction Error");
    }
}
