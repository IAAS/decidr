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

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import org.apache.log4j.Logger;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.commands.system.AddServerCommand;
import de.decidr.model.commands.system.LockServerCommand;
import de.decidr.model.commands.system.RemoveServerCommand;
import de.decidr.model.commands.system.UpdateServerLoadCommand;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.notifications.NotificationEvents;
import de.decidr.model.permissions.ODERole;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

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
            Holder<Integer> averagePeriod,
            Holder<XMLGregorianCalendar> configChanged) {
        log.trace("Entering " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".getConfig()");
        log.debug("fetching update interval from model");
        // RR actually do this
        updateInterval.value = 60;
        // RR actually do this
        averagePeriod.value = 300;
        log.debug("fetching timestamp of last config change from model");
        try {
            // RR actually do this
            configChanged.value = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(2009, 1, 1, 0, 0, 0, 0, 120);
        } catch (DatatypeConfigurationException e) {
            // RR remove when logic is there
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
    public void registerODE(Holder<Boolean> poolInstance, Holder<Long> odeID)
            throws TransactionException {
        log.trace("Entering " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".registerODE()");
        // RR find out amount of pool instances needed
        int neededInstances = 0;
        // RR find out amount of pool instances
        int presentInstances = 0;
        if (neededInstances > presentInstances) {
            poolInstance.value = true;
        } else {
            poolInstance.value = false;
        }

        // RR add location?
        AddServerCommand cmd = new AddServerCommand(ODERole.getInstance(),
                ServerTypeEnum.Ode, null, (byte) 0, false, !poolInstance.value);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        odeID.value = cmd.getNewServer().getId();
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
    // RR server needs to know when it was last updated
    // RR step through all servers and remove ones that don't update
    @Override
    public void updateStats(int wfInstances, int wfModels, int avgLoad,
            long odeID, Holder<XMLGregorianCalendar> configVersion,
            Holder<Boolean> run) throws TransactionException {
        log.trace("Entering " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".updateStats()");
        // RR get from config
        int maxSysLoad = 100;
        // RR get from config
        int minHighSysLoad = 70;
        // RR get from config
        int minSysLoad = 0;
        // RR get from config
        int minInstances = 0;
        // RR get from config
        int maxInstances = 5;
        // RR get from config
        int minUnlockedServers = 1;
        // RR get from config
        boolean isLocked = false;
        List<TransactionalCommand> commands = new ArrayList<TransactionalCommand>(
                5);
        if ((isLocked && ((minInstances > wfInstances) || (avgLoad < minHighSysLoad)))
                || (!isLocked && ((maxInstances < wfInstances) || (avgLoad > maxSysLoad)))) {
            commands.add(new LockServerCommand(ODERole.getInstance(), odeID,
                    !isLocked));
        }
        if (minSysLoad > avgLoad) {
            run.value = false;
        } else {
            run.value = true;
        }

        // RR get from config
        int unlockedServers = 1;
        if (unlockedServers < minUnlockedServers) {
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
        }

        commands.add(new UpdateServerLoadCommand(ODERole.getInstance(), null,
                (byte) avgLoad));
        HibernateTransactionCoordinator.getInstance().runTransaction(commands);

        getConfig(new Holder<Integer>(), new Holder<Integer>(), configVersion);
        log.trace("Leaving " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".updateStats()");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.odemonitor.service.ODEMonitorService#unregisterODE(java.lang
     * .String)
     */
    @Override
    public void unregisterODE(long odeID) throws TransactionException {
        log.trace("Entering " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".unregisterODE(String)");
        RemoveServerCommand cmd = new RemoveServerCommand(
                ODERole.getInstance(), odeID);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        log.trace("Leaving " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".unregisterODE(String)");
    }
}
