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

package de.decidr.modelingtool.client.io;

import de.decidr.modelingtool.client.model.workflow.WorkflowModel;

/**
 * The WorkflowParser is responsible for converting a WorkflowModel to a DWDL
 * 
 * @author Modood Alvi
 */
public interface WorkflowParser {

    /**
     * Converts a workflow model and its variables and child nodes into a DWDL
     * document.
     * 
     * @param model
     *            The model to be converted into an XML string
     * @return the model as XML string
     */
    public String parse(WorkflowModel model);

}
