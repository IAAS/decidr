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

package de.decidr.model.workflowmodel.deployment;

import java.util.List;

import de.decidr.model.entities.ServerLoadView;

/**
 * This exception is thrown by {@link Deployer}.<br>
 * MA: and why? What does this exception indicate?
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class ODESelectorException extends Exception {

    public ODESelectorException(@SuppressWarnings("unused") List<ServerLoadView> serverStatistics) {
        // for further development
    }

    private static final long serialVersionUID = 1L;

}
