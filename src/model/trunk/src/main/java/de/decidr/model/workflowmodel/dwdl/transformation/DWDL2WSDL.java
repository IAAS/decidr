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

import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.Text;

import com.ibm.wsdl.BindingImpl;
import com.ibm.wsdl.BindingInputImpl;
import com.ibm.wsdl.BindingOperationImpl;
import com.ibm.wsdl.BindingOutputImpl;
import com.ibm.wsdl.DefinitionImpl;
import com.ibm.wsdl.ImportImpl;
import com.ibm.wsdl.InputImpl;
import com.ibm.wsdl.MessageImpl;
import com.ibm.wsdl.OperationImpl;
import com.ibm.wsdl.OutputImpl;
import com.ibm.wsdl.PartImpl;
import com.ibm.wsdl.PortImpl;
import com.ibm.wsdl.PortTypeImpl;
import com.ibm.wsdl.ServiceImpl;
import com.ibm.wsdl.TypesImpl;
import com.ibm.wsdl.extensions.schema.SchemaImpl;
import com.ibm.wsdl.extensions.soap.SOAPAddressImpl;
import com.ibm.wsdl.extensions.soap.SOAPBindingImpl;
import com.ibm.wsdl.extensions.soap.SOAPBodyImpl;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.bpel.partnerlinktype.PartnerLinkType;
import de.decidr.model.workflowmodel.bpel.varprop.Property;
import de.decidr.model.workflowmodel.bpel.varprop.PropertyAlias;
import de.decidr.model.workflowmodel.bpel.varprop.Query;
import de.decidr.model.workflowmodel.dwdl.Actor;
import de.decidr.model.workflowmodel.dwdl.Boolean;
import de.decidr.model.workflowmodel.dwdl.Role;
import de.decidr.model.workflowmodel.dwdl.Variable;
import de.decidr.model.workflowmodel.dwdl.Workflow;

/**
 * This class converts a given DWDL object and returns the resulting WSDL.<br>
 * Following naming conventions are used: <br>
 * <li>Binding name = DWDL workflow name + "SOAPBinding"</li> <li>Message part
 * name = "payload"</li> <li>Role name in partner link type = DWDL workflow name
 * + "Provider"</li> <li>Port type = DWDL workflow name + "PT"</li> <li>Location
 * URL = Server location + "/ode/processes/" + DWDL workflow name</li>
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DWDL2WSDL {

    private static Logger log = DefaultLogger.getLogger(DWDL2WSDL.class);

    private Workflow dwdl = null;
    private Definition wsdl = null;
    private String serverLocation = null;
    private Element schemaElement = null;
    private Message startMessageRequest = null;
    private Message startMessageResponse = null;
    private PortType processPortType = null;
    private Binding processBinding = null;
    private Operation startOperation = null;

    public Definition getWSDL(Workflow dwdl, String serverLocation,
            String tenantName) throws JDOMException {
        wsdl = new DefinitionImpl();
        wsdl.setQName(new QName(dwdl.getTargetNamespace(), dwdl.getName()));
        this.serverLocation = serverLocation;
        this.dwdl = dwdl;

        log.trace("setting disclaimer");
        setDisclaimer();

        log.trace("setting namespaces");
        setNamespaces();

        log.trace("setting imports");
        setImports();

        log.trace("setting types");
        setTypes();

        log.trace("setting messages");
        setMessages();

        log.trace("setting port types");
        setPortTypes();

        log.trace("setting bindings");
        setBindings();

        log.trace("setting service elements");
        setService();

        log.trace("setting partner link types");
        setPartnerLinkTypes();

        log.trace("setting properties");
        setProperties();

        return wsdl;
    }

    private void setDisclaimer() {
        try {
            org.jdom.Element documenationElement = new Element("documentation",
                    "wsdl", Constants.WSDL_NAMESPACE);
            documenationElement.addContent(new Text(DecidrGlobals.DISCLAIMER));
            wsdl.setDocumentationElement(TransformUtil
                    .jdomToW3c(documenationElement));
        } catch (JDOMException e) {
            log.error("Can't build documentation element", e);
        }

    }

    private void setBindings() {
        processBinding = new BindingImpl();
        processBinding.setQName(new QName(wsdl.getTargetNamespace(), dwdl
                .getName()
                + "SOAPBinding"));
        processBinding.setPortType(processPortType);
        SOAPBinding soapBindingElement = new SOAPBindingImpl();
        soapBindingElement.setStyle("document");
        soapBindingElement
                .setTransportURI("http://schemas.xmlsoap.org/soap/http");
        processBinding.addExtensibilityElement(soapBindingElement);
        BindingOperation bindingOperation = new BindingOperationImpl();
        bindingOperation.setName(WSDLConstants.PROCESS_OPERATION);
        bindingOperation.setOperation(startOperation);
        BindingInput bindingInput = new BindingInputImpl();
        SOAPBody soapBodyIn = new SOAPBodyImpl();
        soapBodyIn.setUse("literal");
        bindingInput.addExtensibilityElement(soapBodyIn);
        BindingOutput bindingOutput = new BindingOutputImpl();
        SOAPBody soapBodyOut = new SOAPBodyImpl();
        soapBodyOut.setUse("literal");
        bindingOutput.addExtensibilityElement(soapBodyOut);

        bindingOperation.setBindingInput(bindingInput);
        bindingOperation.setBindingOutput(bindingOutput);
        processBinding.addBindingOperation(bindingOperation);
        processBinding.setUndefined(false);
        wsdl.addBinding(processBinding);
    }

    private void setImports() {
        Import decidrTypes = new ImportImpl();
        Import decidrProcessTypes = new ImportImpl();
        decidrTypes.setNamespaceURI(Constants.DECIDRTYPES_NAMESPACE);
        decidrTypes.setLocationURI(Constants.DECIDRTYPES_LOCATION);
        decidrProcessTypes
                .setNamespaceURI(Constants.DECIDRPROCESSTYPES_NAMESPACE);
        decidrProcessTypes
                .setLocationURI(Constants.DECIDRPROCESSTYPES_LOCATION);
        wsdl.addImport(decidrTypes);
        wsdl.addImport(decidrProcessTypes);
    }

    private void setMessages() {
        startMessageRequest = new MessageImpl();
        startMessageRequest.setQName(new QName(wsdl.getTargetNamespace(),
                WSDLConstants.PROCESS_MESSAGE_IN));
        Part messagePart1 = new PartImpl();
        messagePart1.setName("payload");
        messagePart1.setElementName(new QName(wsdl.getTargetNamespace(),
                WSDLConstants.PROCESS_MESSAGE_IN_ELEMENT, "tns"));
        startMessageRequest.addPart(messagePart1);
        startMessageRequest.setUndefined(false);

        startMessageResponse = new MessageImpl();
        startMessageResponse.setQName(new QName(wsdl.getTargetNamespace(),
                WSDLConstants.PROCESS_MESSAGE_OUT));
        Part messagePart2 = new PartImpl();
        messagePart2.setName("payload");
        messagePart2.setElementName(new QName(wsdl.getTargetNamespace(),
                WSDLConstants.PROCESS_MESSAGE_OUT_ELEMENT, "tns"));
        startMessageResponse.addPart(messagePart2);
        startMessageResponse.setUndefined(false);

        wsdl.addMessage(startMessageRequest);
        wsdl.addMessage(startMessageResponse);
    }

    private void setNamespaces() {
        wsdl.setTargetNamespace(dwdl.getTargetNamespace());
        wsdl.addNamespace("http", "http://schemas.xmlsoap.org/wsdl/http/");
        wsdl.addNamespace("mime", "http://schemas.xmlsoap.org/wsdl/mime/");
        wsdl.addNamespace("plnk", Constants.PARTNERLINKTYPE_NAMESPACE);
        wsdl.addNamespace("soap", "http://schemas.xmlsoap.org/wsdl/soap/");
        wsdl.addNamespace("tns", wsdl.getTargetNamespace());
        wsdl.addNamespace("vprop", Constants.VARPROP_NAMESPACE);
        wsdl.addNamespace("xsd", XMLConstants.W3C_XML_SCHEMA_NS_URI);
        wsdl.addNamespace(BPELConstants.DECIDRTYPES_PREFIX,
                Constants.DECIDRTYPES_NAMESPACE);
        wsdl.addNamespace(BPELConstants.DECIDRPROCESSTYPES_PREFIX,
                Constants.DECIDRPROCESSTYPES_NAMESPACE);
    }

    private void setPartnerLinkTypes() throws JDOMException {
        PartnerLinkType partnerLinkType = new PartnerLinkType();
        partnerLinkType.setName(WSDLConstants.PROCESS_PARTNERLINKTYPE);
        de.decidr.model.workflowmodel.bpel.partnerlinktype.Role myRole = new de.decidr.model.workflowmodel.bpel.partnerlinktype.Role();
        myRole.setName(dwdl.getName() + "Provider");
        myRole.setPortType(processPortType.getQName());
        partnerLinkType.getRole().add(myRole);

        wsdl.addExtensibilityElement(partnerLinkType);
    }

    private void setPortTypes() {
        processPortType = new PortTypeImpl();
        processPortType.setQName(new QName(wsdl.getTargetNamespace(), dwdl
                .getName()
                + "PT"));
        startOperation = new OperationImpl();
        startOperation.setName(WSDLConstants.PROCESS_OPERATION);
        Input input = new InputImpl();
        input.setName("input");
        input.setMessage(startMessageRequest);
        Output output = new OutputImpl();
        output.setName("output");
        output.setMessage(startMessageResponse);
        startOperation.setInput(input);
        startOperation.setOutput(output);
        startOperation.setUndefined(false);
        processPortType.addOperation(startOperation);
        processPortType.setUndefined(false);
        wsdl.addPortType(processPortType);
    }

    private void setProperties() throws JDOMException {

        if (dwdl.isSetVariables()) {
            for (Variable variable : dwdl.getVariables().getVariable()) {
                if (variable.isSetConfigurationVariable()
                        && variable.getConfigurationVariable().equals(
                                Boolean.YES)) {
                    Property property = new Property();
                    PropertyAlias propertyAlias = new PropertyAlias();
                    Query query = new Query();

                    property.setName(variable.getName());
                    property.setType(new QName(
                            XMLConstants.W3C_XML_SCHEMA_NS_URI, variable
                                    .getType()));

                    propertyAlias.setPropertyName(new QName(dwdl
                            .getTargetNamespace(), property.getName()));
                    propertyAlias
                            .setMessageType(startMessageRequest.getQName());
                    propertyAlias.setPart(startMessageRequest
                            .getPart("payload").getName());

                    query
                            .getContent()
                            .add(
                                    "/"
                                            + wsdl.getPrefix(wsdl
                                                    .getTargetNamespace())
                                            + ":"
                                            + WSDLConstants.PROCESS_MESSAGE_IN_ELEMENT
                                            + "/"
                                            + wsdl
                                                    .getPrefix(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                                            + ":" + variable.getName());

                    propertyAlias.setQuery(query);

                    wsdl.addExtensibilityElement(property);
                    wsdl.addExtensibilityElement(propertyAlias);
                }
            }
        }
    }

    private void setService() {
        Service processService = new ServiceImpl();
        processService.setQName(new QName(wsdl.getTargetNamespace(), dwdl
                .getName()));
        Port servicePort = new PortImpl();
        servicePort.setBinding(processBinding);
        servicePort.setName(processBinding.getQName().getLocalPart());
        SOAPAddress soapLocation = new SOAPAddressImpl();
        soapLocation.setLocationURI(serverLocation);
        servicePort.addExtensibilityElement(soapLocation);
        processService.addPort(servicePort);
        wsdl.addService(processService);
    }

    private void setTypes() throws JDOMException {
        Types types = new TypesImpl();

        schemaElement = new Element("schema", wsdl
                .getPrefix(XMLConstants.W3C_XML_SCHEMA_NS_URI),
                XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Element messageElementIn = new Element("element", wsdl
                .getPrefix(XMLConstants.W3C_XML_SCHEMA_NS_URI),
                XMLConstants.W3C_XML_SCHEMA_NS_URI);
        messageElementIn.setAttribute("name",
                WSDLConstants.PROCESS_MESSAGE_IN_ELEMENT);
        messageElementIn.setAttribute("type",
                WSDLConstants.PROCESS_MESSAGE_ELEMENT_TYPE);

        Element messageElementOut = new Element("element", wsdl
                .getPrefix(XMLConstants.W3C_XML_SCHEMA_NS_URI),
                XMLConstants.W3C_XML_SCHEMA_NS_URI);
        messageElementOut.setAttribute("name",
                WSDLConstants.PROCESS_MESSAGE_OUT_ELEMENT);
        messageElementOut.setAttribute("type", "xsd:string");

        Element messageType = new Element("complexType", wsdl
                .getPrefix(XMLConstants.W3C_XML_SCHEMA_NS_URI),
                XMLConstants.W3C_XML_SCHEMA_NS_URI);
        messageType.setAttribute("name",
                WSDLConstants.PROCESS_MESSAGE_ELEMENT_TYPE);
        Element all = new Element("all", wsdl
                .getPrefix(XMLConstants.W3C_XML_SCHEMA_NS_URI),
                XMLConstants.W3C_XML_SCHEMA_NS_URI);
        if (dwdl.isSetVariables()) {
            for (Variable variable : dwdl.getVariables().getVariable()) {
                if (variable.isSetConfigurationVariable()
                        && variable.getConfigurationVariable().equals(
                                Boolean.YES)) {
                    Element variableElement = new Element("element", wsdl
                            .getPrefix(XMLConstants.W3C_XML_SCHEMA_NS_URI),
                            XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    variableElement.setAttribute("name", variable.getName());
                    variableElement.setAttribute("type", wsdl
                            .getPrefix(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                            + ":" + variable.getType());
                    all.addContent(variableElement);
                }
            }
        }
        if (dwdl.isSetRoles()) {
            for (Role role : dwdl.getRoles().getRole()) {
                if (role.isSetConfigurationVariable()
                        && role.getConfigurationVariable().equals(Boolean.YES)) {
                    Element roleElement = new Element("element", wsdl
                            .getPrefix(XMLConstants.W3C_XML_SCHEMA_NS_URI),
                            XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    roleElement.setAttribute("name", "role");
                    roleElement.setAttribute("type", wsdl
                            .getPrefix(Constants.DECIDRTYPES_NAMESPACE)
                            + ":" + "tRole");
                    all.addContent(roleElement);
                }
            }
            for (Actor actor : dwdl.getRoles().getActor()) {
                if (actor.isSetConfigurationVariable()
                        && actor.getConfigurationVariable().equals(Boolean.YES)) {
                    Element actorElement = new Element("element", wsdl
                            .getPrefix(XMLConstants.W3C_XML_SCHEMA_NS_URI),
                            XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    actorElement.setAttribute("name", "actor");
                    actorElement.setAttribute("type", "tActor", Namespace
                            .getNamespace("decidr",
                                    Constants.DECIDRTYPES_NAMESPACE));
                    all.addContent(actorElement);
                }
            }
        }

        messageType.addContent(all);

        schemaElement.addContent(messageElementIn);
        schemaElement.addContent(messageType);
        schemaElement.addContent(messageElementOut);

        Schema schema = new SchemaImpl();
        schema.setElement(TransformUtil.jdomToW3c(schemaElement));
        schema.setElementType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI,
                "schema"));

        types.addExtensibilityElement(schema);

        wsdl.setTypes(types);
    }

}
