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

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.workflowmodel.wsc.TConfiguration;

/**
 * This bean represents a role from the {@link TConfiguration} and stores only
 * the actor. This bean is used for the tree to add items to the tree container.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2465", currentReviewState = State.PassedWithComments)
public class RoleBean {

    private String actor;

    /**
     * Constructor of the bean, with given parameter actor and email.<br>
     * Aleks: wrong ;-) ~rr
     * 
     * @param actor
     *            TODO document
     */
    public RoleBean(String actor) {
        this.actor = actor;
    }

    /**
     * Gets the actor.
     * 
     * @return the actor TODO document
     */
    public String getActor() {
        return actor;
    }

    /**
     * Sets the actor.
     * 
     * @param actor
     *            the actor to set
     */
    public void setActor(String actor) {
        this.actor = actor;
    }
}
