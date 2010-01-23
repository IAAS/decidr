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
package de.decidr.model.workflowmodel.deployment;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import javax.wsdl.WSDLException;
import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import de.decidr.model.entities.KnownWebService;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.workflowmodel.factories.DWDLFactory;
import de.decidr.model.workflowmodel.factories.KnownWebserviceFactory;
import de.decidr.model.workflowmodel.factories.ServerLoadViewFactory;

public class DeployerTest {

    static Deployer deployer = null;
    static List<KnownWebService> knownWebservices = null;
    static DeploymentStrategy strategy = null;
    static List<ServerLoadView> serverStatistics = null;
    static byte[] dwdl = null;
    static String tenantName = null;
    static String location = "http://129.69.214.90:8080";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        deployer = new DeployerImpl();
        knownWebservices = KnownWebserviceFactory.getKnownWebservice();
        strategy = new StandardDeploymentStrategy();

        ServerLoadViewFactory.setLocation(location);
        serverStatistics = ServerLoadViewFactory.getServerStatistics();
        dwdl = DWDLFactory.getDWDLWorkflowByteArray();
        tenantName = "Hugo";
    }

    @Test
    public void testDeploy() throws DWDLValidationException,
            ODESelectorException, IOException, JAXBException, WSDLException,
            SOAPException, SAXException {

        DeploymentResult result = deployer.deploy(dwdl, knownWebservices,
                tenantName, serverStatistics, strategy);
        assertNotNull(result);
    }

    @Test
    public void testUndeploy() {
        fail("Not yet implemented");
    }
}
