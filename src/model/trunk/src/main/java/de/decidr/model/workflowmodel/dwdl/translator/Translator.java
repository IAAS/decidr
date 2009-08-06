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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.xml.WSDLReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.InputSource;

import de.decidr.model.entities.KnownWebService;
import de.decidr.model.workflowmodel.bpel.Process;
import de.decidr.model.workflowmodel.dd.TDeployment;
import de.decidr.model.workflowmodel.dwdl.Workflow;

/**
 * This class provides the functionality to translate a given DWDL into
 * different formats
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class Translator {

    private Workflow dwdlWorkflow = null;
    private Process bpelProcess = null;
    private TDeployment dd = null;
    private byte[] soap = null;
    private Definition wsdl = null;
    private List<Definition> webservicesWSDLs = null;
    private String tenantName = null;

    public void load(byte[] dwdl, String tenantName) throws JAXBException {
        JAXBContext dwdlCntxt = JAXBContext.newInstance(Workflow.class);
        Unmarshaller dwdlUnmarshaller = dwdlCntxt.createUnmarshaller();
        JAXBElement<Workflow> element = dwdlUnmarshaller.unmarshal(
                new StreamSource(new ByteArrayInputStream(dwdl)),
                Workflow.class);

        dwdlWorkflow = element.getValue();
        this.tenantName = tenantName;
    }

    public void load(byte[] dwdl, List<KnownWebService> webservices,
            String tenantName) throws JAXBException, WSDLException, IOException {

        webservicesWSDLs = new ArrayList<Definition>();
        JAXBContext dwdlCntxt = JAXBContext.newInstance(Workflow.class);
        Unmarshaller dwdlUnmarshaller = dwdlCntxt.createUnmarshaller();
        JAXBElement<Workflow> dwdlElement = dwdlUnmarshaller.unmarshal(
                new StreamSource(new ByteArrayInputStream(dwdl)),
                Workflow.class);
        WSDLReader reader = new com.ibm.wsdl.xml.WSDLReaderImpl();
        InputSource in = null;
        for (KnownWebService webservice : webservices) {
            in = new InputSource(new ByteArrayInputStream(webservice.getWsdl()));
            Definition def = reader.readWSDL(null,in);
            webservicesWSDLs.add(def);
        }
        dwdlWorkflow = dwdlElement.getValue();
        this.tenantName = tenantName;
    }

    public Process getBPEL() {
        DWDL2BPEL bpelConverter = new DWDL2BPEL();
        bpelProcess = bpelConverter.getBPEL(dwdlWorkflow, tenantName);
        return bpelProcess;
    }

    public Definition getWSDL(String location, String tenantName) {
        return wsdl;
    }

    public TDeployment getDD() {
        return dd;
    }

    public byte[] getSOAP() {
        return soap;
    }

}
