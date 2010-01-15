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

import java.io.IOException;
import java.util.Iterator;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;

import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.soap.types.Actor;
import de.decidr.model.soap.types.Role;
import de.decidr.model.workflowmodel.dwdl.transformation.Constants;
import de.decidr.model.workflowmodel.wsc.TAssignment;
import de.decidr.model.workflowmodel.wsc.TConfiguration;

/**
 * This class generates a SOAP message, using the SOAP template of a workflow
 * model and a start configuration, to fill the template with data.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class SOAPGenerator {

    private static Logger log = DefaultLogger.getLogger(SOAPGenerator.class);

    private SOAPMessage soapMessage = null;

    /**
     * The function expects a SOAP template and a start configuration. Using
     * this input, the function generates a complete SOAP message which can be
     * used to start a new instance of a deployed workflow model with the
     * information contained in the starting configuration. The generated SOAP
     * message is returned.
     * 
     * @param template
     *            TODO document
     * @param startConfiguration
     *            TODO document
     * @return {@link SOAPMessage} The generated SOAP message
     * @throws SOAPException
     *             TODO document
     * @throws IOException
     *             TODO document
     * @throws JAXBException
     *             TODO document
     */
    public SOAPMessage getSOAP(SOAPMessage template,
            TConfiguration startConfiguration) throws SOAPException,
            IOException, JAXBException {

        soapMessage = template;

        SOAPElement operationElement = getOperationElement();

        if (startConfiguration.getRoles() != null) {
            if (!startConfiguration.getRoles().getRole().isEmpty()) {
                SOAPElement roleElement = null;
                for (Role role : startConfiguration.getRoles().getRole()) {
                    roleElement = addRole(operationElement, role);
                    if (!role.getActor().isEmpty()) {
                        for (Actor actor : role.getActor()) {
                            addActor(roleElement, actor);
                        }
                    }
                }
            }
        }

        if (!startConfiguration.getRoles().getActor().isEmpty()) {
            for (Actor actor : startConfiguration.getRoles().getActor()) {
                addActor(operationElement, actor);
            }
        }
        if (!startConfiguration.getAssignment().isEmpty()) {
            for (TAssignment assignment : startConfiguration.getAssignment()) {
                setAssignment(operationElement, assignment);
            }
        }
        soapMessage.saveChanges();
        return soapMessage;
    }

    private SOAPElement addRole(SOAPElement soapElement, Role role)
            throws SOAPException {
        SOAPElement element = soapElement.addChildElement(new QName(
                Constants.DECIDRTYPES_NAMESPACE, "role", "decidr"));
        element.addAttribute(new QName("name"), role.getName());
        return element;
    }

    private void addActor(SOAPElement soapElement, Actor actor)
            throws SOAPException {
        SOAPElement element = soapElement.addChildElement(new QName(
                Constants.DECIDRTYPES_NAMESPACE, "actor", "decidr"));
        element.addAttribute(new QName(Constants.DECIDRTYPES_NAMESPACE, "email", "decidr"),
                actor.getEmail() == null ? "" : actor.getEmail());
        element.addAttribute(new QName(Constants.DECIDRTYPES_NAMESPACE, "name", "decidr"),
                actor.getName() == null ? "" : actor.getName());
        element.addAttribute(new QName(Constants.DECIDRTYPES_NAMESPACE, "userId", "decidr"), ""+actor.getUserid());
    }

    private void setAssignment(SOAPElement bodyElement, TAssignment assignment) {
        SOAPElement element = findElement(bodyElement, assignment.getKey());
        String valuePart = "";
        if (!assignment.getValue().isEmpty()) {
            for (String string : assignment.getValue()) {
                valuePart += string + " ";
            }
            element.setValue(valuePart.trim());
        }
    }

    private SOAPElement getOperationElement() throws SOAPException {
        Iterator<?> iterator = soapMessage.getSOAPBody().getChildElements();
        while (iterator.hasNext()) {
            SOAPElement element = (SOAPElement) iterator.next();
            if (soapMessage.getSOAPHeader().getAttribute("bodyElementName")
                    .equals(element.getElementName().getLocalName())) {
                return element;
            }
        }
        log.warn("Can't find operation "
                + soapMessage.getSOAPHeader().getAttribute("bodyElementName")
                + " element in SOAP message");
        return null;
    }

    private SOAPElement findElement(SOAPElement parent, String elementName) {
        Iterator<?> iterator = parent.getChildElements();
        while (iterator.hasNext()) {
            SOAPElement element = (SOAPElement) iterator.next();
            if (element.getElementName().getLocalName().equals(elementName)) {
                return element;
            }
        }
        log.warn("Can't find " + elementName + "elment in "
                + parent.getNodeName());
        return null;
    }
}
