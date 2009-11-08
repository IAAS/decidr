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

import javax.wsdl.Definition;
import javax.xml.soap.SOAPException;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2SOAP;

/**
 * This class tests the DWDL to SOAP transformation
 *
 * @author Modood Alvi
 */
public class DWDL2SOAPTest {
    
    static DWDL2SOAP translater = null;
    static Definition wsdl = null;
    static String portName = null;
    static String operationName = null;

    /**
     * Generate all relevant data
     *
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        translater = new DWDL2SOAP();
    }

    /**
     * Test method for {@link de.decidr.model.workflowmodel.dwdl.transformation.DWDL2SOAP#getSOAP(javax.wsdl.Definition, java.lang.String, java.lang.String)}.
     * @throws SOAPException 
     * @throws UnsupportedOperationException 
     */
    @Test
    public void testGetSOAP() throws UnsupportedOperationException, SOAPException {
        
        translater.getSOAP(wsdl, portName, operationName);
    }

}
