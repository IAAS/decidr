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

import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.xml.namespace.QName;

import de.decidr.model.workflowmodel.bpel.PartnerLink;
import de.decidr.model.workflowmodel.bpel.PartnerLinkType;

/**
 * This class supplies methods for the conversion components.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DecidrWebserviceAdapter {

    WebserviceMapping mapping = null;
    Definition definition = null;

    public DecidrWebserviceAdapter(WebserviceMapping mapping,
            Definition definition) {
        this.mapping = mapping;
        this.definition = definition;
    }

    public QName getName() {
        return definition.getQName();
    }

    public PartnerLink getPartnerLink() {
        // MA what about ParnterLinks?
        return null;
    }

    public PartnerLinkType getPartnerLinkType() {
        // MA what about PartnerLinkTypes?
        return null;
    }

    public String getTargetNamespace() {
        return definition.getTargetNamespace();
    }

    public String getLocation() {
        return null;
    }

    public PortType getPortType() {
        return definition.getPortType(new QName(mapping.portType));
    }

    public Operation getOpertation() {
        return definition.getPortType(
                new QName(definition.getTargetNamespace(), mapping.portType))
                .getOperation(null, mapping.operation, null);
    }
    
    public Definition getDefinition(){
        return definition;
    }
    
    public QName getInputMessageType() {
        return getOpertation().getInput().getMessage().getQName();
    }
    
    public QName getOutputMessageType() {
        return getOpertation().getOutput().getMessage().getQName();
    }
    
    public Service getService(){
        // MA What about Service?
        return null;
    }
    
}
