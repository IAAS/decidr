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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import org.apache.log4j.Logger;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.odemonitor.service.ODEMonitor;
import de.decidr.odemonitor.service.ODEMonitorService;

/**
 * The actual monitoring logic, implemented as a separate thread to allow
 * daemon-style execution.
 * 
 * @author Reinhold
 */
public class MonitoringThread extends Thread {

    private static Logger log = DefaultLogger.getLogger(MonitoringThread.class);
    private static final int DEFAULT_INTERVAL = 60;
    private static final int DEFAULT_PERIOD = 300;
    private static final AbstractOSStatsCollector osStats = OSStatsCollectorFactory
            .getCollector();
    private int updateInterval = DEFAULT_INTERVAL;
    private long odeID = -1;
    private int averagePeriod = DEFAULT_PERIOD;
    private boolean poolInstance = true;
    private XMLGregorianCalendar lastUpdate;
    private Map<Long, Integer> loadMap = new HashMap<Long, Integer>();

    private boolean exitLoop = false;

    /**
     * Tells the Client to exit the loop and stop the loop.
     */
    public void stopClient() {
        exitLoop = true;
    }

    /**
     * Change the updating interval and force an update.
     * 
     * @param interval
     *            the interval in seconds between updates.
     */
    public void changeInterval(int interval) {
        if (interval > 0) {
            updateInterval = interval;
        } else {
            updateInterval = DEFAULT_INTERVAL;
        }
        interrupt();
    }

    /**
     * Change the period of time to average the system load over
     * 
     * @param interval
     *            the period of time to average over
     */
    public void changePeriod(int period) {
        if (period > 0) {
            averagePeriod = period;
        } else {
            averagePeriod = DEFAULT_PERIOD;
        }
    }

    /**
     * Updates the current config from the
     * <code>{@link ODEMonitorService}</code>.
     */
    public void fetchNewConfig() {
        Holder<Integer> intervalHolder = new Holder<Integer>();
        Holder<Integer> periodHolder = new Holder<Integer>();
        Holder<XMLGregorianCalendar> calendarHolder = new Holder<XMLGregorianCalendar>();

        getServer().getConfig(intervalHolder, periodHolder, calendarHolder);
        changeInterval(intervalHolder.value);
        changePeriod(periodHolder.value);
        lastUpdate = calendarHolder.value;
    }

    /**
     * Gets the average system load over the configured period of time.
     * 
     * @return The average system load
     */
    private int getAvgLoad() {
        int average = 0;
        int total = 0;
        int systemLoad = osStats.getSystemLoad();
        long oldestTime = DecidrGlobals.getTime().getTimeInMillis()
                - averagePeriod;

        // clean Map of outdated entries
        for (Long time : loadMap.keySet()) {
            if (time < oldestTime) {
                loadMap.remove(time);
            }
        }

        // add new entry
        loadMap.put(DecidrGlobals.getTime().getTimeInMillis(), systemLoad);

        // get average
        for (Long time : loadMap.keySet()) {
            average += loadMap.get(time);
            total++;
        }
        if (total == 0) {
            average = systemLoad;
        } else {
            average = Math.round(((float) average) / total);
        }
        return average;
    }

    /**
     * Gets the <code>{@link ODEMonitorService}</code> using either the default
     * ESB or the provided one.
     * 
     * @return A usable instance of <code>{@link ODEMonitorService}</code>.
     * @throws IllegalArgumentException
     *             thrown if the ESB location wasn't a well-formed
     *             <code>{@link URL}</code>.
     */
    private ODEMonitorService getServer() {
        ODEMonitorService server;
        try {
            // server = ODEMonitor.getODEMonitorClient();
            server = new ODEMonitor().getODEMonitorSOAP();
        } catch (MalformedURLException e) {
            log.error("can't access ESB", e);
            throw new IllegalArgumentException("unusable ESB", e);
        }
        return server;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        super.run();

        // variables
        log.debug("setting variables...");
        Holder<XMLGregorianCalendar> calendarHolder = new Holder<XMLGregorianCalendar>();
        Holder<Boolean> booleanHolder = new Holder<Boolean>();
        ODEMonitorService server = getServer();
        LocalInstanceStats localStats = new LocalInstanceStats();
        InstanceManager manager = new LocalInstanceManager();
        boolean errorOccurred = false;

        // register with server
        // try to register every updateInterval until we succeed
        while (true) {
            if (exitLoop) {
                break;
            }

            log.debug("trying to register with the web service...");
            try {
                server.registerODE(booleanHolder, odeID);
                log.debug("...success");
                break;
            } catch (TransactionException e) {
                log.debug("...failed", e);
                try {
                    Thread.sleep(updateInterval);
                } catch (InterruptedException ex) {
                    // we wake and resume work due to someone watching us
                }
            } catch (IllegalArgumentException e) {
                log.fatal("The specified ODE server ID could not be found.", e);
                System.exit(1);
            }

            if (exitLoop) {
                break;
            }
        }
        poolInstance = booleanHolder.value;
        log
                .debug("this is " + (poolInstance ? "" : "not ")
                        + "a pool instance");

        // get config
        try {
            fetchNewConfig();
            log.debug("managed to get config from the web service");
        } catch (Exception e) {
            log.error("There seems to be a problem with the"
                    + " ODE monitoring web service - using default config", e);
        }

        try {
            log.debug("entering update loop");

            // run periodic update
            while (true) {
                errorOccurred = false;

                if (exitLoop) {
                    break;
                }

                // use current server (ESB might have changed)
                log.debug("try to refresh server...");
                try {
                    server = getServer();
                    log.debug("...success");
                } catch (RuntimeException e) {
                    log.debug("...failed");
                    log.error("There seems to be a problem with the"
                            + "ODE monitoring web service", e);
                    if (server == null) {
                        throw (e);
                    }
                }

                // update stats & config, if necessary
                log.debug("attempting to send updated stats to web service");
                try {
                    server.updateStats(localStats.getNumInstances(), localStats
                            .getNumModels(), getAvgLoad(), odeID,
                            calendarHolder, booleanHolder);
                } catch (TransactionException e) {
                    // try again during next iteration
                    log.debug("The web service seems (temporarily)"
                            + " unable to do it's job", e);
                    errorOccurred = true;
                } catch (Exception e) {
                    log.error("There seems to be a problem with the"
                            + "ODE monitoring web service", e);
                    errorOccurred = true;
                }

                if (!errorOccurred) {
                    // stop instance, unless we are a pool instance
                    if (!poolInstance && !booleanHolder.value) {
                        log.debug("web service wants this instance shut down");
                        manager.stopInstance();
                        break;
                    }
                    // get new config if it was altered
                    if (calendarHolder.value.toGregorianCalendar().after(
                            lastUpdate.toGregorianCalendar())) {
                        log.debug("the config seems to have changed"
                                + " - updating...");
                        try {
                            fetchNewConfig();
                        } catch (Exception e) {
                            log.error("There seems to be a problem with the"
                                    + "ODE monitoring web service", e);
                        }
                    }
                }

                if (exitLoop) {
                    break;
                }

                try {
                    log.debug("sleeping " + updateInterval + " seconds...");
                    Thread.sleep(updateInterval * 1000);
                } catch (InterruptedException e) {
                    // we wake and resume work due to someone watching us
                }
            }
        } finally {
            // attempt to unregister until we are successful
            while (true) {
                log.debug("trying to unregister the client...");
                try {
                    server.unregisterODE(odeID);
                    log.debug("...success");
                    break;
                } catch (TransactionException e) {
                    log.debug("...failed", e);
                    try {
                        Thread.sleep(updateInterval);
                    } catch (InterruptedException ex) {
                        // we wake and resume work due to someone watching us
                    }
                }
            }
        }
    }

    public synchronized void start(long id) {
        odeID = id;
        super.start();
    }
}
