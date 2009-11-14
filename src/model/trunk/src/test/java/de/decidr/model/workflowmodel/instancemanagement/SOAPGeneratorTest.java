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

package de.decidr.model.workflowmodel.instancemanagement;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.wsdl.Definition;
import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2SOAP;
import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2WSDL;
import de.decidr.model.workflowmodel.dwdl.transformation.WSDLConstants;
import de.decidr.model.workflowmodel.factories.DWDLFactory;
import de.decidr.model.workflowmodel.wsc.TActor;
import de.decidr.model.workflowmodel.wsc.TAssignment;
import de.decidr.model.workflowmodel.wsc.TConfiguration;
import de.decidr.model.workflowmodel.wsc.TRole;
import de.decidr.model.workflowmodel.wsc.TRoles;

/**
 * This class JUnit test the correct construction of a SOAP template message
 * with the corresponding start configuration.
 * 
 * @author Modood Alvi
 */
public class SOAPGeneratorTest {

    static SOAPGenerator generator;
    static DWDL2SOAP soapConverter;
    static DWDL2SOAP translater = null;
    static DWDL2WSDL wsdlconv = null;
    static Definition wsdl = null;
    static String portName = null;
    static String operationName = null;
    static Workflow dwdl = null;
    static String location = "";
    static String tenantName = "";
    static TConfiguration startConfiguration;
    static SOAPMessage template;

    /**
     * Generate all relevant data
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        translater = new DWDL2SOAP();
        wsdlconv = new DWDL2WSDL();
        dwdl = DWDLFactory.getDWDLWorkflow();
        wsdl = wsdlconv.getWSDL(dwdl, location, tenantName);
        portName = dwdl.getName() + "PT";
        operationName = WSDLConstants.PROCESS_OPERATION;
        generator = new SOAPGenerator();
        soapConverter = new DWDL2SOAP();
        template = soapConverter.getSOAP(wsdl, portName, operationName);
        startConfiguration = new TConfiguration();
        // Aleks, bitte hier entsprechen der template befüllen
        TAssignment a1 = new TAssignment();
        TAssignment a2 = new TAssignment();
        TAssignment a3 = new TAssignment();
        TRoles roles = new TRoles();
        TRole role = new TRole();
        TActor actor1 = new TActor();
        role.getActor().add(actor1);
        roles.getRole().add(role);
        startConfiguration.getAssignment().add(a1);
        startConfiguration.getAssignment().add(a2);
        startConfiguration.getAssignment().add(a3);
        startConfiguration.setRoles(roles);
    }


    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.instancemanagement.SOAPGenerator#getSOAP(javax.xml.soap.SOAPMessage, de.decidr.model.workflowmodel.wsc.TConfiguration)}
     * .
     * @throws JAXBException 
     * @throws IOException 
     * @throws SOAPException 
     */
    @Test
    public void testGetSOAP() throws SOAPException, IOException, JAXBException {
        SOAPMessage msg = generator.getSOAP(template, startConfiguration);
        assertNotNull(msg);
        msg.writeTo(System.out);
    }

}
