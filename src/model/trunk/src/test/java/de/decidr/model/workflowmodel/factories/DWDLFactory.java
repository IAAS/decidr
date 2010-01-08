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

package de.decidr.model.workflowmodel.factories;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;

/**
 * This class returns the SampleProcess DWDL workflow
 * 
 * @author Modood Alvi
 */
public class DWDLFactory {

    static String dwdlSimpleName = "/dwdl/verySimpleProcess.xml";
    static String dwdlSampleName = "/test/HumanTaskProcess.xml";

    public static byte[] getDWDLWorkflowByteArray() throws IOException {
        InputStream in = DWDLFactory.class.getResourceAsStream(dwdlSampleName);
        byte[] bytesDWDL = new byte[in.available()];
        in.read(bytesDWDL, 0, in.available());
        in.close();
        return bytesDWDL;
    }

    public static Workflow getDWDLWorkflow() throws JAXBException, IOException,
            SAXException {
        return TransformUtil.bytesToWorkflow(getDWDLWorkflowByteArray());
    }
}
