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

import static org.junit.Assert.*;

import java.io.IOException;

import javax.wsdl.Definition;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2SOAP;
import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2WSDL;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.dwdl.transformation.WSDLConstants;
import de.decidr.model.workflowmodel.factories.DWDLFactory;

/**
 * This class tests the DWDL to SOAP transformation
 * 
 * @author Modood Alvi
 */
public class DWDL2SOAPTest {

    static DWDL2SOAP translater = null;
    static DWDL2WSDL wsdlconv = null;
    static Definition wsdl = null;
    static String portName = null;
    static String operationName = null;
    static Workflow dwdl = null;
    static String location = "";
    static String tenantName = "";

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
        System.out.println(new String(TransformUtil.definitionToBytes(wsdl)));
        System.out.println();
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.dwdl.transformation.DWDL2SOAP#getSOAP(javax.wsdl.Definition, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws SOAPException
     * @throws UnsupportedOperationException
     * @throws IOException 
     */
    @Test
    public void testGetSOAP() throws UnsupportedOperationException,
            SOAPException, IOException {
        SOAPMessage msg = translater.getSOAP(wsdl, portName, operationName);
        assertNotNull(msg);
        msg.writeTo(System.out);
    }

}
