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

import de.decidr.model.logging.DefaultLogger;

/**
 * The main ODE monitoring class being started after the ODE instance is up.
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
     *            <td><code>--esb&nbsp;&lt;ESB_URL_AND_PATH&gt;</code></td>
     *            <td>Forces the use of the specified ESB rather than the
     *            default one. If either &quot;<code>default</code>&quot; or
     *            &quot;<code>config</code>&quot; are passed, the default ESB is
     *            used.</td>
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
        String esbLocation = null;

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
            } else if (args[i].equals("--config")) {
                if (monitor != null) {
                    monitor.fetchNewConfig();
                    monitor.interrupt();
                } else {
                    System.err.println("Error: ODE monitoring instance has "
                            + "not been started yet.");
                }
            } else if (args[i].equals("--esb")) {
                i++;
                esbLocation = args[i];
                if (monitor != null) {
                    monitor.useESB(esbLocation);
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
                new LocalInstanceManager().startInstance();
                return;
            } else if (args[i].equals("--stop")) {
                new LocalInstanceManager().stopInstance();
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

        new LocalInstanceManager().startInstance();

        monitor = new MonitoringThread();
        monitor.changeInterval(interval);
        monitor.useESB(esbLocation);
        monitor.start();
    }

    /**
     * Prints the version information to stdout.
     */
    private static void printVersion() {
        // TODO add correct version
        System.out.println("DecidR version " + "0.0.1");
        System.out.println("ODE Monitoring Tool version " + VERSION);
    }

    /**
     * Prints the usage information to stdout.
     * 
     * @param args
     */
    private static void printUsage() {
        printVersion();
        System.out.println("");
        System.out
                .println("This is the command line interface to the DecidR ODE Monitoring Client.");
        System.out.println("");
        System.out
                .println("java -jar ODEMonitorClient.jar [-h | --help | -V | --version | --debug | --trace | --start | --stop");
        System.out
                .println("                               | --cpu | --mem | --inst | -wfm | -i <int> | --esb <ESB_URL_AND_PATH>");
        System.out.println("                               | --config]");
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
}
