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

import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.apache.ode.axis2.service.ServiceClientUtil;

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
    public static final String ODE_LOCATION = "http://localhost:8080/ode/processes/";

    /**
     * Returns the number of workflow model instances running on this ODE
     * instance.
     * 
     * @return The number of workflow model instances being executed on the ODE
     *         instance. <code>-1</code> means an error occurred and the amount
     *         of running instances could not be retrieved.
     */
    public int getNumInstances() {
        log.trace("Entering " + LocalInstanceStats.class.getSimpleName()
                + ".getNumInstances()");
        int numInst = -1;

        try {
            ServiceClientUtil _client = new ServiceClientUtil();
            OMElement root = _client.buildMessage("listAllInstances",
                    new String[] {}, new String[] {});
            OMElement result = _client.send(root,
                    "http://localhost:8080/ode/processes/InstanceManagement");

            numInst = 0;
            Iterator<?> list = result.getFirstChildWithName(
                    new QName("instance-info-list")).getChildElements();
            while (list.hasNext()) {
                list.next();
                numInst++;
            }
        } catch (AxisFault e) {
            log.error("Couldn't communicate with "
                    + "ODE process management service", e);
        }

        log.trace("Leaving " + LocalInstanceStats.class.getSimpleName()
                + ".getNumInstances()");
        return numInst;
    }

    /**
     * Returns the number of workflow models deployed on this ODE instance.
     * 
     * @return The number of workflow models deployed on the ODE instance.
     *         <code>-1</code> means an error occurred and the amount of
     *         deployed models could not be retrieved.
     */
    public int getNumModels() {
        log.trace("Entering " + LocalInstanceStats.class.getSimpleName()
                + ".getNumInstances()");
        int numModels = -1;

        try {
            ServiceClientUtil _client = new ServiceClientUtil();
            OMElement root = _client.buildMessage("listAllProcesses",
                    new String[] {}, new String[] {});
            OMElement result = _client.send(root,
                    "http://localhost:8080/ode/processes/ProcessManagement");

            numModels = 0;
            Iterator<?> list = result.getFirstChildWithName(
                    new QName("process-info-list")).getChildElements();
            while (list.hasNext()) {
                list.next();
                numModels++;
            }
        } catch (AxisFault e) {
            log.error("Couldn't communicate with "
                    + "ODE process management service", e);
        }

        log.trace("Leaving " + LocalInstanceStats.class.getSimpleName()
                + ".getNumInstances()");
        return numModels;
    }
}
