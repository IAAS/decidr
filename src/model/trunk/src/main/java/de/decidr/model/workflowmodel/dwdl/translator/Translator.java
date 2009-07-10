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
import java.io.FileInputStream;

import javax.wsdl.Definition;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPMessage;

import de.decidr.model.workflowmodel.bpel.TProcess;
import de.decidr.model.workflowmodel.dd.TDeployment;
import de.decidr.model.workflowmodel.dwdl.TWorkflow;

/**
 * This class provides the functionality to translate a given DWDL into
 * different formats
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class Translator {

    private TWorkflow dwdlWorkflow = null;
    private TProcess bpelProcess = null;
    private TDeployment dd = null;
    private SOAPMessage soap = null;
    private Definition wsdl = null;

    public void load(byte[] dwdl) throws JAXBException {

        JAXBContext dwdlCntxt = JAXBContext.newInstance(TWorkflow.class);
        Unmarshaller dwdlUnmarshaller = dwdlCntxt.createUnmarshaller();
        JAXBElement<TWorkflow> element = parse(dwdl, dwdlUnmarshaller);
        
        dwdlWorkflow = element.getValue();
    }

    private JAXBElement<TWorkflow> parse(byte[] dwdl,
            Unmarshaller dwdlUnmarshaller) throws JAXBException {
        return (JAXBElement<TWorkflow>) dwdlUnmarshaller
        .unmarshal(new ByteArrayInputStream(dwdl));
    }

    public TProcess getBPEL() {
        DWDL2BPEL bpelConverter = new DWDL2BPEL();
        bpelProcess = bpelConverter.getBPEL(dwdlWorkflow);
        return bpelProcess;
    }

    public Definition getWSDL(String loaction, String tenantName) {
        return wsdl;
    }

    public TDeployment getDD() {
        return dd;
    }

    public SOAPMessage getSOAP() {
        return soap;
    }

}
