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

import java.io.IOException;
import java.util.List;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.ServerLoadView;

/**
 * An interface for the deployment component
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public interface IDeployer {

    /**
     * This class provides an interface for other components to access the
     * functionality to deploy a workflow instance.
     * 
     * @param dwfm
     *            Deployed workflow model of the workflow model which is to be
     *            deployed
     * @param serverStatistics
     *            A server load view, which lists all available servers and
     *            details of these servers, like server load, running
     *            instances, deployed models
     * @return The function returns a list of Long values, the IDs of the
     *         servers, on which the workflow model has been deployed on
     * @throws IOException 
     * @throws DWDLValidationException
     * @throws ODESelectorException
     */
    public List<Long> deploy(DeployedWorkflowModel dwfm,
            List<ServerLoadView> serverStatistics) throws DWDLValidationException, ODESelectorException, IOException;

}
