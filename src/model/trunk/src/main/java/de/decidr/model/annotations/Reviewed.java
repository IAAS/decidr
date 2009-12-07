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

package de.decidr.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation contains the reviewer of this type, method, constructor or
 * annotation.
 * 
 * @author Reinhold
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = { ElementType.TYPE, ElementType.METHOD,
        ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE })
public @interface Reviewed {

    /**
     * The reviewers of this part, the most recent reviewer last.
     */
    String[] reviewers();

    /**
     * The SVN revision reviewed by the last reviewer.
     */
    String lastRevision();

    /**
     * Indicates the current review {@link State}. Should be changed every
     * review or set to {@link State#NeedsReview} whenever a big change is made.
     */
    State currentReviewState();

    public enum State {
        Passed, Rejected, PassedWithComments, NeedsReview
    }
}
