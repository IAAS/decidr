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

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;

import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.logging.DefaultLogger;

/**
 * This class invokes a web service with a given SOAP message.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class SOAPExecution {
    
    private static Logger log = DefaultLogger.getLogger(SOAPExecution.class);

    /**
     * The function expects the address of a Web service and a matching SOAP
     * message. If the function is called, the specified Web service is invoked
     * using the provided SOAP message.
     * 
     * @param server
     * @param soapMessage
     * @throws SOAPException
     */
    public SOAPMessage invoke(ServerLoadView server, SOAPMessage soapMessage)
            throws SOAPException {
        SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory
                .newInstance();
        SOAPConnection connection = soapConnFactory.createConnection();
        SOAPMessage reply = connection.call(soapMessage, server.getLocation());
        connection.close();
        return reply;
    }

}
