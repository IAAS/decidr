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

import javax.xml.bind.JAXBException;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.workflowmodel.dd.TDeployment;
import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2BPEL;
import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2DD;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.factories.DWDLFactory;
import de.decidr.model.workflowmodel.factories.DecidrWebserviceAdapterFactory;
import de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter;

public class DWDL2DDTest {

    static DWDL2DD translater = null;
    static Workflow dwdl = null;
    static String tenant = null;
    static DecidrWebserviceAdapter humanTask = null;
    static DecidrWebserviceAdapter email = null;
    static Map<String, DecidrWebserviceAdapter> adapters = null;
    static de.decidr.model.workflowmodel.bpel.Process bpel = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        adapters = new HashMap<String, DecidrWebserviceAdapter>();
        translater = new DWDL2DD();
        dwdl = DWDLFactory.getDWDLWorkflow();
        tenant = "Hugo";
        humanTask = DecidrWebserviceAdapterFactory
                .getHumanTaskWebserviceAdapter();
        email = DecidrWebserviceAdapterFactory.getEmailWebserviceAdapter();
        adapters.put("Decidr-HumanTask", humanTask);
        adapters.put("Decidr-Email", email);
        DWDL2BPEL bpelConverter = new DWDL2BPEL();
        bpel = bpelConverter.getBPEL(dwdl, adapters);

    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.dwdl.transformation.DWDL2DD#getDD(de.decidr.model.workflowmodel.bpel.Process, java.util.Map)}
     * .
     * 
     * @throws JAXBException
     */
    @Test
    public void testGetDD() throws JAXBException {

        TDeployment dd = translater.getDD(bpel, adapters);
        assertNotNull(dd);
        String result = new String(TransformUtil.ddToBytes(dd));
        System.out.println(result);
    }

}
