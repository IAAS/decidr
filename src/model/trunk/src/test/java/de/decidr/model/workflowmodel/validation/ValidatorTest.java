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
package de.decidr.model.workflowmodel.validation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        assertNotNull(problems);
        for (IProblem problem : problems) {
            System.out.println("Error Description: "
                    + problem.getErrorDescription());
            System.out.println("Error Posision: " + problem.getErrorPosition());
        }
        assertTrue(problems.isEmpty());
    }

}
