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

package de.decidr.model.exceptions;

/**
 * Thrown to indicate that a workflow model cannot be started since no deployed
 * version is available or the workflow model has been set to not executable.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class WorkflowModelNotStartableException extends TransactionException {

    private static final long serialVersionUID = 1L;

    public WorkflowModelNotStartableException(Long workflowModelId) {
        super(String.format("The workflow model %1$d cannot be started",
                workflowModelId));
    }
}
