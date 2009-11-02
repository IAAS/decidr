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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import de.decidr.model.entities.Activity;
import de.decidr.model.entities.KnownWebService;

/**
 * This class returns a list with HumanTask web service and EmailWS web service
 * 
 * @author Modood Alvi
 */
public class KnownWebserviceFactory {

    public static List<KnownWebService> getKnownWebservice()
            throws IOException, JAXBException {
        List<KnownWebService> knownWebServices = new ArrayList<KnownWebService>();
        KnownWebService humanTaskWS = new KnownWebService();
        humanTaskWS.setWsdl(WSDLFactory.getHumanTaskDefinitionByteArray());
        Set<Activity> htasactivities = new HashSet<Activity>();
        Activity htactiviy = new Activity();
        htactiviy.setMapping(MappingFactory.getEmailMappingBytes());
        htasactivities.add(htactiviy);
        humanTaskWS.setActivities(htasactivities);
        KnownWebService emailWS = new KnownWebService();
        emailWS.setWsdl(WSDLFactory.getEmailDefinitionByteArray());
        Set<Activity> emailactivities = new HashSet<Activity>();
        Activity eactivity = new Activity();
        eactivity.setMapping(MappingFactory.getEmailMappingBytes());
        emailactivities.add(eactivity);
        emailWS.setActivities(emailactivities);
        knownWebServices.add(humanTaskWS);
        knownWebServices.add(emailWS);
        return knownWebServices;
    }

}
