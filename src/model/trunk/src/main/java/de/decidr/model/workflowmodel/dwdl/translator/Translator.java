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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.xml.WSDLReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;


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
 * different formats
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class Translator {
    
    private static Logger log = DefaultLogger.getLogger(Translator.class);

    private Workflow dwdlWorkflow = null;
    private Process bpelProcess = null;
    private TDeployment dd = null;
    private byte[] soap = null;
    private Definition wsdl = null;
    private List<Definition> definitions = null;
    private Map<String, DecidrWebserviceAdapter> webserviceAdapters = null;
    private String tenantName = null;

    public void load(byte[] dwdl, String tenantName,
            List<KnownWebService> knownWebservices) throws JAXBException,
            WSDLException, IOException {

        this.dwdlWorkflow = parseDWDLWorkflow(dwdl);
        this.tenantName = tenantName;
        this.webserviceAdapters = createAdapters(knownWebservices);
    }

    private Map<String, DecidrWebserviceAdapter> createAdapters(
            List<KnownWebService> knownWebservices) throws JAXBException,
            WSDLException {
        HashMap<String, DecidrWebserviceAdapter> adapters = new HashMap<String, DecidrWebserviceAdapter>();
        definitions = new ArrayList<Definition>();
        for (KnownWebService webservice : knownWebservices) {
            for (Activity activity : webservice.getActivities()) {
                WebserviceMapping mapping = parseMapping(activity.getMapping());
                Definition definition = parseDefinition(webservice);
                definitions.add(definition);
                DecidrWebserviceAdapter adapter = new DecidrWebserviceAdapter(
                        mapping, definition);
                adapters.put(activity.getName(), adapter);
            }
        }
        return adapters;
    }

    private WebserviceMapping parseMapping(byte[] mapping) throws JAXBException {
        JAXBContext dwdlCntxt = JAXBContext.newInstance(Workflow.class);
        Unmarshaller dwdlUnmarshaller = dwdlCntxt.createUnmarshaller();
        JAXBElement<WebserviceMapping> dwdlElement = dwdlUnmarshaller
                .unmarshal(new StreamSource(new ByteArrayInputStream(mapping)),
                        WebserviceMapping.class);
        return dwdlElement.getValue();
    }

    private Workflow parseDWDLWorkflow(byte[] dwdl) throws JAXBException {
        JAXBContext dwdlCntxt = JAXBContext.newInstance(Workflow.class);
        Unmarshaller dwdlUnmarshaller = dwdlCntxt.createUnmarshaller();
        JAXBElement<Workflow> dwdlElement = dwdlUnmarshaller.unmarshal(
                new StreamSource(new ByteArrayInputStream(dwdl)),
                Workflow.class);
        return dwdlElement.getValue();
    }

    private Definition parseDefinition(KnownWebService knownWebservice)
            throws WSDLException {
        WSDLReader reader = new com.ibm.wsdl.xml.WSDLReaderImpl();
        InputSource in = new InputSource(new ByteArrayInputStream(
                knownWebservice.getWsdl()));
        Definition def = reader.readWSDL(null, in);
        return def;
    }

    public Process getBPEL() {
        DWDL2BPEL bpelConverter = new DWDL2BPEL();
        try {
            bpelProcess = bpelConverter.getBPEL(dwdlWorkflow, tenantName,
                    webserviceAdapters);
        } catch (TransformerException e) {
            log.error("Can't transform dwdl to bpel", e);
        }
        return bpelProcess;
    }

    public Definition getWSDL(String location) {
        DWDL2WSDL wsdlConverter = new DWDL2WSDL();
        wsdl = wsdlConverter.getWSDL(dwdlWorkflow, location, tenantName);
        return wsdl;
    }

    public TDeployment getDD() {
        DWDL2DD ddConverter = new DWDL2DD();
        dd = ddConverter.getDD(bpelProcess,webserviceAdapters);
        return dd;
    }

    public byte[] getSOAP() {
        return soap;
    }

}
