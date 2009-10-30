package de.decidr.model.workflowmodel.validation;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.workflowmodel.dwdl.validation.IProblem;
import de.decidr.model.workflowmodel.dwdl.validation.Validator;
import de.decidr.model.workflowmodel.factories.DWDLFactory;

public class ValidatorTest {

    static byte[] dwdlWorkflow;
    static Validator validator;
    static List<IProblem> problems;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        dwdlWorkflow = DWDLFactory.getDWDLWorkflowByteArray();
        validator = new Validator();
    }

    @Test
    public void testValidateByteArray() {
        problems = validator.validate(dwdlWorkflow);
        assertTrue(problems.isEmpty());
    }

}
