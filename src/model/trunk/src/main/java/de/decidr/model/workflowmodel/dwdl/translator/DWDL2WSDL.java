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
import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.Types;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.ibm.wsdl.ImportImpl;
import com.ibm.wsdl.TypesImpl;
import de.decidr.model.logging.DefaultLogger;
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
    
    public Definition getWSDL(Workflow dwdl, String location,
            String tenantName) {
        wsdl = new com.ibm.wsdl.DefinitionImpl();
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
       
        
    }

    private void setNamespaces() {
       wsdl.setTargetNamespace(dwdl.getTargetNamespace());
       wsdl.addNamespace("wsdl", "http://schemas.xmlsoap.org/wsdl/http/");
       wsdl.addNamespace("http", "http://schemas.xmlsoap.org/wsdl/http/");
       wsdl.addNamespace("mime", "http://schemas.xmlsoap.org/wsdl/mime/");
       wsdl.addNamespace("plnk", "http://docs.oasis-open.org/wsbpel/2.0/plnktype/");
       wsdl.addNamespace("soap", "http://schemas.xmlsoap.org/wsdl/soap/");
       wsdl.addNamespace("tns", dwdl.getTargetNamespace());
       wsdl.addNamespace("vprop", "http://docs.oasis-open.org/wsbpel/2.0/varprop/");
    }

    private void setImports() {
        Import decidrTypes = new ImportImpl();
        decidrTypes.setNamespaceURI(BPELConstants.DECIDRTYPES_NAMESPACE);
        decidrTypes.setLocationURI(BPELConstants.DECIDRTYPES_LOCATION);
        wsdl.addImport(decidrTypes);
    }

    private void setTypes() {
        Types types = new TypesImpl();
        UnknownExtensibilityElement schema = new UnknownExtensibilityElement();
        DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
        }
        catch (ParserConfigurationException e) {
            log.warn("creation of com.w3c.document failed for some really mysterious reasons");
        }
        // MA create a schema element
        Element schemaElement = doc.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, "xs:schema");
        
        schema.setElement(schemaElement);
        types.addExtensibilityElement(schema);
    }

}
