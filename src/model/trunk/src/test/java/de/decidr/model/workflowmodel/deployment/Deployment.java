package de.decidr.model.workflowmodel.deployment;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.decidr.model.entities.KnownWebService;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.workflowmodel.factories.WSDLFactory;

public class Deployment {
    
    List<KnownWebService> knownWebservices;
    DeploymentStrategy strategy;
    List<ServerLoadView> serverStatistics;

    @Before
    public void setUp() throws Exception {
        knownWebservices = new ArrayList<KnownWebService>();
        strategy = new StandardDeploymentStrategy();
        serverStatistics = new ArrayList<ServerLoadView>();
        KnownWebService humanTaskWS= new KnownWebService();
        humanTaskWS.setWsdl(WSDLFactory.getHumanTaskDefinitionByteArray());
        KnownWebService emailWS = new KnownWebService();
        emailWS.setWsdl(WSDLFactory.getEmailDefinitionByteArray());
        knownWebservices.add(humanTaskWS);
        knownWebservices.add(emailWS);
    }

    @Test
    public void testDeploy() {
        Deployer deployer = new DeployerImpl();
        deployer.deploy(dwdl, knownWebservices, "Hugo", serverStatistics, strategy);
        fail("Not yet implemented");
    }

    @Test
    public void testUndeploy() {
        fail("Not yet implemented");
    }

}
