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

package de.decidr.model.workflowmodel.deployment.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import de.decidr.model.workflowmodel.dwdl.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.decidr.model.workflowmodel.bpel.TProcess;
import de.decidr.model.workflowmodel.dwdl.TWorkflow;
import de.decidr.model.workflowmodel.dwdl.translator.Translator;

/**
 * MA: add comment
 * 
 * @author Modood Alvi
 */
public class DeploymentTest {

    /**
     * MA: add comment
     * 
     * @param args
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) {
        JAXBContext dwdlCntxt;
        JAXBContext bpelCntxt;
        try {
            dwdlCntxt = JAXBContext.newInstance(TWorkflow.class);
            Marshaller dwdlMarshaller = dwdlCntxt.createMarshaller();
            Unmarshaller dwdlUnmarshaller = dwdlCntxt.createUnmarshaller();
            bpelCntxt = JAXBContext.newInstance(TProcess.class);
            Marshaller bpelMarshaller = bpelCntxt.createMarshaller();
            Unmarshaller bpelUnmarshaller = bpelCntxt.createUnmarshaller();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            TWorkflow testWorkflow = getTestWorkflow();
//            TWorkflow dwdlWorkflow = null;
//            JAXBElement<TWorkflow> element = (JAXBElement<TWorkflow>) dwdlUnmarshaller
//                    .unmarshal(new FileInputStream("sampleProcess.xml"));
//            dwdlWorkflow = element.getValue();
//            dwdlMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            dwdlMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//            dwdlMarshaller.marshal(dwdlWorkflow, out);
            dwdlMarshaller.marshal(testWorkflow, out);
            System.out.println(out.toString());
            Translator t = new Translator();
            t.load(out.toByteArray());
            out.reset();
            TProcess process = t.getBPEL();
            bpelMarshaller.marshal(process, out);
            System.out.println(out.toString());  
                        
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Ups");
        }
    }

    /**
     * MA: add comment
     *
     * @return
     */
    private static TWorkflow getTestWorkflow() {
        ObjectFactory factory = new ObjectFactory();
        TWorkflow w = factory.createTWorkflow();
        w.setName("HalloWelt");

        return w;
    }
}    