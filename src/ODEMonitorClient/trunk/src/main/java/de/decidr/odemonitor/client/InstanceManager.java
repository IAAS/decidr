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
 * starts and stops it.<br>
 * 
 * @author Reinhold
 */
public interface InstanceManager {

    /**
     * Checks whether the managed ODE instance is running.
     * 
     * @return - <code>true</code>, if the managed instance is running<br>
     *         - <code>false</code>, if it isn't.
     */
    public boolean isRunning();

    /**
     * Triggers the starting of a new ODE instance to be managed by this ODE
     * instance manager if there is no ODE instance running locally.
     * 
     * @return - <code>true</code>, if the starting of the instance was
     *         successful or was successfully delegated,<br>
     *         - <code>false</code>, if not
     */
    public boolean startInstance();

    /**
     * Shuts the managed instance down if it is running.
     * 
     * 
     * @return - <code>true</code>, if the instance was successfully shut down,<br>
     *         - <code>false</code>, if not
     */
    public boolean stopInstance();
}