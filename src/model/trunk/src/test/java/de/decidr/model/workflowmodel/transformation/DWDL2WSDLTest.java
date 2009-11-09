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
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.namespace.QName;

import org.jdom.JDOMException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.wsdl.extensions.PopulatedExtensionRegistry;
import com.ibm.wsdl.xml.WSDLWriterImpl;

import de.decidr.model.workflowmodel.bpel.partnerlinktype.PartnerLinkType;
import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.dwdl.transformation.Constants;
import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2WSDL;
import de.decidr.model.workflowmodel.factories.DWDLFactory;
import de.decidr.model.workflowmodel.webservices.PartnerLinkTypeSerializer;

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
        ExtensionRegistry extensionRegistry = new PopulatedExtensionRegistry();
        PartnerLinkTypeSerializer ser = new PartnerLinkTypeSerializer();
        extensionRegistry.registerSerializer(PartnerLinkType.class, new QName(
                Constants.PARTNERLINKTYPE_NAMESPACE, "partnerLinkType"), ser);
        extensionRegistry.registerDeserializer(PartnerLinkType.class,
                new QName(Constants.PARTNERLINKTYPE_NAMESPACE,
                        "partnerLinkType"), ser);
        extensionRegistry.mapExtensionTypes(PartnerLinkType.class, new QName(
                Constants.PARTNERLINKTYPE_NAMESPACE, "partnerLinkType"),
                PartnerLinkType.class);
        wsdl.setExtensionRegistry(extensionRegistry);
        WSDLWriter writer = new WSDLWriterImpl();
        writer.writeWSDL(wsdl, System.out);
        assertEquals(wsdl.getTargetNamespace(), dwdl.getTargetNamespace());
    }

}
