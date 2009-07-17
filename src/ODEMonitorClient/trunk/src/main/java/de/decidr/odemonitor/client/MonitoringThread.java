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

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import de.decidr.odemonitor.service.ODEMonitor;
import de.decidr.odemonitor.service.ODEMonitorService;

/**
 * The actual monitoring logic, implemented as a separate thread to allow
 * daemon-style execution.
 * 
 * @author Reinhold
 */
// No logging in this class to not spam stdout
public class MonitoringThread extends Thread {

    private static final int DEFAULT_INTERVAL = 60;
    private int updateInterval = DEFAULT_INTERVAL;
    private String odeID = null;
    private boolean poolInstance = true;
    private XMLGregorianCalendar lastUpdate;

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
     * Updates the current config from the
     * <code>{@link ODEMonitorService}</code>.
     */
    public void fetchNewConfig() {
        Holder<Integer> intervalHolder = new Holder<Integer>();
        Holder<XMLGregorianCalendar> calendarHolder = new Holder<XMLGregorianCalendar>();

        getServer().getConfig(intervalHolder, calendarHolder);
        updateInterval = intervalHolder.value;
        lastUpdate = calendarHolder.value;
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
        Holder<XMLGregorianCalendar> calendarHolder = new Holder<XMLGregorianCalendar>();
        Holder<Boolean> booleanHolder = new Holder<Boolean>();
        ODEMonitorService server = getServer();
        LocalInstanceStats localStats = new LocalInstanceStats();
        AbstractOSStatsCollector osStats = OSStatsCollectorFactory
                .getCollector();
        InstanceManager manager = new LocalInstanceManager();

        // register with server
        Holder<String> odeIDHolder = new Holder<String>();
        server.registerODE(booleanHolder, odeIDHolder);
        poolInstance = booleanHolder.value;
        odeID = odeIDHolder.value;
        odeIDHolder = null;

        // get config
        fetchNewConfig();

        // keep the JVM alive, even if we are the only thread running
        setDaemon(false);

        // run periodic update
        while (true) {
            // use current server (ESB might have changed)
            server = getServer();

            // update stats & config, if necessary
            server.updateStats(localStats.getNumInstances(), localStats
                    .getNumModels(), osStats.getCPULoad(),
                    osStats.getMemLoad(), odeID, calendarHolder, booleanHolder);
            // start/stop instance, unless we are a pool instance
            if (!poolInstance && booleanHolder.value) {
                manager.startInstance();
            } else {
                manager.stopInstance();
            }
            // get new config if it was altered
            if (calendarHolder.value.toGregorianCalendar().after(
                    lastUpdate.toGregorianCalendar())) {
                fetchNewConfig();
            }

            try {
                Thread.sleep(updateInterval);
            } catch (InterruptedException e) {
                // we wake and resume work due to someone watching us
            }
        }
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
            server = new ODEMonitor().getODEMonitorSOAP();
        } catch (MalformedURLException e) {
            System.err.println("Error: can't access ESB");
            System.err.println("Java stack trace, providing reason:");
            e.printStackTrace();
            throw new IllegalArgumentException("unusable ESB", e);
        }
        return server;
    }
}
