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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.dwdl.transformation.Constants;
import de.decidr.model.workflowmodel.dwdl.transformation.DWDL2WSC;
import de.decidr.model.workflowmodel.factories.DWDLFactory;
import de.decidr.model.workflowmodel.wsc.TConfiguration;

/**
 * This class tests the correct translation of a DWDL to a WSC object
 * 
 * @author Modood Alvi
 */
public class DWDL2WSCTest {

    static DWDL2WSC translater = null;
    static Workflow dwdl = null;

    /**
     * Initialize all relevant data
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        translater = new DWDL2WSC();
        dwdl = DWDLFactory.getDWDLWorkflow();
    }

    /**
     * Test method for
     * {@link de.decidr.model.workflowmodel.dwdl.transformation.DWDL2WSC#getStartConfiguration(de.decidr.model.workflowmodel.dwdl.Workflow)}
     * .
     * 
     * @throws JAXBException
     */
    @Test
    public void testGetStartConfiguration() throws JAXBException {
        TConfiguration config = translater.getStartConfiguration(dwdl);
        if (config != null) {
            Marshaller m = JAXBContext.newInstance(TConfiguration.class)
                    .createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(new JAXBElement<TConfiguration>(new QName(
                    Constants.CONFIGURATION_NAMESPACE, "configurations"),
                    TConfiguration.class, config), System.out);
        }
        assertNotNull(config);
    }

}
