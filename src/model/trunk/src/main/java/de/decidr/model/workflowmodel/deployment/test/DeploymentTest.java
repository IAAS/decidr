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

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.decidr.model.workflowmodel.dwdl.TWorkflow;

/**
 * TODO: add comment
 * 
 * @author Modood Alvi
 */
public class DeploymentTest {

    /**
     * TODO: add comment
     * 
     * @param args
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) {
        JAXBContext dwdlCntxt;
        try {
            dwdlCntxt = JAXBContext.newInstance(TWorkflow.class);
            Unmarshaller unmarshaller;
            unmarshaller = dwdlCntxt.createUnmarshaller();
            TWorkflow dwdlWorkflow;
            dwdlWorkflow = (TWorkflow) unmarshaller
                    .unmarshal(new FileInputStream("sampleProcess"));
            dwdlWorkflow.toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("filenotfound");
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("jaxbexception");
        }
    }

}
