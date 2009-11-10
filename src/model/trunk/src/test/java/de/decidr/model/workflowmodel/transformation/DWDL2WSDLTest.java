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

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;

import org.jdom.JDOMException;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2WSDL;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.factories.DWDLFactory;

/**
 * This JUnit test case tests the DWDL to WSDL translation
 * 
 * @author Modood Alvi
 */
public class DWDL2WSDLTest {

    static DWDL2WSDL translater = null;
    static Workflow dwdl = null;
    static String location = null;
    static String tenantName = null;

    /**
     * Retrieve all relevant objects
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        translater = new DWDL2WSDL();
        dwdl = DWDLFactory.getDWDLWorkflow();
        location = "http://ec2-174-129-24-232.compute-1.amazonaws.com:8080/ode/";
        tenantName = "Hugo";
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.dwdl.transformation.DWDL2WSDL#getWSDL(de.decidr.model.workflowmodel.dwdl.Workflow, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws JDOMException
     * @throws WSDLException
     */
    @Test
    public void testGetWSDL() throws JDOMException, WSDLException {
        Definition wsdl = translater.getWSDL(dwdl, location, tenantName);
        assertNotNull(wsdl);
        byte [] byteWSDL = TransformUtil.definitionToBytes(wsdl);
        String wsdlString = new String(byteWSDL);
        System.out.println(wsdlString);
        assertEquals(wsdl.getTargetNamespace(), dwdl.getTargetNamespace());
    }

}
