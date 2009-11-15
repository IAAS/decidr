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

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.workflowmodel.factories.MappingFactory;
import de.decidr.model.workflowmodel.factories.WSDLFactory;
import de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter;
import de.decidr.model.workflowmodel.webservices.WebserviceMapping;

/**
 * This JUnitTest tests the right implementation of the
 * <code>DecidrWebserviceAdpater</code>
 * 
 * @author Modood Alvi
 */
public class WebserviceAdapterTest {

    static Definition humanTask = null;
    static Definition email = null;
    static WebserviceMapping emailMapping = null;
    static WebserviceMapping humanTaskMapping = null;

    static DecidrWebserviceAdapter humanTaskAdapter = null;
    static DecidrWebserviceAdapter emailAdapter = null;

    static String HumanTaskURL = "http://127.0.0.2:8080/axis2/services/HumanTask.HumanTaskSOAP11/";
    static String EmailURL = "http://localhost:8080/axis2/services/Email.EmailSOAP11/";

    /**
     * Read HumanTask WSDL and Email WSDL and retrieve generated mapping
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        humanTask = WSDLFactory.getHumanTaskDefintion();
        email = WSDLFactory.getEmailDefinition();
        emailMapping = MappingFactory.getEmailMapping();
        humanTaskMapping = MappingFactory.getHumanTaskMapping();

        humanTaskAdapter = new DecidrWebserviceAdapter(humanTaskMapping,
                humanTask);
        emailAdapter = new DecidrWebserviceAdapter(emailMapping, email);
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter#getName()}
     * .
     */
    @Test
    public void testGetName() {
        assertEquals(humanTaskAdapter.getName(), humanTask.getQName());
        assertEquals(emailAdapter.getName(), email.getQName());
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter#getPartnerLink()}
     * .
     */
    @Test
    public void testGetPartnerLink() {
        assertEquals(humanTaskAdapter.getPartnerLink().getName(), humanTask
                .getQName().getLocalPart()
                + "PL");
        assertEquals(emailAdapter.getPartnerLink().getName(), email.getQName()
                .getLocalPart()
                + "PL");
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter#getPartnerLinkType()}
     * .
     */
    @Test
    public void testGetPartnerLinkType() {
        assertEquals(humanTaskAdapter.getPartnerLinkType().getName(), humanTask
                .getQName().getLocalPart()
                + "PLT");
        assertEquals(emailAdapter.getPartnerLinkType().getName(), email
                .getQName().getLocalPart()
                + "PLT");
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter#getTargetNamespace()}
     * .
     */
    @Test
    public void testGetTargetNamespace() {
        assertEquals(humanTaskAdapter.getTargetNamespace(), humanTask
                .getTargetNamespace());
        assertEquals(emailAdapter.getTargetNamespace(), email
                .getTargetNamespace());
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter#getLocation()}
     * .
     */
    @Test
    public void testGetLocation() {
        assertEquals(humanTaskAdapter.getLocation(), HumanTaskURL);
        assertEquals(emailAdapter.getLocation(), EmailURL);
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter#getPortType()}
     * .
     */
    @Test
    public void testGetPortType() {
        assertNotNull(humanTaskAdapter.getPortType());
        assertNotNull(emailAdapter.getPortType());
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter#getOpertation()}
     * .
     */
    @Test
    public void testGetOpertation() {
        assertNotNull(humanTaskAdapter.getOpertation());
        assertNotNull(emailAdapter.getOpertation());
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter#getDefinition()}
     * .
     */
    @Test
    public void testGetDefinition() {
        assertNotNull(humanTaskAdapter.getDefinition());
        assertNotNull(emailAdapter.getDefinition());
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter#getInputMessageType()}
     * .
     */
    @Test
    public void testGetInputMessageType() {
        assertNotNull(humanTaskAdapter.getInputMessageType());
        assertNotNull(emailAdapter.getInputMessageType());
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter#getOutputMessageType()}
     * .
     */
    @Test
    public void testGetOutputMessageType() {
        assertNotNull(humanTaskAdapter.getOutputMessageType());
        assertNotNull(emailAdapter.getOutputMessageType());
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter#getService()}
     * .
     */
    @Test
    public void testGetService() {
        assertNotNull(humanTaskAdapter.getService());
        assertNotNull(emailAdapter.getService());
    }

}
