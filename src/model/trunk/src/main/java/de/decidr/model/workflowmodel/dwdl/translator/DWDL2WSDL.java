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

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.output.DOMOutputter;

import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.Types;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import com.ibm.wsdl.DefinitionImpl;
import com.ibm.wsdl.ImportImpl;
import com.ibm.wsdl.TypesImpl;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.dwdl.Actor;
import de.decidr.model.workflowmodel.dwdl.Boolean;
import de.decidr.model.workflowmodel.dwdl.Role;
import de.decidr.model.workflowmodel.dwdl.Variable;
import de.decidr.model.workflowmodel.dwdl.Workflow;

/**
 * This class converts a given DWDL object and returns the resulting WSDL.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DWDL2WSDL {

    private static Logger log = DefaultLogger.getLogger(DWDL2WSDL.class);

    private Workflow dwdl = null;
    private Definition wsdl = null;
    private String tenantName = null;
    private String location = null;

    public Definition getWSDL(Workflow dwdl, String location, String tenantName) throws JDOMException {
        wsdl = new DefinitionImpl();
        this.location = location;
        this.tenantName = tenantName;
        this.dwdl = dwdl;

        log.trace("setting namespaces");
        setNamespaces();

        log.trace("setting imports");
        setImports();

        log.trace("setting types");
        setTypes();

        log.trace("setting messages");
        setMessages();

        return wsdl;
    }

    private void setMessages() {
        // XXX: document: why is this empty
    }

    private void setNamespaces() {
        wsdl.setTargetNamespace(dwdl.getTargetNamespace());
        wsdl.addNamespace("wsdl", "http://schemas.xmlsoap.org/wsdl/http/");
        wsdl.addNamespace("http", "http://schemas.xmlsoap.org/wsdl/http/");
        wsdl.addNamespace("mime", "http://schemas.xmlsoap.org/wsdl/mime/");
        wsdl.addNamespace("plnk",
                "http://docs.oasis-open.org/wsbpel/2.0/plnktype/");
        wsdl.addNamespace("soap", "http://schemas.xmlsoap.org/wsdl/soap/");
        wsdl.addNamespace("tns", dwdl.getTargetNamespace());
        wsdl.addNamespace("vprop",
                "http://docs.oasis-open.org/wsbpel/2.0/varprop/");
        wsdl.addNamespace("xsd", XMLConstants.W3C_XML_SCHEMA_NS_URI);
        wsdl.addNamespace("decidr", BPELConstants.DECIDRTYPES_NAMESPACE);
    }

    private void setImports() {
        Import decidrTypes = new ImportImpl();
        decidrTypes.setNamespaceURI(BPELConstants.DECIDRTYPES_NAMESPACE);
        decidrTypes.setLocationURI(BPELConstants.DECIDRTYPES_LOCATION);
        wsdl.addImport(decidrTypes);
    }

    private void setTypes() throws JDOMException {
        Types types = new TypesImpl();

        Document jdomDoc = new Document();
        Element schemaElement = new Element("schema", "xsd",
                XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Element messageRoot = new Element("element", "xsd",
                XMLConstants.W3C_XML_SCHEMA_NS_URI);
        messageRoot.setAttribute("name", "startProcess");
        messageRoot.setAttribute("type", "startMessage");

        Element messageType = new Element("complexType", "xsd",
                XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Element all = new Element("all", "xsd",
                XMLConstants.W3C_XML_SCHEMA_NS_URI);
        for (Variable variable : dwdl.getVariables().getVariable()) {
            if (variable.isSetConfigurationVariable()
                    && variable.getConfigurationVariable().equals(Boolean.YES)) {
                Element variableElement = new Element("element", "xsd",
                        XMLConstants.W3C_XML_SCHEMA_NS_URI);
                variableElement.setAttribute("name", variable.getName());
                variableElement.setAttribute("type", variable.getType());
                all.addContent(variableElement);
            }
        }
        for (Role role : dwdl.getRoles().getRole()) {
            if (role.isSetConfigurationVariable()
                    && role.getConfigurationVariable().equals(Boolean.YES)) {
                Element roleElement = new Element("element", "xsd",
                        XMLConstants.W3C_XML_SCHEMA_NS_URI);
                roleElement.setAttribute("name", "role");
                roleElement.setAttribute("type", "tRole", Namespace
                        .getNamespace("decidr",
                                BPELConstants.DECIDRTYPES_NAMESPACE));
                all.addContent(roleElement);
            }
        }
        for (Actor actor : dwdl.getRoles().getActor()) {
            if (actor.isSetConfigurationVariable()
                    && actor.getConfigurationVariable().equals(Boolean.YES)) {
                Element actorElement = new Element("element", "xsd",
                        XMLConstants.W3C_XML_SCHEMA_NS_URI);
                actorElement.setAttribute("name", "actor");
                actorElement.setAttribute("type", "tActor", Namespace
                        .getNamespace("decidr",
                                BPELConstants.DECIDRTYPES_NAMESPACE));
                all.addContent(actorElement);
            }
        }
        jdomDoc.setRootElement(schemaElement);
        schemaElement.addContent(messageRoot);
        schemaElement.addContent(messageType);
        messageType.addContent(all);
        
        DOMOutputter domOut = new DOMOutputter();
        org.w3c.dom.Document w3cDoc = domOut.output(jdomDoc);
        org.w3c.dom.Element w3cElment = w3cDoc.getDocumentElement();

        UnknownExtensibilityElement extensibilityElement = new UnknownExtensibilityElement();
        extensibilityElement.setElement(w3cElment);
        extensibilityElement.setElementType(new QName(w3cElment.getNamespaceURI()));
        
        types.addExtensibilityElement(extensibilityElement);

    }
}
