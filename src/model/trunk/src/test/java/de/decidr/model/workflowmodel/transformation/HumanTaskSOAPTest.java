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

package de.decidr.model.workflowmodel.transformation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.factory.WSDLFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;

import com.ibm.wsdl.extensions.schema.SchemaImpl;

/**
 * Test soap message creation for HumanTask removeTask
 * 
 * @author modood, aleks
 */
public class HumanTaskSOAPTest {

    private static Namespace ns = Namespace.getNamespace("xsd",
            "http://www.w3.org/2001/XMLSchema");

    public static void main(String[] args)
            throws UnsupportedOperationException, SOAPException, IOException,
            WSDLException, URISyntaxException {

        String wsdlName = "/HumanTaskTest.wsdl";
        WSDLFactory wsdlFactory = WSDLFactory.newInstance();
        javax.wsdl.xml.WSDLReader wsdlReader = wsdlFactory.newWSDLReader();

        wsdlReader.setFeature("javax.wsdl.verbose", true);
        wsdlReader.setFeature("javax.wsdl.importDocuments", true);

        System.out.println("getting wsdl definition...");
        Definition definition = wsdlReader.readWSDL(WSDLTypesExtraction.class
                .getResource(wsdlName).toURI().toString());
        System.out.println("definition got. parsing.");
        if (definition == null) {
            System.err.println("definition element is null");
            // MA WTF is that supposed to do??? The proper way of doing this is
            // throwing some Exception (even if this is just a test - it's good
            // practice) ~rr
            // System.exit(1);
            throw new IllegalStateException("didn't manage to get the WSDL");
        }

        org.w3c.dom.Element schemaElement = null;

        ExtensibilityElement schemaExtElem = findExtensibilityElement(
                definition.getTypes().getExtensibilityElements(), "schema");

        if ((schemaExtElem != null) && (schemaExtElem instanceof SchemaImpl)) {
            schemaElement = ((SchemaImpl) schemaExtElem).getElement();
        }
        DOMBuilder domBuilder = new DOMBuilder();
        org.jdom.Element jdomSchemaElement = domBuilder.build(schemaElement);

        List<Element> soapElements = new ArrayList<Element>();

        Element removeTask = findElement("removeTasks", jdomSchemaElement);

        Iterator<?> iter = removeTask.getChildren().iterator();

        while (iter.hasNext()) {
            Element child = (Element) iter.next();
            if (isComplexType(child)) {
                // System.out.println(child);
                Iterator<?> iter1 = child.getChildren().iterator();
                while (iter1.hasNext()) {
                    Element complexChild = (Element) iter1.next();
                    if (isSequence(complexChild)) {
                        // System.out.println(complexChild);
                        Iterator<?> iter2 = complexChild.getChildren()
                                .iterator();
                        while (iter2.hasNext()) {
                            Element sequenceChild = (Element) iter2.next();
                            if (sequenceChild.getName().equals("element")) {
                                soapElements.add(sequenceChild);
                            }

                        }
                    }
                }
            }
        }

        XMLOutputter out = new XMLOutputter();
        // out.output(removeTask.getChildren(), System.out);

        SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory
                .newInstance();
        SOAPConnection connection = soapConnFactory.createConnection();

        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage message = messageFactory.createMessage();

        SOAPPart soapPart = message.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        SOAPBody body = envelope.getBody();

        SOAPElement bodyElement = body.addChildElement(envelope.createName(
                removeTask.getAttributeValue("name"), "hum", definition
                        .getTargetNamespace()));

        for (Element element : soapElements) {
            bodyElement.addChildElement(element.getAttributeValue("name"),
                    "hum").addTextNode("?");
        }

        String destination = "http://localhost:18181/InsuranceProcessService/InsuranceProcessPort";

        message.saveChanges();
        message.writeTo(System.out);
        // SOAPMessage reply = connection.call(message, destination);

        connection.close();

    }

    private static boolean isSequence(Element complexChild) {
        return complexChild.getName().equals("sequence");
    }

    private static boolean isComplexType(Element child) {
        return child.getName().equals("complexType");
    }

    private static ExtensibilityElement findExtensibilityElement(
            List<?> extensibilityElements, String name) {
        Iterator<?> itt = extensibilityElements.iterator();
        while (itt.hasNext()) {
            ExtensibilityElement ext = (ExtensibilityElement) itt.next();
            if (ext.getElementType().getLocalPart().equals(name)) {
                return ext;
            }
        }
        throw new RuntimeException("Ext element with name " + name
                + " was not found.");
    }

    private static Element findElement(String name, Element root) {

        Iterator<?> iterator = root.getChildren("element", ns).iterator();
        while (iterator.hasNext()) {
            Element element = (Element) iterator.next();
            if (element.getAttribute("name").getValue().equals(name)) {
                return element;
            }
        }
        throw new RuntimeException("Element nicht gefunden");
    }

}
