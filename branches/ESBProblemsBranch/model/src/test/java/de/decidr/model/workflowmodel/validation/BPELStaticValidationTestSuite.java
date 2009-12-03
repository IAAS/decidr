package de.decidr.model.workflowmodel.validation;

import junit.framework.Test;
import junit.framework.TestSuite;

public class BPELStaticValidationTestSuite extends TestSuite{

    public static Test suite() throws Exception {
        TestSuite suite = new BPELStaticValidationTestSuite();
        suite.addTest(new BPELStaticValidationTest("NoRootActivity"));
        suite.addTest(new BPELStaticValidationTest("PortTypeMismatch"));
        suite.addTest(new BPELStaticValidationTest("UndeclaredPropertyAlias"));
        suite.addTest(new BPELStaticValidationTest("UnknownBpelFunction"));
        suite.addTest(new BPELStaticValidationTest("UndeclaredVariable"));
        suite.addTest(new BPELStaticValidationTest("DuplicateLinkTarget"));
        suite.addTest(new BPELStaticValidationTest("DuplicateLinkSource"));
        suite.addTest(new BPELStaticValidationTest("DuplicateLinkDecl"));
        suite.addTest(new BPELStaticValidationTest("LinkMissingSourceActivity"));
        suite.addTest(new BPELStaticValidationTest("LinkMissingTargetActivity"));
        suite.addTest(new BPELStaticValidationTest("DuplicateVariableDecl"));
        suite.addTest(new BPELStaticValidationTest("UndeclaredExtensionActivity"));
        suite.addTest(new BPELStaticValidationTest("UndeclaredExtensionAssignOperation"));
        suite.addTest(new BPELStaticValidationTest("MissingExtensionActivityElement"));
        suite.addTest(new BPELStaticValidationTest("MissingExtensionAssignOperationElement"));
        suite.addTest(new BPELStaticValidationTest("ExtensibleAssignNotSupported"));
        // We simply can't test the next one without using the BOM; both the parser
        // and schema validation would rule it out.
        //suite.addTest(new StaticCheckTest("CompensateNAtoContext"));
        return suite;
      }

}
