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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.xml.sax.SAXException;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.Activity;
import de.decidr.model.entities.KnownWebService;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.bpel.Process;
import de.decidr.model.workflowmodel.dd.TDeployment;
import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter;
import de.decidr.model.workflowmodel.webservices.WebserviceMapping;

/**
 * This class provides the functionality to translate a given DWDL into
 * different formats. The order of translation is important. You can't create
 * e.g. create a SOAP Message without having built a WSDL.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class Translator {

    private static Logger log = DefaultLogger.getLogger(Translator.class);

    private Workflow dwdlWorkflow = null;
    private Process bpelProcess = null;
    private TDeployment dd = null;
    private SOAPMessage soap = null;
    private Definition wsdl = null;
    private Map<String, DecidrWebserviceAdapter> webserviceAdapters = null;
    private String tenantName = null;

    private Map<String, DecidrWebserviceAdapter> createAdapters(
            List<KnownWebService> knownWebservices) throws JAXBException,
            WSDLException {
        HashMap<String, DecidrWebserviceAdapter> adapters = new HashMap<String, DecidrWebserviceAdapter>();
        for (KnownWebService webservice : knownWebservices) {
            for (Activity activity : webservice.getActivities()) {
                DecidrWebserviceAdapter adapter = new DecidrWebserviceAdapter(
                        parseMapping(activity.getMapping()),
                        parseDefinition(webservice));
                adapters.put(activity.getName(), adapter);
            }
        }
        return adapters;
    }

    public Process getBPEL() {
        DWDL2BPEL bpelConverter = new DWDL2BPEL();
        bpelProcess = bpelConverter.getBPEL(dwdlWorkflow, webserviceAdapters);

        return bpelProcess;
    }

    public TDeployment getDD() {
        DWDL2DD ddConverter = new DWDL2DD();
        dd = ddConverter.getDD(bpelProcess, webserviceAdapters);
        return dd;
    }

    public SOAPMessage getSOAPTemplate() {
        DWDL2SOAP soapConverter = new DWDL2SOAP();
        try {
            soap = soapConverter.getSOAP(wsdl, wsdl.getQName().getLocalPart()
                    + "PT", WSDLConstants.PROCESS_OPERATION);
        } catch (UnsupportedOperationException e) {
            log.error("Can't find operation " + WSDLConstants.PROCESS_OPERATION
                    + " for dwdl to soap translation", e);
        } catch (SOAPException e) {
            log.error("Can't translate dwdl to soap", e);
        }
        return soap;
    }

    public Definition getWSDL(String serverLocation) {
        DWDL2WSDL wsdlConverter = new DWDL2WSDL();
        try {
            wsdl = wsdlConverter.getWSDL(dwdlWorkflow, serverLocation,
                    tenantName);
        } catch (JDOMException e) {
            log.error("Can't translate dwdl to wsdl", e);
        }
        return wsdl;
    }

    public void load(byte[] dwdl, String tenantName,
            List<KnownWebService> knownWebservices) throws JAXBException,
            WSDLException, SAXException {

        this.dwdlWorkflow = parseDWDLWorkflow(dwdl);
        this.tenantName = tenantName;
        this.webserviceAdapters = createAdapters(knownWebservices);
    }

    private Definition parseDefinition(KnownWebService knownWebservice)
            throws WSDLException {
        return TransformUtil.bytesToDefinition(DecidrGlobals
                .getWebServiceWsdl(knownWebservice.getName()));
    }

    private Workflow parseDWDLWorkflow(byte[] dwdl) throws JAXBException,
            SAXException {
        return TransformUtil.bytesToWorkflow(dwdl);
    }

    private WebserviceMapping parseMapping(byte[] mapping) throws JAXBException {
        return TransformUtil.bytesToMapping(mapping);
    }
}
