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

import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.DOMBuilder;

import com.ibm.wsdl.extensions.schema.SchemaImpl;

/**
 * This class traverses a given WSDL and returns the resulting SOAP template,
 * i.e. a SOAP message that holds wildcard characters for tag values.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DWDL2SOAP {

    private Definition definition = null;

    private static Namespace ns = Namespace.getNamespace("xsd",
            "http://www.w3.org/2001/XMLSchema");

    public SOAPMessage getSOAP(Definition wsdl, String portName,
            String operationName) throws UnsupportedOperationException,
            SOAPException {
        definition = wsdl;
        SOAPMessage soapMessage = null;
        PortType port = wsdl.getPortType(new QName(wsdl.getTargetNamespace(),
                portName));
        Operation operation = port.getOperation(operationName, null, null);
        Message message = operation.getInput().getMessage();

        List<Element> soapElements = new ArrayList<Element>();

        Iterator<?> partIter = message.getParts().values().iterator();
        while (partIter.hasNext()) {
            Part messagePart = (Part) partIter.next();
            QName elementName = messagePart.getElementName();
            QName typeName = messagePart.getElementName();
            if (elementName != null) {
                Element element = findElement(elementName.getLocalPart());
                Iterator<?> iter = element.getChildren().iterator();

                while (iter.hasNext()) {
                    Element child = (Element) iter.next();
                    if (isComplexType(child)) {
                        Iterator<?> iter1 = child.getChildren().iterator();
                        while (iter1.hasNext()) {
                            Element complexChild = (Element) iter1.next();
                            if (isSequence(complexChild) || isAll(complexChild)) {
                                Iterator<?> iter2 = complexChild.getChildren()
                                        .iterator();
                                while (iter2.hasNext()) {
                                    Element sequenceChild = (Element) iter2
                                            .next();
                                    if (sequenceChild.getName().equals(
                                            "element")) {
                                        soapElements.add(sequenceChild);
                                    }
                                }
                            }
                        }
                    }
                }
                soapMessage = buildMessage(soapElements, element);
                
            } else if (typeName != null) {
                // TODO document empty block
            }
        }
        return soapMessage;
    }

    /**
     * TODO: add comment
     *
     * @param complexChild
     * @return
     */
    private boolean isAll(Element complexChild) {
        return complexChild.getName().equals("all");
    }

    private SOAPMessage buildMessage(List<Element> soapElements, Element element) throws UnsupportedOperationException, SOAPException {
        
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage message = messageFactory.createMessage();
        
        SOAPPart soapPart = message.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        SOAPHeader header = envelope.getHeader();
        SOAPBody body = envelope.getBody();
        
        header.addAttribute(envelope.createName("bodyElementName"), element.getAttributeValue("name"));
        header.addAttribute(envelope.createName("targetNamespace"), definition.getTargetNamespace());
        
        SOAPElement bodyElement = body.addChildElement(envelope.createName(element.getAttributeValue("name"), "decidr",
                definition.getTargetNamespace()));

        for (Element soapElement : soapElements){
            if(!soapElement.getAttributeValue("name").equals("role")){
                bodyElement.addChildElement(soapElement.getAttributeValue("name"), "decidr").addTextNode("?");
            }
        }
        
        message.saveChanges();
        
        return message;
    }

    /**
     * TODO: add comment
     * 
     * @param complexChild
     * @return
     */
    private boolean isSequence(Element complexChild) {
        return complexChild.getName().equals("sequence");
    }

    /**
     * TODO: add comment
     * 
     * @param child
     * @return
     */
    private boolean isComplexType(Element child) {
        return child.getName().equals("complexType");
    }

    private Element findElement(String name) {
        org.w3c.dom.Element schemaElement = getSchemaElement(definition);

        DOMBuilder domBuilder = new DOMBuilder();
        org.jdom.Element jdomSchemaElement = domBuilder.build(schemaElement);

        Element messageElement = retrieveElement(name, jdomSchemaElement);
        return messageElement;
    }

    /**
     * TODO: add comment
     * 
     * @param name
     * @param jdomSchemaElement
     * @return
     */
    private Element retrieveElement(String name, Element root) {
        Iterator<?> iterator = root.getChildren("element", ns).iterator();
        while (iterator.hasNext()) {
            Element element = (Element) iterator.next();
            if (element.getAttribute("name").getValue().equals(name)) {
                return element;
            }
        }
        throw new RuntimeException("Element nicht gefunden");
    }

    private org.w3c.dom.Element getSchemaElement(Definition definition) {
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
                return schemaElement;
            }
        }
        return null;
    }

}
