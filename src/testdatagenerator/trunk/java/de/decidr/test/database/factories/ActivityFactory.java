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

package de.decidr.test.database.factories;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import de.decidr.model.entities.Activity;
import de.decidr.model.entities.KnownWebService;
import de.decidr.test.database.main.ProgressListener;

/**
 * Creates activities and the required known web services.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class ActivityFactory extends EntityFactory {

    /**
     * @param session
     * @param progressListener
     */
    public ActivityFactory(Session session, ProgressListener progressListener) {
        super(session, progressListener);
    }

    /**
     * @param session
     */
    public ActivityFactory(Session session) {
        super(session);
    }

    /**
     * Create the default persisted activity entites and known web services.
     * 
     * @return TODO document
     */
    public List<Activity> createActivites() {
        ArrayList<Activity> result = new ArrayList<Activity>();

        XmlFactory xml = new XmlFactory(session);

        // create known web services
        KnownWebService emailWS = new KnownWebService();
        emailWS.setName("Email");
        session.save(emailWS);

        KnownWebService humanTaskWS = new KnownWebService();
        humanTaskWS.setName("HumanTask");
        session.save(humanTaskWS);

        // create activities that use the known web services
        Activity emailActivity = new Activity();
        emailActivity.setKnownWebService(emailWS);
        emailActivity.setMapping(xml.getActivityMapping("email"));
        emailActivity.setName("DecidR-Email");
        session.save(emailActivity);

        Activity humanTaskActivity = new Activity();
        humanTaskActivity.setKnownWebService(humanTaskWS);
        humanTaskActivity.setMapping(xml.getActivityMapping("humanTask"));
        humanTaskActivity.setName("DecidR-HumanTask");
        session.save(humanTaskActivity);

        fireProgressEvent(2, 2);

        return result;
    }

}
