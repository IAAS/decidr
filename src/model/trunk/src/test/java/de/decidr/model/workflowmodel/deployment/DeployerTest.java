package de.decidr.model.workflowmodel.deployment;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import javax.wsdl.WSDLException;
import javax.xml.bind.JAXBException;

import org.junit.BeforeClass;
import org.junit.Test;

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

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        deployer = new DeployerImpl();
        knownWebservices = KnownWebserviceFactory.getKnownWebservice();
        strategy = new StandardDeploymentStrategy();
        serverStatistics = ServerLoadViewFactory.getServerStatistics();
        dwdl = DWDLFactory.getDWDLWorkflowByteArray();
        tenantName = "Hugo";
    }

    @Test
    public void testDeploy() throws DWDLValidationException, ODESelectorException, IOException, JAXBException, WSDLException {
        

            DeploymentResult result = deployer.deploy(dwdl, knownWebservices,
                    tenantName, serverStatistics, strategy);
            assertNotNull(result);
    }

    @Test
    public void testUndeploy() {
        fail("Not yet implemented");
    }

}
