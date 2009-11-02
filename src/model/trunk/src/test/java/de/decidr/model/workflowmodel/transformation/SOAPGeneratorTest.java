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

package de.decidr.model.workflowmodel.transformation;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2SOAP;
import de.decidr.model.workflowmodel.factories.HumanTaskStartConfigurationFactory;
import de.decidr.model.workflowmodel.factories.WSDLFactory;
import de.decidr.model.workflowmodel.instancemanagement.SOAPGenerator;
import de.decidr.model.workflowmodel.wsc.TConfiguration;

/**
 * TODO: add comment
 * 
 * @author AT
 */
public class SOAPGeneratorTest {

    /**
     * TODO: add comment
     * 
     * @param args
     * @throws URISyntaxException
     * @throws WSDLException
     * @throws SOAPException
     * @throws UnsupportedOperationException
     * @throws JAXBException
     * @throws IOException
     */
    public static void main(String[] args) throws WSDLException,
            URISyntaxException, UnsupportedOperationException, SOAPException,
            IOException, JAXBException {
        TConfiguration tConfiguration = HumanTaskStartConfigurationFactory
                .getHumanTaskStartConfiguration();
        SOAPGenerator soapGenerator = new SOAPGenerator();
        Definition definition = WSDLFactory.getHumanTaskDefinition();
        DWDL2SOAP dwdl2Soap = new DWDL2SOAP();
        SOAPMessage template = dwdl2Soap.getSOAP(definition, "HumanTaskPT",
                "createTask");

        // template.writeTo(System.out);

        soapGenerator.getSOAP(template, tConfiguration).writeTo(System.out);

    }

}
