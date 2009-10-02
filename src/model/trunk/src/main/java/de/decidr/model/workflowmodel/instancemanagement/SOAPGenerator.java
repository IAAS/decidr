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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import org.apache.axiom.soap.SOAPFactory;

import de.decidr.model.workflowmodel.dwdl.translator.TransformUtil;
import de.decidr.model.workflowmodel.wsc.TActor;
import de.decidr.model.workflowmodel.wsc.TAssignment;
import de.decidr.model.workflowmodel.wsc.TConfiguration;
import de.decidr.model.workflowmodel.wsc.TRole;

/**
 * This class generates a SOAP message, using the SOAP template of a workflow
 * model and a start configuration, to fill the template with data.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class SOAPGenerator {

    private String contentType = "Content-Type";
    private String contentValue = "text/xml; charset=UTF-8";

    /**
     * The function expects a SOAP template and a start configuration. Using
     * this input, the function generates a complete SOAP message which can be
     * used to start a new instance of a deployed workflow model with the
     * information contained in the starting configuration. The generated SOAP
     * message is returned.
     * 
     * @param template
     * @param startConfig
     * @return {@link SOAPMessage} The generated SOAP message
     * @throws SOAPException
     * @throws IOException
     * @throws JAXBException
     */
    public SOAPMessage getSOAP(byte[] template, byte[] startConfig)
            throws SOAPException, IOException, JAXBException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader(contentType, contentValue);
        SOAPMessage soapTemplate = messageFactory.createMessage(headers,
                new ByteArrayInputStream(template));
        
        TConfiguration startConfiguration = TransformUtil
                .bytes2Configuration(startConfig);
        javax.xml.soap.SOAPBody messageBody = soapTemplate.getSOAPBody();
        if (startConfiguration.getRoles()!=null){
            if (!startConfiguration.getRoles().getRole().isEmpty()) {
                for (TRole role : startConfiguration.getRoles().getRole()) {
                    
                }
            } 
            if (!startConfiguration.getRoles().getActor().isEmpty()){
                for (TActor actor : startConfiguration.getRoles().getActor()){
                    
                }
            }
        }
        if (!startConfiguration.getAssignment().isEmpty()){
            for (TAssignment assignment : startConfiguration.getAssignment()){
                
            }
        }
                

        return soapTemplate;
    }

}
