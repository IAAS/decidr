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
package de.decidr.model.webservices;

import java.io.InputStream;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.decidr.model.logging.DefaultLogger;

/**
 * This handler validates inbound messages against the WSDL to ensure valid
 * input.<br>
 * <em>This class is heavily inspired by an example from Mark T. Hansen's
 * Book &quot;SOA: Using Java Web Services&quot;</em>
 * 
 * @author Reinhold
 */
public class ValidatingHandler implements SOAPHandler<SOAPMessageContext> {
    private static final Logger log = DefaultLogger
            .getLogger(ValidatingHandler.class);

    @Override
    public void close(MessageContext context) {
        // nothing to be closed
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    /**
     * Gets the namespaces of this element and the parent elements.
     * 
     * @param element
     *            The <code>{@link Element}</code> to extract the namespaces
     *            from.
     * @return The namespaces
     */
    private NamedNodeMap getNamespaces(Element element) {
        log.trace("Entering " + ValidatingHandler.class.getSimpleName()
                + ".getNamespaces(Element)");
        NamedNodeMap attributes = element.getAttributes();
        if (attributes == null) {
            log.debug("Not an element node - ignoring");
            return null;
        }

        log.debug("attempting to instantiate a new NamedNodeMap...");
        NamedNodeMap result;
        try {
            result = attributes.getClass().newInstance();
        } catch (InstantiationException e) {
            result = null;
        } catch (IllegalAccessException e) {
            result = null;
        }
        if (result == null) {
            log.debug("... failed - cloning & emptying");
            result = element.cloneNode(true).getAttributes();
            while (result.getLength() > 0) {
                result.removeNamedItem(result.item(0).getNodeName());
            }
        } else {
            log.debug("... success!");
        }

        log.debug("getting all XML namespaces for this node");
        for (int i = 0; i < attributes.getLength(); i++) {
            Attr attribute = (Attr) attributes.item(i);
            if ((attribute.getPrefix() != null)
                    && attribute.getPrefix().equals("xmlns")) {
                result.setNamedItem(attributes.item(i));
            }
        }

        log.debug("checking whether there is a parent element");
        Node parent = element.getParentNode();
        if ((parent != null) && (parent.getNodeType() == Node.ELEMENT_NODE)) {
            log.debug("getting parent element's namespaces");
            NamedNodeMap parentAttrs = getNamespaces((Element) parent);
            log.debug("merging parent element namespaces "
                    + "into already found namespaces");
            for (int i = 0; i < parentAttrs.getLength(); i++) {
                result.setNamedItem(parentAttrs.item(i).cloneNode(true));
            }
        }
        log.trace("Leaving " + ValidatingHandler.class.getSimpleName()
                + ".getNamespaces(Element)");
        return result;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        log.trace("Entering " + ValidatingHandler.class.getSimpleName()
                + ".handleMessage(SOAPMessageContext)");
        // only handle inbound messages
        if ((Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)) {
            log.debug("ignoring outbound message");
            return true;
        }

        log.debug("retrieving SOAP body");
        SOAPBody body;
        try {
            body = context.getMessage().getSOAPBody();
        } catch (SOAPException e) {
            log.error("No SOAP-Body => invalid message");
            log.trace("Leaving " + ValidatingHandler.class.getSimpleName()
                    + ".handleMessage(SOAPMessageContext)");
            throw new IllegalArgumentException(
                    "The SOAP body could not be retrieved!");
        }

        log.debug("get toplevel message element");
        // SOAP body may only have one child to be WS-I Basic Profile compatible
        SOAPElement messageElement = (SOAPElement) body.getFirstChild();
        boolean valid;
        try {
            valid = validate(context, messageElement);
        } catch (TransformerException e) {
            valid = false;
        } catch (SAXException e) {
            valid = false;
        }

        if (!valid) {
            log.error("invalid message received");
            log.trace("Leaving " + ValidatingHandler.class.getSimpleName()
                    + ".handleMessage(SOAPMessageContext)");
            throw new IllegalArgumentException(
                    "The received message is invalid!");
        }

        log.trace("Leaving " + ValidatingHandler.class.getSimpleName()
                + ".handleMessage(SOAPMessageContext)");
        return valid;
    }

    /**
     * Validates a <code>{@link SOAPElement}</code> against a WSDL. Assumes that
     * the element is valid in case of doubt.
     * 
     * @param mContext
     *            The context of the Message, hopefully containing a reference
     *            to a WSDL.
     * @param requestElement
     *            The element to validate
     * @return -<code>true</code>, if the message is valid<br>
     *         -<code>false</code>, if it isn't
     * @throws TransformerException
     *             means that the message is invalid
     * @throws SAXException
     *             means that the message is invalid
     */
    private boolean validate(MessageContext mContext, SOAPElement requestElement)
            throws TransformerException, SAXException {
        log.trace("Entering " + ValidatingHandler.class.getSimpleName()
                + ".validate(MessageContext, SOAPElement)");
        ServletContext sContext = (ServletContext) mContext
                .get(MessageContext.SERVLET_CONTEXT);
        if (sContext == null) {
            log.debug("couldn't attempt validation => assuming valid");
            log.trace("Leaving " + ValidatingHandler.class.getSimpleName()
                    + ".validate(MessageContext, SOAPElement)");
            return true;
        }

        log.debug("getting WSDL location");
        Set<Object> wsdlSet = sContext.getResourcePaths("/META-INF/");
        String wsdlPath = null;
        // gets the location of the first WSDL found - valid, since we only
        // provide one WSDL
        for (Object o : wsdlSet) {
            wsdlPath = (String) o;
            if (wsdlPath.endsWith(".wsdl")) {
                break;
            }
        }
        if (wsdlPath == null) {
            log.debug("couldn't find WSDL => assuming valid");
            log.trace("Leaving " + ValidatingHandler.class.getSimpleName()
                    + ".validate(MessageContext, SOAPElement)");
            return true;
        }

        log.debug("opening WSDL");
        InputStream wsdlStream = sContext.getResourceAsStream(wsdlPath);
        if (wsdlStream == null) {
            log.debug("couldn't open WSDL => assuming valid");
            log.trace("Leaving " + ValidatingHandler.class.getSimpleName()
                    + ".validate(MessageContext, SOAPElement)");
            return true;
        }

        log.debug("creating Document for WSDL");
        DocumentBuilder docBuilder;
        try {
            docBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.debug("couldn't attempt validation => assuming valid");
            log.trace("Leaving " + ValidatingHandler.class.getSimpleName()
                    + ".validate(MessageContext, SOAPElement)");
            return true;
        }
        Document wsdlDocument = docBuilder.newDocument();

        log.debug("fill the WSDL Document instance with WSDL's content");
        Transformer xformer;
        try {
            xformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            log.debug("couldn't attempt validation => assuming valid");
            log.trace("Leaving " + ValidatingHandler.class.getSimpleName()
                    + ".validate(MessageContext, SOAPElement)");
            return true;
        } catch (TransformerFactoryConfigurationError e) {
            log.debug("couldn't attempt validation => assuming valid");
            log.trace("Leaving " + ValidatingHandler.class.getSimpleName()
                    + ".validate(MessageContext, SOAPElement)");
            return true;
        }
        // copy contents of stream to Document
        xformer.transform(new StreamSource(wsdlStream), new DOMResult(
                wsdlDocument));

        log.debug("extract schemas used in WSDL");
        NodeList schemaNodes = wsdlDocument.getElementsByTagNameNS(
                XMLConstants.W3C_XML_SCHEMA_NS_URI, "schema");
        int numOfSchemas = schemaNodes.getLength();
        Source[] schemas = new Source[numOfSchemas];
        Document schemaDoc;
        Element schemaElement;
        for (int i = 0; i < numOfSchemas; i++) {
            schemaDoc = docBuilder.newDocument();
            NamedNodeMap namespaces;
            namespaces = getNamespaces((Element) schemaNodes.item(i));
            schemaElement = (Element) schemaDoc.importNode(schemaNodes.item(i),
                    true);
            for (int j = 0; j < namespaces.getLength(); j++) {
                Attr attribute = (Attr) schemaDoc.importNode(
                        namespaces.item(j), true);
                schemaElement.setAttributeNodeNS(attribute);
            }
            schemaDoc.appendChild(schemaElement);
            schemas[i] = new DOMSource(schemaDoc);
        }

        log.debug("create a schema summarising all found schemas");
        Schema schema = SchemaFactory.newInstance(
                XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemas);

        log.debug("copy the child element of the SOAP Body"
                + " to a separate Document instance.");
        Document payload = docBuilder.newDocument();
        xformer
                .transform(new DOMSource(requestElement),
                        new DOMResult(payload));

        log.debug("get validator from schema and attempt validation");
        Validator validator = schema.newValidator();
        try {
            validator.validate(new DOMSource(payload));
        } catch (Exception e) {
            log.debug("Error during validation");
            log.trace("Leaving " + ValidatingHandler.class.getSimpleName()
                    + ".validate(MessageContext, SOAPElement)");
            return false;
        }

        log.debug("message appears to be valid");
        log.trace("Leaving " + ValidatingHandler.class.getSimpleName()
                + ".validate(MessageContext, SOAPElement)");
        return true;
    }
}
