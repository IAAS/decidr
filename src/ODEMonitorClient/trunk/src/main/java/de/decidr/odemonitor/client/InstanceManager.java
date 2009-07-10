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

/**
 * An <code>{@link InstanceManager}</code> manages an ODE instance. It mainly
 * starts, stops, locks and unlocks it.<br>
 * XXX: locking done here or in ode server?
 * 
 * @author Reinhold
 */
public interface InstanceManager {

    /**
     * Triggers the starting of a new ODE instance to be managed by this ODE
     * instance manager.
     * 
     * @return - <code>true</code>, if the starting of the instance was
     *         successful or was successfully delegated,<br>
     *         - <code>false</code>, if not
     */
    public boolean startInstance();

    /**
     * Shuts the managed instance down.
     * 
     * 
     * @return - <code>true</code>, if the instance was successfully shut down,<br>
     *         - <code>false</code>, if not
     */
    public boolean stopInstance();

    /**
     * Un-/locks the monitored ODE instance so that instance starting will be
     * suspended/restarted.<br>
     * XXX: locking done here or in ode server?
     * 
     * @param locked
     */
    public void lockInstance(boolean locked);

}