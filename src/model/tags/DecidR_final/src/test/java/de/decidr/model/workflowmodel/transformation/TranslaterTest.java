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

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.wsdl.Definition;
import javax.xml.soap.SOAPMessage;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.entities.KnownWebService;
import de.decidr.model.workflowmodel.bpel.Process;
import de.decidr.model.workflowmodel.dd.TDeployment;
import de.decidr.model.workflowmodel.dwdl.transformation.Translator;
import de.decidr.model.workflowmodel.factories.DWDLFactory;
import de.decidr.model.workflowmodel.factories.KnownWebserviceFactory;

/**
 * This JUnit test tests the correct transformation of all deployment relevant
 * objects
 * 
 * @author Modood Alvi
 */
public class TranslaterTest {

    static Translator translater = null;
    static String location = null;
    static byte[] dwdl = null;
    static String tenantName = null;
    static List<KnownWebService> knownWebservices = null;

    /**
     * Load all relevant data for transformation process
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        translater = new Translator();
        dwdl = DWDLFactory.getDWDLWorkflowByteArray();
        tenantName = "Hugo";
        knownWebservices = KnownWebserviceFactory.getKnownWebservice();
        translater.load(dwdl, tenantName, knownWebservices);
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.dwdl.transformation.Translator#getBPEL()}
     * .
     */
    @Test
    public void testGetBPEL() {
        Process bpel = translater.getBPEL();
        assertNotNull(bpel);
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.dwdl.transformation.Translator#getDD()}
     * .
     */
    @Test
    public void testGetDD() {
        TDeployment dd = translater.getDD();
        assertNotNull(dd);
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.dwdl.transformation.Translator#getSOAPTemplate()}
     * .
     */
    @Test
    public void testGetSOAPTemplate() {
        SOAPMessage msg = translater.getSOAPTemplate();
        assertNotNull(msg);
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.dwdl.transformation.Translator#getWSDL(java.lang.String)}
     * .
     */
    @Test
    public void testGetWSDL() {
        Definition def = translater.getWSDL(location);
        assertNotNull(def);
    }

}
