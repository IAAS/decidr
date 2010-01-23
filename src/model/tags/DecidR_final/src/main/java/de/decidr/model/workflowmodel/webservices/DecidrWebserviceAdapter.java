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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap12.SOAP12Address;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.bpel.PartnerLink;
import de.decidr.model.workflowmodel.bpel.partnerlinktype.PartnerLinkType;
import de.decidr.model.workflowmodel.bpel.partnerlinktype.Role;

/**
 * This class supplies methods for the conversion components. All web service
 * specific data needed for conversion is prepared and provided through this
 * adapter.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DecidrWebserviceAdapter {

    private static Logger log = DefaultLogger
            .getLogger(DecidrWebserviceAdapter.class);

    WebserviceMapping mapping = null;
    Definition definition = null;

    public DecidrWebserviceAdapter(WebserviceMapping mapping,
            Definition definition) {
        this.mapping = mapping;
        this.definition = definition;
    }

    public Definition getDefinition() {
        return definition;
    }

    public QName getInputMessageType() {
        return getOpertation().getInput().getMessage().getQName();
    }

    public String getLocation() {
        Iterator<?> iter = getService().getPort(mapping.getServicePort())
                .getExtensibilityElements().iterator();
        while (iter.hasNext()) {
            ExtensibilityElement element = (ExtensibilityElement) iter.next();
            if (element instanceof SOAP12Address) {
                SOAP12Address adress = (SOAP12Address) element;
                return adress.getLocationURI();
            } else if (element instanceof SOAPAddress) {
                SOAPAddress adress = (SOAPAddress) element;
                return adress.getLocationURI();
            }
        }
        log.warn("Location in " + definition.getTargetNamespace()
                + " DecidrWebserviceAdapter is null");
        return null;

    }

    public WebserviceMapping getMapping() {
        return mapping;
    }

    public QName getName() {
        return definition.getQName();
    }

    public Operation getOpertation() {
        return getPortType().getOperation(mapping.operation, null, null);
    }

    public QName getOutputMessageType() {
        return getOpertation().getOutput().getMessage().getQName();
    }

    public PartnerLink getPartnerLink() {

        PartnerLink partnerLink = new PartnerLink();
        partnerLink.setName(definition.getQName().getLocalPart() + "PL");
        if (mapping.getPartnerLinkType().getMyRole() != null) {
            partnerLink.setMyRole(mapping.getPartnerLinkType().getMyRole());
        }
        if (mapping.getPartnerLinkType().getPartnerRole() != null) {
            partnerLink.setPartnerRole(mapping.getActivity() + "Provider");
        }
        partnerLink.setPartnerLinkType(new QName(definition
                .getTargetNamespace(), getPartnerLinkType().getName()));
        return partnerLink;
    }

    private List<PartnerLinkType> getPartnerLinksElements(Definition def) {
        List<PartnerLinkType> list = new ArrayList<PartnerLinkType>();
        Iterator<?> extElementIter = def.getExtensibilityElements().iterator();
        while (extElementIter.hasNext()) {
            ExtensibilityElement extElement = (ExtensibilityElement) extElementIter
                    .next();
            if (extElement instanceof PartnerLinkType) {
                list.add((PartnerLinkType) extElement);
            }
        }
        return list;
    }

    public PartnerLinkType getPartnerLinkType() {

        // get all partner link elements in the wsdl definition
        List<PartnerLinkType> partnerLinkTypes = getPartnerLinksElements(definition);

        // return only the partner link type that contains a role element
        // that itself references to the port type specified in the mapping
        for (PartnerLinkType plnk : partnerLinkTypes) {
            if (plnk.isSetRole()) {
                for (Role role : plnk.getRole()) {
                    if (role.isSetPortType()) {
                        if (role.getPortType().getLocalPart().equals(
                                mapping.getPortType())) {
                            return plnk;
                        }
                    }
                }
            }
        }
        // otherwise return null
        log.warn("Partner link type in " + definition.getTargetNamespace()
                + " DecidrWebserviceAdapter is null");
        return null;
    }

    public PortType getPortType() {
        return definition.getPortType(new QName(
                definition.getTargetNamespace(), mapping.portType));
    }

    public Service getService() {
        return definition.getService(new QName(definition.getTargetNamespace(),
                mapping.getService()));
    }

    public String getTargetNamespace() {
        return definition.getTargetNamespace();
    }

}
