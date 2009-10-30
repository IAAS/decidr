package de.decidr.model.workflowmodel.deployment;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.decidr.model.entities.KnownWebService;

public class Deployment {

    @Before
    public void setUp() throws Exception {
        KnownWebService humanTask = new KnownWebService();
        humanTask.setWsdl(wsdl);
    }

    @Test
    public void testDeploy() {
        fail("Not yet implemented");
    }

    @Test
    public void testUndeploy() {
        fail("Not yet implemented");
    }

}
