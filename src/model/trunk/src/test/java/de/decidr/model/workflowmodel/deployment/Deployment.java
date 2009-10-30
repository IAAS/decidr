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

public class Deployment {
    
    static List<KnownWebService> knownWebservices;
    static DeploymentStrategy strategy;
    static List<ServerLoadView> serverStatistics;
    static byte[] dwdl;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        knownWebservices = KnownWebserviceFactory.getKnownWebservice();
        strategy = new StandardDeploymentStrategy();
        serverStatistics = ServerLoadViewFactory.getServerStatistics();
        dwdl = DWDLFactory.getDWDLWorkflowByteArray();
    }

    @Test
    public void testDeploy() {
        Deployer deployer = new DeployerImpl();
        try {
            deployer.deploy(dwdl, knownWebservices, "Hugo", serverStatistics, strategy);
        } catch (DWDLValidationException e) {
            // Upsa
            e.printStackTrace();
        } catch (ODESelectorException e) {
            // Upsa
            e.printStackTrace();
        } catch (IOException e) {
            // Upsa
            e.printStackTrace();
        } catch (JAXBException e) {
            // Upsa
            e.printStackTrace();
        } catch (WSDLException e) {
            // Upsa
            e.printStackTrace();
        }
        
    }

    @Test
    public void testUndeploy() {
        fail("Not yet implemented");
    }

}
