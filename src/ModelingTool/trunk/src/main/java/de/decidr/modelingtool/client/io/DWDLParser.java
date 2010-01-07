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

import de.decidr.modelingtool.client.model.WorkflowModel;

/**
 * The DWDLParser is responsible for converting a DWDL into a
 * {@link WorkflowModel}.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public interface DWDLParser {

    /**
     * Parses an xml dwdl document and created a {@link WorkflowModel} from it.
     * 
     * @param dwdl
     *            the dwdl to parse
     * @return {@link WorkflowModel}
     */
    public WorkflowModel parse(String dwdl);

}