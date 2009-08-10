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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import de.decidr.model.workflowmodel.dwdl.translator.DWDL2BPEL;
import de.decidr.model.workflowmodel.dwdl.translator.Translator;

/**
 * Simple deployment test class
 * 
 * @author Modood Alvi
 */
public class DeploymentTest {

    /**
     * This class tests the translator component {@link DWDL2BPEL}
     * 
     * @param args
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) {
        try {
            Translator t = new Translator();
            // MA doit pleeeaaaaassseee
            
            System.out.println("Done");
            de.decidr.model.workflowmodel.bpel.Process p = t.getBPEL();
            JAXBContext cntxt = JAXBContext.newInstance(Process.class);
            Marshaller marshaller = cntxt.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            marshaller.marshal(p, System.out);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ups");
        }
    }
    
}