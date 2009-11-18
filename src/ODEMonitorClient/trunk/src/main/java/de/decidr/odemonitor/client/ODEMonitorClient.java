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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.logging.DefaultLogger;

/**
 * The main ODE monitoring class to be started after the ODE instance is up.
 * 
 * @author Reinhold
 */
public class ODEMonitorClient {

    private static MonitoringThread monitor;
    private static final Logger log = DefaultLogger
            .getLogger(ODEMonitorClient.class);
    private static final String VERSION = "0.1";

    /**
     * Starts the client, registers with the server, fetches the config and
     * triggers the monitoring thread.
     * 
     * @param args
     *            Command line parameters. Recognised parameters are:<br>
     *            <table border="1" width="550">
     *            <tr>
     *            <td><code>-i&nbsp;&lt;int&gt;</code></td>
     *            <td>Sets a new interval for the monitoring thread. Will be
     *            overridden whenever the central configuration changes.</td>
     *            </tr>
     *            <tr>
     *            <td><code>--config</code></td>
     *            <td>Forces fetching the configuration.</td>
     *            </tr>
     *            <tr>
     *            <td><code>--id</code></td>
     *            <td>Forces fetching the configuration.</td>
     *            </tr>
     *            <tr>
     *            <td><code>--help | -h</code></td>
     *            <td>A summary of the available options is printed to stdout.</td>
     *            </tr>
     *            <tr>
     *            <td><code>--debug</code></td>
     *            <td>Activates debug level logging.</td>
     *            </tr>
     *            <tr>
     *            <td><code>--trace</code></td>
     *            <td>Activates trace mode.</td>
     *            </tr>
     *            <tr>
     *            <td><code>--version | -V</code></td>
     *            <td>Prints version information to stdout and quits.</td>
     *            </tr>
     *            <tr>
     *            <td><code>--start</code></td>
     *            <td>Starts a managed ODE instance.</td>
     *            </tr>
     *            <tr>
     *            <td><code>--stop</code></td>
     *            <td>Stops the managed ODE instance</td>
     *            </tr>
     *            <tr>
     *            <td><code>--mem</code></td>
     *            <td>Prints the memory usage (including swap) rounded to the
     *            nearest percent.</td>
     *            </tr>
     *            <tr>
     *            <td><code>--cpu</code></td>
     *            <td>Prints the CPU usage rounded to the nearest percent.</td>
     *            </tr>
     *            <tr>
     *            <td><code>--inst</code></td>
     *            <td>Prints the amount of workflow instances running on the
     *            monitored ODE instance.</td>
     *            </tr>
     *            <tr>
     *            <td><code>--wfm</code></td>
     *            <td>Prints the amount of workflow models deployed on the
     *            monitored ODE instance.</td>
     *            </tr>
     *            </table>
     */
    public static void main(String[] args) {
        int interval = -1;
        Long ID = null;
        LocalInstanceManager localInstanceManager = new LocalInstanceManager();

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--wfm")) {
                System.out.println(new LocalInstanceStats().getNumModels());
                return;
            } else if (args[i].equals("-i")) {
                i++;
                interval = new Integer(args[i]);
                if (monitor != null) {
                    monitor.changeInterval(interval);
                }
            } else if (args[i].equals("--id")) {
                if (monitor != null) {
                    log.error("Can't change monitored ID.");
                    return;
                }

                i++;
                try {
                    ID = new Long(args[i]);
                } catch (NumberFormatException e) {
                    log.error("The specified ID is not a valid long!");
                }
            } else if (args[i].equals("--config")) {
                if (monitor != null) {
                    monitor.fetchNewConfig();
                    monitor.interrupt();
                } else {
                    log.error("ODE monitoring instance has "
                            + "not been started yet.");
                }
            } else if (args[i].equals("--help") || args[i].equals("-h")) {
                printUsage();
                return;
            } else if (args[i].equals("--debug")) {
                log.setLevel(Level.DEBUG);
            } else if (args[i].equals("--trace")) {
                log.setLevel(Level.TRACE);
            } else if (args[i].equals("--version") || args[i].equals("-V")) {
                printVersion();
                return;
            } else if (args[i].equals("--start")) {
                localInstanceManager.startInstance();
                return;
            } else if (args[i].equals("--stop")) {
                localInstanceManager.stopInstance();
                return;
            } else if (args[i].equals("--mem")) {
                System.out.println(OSStatsCollectorFactory.getCollector()
                        .getCPULoad());
                return;
            } else if (args[i].equals("--cpu")) {
                System.out.println(OSStatsCollectorFactory.getCollector()
                        .getMemLoad());
                return;
            } else if (args[i].equals("--inst")) {
                System.out.println(new LocalInstanceStats().getNumInstances());
                return;
            } else {
                log.error("Unrecognised option: " + args[i]);
            }
        }

        if (ID == null) {
            log.error("You have to specify the ID"
                    + " of the local ODE server.");
            System.exit(1);
        }

        // exit if a local instance is already running
        if (monitor != null) {
            monitor.changeInterval(interval);
            return;
        }

        if (!localInstanceManager.isRunning()
                && !localInstanceManager.startInstance()) {
            log.error("can't start any instances locally");
        }

        // in milliseconds
        long timeToWait = 5000;
        long maxTimeToWait = 300000;
        double waitingFactor = 1.1;
        while (!localInstanceManager.isRunning()) {
            log.info("Waiting " + Math.round(timeToWait / 1000.0)
                    + "s for local instance to become available...");
            try {
                Thread.sleep(timeToWait);
                timeToWait = Math.round(timeToWait * waitingFactor);
                if (timeToWait > maxTimeToWait) {
                    timeToWait = maxTimeToWait;
                }
            } catch (InterruptedException e) {
                // we were interrupted - nothing to do, so see if we should
                // continue sleeping
            }
        }

        monitor = new MonitoringThread();
        monitor.changeInterval(interval);
        monitor.start(ID);
    }

    /**
     * Prints the usage information to stdout.
     */
    private static void printUsage() {
        printVersion();
        System.out.println("");
        System.out
                .println("This is the command line interface to the DecidR ODE Monitoring Client.");
        System.out.println("");
        System.out
                .println("java -jar ODEMonitorClient.jar --id <long> [-h | --help | -V | --version | --debug | --trace | --start");
        System.out
                .println("                               | --stop | --cpu | --mem | --inst | -wfm | -i <int>");
        System.out
                .println("                               | --esb <ESB_URL_AND_PATH> | --config]");
        System.out.println("");
        System.out.println("\t--id <long>");
        System.out.println("\t\tThe ID of the local ODE server.");
        System.out.println("");
        System.out.println("\t-h | --help");
        System.out.println("\t\tDisplay this help.");
        System.out.println("");
        System.out.println("\t-V | --version");
        System.out.println("\t\tDisplay only version information.");
        System.out.println("");
        System.out.println("\t--debug");
        System.out
                .println("\t\tTurn on debug level logging. Overrides any previous --trace parameters.");
        System.out.println("");
        System.out.println("\t--trace");
        System.out
                .println("\t\tTurn on trace level logging. Overrides any previous --trace parameters.");
        System.out.println("");
        System.out.println("\t--start");
        System.out.println("\t\tOnly start the monitored ODE instance.");
        System.out.println("");
        System.out.println("\t--stop");
        System.out.println("\t\tOnly stop the monitored ODE instance.");
        System.out.println("");
        System.out.println("\t--cpu");
        System.out
                .println("\t\tOnly print CPU usage info rounded to the nearest percent.");
        System.out.println("");
        System.out.println("\t--mem");
        System.out
                .println("\t\tOnly print memory (including swap) usage info rounded to the nearest percent.");
        System.out.println("");
        System.out.println("\t--inst");
        System.out
                .println("\t\tOnly print the amount of workflow instances started on the monitored ODE instance.");
        System.out.println("");
        System.out.println("\t--wfm");
        System.out
                .println("\t\tOnly print the amount of workflow models deployed on the monitored ODE instance.");
        System.out.println("");
        System.out.println("\t-i <int>");
        System.out
                .println("\t\tChanges the update interval in seconds. Overwritten by the next change to the");
        System.out.println("\t\tglobal configuration.");
        System.out.println("");
        System.out.println("\t--esb <ESB_URL_AND_PATH>");
        System.out
                .println("\t\tForces the use of the specified ESB rather than the default one.");
        System.out
                .println("\t\tIf either \"default\" or \"config\" are passed, the default ESB is used.");
        System.out.println("");
        System.out.println("\t--config");
        System.out
                .println("\t\tForces an update of the local configuration to the global configration.");
        System.out.println("");
    }

    /**
     * Prints the version information to stdout.
     */
    private static void printVersion() {
        System.out.println("DecidR version " + DecidrGlobals.getVersion());
        System.out.println("ODE Monitoring Tool version " + VERSION);
    }
}
