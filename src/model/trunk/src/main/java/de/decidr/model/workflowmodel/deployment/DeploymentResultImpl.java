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

import java.util.Date;
import java.util.List;
import javax.xml.soap.SOAPMessage;



/**
 * An interface for the deployment component
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DeploymentResultImpl implements DeploymentResult {

    private List<Long> servers;
    private SOAPMessage soapMessage;
    private Date deployDate;
    @Override
    public Date getDoplementDate() {
        return deployDate;
    }
    @Override
    public SOAPMessage getSOAPMessage() {
        return soapMessage;
    }
    @Override
    public List<Long> getServers() {
        return servers;
    }
    @Override
    public void setDoplementDate(Date deploymentDate) {
        deployDate = deploymentDate;
    }
    @Override
    public void setSOAPMessage(SOAPMessage soap) {
        soapMessage = soap;
    }
    @Override
    public void setServers(List<Long> servers) {
        this.servers = servers;
    }
    

}
