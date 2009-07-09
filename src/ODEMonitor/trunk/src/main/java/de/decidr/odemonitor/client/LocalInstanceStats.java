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
import org.apache.ode.bpel.pmapi.InstanceManagement;

import de.decidr.model.logging.DefaultLogger;

/**
 * This class provides an interface to the ODE instance. It retrieves all stats
 * needed by the ODE monitoring web service and ODE monitoring client to perform
 * load balancing.
 * 
 * @author Reinhold
 */
public class LocalInstanceStats {

    Logger log = DefaultLogger.getLogger(LocalInstanceStats.class);

    /**
     * Returns the number of workflow model instances running on this ODE
     * instance.
     * 
     * @return The number of workflow model instances being executed on the ODE
     *         instance.
     */
    public int getNumInstances() {
        log.trace("Entering " + LocalInstanceStats.class.getSimpleName()
                + ".getNumInstances()");
        // RR implement
        log.trace("Leaving " + LocalInstanceStats.class.getSimpleName()
                + ".getNumInstances()");
        return 0;
    }
}
