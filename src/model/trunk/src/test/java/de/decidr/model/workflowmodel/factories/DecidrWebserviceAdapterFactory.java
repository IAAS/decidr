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

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;

import de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter;
import de.decidr.model.workflowmodel.webservices.WebserviceMapping;

/**
 * Creates HumanTask and Email web service adapters
 * 
 * @author Modood Alvi
 */
public class DecidrWebserviceAdapterFactory {

    static Definition humanTask = null;
    static Definition email = null;
    static WebserviceMapping emailMapping = null;
    static WebserviceMapping humanTaskMapping = null;

    static DecidrWebserviceAdapter humanTaskAdapter = null;
    static DecidrWebserviceAdapter emailAdapter = null;

    public static DecidrWebserviceAdapter getHumanTaskWebserviceAdapter()
            throws WSDLException, IOException {
        humanTask = WSDLFactory.getHumanTaskDefintion();
        humanTaskMapping = MappingFactory.getHumanTaskMapping();
        humanTaskAdapter = new DecidrWebserviceAdapter(humanTaskMapping,
                humanTask);

        return humanTaskAdapter;
    }

    public static DecidrWebserviceAdapter getEmailWebserviceAdapter()
            throws WSDLException, IOException {
        email = WSDLFactory.getEmailDefinition();
        emailMapping = MappingFactory.getEmailMapping();
        emailAdapter = new DecidrWebserviceAdapter(emailMapping, email);

        return emailAdapter;

    }

}
