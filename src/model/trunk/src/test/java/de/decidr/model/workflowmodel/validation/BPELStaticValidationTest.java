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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.ode.bpel.compiler.BpelC;
import org.apache.ode.bpel.compiler.api.CompilationException;
import org.apache.ode.bpel.compiler.api.CompilationMessage;
import org.apache.ode.bpel.compiler.api.CompileListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.logging.DefaultLogger;

/**
 * Testcase for BPEL validation according to <code>StaticCheckSuite.java</code>
 * 
 * @author Modood Alvi
 */
public class BPELStaticValidationTest {

    private static Logger log = DefaultLogger
            .getLogger(BPELStaticValidationTest.class);

    static BpelC compiler = null;
    static List<CompilationMessage> errors = null;
    static String bpelFileName = "/test/HumanTaskProcess.bpel";
    static String wsdlFileName = "/test/HumanTaskProcess.wsdl";
    static File bpelFile = null;

    /**
     * Initialize all relevant input objects
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        errors = new ArrayList<CompilationMessage>();
        compiler = BpelC.newBpelCompiler();
        compiler.setCompileListener(new CompileListener() {

            @Override
            public void onCompilationMessage(CompilationMessage arg0) {
                onMessage(arg0);
            }
        });

        bpelFile = new File(BPELStaticValidationTest.class.getResource(
                bpelFileName).getFile());
        compiler.setProcessWSDL(BPELStaticValidationTest.class.getResource(
                wsdlFileName).toURI());
    }

    @Test
    public void runTest() throws Exception {
        try {
            compiler.setDryRun(true);
            compiler.compile(bpelFile);
        } catch (CompilationException ce) {
            errors.add(ce.getCompilationMessage());
        }

        assertTrue(errors.size() == 0);
        log.debug(errors.size() + " errors found by compiler");
    }

    public static void onMessage(CompilationMessage compilationMessage) {
        errors.add(compilationMessage);
        log.debug(compilationMessage.messageText);
    }

    @AfterClass
    public static void afterTest() throws Exception {
        // TODO fill with content or delete
    }

}
