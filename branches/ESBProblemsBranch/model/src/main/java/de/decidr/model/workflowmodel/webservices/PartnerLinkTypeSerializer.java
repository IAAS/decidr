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

package de.decidr.model.workflowmodel.webservices;

import java.io.PrintWriter;
import java.io.Serializable;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.ExtensionDeserializer;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.extensions.ExtensionSerializer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.bpel.partnerlinktype.PartnerLinkType;
import de.decidr.model.workflowmodel.dwdl.transformation.Constants;

/**
 * A custom serializer to read and easily handle partner link types in
 * {@link Definition} This serializer wraps the JAXB marshaller and unmarshaller
 * created for partner link types.
 * 
 * @author Modood Alvi
 */
public class PartnerLinkTypeSerializer implements ExtensionSerializer,
        ExtensionDeserializer, Serializable {
    
    public static final long serialVersionUID = 1l;
    
    private static Logger log = DefaultLogger.getLogger(PartnerLinkTypeSerializer.class);
    

    /*
     * (non-Javadoc)
     * 
     * @see javax.wsdl.extensions.ExtensionSerializer#marshall(java.lang.Class,
     * javax.xml.namespace.QName, javax.wsdl.extensions.ExtensibilityElement,
     * java.io.PrintWriter, javax.wsdl.Definition,
     * javax.wsdl.extensions.ExtensionRegistry)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void marshall(Class parentType, QName elementType,
            ExtensibilityElement extension, PrintWriter pw, Definition def,
            ExtensionRegistry extReg) throws WSDLException {
        try {
            PartnerLinkType partnerLinkType = (PartnerLinkType) extension;
            Marshaller m = JAXBContext.newInstance(PartnerLinkType.class).createMarshaller();
            JAXBElement<PartnerLinkType> jaxbElement = new JAXBElement<PartnerLinkType>(
                    new QName(Constants.PARTNERLINKTYPE_NAMESPACE,
                            "partnerLinkType"), PartnerLinkType.class, partnerLinkType);
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            m.marshal(jaxbElement, pw);
            pw.println();
        } catch (JAXBException e) {
            log.error("Can't marshall the partner link type element in "+def.getTargetNamespace(), e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.wsdl.extensions.ExtensionDeserializer#unmarshall(java.lang.Class,
     * javax.xml.namespace.QName, org.w3c.dom.Element, javax.wsdl.Definition,
     * javax.wsdl.extensions.ExtensionRegistry)
     */
    @SuppressWarnings("unchecked")
    @Override
    public ExtensibilityElement unmarshall(Class parentType, QName elementType,
            Element el, Definition def, ExtensionRegistry extReg)
            throws WSDLException {
        PartnerLinkType extpartnerLinkType = (PartnerLinkType) extReg.createExtension(parentType,
                elementType);
        PartnerLinkType tmpPartnerLinkType = null;
        try {
            Unmarshaller u = JAXBContext.newInstance(PartnerLinkType.class).createUnmarshaller();
            JAXBElement<PartnerLinkType> jaxbElement = u.unmarshal(el, PartnerLinkType.class);
            tmpPartnerLinkType = jaxbElement.getValue();
        } catch (JAXBException e) {
            log.error("Can't unmarshall the partner link type element in "+def.getTargetNamespace(), e);
        }
        if (tmpPartnerLinkType != null){
            if (tmpPartnerLinkType.isSetName()){
                extpartnerLinkType.setName(tmpPartnerLinkType.getName());
            }
            if (tmpPartnerLinkType.isSetRole()){
                extpartnerLinkType.getRole().addAll(tmpPartnerLinkType.getRole());
            }
            if (tmpPartnerLinkType.isSetDocumentation()){
                extpartnerLinkType.getDocumentation().addAll(tmpPartnerLinkType.getDocumentation());
            }
            if (tmpPartnerLinkType.isSetAny()){
                extpartnerLinkType.getAny().addAll(tmpPartnerLinkType.getAny());
            }
        }
        else {
            log.warn("Partner link in type is null in "+def.getTargetNamespace());
        }
        return extpartnerLinkType;
    }

}
