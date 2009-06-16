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

package de.decidr.model.workflowmodel.instancemanagement;

/**
 * This class generates a SOAP message, using the SOAP template of a workflow 
 * model and a start configuration, to fill the template with data.
 *
 * @author Modood Alvi
 * @version 0.1
 */
public class SOAPGenerator {
    
    /**
     * The function expects a SOAP template and a start configuration. Using
     * this input, the function generates a complete SOAP message which can
     * be used to start a new instance of a deployed workflow model with the
     * information contained in the starting configuration. The generated SOAP
     * message is returned.
     *
     * @param template
     * @param bystartConfig 
     * @return The generated SOAP message
     */
    public String getSOAP(String template,byte[] bystartConfig){
        
        javax.xml.soap.MessageFactory mf = null;
        
        return null;
    }

}
