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

/**
 * This class provides the functionality to deploy the files of a workflow model
 * process on selected servers
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class FileDeployer {

    /**
     * This function expects a list of the server addresses on which the files
     * should be deployed, the name of the process which represents the name of
     * the folder in the ODE and has to be unique for each workflow model, as
     * well as the BPEL, WSDL and Deployment Descriptor as strings. If a process
     * is deployed in Apache ODE, it automatically receives a version number,
     * which is re-turned by the function deploy. If the function is called, a
     * folder with the passed name is created locally on the server. The passed
     * BPEL, WSDL and Deployment Descriptor are written to files in this folder.
     * This folder is compressed as a zip file. By invoking the Deployment Web
     * service of the Apache ODEs on the given servers the zip file is deployed
     * on all these servers. The returned ODE versions of the processes are
     * collected, after all servers are handled the highest one is returned.
     * After deploying the files on all servers, all files that have been
     * created locally are deleted.
     * 
     * 
     * @param server List of server addresses
     * @param name Name of the process
     * @param bpel BPEL representation of the process
     * @param wsdl WSDL representation of the process
     * @param dd The Deployment Descriptor
     * @return The highest ODE Version of the the processes
     */
    public long deploy(List<String> server, String name, String bpel,
            String wsdl, String dd) {
        return 1l;
    }

}
