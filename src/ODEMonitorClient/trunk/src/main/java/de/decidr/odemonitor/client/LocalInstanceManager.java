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

import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.notifications.NotificationEvents;
import de.decidr.model.webservices.ODEInstanceClient;

/**
 * Manages a local ODE instance. Needs the system administrator's help to start
 * a new instance.
 * 
 * @author Reinhold
 */
public class LocalInstanceManager implements InstanceManager {

    Logger log = DefaultLogger.getLogger(LocalInstanceManager.class);

    /**
     * Checks if the local ODE instance is running.
     * 
     * @return - <code>true</code>, if it is running<br>
     *         - <code>false</code>, if it isn't.
     */
    @Override
    public boolean isRunning() {
        log.trace("Entering " + LocalInstanceManager.class.getSimpleName()
                + ".isRunning()");
        boolean running = false;
        try {
            // get URLConnection
            URL localOdeUrl = new URL(ODEInstanceClient.LOCAL_ODE_LOCATION);
            URLConnection con = localOdeUrl.openConnection();

            // set connection properties & connect
            con.setDoInput(true);
            con.setUseCaches(false);
            con.connect();

            // see if we got a return code 2xx or 3xx (OK or forward)
            running = con.getHeaderField(0).matches("2\\d\\d|3\\d\\d");

            // close unneeded streams
            con.getInputStream().close();
            con.getOutputStream().close();
        } catch (Exception e) {
            // apparently the local instance isn't accessible
        }
        log.trace("Leaving " + LocalInstanceManager.class.getSimpleName()
                + ".isRunning()");
        return running;
    }

    /**
     * Sends an email to the system administrator to request the addition of a
     * new ODE instance on this machine, unless there is already a running
     * instance.
     * 
     * @return - <code>true</code>, if the email was sent successfully or the
     *         instance is already running,<br>
     *         - <code>false</code>, if not
     */
    @Override
    public boolean startInstance() {
        log.trace("Entering " + LocalInstanceManager.class.getSimpleName()
                + ".startInstance()");
        boolean state = isRunning();
        if (!state) {
            try {
                NotificationEvents.requestNewODEInstance(null);
                state = true;
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                // can't request new instance - leave state as false
            }
        }
        log.trace("Leaving " + LocalInstanceManager.class.getSimpleName()
                + ".startInstance()");
        return state;
    }

    /**
     * Not implemented by this <code>{@link InstanceManager}</code>;
     * 
     * @return <code>false</code>
     */
    @Override
    public boolean stopInstance() {
        log.trace("Entering & leaving "
                + LocalInstanceManager.class.getSimpleName()
                + ".stopInstance()");
        log.info("This InstanceManager cannot stop it's ODE instance!");
        return false;
    }
}
