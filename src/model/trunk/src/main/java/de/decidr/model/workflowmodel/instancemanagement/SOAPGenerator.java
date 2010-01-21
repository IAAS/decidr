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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
     * message is returned. <br>
     * <b>This method makes changes to the given "template", transforming it
     * into a read-to-send SOAPmessage!</b>
     * 
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

        if (log.isDebugEnabled()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            template.writeTo(out);
            log.debug("Incoming SOAP template");
            log.debug(new String(out.toByteArray(), "UTF-8"));
        }

        soapMessage = template;

        Element messageRootElement = getOperationElement();
        if (messageRootElement == null) {
            throw new NullPointerException("Message root element not found!");
        }

        if (startConfiguration.getRoles() != null) {
            if (!startConfiguration.getRoles().getRole().isEmpty()) {
                for (Role role : startConfiguration.getRoles().getRole()) {
                    addRole(messageRootElement, role);
                }
            }
        }

        if (!startConfiguration.getRoles().getActor().isEmpty()) {
            for (Actor actor : startConfiguration.getRoles().getActor()) {
                addActor(messageRootElement, actor);
            }
        }
        if (!startConfiguration.getAssignment().isEmpty()) {
            for (TAssignment assignment : startConfiguration.getAssignment()) {
                setAssignment(messageRootElement, assignment);
            }
        }
        soapMessage.saveChanges();
        return soapMessage;
    }

    private void addRole(Element messageRootElement, Role role)
            throws JAXBException {
        /*
         * Convert JAXB Element to W3C element
         */
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc;
        try {
            doc = factory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            // this is an unexpected error
            throw new RuntimeException(e);
        }

        JAXBContext context = JAXBContext
                .newInstance("de.decidr.model.soap.types");
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(new JAXBElement<Role>(new QName(
                Constants.DECIDRTYPES_NAMESPACE, "role"), Role.class, role),
                doc);

        messageRootElement.appendChild(messageRootElement.getOwnerDocument()
                .importNode(doc.getDocumentElement(), true));
    }

    private void addActor(Element messageRootElement, Actor actor)
            throws JAXBException {
        /*
         * Convert JAXB Element to W3C element
         */
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc;
        try {
            doc = factory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            // this is an unexpected error
            throw new RuntimeException(e);
        }

        JAXBContext context = JAXBContext
                .newInstance("de.decidr.model.soap.types");
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(new JAXBElement<Actor>(new QName(
                Constants.DECIDRTYPES_NAMESPACE, "actor"), Actor.class, actor),
                doc);

        messageRootElement.appendChild(messageRootElement.getOwnerDocument()
                .importNode(doc.getDocumentElement(), true));
    }

    private void setAssignment(Element messageRootElement,
            TAssignment assignment) {
        Element element = findElement(messageRootElement, assignment.getKey());
        String valuePart = "";
        if (!assignment.getValue().isEmpty()) {
            for (String string : assignment.getValue()) {
                valuePart += string + " ";
            }
            element.setTextContent(valuePart.trim());
        }
    }

    private Element getOperationElement() throws SOAPException {
        /*
         * Assume the first element is what we're looking for.
         */
        Element result = null;
        NodeList nodes = soapMessage.getSOAPBody().getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i) instanceof Element) {
                result = (Element) nodes.item(i);
                break;
            }
        }
        return result;
    }

    private Element findElement(Element messageRootElement, String elementName) {
        NodeList nodes = messageRootElement.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.getLocalName().equals(elementName)) {
                    return element;
                }
            }
        }
        log.warn("Can't find " + elementName + "elment in "
                + messageRootElement.getNodeName());
        return null;
    }
}
