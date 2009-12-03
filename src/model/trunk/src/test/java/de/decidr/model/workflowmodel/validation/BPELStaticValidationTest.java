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

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.apache.ode.bpel.compiler.BpelC;
import org.apache.ode.bpel.compiler.api.CompilationException;
import org.apache.ode.bpel.compiler.api.CompilationMessage;
import org.apache.ode.bpel.compiler.api.CompileListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2BPEL;
import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2WSDL;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.factories.DWDLFactory;
import de.decidr.model.workflowmodel.factories.DecidrWebserviceAdapterFactory;
import de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter;

/**
 * Testcase for BPEL validation according to <code>StaticCheckSuite.java</code>
 * 
 * @author Modood Alvi
 */
public class BPELStaticValidationTest {

    static DWDL2BPEL bpelConverter = null;
    static DWDL2WSDL wsdlConverter = null;
    static Workflow dwdl = null;
    static String tenant = null;
    static DecidrWebserviceAdapter humanTask = null;
    static DecidrWebserviceAdapter email = null;
    static Map<String, DecidrWebserviceAdapter> adapters;
    static String location = null;

    static BpelC compiler;
    static List<CompilationMessage> errors = new ArrayList<CompilationMessage>();
    static File bpelFile;
    static File wsdlFile;

    /**
     * Initialize all relevant input objects
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        adapters = new HashMap<String, DecidrWebserviceAdapter>();
        bpelConverter = new DWDL2BPEL();
        wsdlConverter = new DWDL2WSDL();
        dwdl = DWDLFactory.getDWDLWorkflow();
        tenant = "Hugo";
        location = "http://ec2-174-129-24-232.compute-1.amazonaws.com:8080";
        humanTask = DecidrWebserviceAdapterFactory
                .getHumanTaskWebserviceAdapter();
        email = DecidrWebserviceAdapterFactory.getEmailWebserviceAdapter();
        adapters.put("Decidr-HumanTask", humanTask);
        adapters.put("Decidr-Email", email);

        compiler = BpelC.newBpelCompiler();
        compiler.setCompileListener(new CompileListener() {

            @Override
            public void onCompilationMessage(CompilationMessage arg0) {
                this.onCompilationMessage(arg0);
            }
        });

        byte[] bpeldata = TransformUtil.bpelToBytes(bpelConverter.getBPEL(dwdl,
                adapters));
        byte[] wsdldata = TransformUtil.definitionToBytes(wsdlConverter
                .getWSDL(dwdl, location, tenant));

        bpelFile = new File(tenant + ".bpel");
        FileOutputStream fos = new FileOutputStream(bpelFile);
        fos.write(bpeldata);
        fos.flush();
        fos.close();
        fos = null;
        wsdlFile = new File(tenant + ".wsdl");
        fos = new FileOutputStream(wsdlFile);
        fos.write(wsdldata);
        compiler.setProcessWSDL(BPELStaticValidationTest.class.getResource(
                wsdlFile.getAbsolutePath()).toURI());
    }

    @Test
    public void runTest() throws Exception {
        try {
            compiler.compile(bpelFile);
        } catch (CompilationException ce) {
            errors.add(ce.getCompilationMessage());
        }

        assertTrue(errors.size() == 0);
    }

    public static void onCompilationMessage(
            CompilationMessage compilationMessage) {
        errors.add(compilationMessage);
    }

    @AfterClass
    public void afterTest() throws Exception {
        boolean deleted = bpelFile.delete();
        assertTrue(deleted);
    }

}
