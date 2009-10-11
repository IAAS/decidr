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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import org.apache.log4j.Logger;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.ODERole;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.commands.system.GetServerCommand;
import de.decidr.model.commands.system.GetServersCommand;
import de.decidr.model.commands.system.LockServerCommand;
import de.decidr.model.commands.system.UpdateServerLoadCommand;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.SystemFacade;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.notifications.NotificationEvents;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

/**
 * This implementation of the <code>{@link ODEMonitorService}</code> uses the
 * model to access and store data.
 * 
 * @author Reinhold
 */
@WebService(endpointInterface = "de.decidr.odemonitor.service.ODEMonitorService")
public class ODEMonitorServiceImpl implements ODEMonitorService {

    private static final Role ODE_ROLE = ODERole.getInstance();
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
        SystemSettings config = DecidrGlobals.getSettings();
        updateInterval.value = config.getMonitorUpdateIntervalSeconds();
        averagePeriod.value = config.getMonitorAveragingPeriodSeconds();
        log.debug("fetching timestamp of last config change from model");
        try {
            GregorianCalendar date = new GregorianCalendar();
            date.setTime(config.getModifiedDate());
            configChanged.value = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(date);
        } catch (DatatypeConfigurationException e) {
            log.error("Couldn't make new XMLGregorianCalendar"
                    + " => serious misconfiguration", e);
            configChanged.value = null;
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
    public void registerODE(Holder<Boolean> poolInstance, long odeID)
            throws TransactionException, IllegalArgumentException {
        log.trace("Entering " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".registerODE()");
        GetServerCommand cmd = new GetServerCommand(ODE_ROLE, odeID);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);

        if (cmd.getServer() == null) {
            throw new IllegalArgumentException(
                    "Couldn't find the ODE server with the specified ID!");
        }

        poolInstance.value = !cmd.getServer().isDynamicallyAdded();
        log.trace("Leaving " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".registerODE()");
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
        log.debug("attempting to remove the server corresponding"
                + " to the passed ODE ID...");
        new SystemFacade(ODERole.getInstance()).removeServer(odeID);
        log.debug("...success");
        log.trace("Leaving " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".unregisterODE(String)");
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
    public void updateStats(int wfInstances, int wfModels, int avgLoad,
            long odeID, Holder<XMLGregorianCalendar> configVersion,
            Holder<Boolean> run) throws TransactionException {
        log.trace("Entering " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".updateStats()");
        SystemSettings config = DecidrGlobals.getSettings();
        List<TransactionalCommand> commands = new ArrayList<TransactionalCommand>(
                5);
        GetServersCommand cmd1 = new GetServersCommand(ODE_ROLE,
                ServerTypeEnum.Ode);

        commands.add(cmd1);
        HibernateTransactionCoordinator.getInstance().runTransaction(commands);

        int unlockedServers = 0;
        Date deadTime = new Date(DecidrGlobals.getTime().getTimeInMillis()
                - (config.getMonitorUpdateIntervalSeconds() * 10 * 1000));
        Server server = null;
        for (Server serv : cmd1.getResult()) {
            if (serv.getId() == odeID) {
                server = serv;
            }

            // remove servers that don't update
            if (serv.getLastLoadUpdate().before(deadTime)) {
                try {
                    new SystemFacade(ODERole.getInstance()).removeServer(serv
                            .getId());
                } catch (Exception e) {
                    log.warn("Couldn't remove a dead server!", e);
                }
            }

            if (!serv.isLocked()) {
                unlockedServers++;
            }
        }
        if (server == null) {
            log.error("Coudn't find server with ID " + odeID);
            throw new IllegalArgumentException("unknown ODE server ID");
        }
        int maxSysLoad = config.getMinServerLoadForLock();
        int minHighSysLoad = config.getMaxServerLoadForUnlock();
        int minSysLoad = config.getMaxServerLoadForShutdown();
        int minInstances = config.getMaxWorkflowInstancesForShutdown();
        int minHighInstances = config.getMaxWorkflowInstancesForUnlock();
        int maxInstances = config.getMinWorkflowInstancesForLock();
        int minUnlockedServers = config.getMinUnlockedServers();
        boolean isLocked = server.isLocked();

        commands.clear();
        if ((isLocked && ((minHighInstances > wfInstances) || ((avgLoad > -1) && (avgLoad < minHighSysLoad))))
                || (!isLocked && ((maxInstances < wfInstances) || ((avgLoad > -1) && (avgLoad > maxSysLoad))))) {
            commands.add(new LockServerCommand(ODE_ROLE, odeID, !isLocked));
        }
        if (server.isDynamicallyAdded()
                && ((minSysLoad > avgLoad) && (wfInstances < minInstances))) {
            run.value = false;
        } else {
            run.value = true;
        }

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

        commands.add(new UpdateServerLoadCommand(ODE_ROLE, odeID,
                (byte) avgLoad));
        HibernateTransactionCoordinator.getInstance().runTransaction(commands);

        try {
            GregorianCalendar date = new GregorianCalendar();
            date.setTime(config.getModifiedDate());
            configVersion.value = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(date);
        } catch (DatatypeConfigurationException e) {
            log.error("Couldn't make new XMLGregorianCalendar"
                    + " => serious misconfiguration", e);
            configVersion.value = null;
        }
        log.trace("Leaving " + ODEMonitorServiceImpl.class.getSimpleName()
                + ".updateStats()");
    }
}
