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

package de.decidr.model.workflowmodel.dwdl.translator;

import javax.wsdl.Definition;
import de.decidr.model.workflowmodel.bpel.TProcess;
import de.decidr.model.workflowmodel.dd.TDeployment;

/**
 * This class provides the functionality to translate a given DWDL into 
 * different formats
 *
 * @author Modood Alvi
 * @version 0.1
 */
public class Translator {
    
    
    public void load(String dwdl){
        
    }
    
    public void laod(byte[] dwdl){
        
    }
    
    public TProcess getBPEL(){
        return null;
    }
    
    public Definition getWSDL(String loaction, String tenantName){
        return null;
    }
    
    public TDeployment getDD(){
        return null;
    }

    public javax.xml.soap.SOAPMessage getSOAP(){
        return null;
    }
    
}
