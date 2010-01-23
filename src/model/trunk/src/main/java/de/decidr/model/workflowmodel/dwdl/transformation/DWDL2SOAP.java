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

package de.decidr.model.workflowmodel.dwdl.transformation;

import java.util.ArrayList;
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
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.input.DOMBuilder;

import com.ibm.wsdl.extensions.schema.SchemaImpl;

import de.decidr.model.logging.DefaultLogger;

/**
 * This class transforms a given WSDL and returns the resulting SOAP template,
 * i.e. a SOAP message that holds wildcard characters for tag values. Please
 * note that the construction of the soap message is highly correlated to the
 * construction of a wsdl done in {@link DWDL2WSDL}
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DWDL2SOAP {

    private static Logger log = DefaultLogger.getLogger(DWDL2SOAP.class);

    private Definition definition = null;

    private SOAPMessage buildMessage(List<Element> soapElements,
            Element jdomBodyElement) throws UnsupportedOperationException,
            SOAPException {

        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage message = messageFactory.createMessage();

        SOAPPart soapPart = message.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        SOAPHeader header = envelope.getHeader();
        SOAPBody body = envelope.getBody();

        header.addAttribute(envelope.createName("bodyElementName"),
                jdomBodyElement.getAttributeValue("name"));
        header.addAttribute(envelope.createName("targetNamespace"), definition
                .getTargetNamespace());

        SOAPElement bodyElement = body.addChildElement(envelope.createName(
                jdomBodyElement.getAttributeValue("name"), "tns", definition
                        .getTargetNamespace()));

        for (Element soapElement : soapElements) {
            if (!soapElement.getAttributeValue("name").equals("role")) {
                bodyElement.addChildElement(
                        soapElement.getAttributeValue("name"), "tns")
                        .addTextNode("?");
            }
        }

        message.saveChanges();

        return message;
    }

    private Element findElement(String elementName, Element parentElement) {
        Iterator<?> iterator = parentElement.getChildren("element",
                Constants.XML_SCHEMA_NS).iterator();
        while (iterator.hasNext()) {
            Element element = (Element) iterator.next();
            if (element.getAttribute("name").getValue().equals(elementName)) {
                return element;
            }
        }
        log.warn("Element " + elementName + " nicht gefunden in "
                + parentElement.getName());
        return null;
    }

    private Element findElement(String elementName, String typeName,
            Element parentElement) {
        Iterator<?> iterator = parentElement.getChildren(elementName,
                Constants.XML_SCHEMA_NS).iterator();
        while (iterator.hasNext()) {
            Element element = (Element) iterator.next();
            if (element.getAttribute("name").getValue().equals(typeName)) {
                return element;
            }
        }
        log.warn("Element " + elementName + " nicht gefunden in "
                + parentElement.getName());
        return null;
    }

    private org.jdom.Element getSchemaElement(Definition definition) {
        final String schema = "schema";
        org.w3c.dom.Element schemaElement = null;
        Iterator<?> extElementIter = definition.getTypes()
                .getExtensibilityElements().iterator();
        while (extElementIter.hasNext()) {
            ExtensibilityElement extElement = (ExtensibilityElement) extElementIter
                    .next();
            if (extElement.getElementType().getLocalPart().equals(schema)) {
                if (extElement instanceof SchemaImpl) {
                    schemaElement = ((SchemaImpl) extElement).getElement();
                }
                return new DOMBuilder().build(schemaElement);
            }
        }
        log
                .warn("Schema element in null in "
                        + definition.getTargetNamespace());
        return null;
    }

    public SOAPMessage getSOAP(Definition wsdl, String portName,
            String operationName) throws UnsupportedOperationException,
            SOAPException {
        definition = wsdl;
        SOAPMessage soapMessage = null;

        // Find the message element for the corresponding port name
        PortType port = wsdl.getPortType(new QName(wsdl.getTargetNamespace(),
                portName));
        Operation operation = port.getOperation(operationName, null, null);
        Message message = operation.getInput().getMessage();

        // Create the list of elements that build the soap message
        List<Element> soapElements = new ArrayList<Element>();

        // Iterate over all parts of the message element
        Iterator<?> partIter = message.getParts().values().iterator();
        while (partIter.hasNext()) {
            Part messagePart = (Part) partIter.next();

            // Get the value of the element attribute
            QName elementName = messagePart.getElementName();
            if (elementName != null) {

                // Find the element in the schema declaration in types
                Element element = findElement(elementName.getLocalPart(),
                        getSchemaElement(definition));

                // Find the corresponding complexType
                Element typeElement = findElement("complexType", element
                        .getAttribute("type").getValue().toString(),
                        getSchemaElement(definition));

                // Iterate over all children of the complexType
                Iterator<?> iter = typeElement.getChildren().iterator();

                while (iter.hasNext()) {
                    Element child = (Element) iter.next();

                    // Only if the complex type contains a sequence or
                    // all element go ahead
                    if (isSequence(child) || isAll(child)) {

                        // Iterate over all children of the sequence or
                        // all element
                        Iterator<?> lastIter = child.getChildren().iterator();
                        while (lastIter.hasNext()) {
                            Element sequenceOrAllChild = (Element) lastIter
                                    .next();

                            // Only add elements, ignore attributes
                            if (sequenceOrAllChild.getName().equals("element")) {
                                soapElements.add(sequenceOrAllChild);
                            }
                        }
                    }
                }
                soapMessage = buildMessage(soapElements, element);

            }

        }

        return soapMessage;
    }

    private boolean isAll(Element complexChild) {
        return complexChild.getName().equals("all");
    }

    private boolean isSequence(Element complexChild) {
        return complexChild.getName().equals("sequence");
    }

}
