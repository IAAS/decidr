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

import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.bpel.PartnerLink;
import de.decidr.model.workflowmodel.bpel.Process;
import de.decidr.model.workflowmodel.dd.ObjectFactory;
import de.decidr.model.workflowmodel.dd.TDeployment;
import de.decidr.model.workflowmodel.dd.TInvoke;
import de.decidr.model.workflowmodel.dd.TProvide;
import de.decidr.model.workflowmodel.dd.TService;
import de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter;

/**
 * This class converts a given DWDL object and returns the resulting deployment
 * descriptor.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DWDL2DD {
    
    private static Logger log = DefaultLogger.getLogger(DWDL2DD.class);

    TDeployment deployment = null;
    Process bpel = null;
    Map<String, DecidrWebserviceAdapter> webservices = null;

    public TDeployment getDD(Process bpel,
            Map<String, DecidrWebserviceAdapter> webservices) {
        this.bpel = bpel;
        this.webservices = webservices;
        ObjectFactory factory = new ObjectFactory();
        deployment = factory.createTDeployment();
        de.decidr.model.workflowmodel.dd.TDeployment.Process process = factory
                .createTDeploymentProcess();
        process.setName(new QName(bpel.getTargetNamespace(), bpel.getName(),
                "pns"));
        
        // create for all partner links the corresponding provide and invoke tag
        for (PartnerLink partnerLink : bpel.getPartnerLinks().getPartnerLink()) {
                if (partnerLink.isSetMyRole()) {
                    TProvide provide = factory.createTProvide();
                    provide.setPartnerLink(partnerLink.getName());
                    TService service = factory.createTService();
                    DecidrWebserviceAdapter webservice = findWebserviceAdapter(
                            partnerLink, webservices);
                    if (webservice != null && webservice.getService() != null){
                        service.setName(webservice.getService().getQName());
                        provide.setService(service);
                        provide.setPartnerLink(partnerLink.getName());
                        process.getProvide().add(provide);
                    }
                    
                }
                if (partnerLink.isSetPartnerRole()) {
                    TInvoke invoke = factory.createTInvoke();
                    invoke.setPartnerLink(partnerLink.getName());
                    TService service = factory.createTService();
                    DecidrWebserviceAdapter webservice = findWebserviceAdapter(
                            partnerLink, webservices);
                    if (webservice != null && webservice.getService() != null){
                        service.setName(webservice.getService().getQName());
                        invoke.setService(service);
                        invoke.setPartnerLink(partnerLink.getName());
                        process.getInvoke().add(invoke);
                    }
            }
            // consider special case for the process partner link
            PartnerLink processPartnerLink = getProcessPartnerLink(bpel);
            if (processPartnerLink.isSetMyRole()) {
                TProvide provide = factory.createTProvide();
                provide.setPartnerLink(processPartnerLink.getName());
                TService service = factory.createTService();
                service.setName(new QName(bpel.getTargetNamespace(),bpel.getName()));
                provide.setService(service);
                provide.setPartnerLink(processPartnerLink.getName());
                process.getProvide().add(provide);
            }
            if (processPartnerLink.isSetPartnerRole()) {
                TInvoke invoke = factory.createTInvoke();
                invoke.setPartnerLink(partnerLink.getName());
                TService service = factory.createTService();
                service.setName(new QName(bpel.getTargetNamespace(),bpel.getName()));
                invoke.setService(service);
                invoke.setPartnerLink(processPartnerLink.getName());
                process.getInvoke().add(invoke);
            }
        }
        deployment.getProcess().add(process);
        return deployment;
    }

    private DecidrWebserviceAdapter findWebserviceAdapter(
            PartnerLink partnerLink,
            Map<String, DecidrWebserviceAdapter> webservices) {
        for (DecidrWebserviceAdapter adapter : webservices.values()) {
            if (adapter.getPartnerLink().getName()
                    .equals(partnerLink.getName())) {
                return adapter;
            }
        }
        log.warn("Couldn't find "+partnerLink.getName()+" in DecidrWebserviceAdapter list");
        return null;
    }
    
    private PartnerLink getProcessPartnerLink(Process bpel){
        if (bpel.isSetPartnerLinks()){
            for (PartnerLink partnerLink : bpel.getPartnerLinks().getPartnerLink()){
                if(partnerLink.getName().equals(BPELConstants.PROCESS_PARTNERLINK)){
                    return partnerLink;
                }
            }
        }
        log.warn("Couldn't find partner link "+bpel.getTargetNamespace());
        return null;
    }

}
