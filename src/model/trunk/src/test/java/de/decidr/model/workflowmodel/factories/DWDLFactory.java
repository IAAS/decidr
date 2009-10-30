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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import de.decidr.model.workflowmodel.dwdl.Workflow;

/**
 * This class returns the SampleProcess DWDL workflow
 * 
 * @author Modood Alvi
 */
public class DWDLFactory {

    static String dwdlName = "/dwdl/sampleProcess.xml";

    public static byte[] getDWDLWorkflowByteArray() throws IOException {
        InputStream in = WSDLFactory.class.getResourceAsStream(dwdlName);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] data = new byte[in.available()];
        out.write(data, 0, in.available());
        in.close();
        out.close();
        return out.toByteArray();
    }

    public static Workflow getDWDLWorkflow() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Workflow.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        JAXBElement<Workflow> dwdl = unmarshaller.unmarshal(new StreamSource(
                WSDLFactory.class.getResourceAsStream(dwdlName)),
                Workflow.class);
        return dwdl.getValue();
    }

}
