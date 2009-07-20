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

package de.decidr.odemonitor.service;

import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import org.apache.log4j.Logger;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.notifications.NotificationEvents;

/**
 * This implementation of the <code>{@link ODEMonitorService}</code> uses the
 * model to access and store data.
 * 
 * @author Reinhold
 */
@WebService(endpointInterface = "de.decidr.odemonitor.service.ODEMonitorService")
public class ODEMonitorServiceImpl implements ODEMonitorService {

    private static final Logger log = DefaultLogger
            .getLogger(ODEMonitorServiceImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.odemonitor.service.ODEMonitorService#getConfig(javax.xml.ws
     * .Holder, javax.xml.ws.Holder)
     */
    @Override
    public void getConfig(Holder<Integer> updateInterval,
            Holder<XMLGregorianCalendar> configChanged) {
        log.trace("Entering " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".getConfig()");
        log.debug("fetching update interval from model");
        // RR actually do this
        updateInterval.value = 60;
        log.debug("fetching timestamp of last config change from model");
        try {
            // RR actually do this
            configChanged.value = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(2009, 1, 1, 0, 0, 0, 0, 120);
        } catch (DatatypeConfigurationException e) {
            // RR Auto-generated catch block
            e.printStackTrace();
        }

        log.trace("Leaving " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".getConfig()");
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.odemonitor.service.ODEMonitorService#registerODE(boolean)
     */
    @Override
    public void registerODE(Holder<Boolean> poolInstance, Holder<String> odeID) {
        log.trace("Entering " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".registerODE()");
        // RR Auto-generated method stub
        log.trace("Leaving " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".registerODE()");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.odemonitor.service.ODEMonitorService#updateStats(java.math.
     * BigInteger, java.math.BigInteger, java.lang.String, javax.xml.ws.Holder,
     * javax.xml.ws.Holder)
     */
    @Override
    public void updateStats(int wfInstances, int wfModels, int cpuUsage,
            int memUsage, String odeID,
            Holder<XMLGregorianCalendar> configVersion, Holder<Boolean> run) {
        log.trace("Entering " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".updateStats()");
        // RR Auto-generated method stub
        // RR if need new ode:
        for (int i = 3; i > 0; i--) {
            try {
                NotificationEvents.requestNewODEInstance(null);
                break;
            } catch (TransactionException e) {
                if (i > 1) {
                    log.error("Couldn't request new ODE instance", e);
                } else {
                    log.fatal("Need a new ODE instance but can't"
                            + " send request by mail...", e);
                }
            }
        }

        log.trace("Leaving " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".updateStats()");
    }
}
