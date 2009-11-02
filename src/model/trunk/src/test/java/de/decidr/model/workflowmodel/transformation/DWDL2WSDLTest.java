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

package de.decidr.model.workflowmodel.transformation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;

/**
 * DWDL2WSDL Test
 * 
 * @author Modood Alvi
 */
public class DWDL2WSDLTest {

    public static void main(String[] args) throws IOException, JAXBException {
        System.out.println(new File(".").getCanonicalPath());
        InputStream in = DWDL2WSDLTest.class
                .getResourceAsStream("/dwdl/sampleProcess.xml");
        byte[] bytesDWDL = new byte[in.available()];
        in.read(bytesDWDL, 0, in.available());
        Workflow dwdl = TransformUtil.bytesToWorkflow(bytesDWDL);
        JAXBContext jc = JAXBContext.newInstance(Workflow.class);
        // Create marshaller
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        // Marshal object into file.
        m.marshal(dwdl, System.out);
    }

}
