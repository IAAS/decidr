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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.wsdl.Definition;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.PortType;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

/**
 * This class traverses a given WSDL and returns the resulting SOAP template,
 * i.e. a SOAP message that holds wildcard characters for tag values.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DWDL2SOAP {

    public SOAPMessage getSOAP(Definition wsdl, String portName,
            String operationName) throws UnsupportedOperationException,
            SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        PortType port = wsdl.getPortType(new QName(wsdl.getTargetNamespace(),
                portName));
        Operation operation = port.getOperation(null, operationName, null);
        Message message = operation.getInput().getMessage();
        Iterator<?> partIter = message.getParts().values().iterator();
        while (partIter.hasNext()) {
            Part messagePart = (Part) partIter.next();
            QName elementName = messagePart.getElementName();
            System.out.println("Element name" + elementName);
            QName typeName = messagePart.getElementName();
            System.out.println("Element name" + elementName);
            if (elementName != null) {
                ExtensibilityElement extElement = findExtensibilityElement(wsdl
                        .getTypes().getExtensibilityElements(), "schema");
                System.out.println(extElement);
            } else if (typeName != null) {

            }
        }
        return soapMessage;
    }

    private ExtensibilityElement findExtensibilityElement(List<?> extElements,
            String elementName) {
        Iterator<?> extElementIter = extElements.iterator();
        while (extElementIter.hasNext()) {
            ExtensibilityElement extElement = (ExtensibilityElement) extElementIter
                    .next();
            if (extElement.getElementType().getLocalPart().equals(elementName)) {
                return extElement;
            }
        }
        return null;
    }

}
