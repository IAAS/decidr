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

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.namespace.QName;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.workflowmodel.bpel.Process;
import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.dwdl.transformation.Constants;
import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2BPEL;
import de.decidr.model.workflowmodel.factories.DWDLFactory;
import de.decidr.model.workflowmodel.factories.DecidrWebserviceAdapterFactory;
import de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter;

/**
 * This JUnit testcase tests the correct transformation of an DWDL
 * 
 * @author Modood Alvi
 */
public class DWDL2BPELTest {

    static DWDL2BPEL translater = null;
    static Workflow dwdl = null;
    static String tenant = null;
    static DecidrWebserviceAdapter humanTask = null;
    static DecidrWebserviceAdapter email = null;
    static Map<String, DecidrWebserviceAdapter> adapters;

    /**
     * Initialize all relevant input objects
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        adapters = new HashMap<String, DecidrWebserviceAdapter>();
        translater = new DWDL2BPEL();
        dwdl = DWDLFactory.getDWDLWorkflow();
        tenant = "Hugo";
        humanTask = DecidrWebserviceAdapterFactory
                .getHumanTaskWebserviceAdapter();
        email = DecidrWebserviceAdapterFactory.getEmailWebserviceAdapter();
        adapters.put("Decidr-HumanTask", humanTask);
        adapters.put("Decidr-Email", email);
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.dwdl.transformation.DWDL2BPEL#getBPEL(de.decidr.model.workflowmodel.dwdl.Workflow, java.util.Map)}
     * .
     */
    @Test
    public void testGetBPEL() {
        Process process = null;
        try {
            process = translater.getBPEL(dwdl, adapters);
            assertNotNull(process);
            JAXBContext cntx = JAXBContext.newInstance(Process.class);
            Marshaller m = cntx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            JAXBElement<Process> element = new JAXBElement<Process>(new QName(
                    Constants.BPEL_NAMESPACE, "process"), Process.class,
                    process);
            m.marshal(element, System.out);
        } catch (PropertyException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

}
