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

package de.decidr.odemonitor.client;

import org.apache.log4j.Logger;

import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.notifications.NotificationEvents;

/**
 * Manages a local ODE instance. Needs the system administrator's help to start
 * a new instance.
 * 
 * @author Reinhold
 */
public class LocalInstanceManager implements InstanceManager {

    Logger log = DefaultLogger.getLogger(LocalInstanceManager.class);

    /**
     * Sends an email to the system administrator to request the addition of a
     * new ODE instance on this machine, unless there is already a running
     * instance.
     * 
     * @return - <code>true</code>, if the email was sent successfully,<br>
     *         - <code>false</code>, if not
     */
    @Override
    public boolean startInstance() {
        log.trace("Entering " + LocalInstanceManager.class.getSimpleName()
                + ".startInstance()");
        boolean state = isRunning();
        if (!state) {
            // RR somehow get local machine identifier
            NotificationEvents.requestNewODEInstance("");
        }
        log.trace("Leaving " + LocalInstanceManager.class.getSimpleName()
                + ".startInstance()");
        return state;
    }

    /**
     * RR: add comment
     * 
     * @return
     */
    @Override
    public boolean stopInstance() {
        log.trace("Entering " + LocalInstanceManager.class.getSimpleName()
                + ".stopInstance()");
        boolean state = !isRunning();
        // MA kann ich aus java die ODE-Instanz herunterfahren
        // RR do what? send email that instance unneeded?
        log.trace("Leaving " + LocalInstanceManager.class.getSimpleName()
                + ".stopInstance()");
        return state;
    }

    /**
     * RR: add comment
     * 
     * @return
     */
    private boolean isRunning() {
        log.trace("Entering " + LocalInstanceManager.class.getSimpleName()
                + ".isRunning()");
        boolean running = false;
        // RR check if local instance is running
        log.trace("Leaving " + LocalInstanceManager.class.getSimpleName()
                + ".isRunning()");
        return running;
    }

}
