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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import de.decidr.model.logging.DefaultLogger;

/**
 * Contains basic code for getting stats on UNIX systems.
 * 
 * @author Reinhold
 */
public abstract class UnixStatsCollector extends AbstractOSStatsCollector {

    private static final Logger log = DefaultLogger
            .getLogger(UnixStatsCollector.class);

    /**
     * Calls the script and parses the output to a float.
     * 
     * @param command
     *            The command to run and its parameters.
     * @return The float returned by the command or -1, if an error occurred.
     */
    public float runCommand(String... command) {
        log.trace("Entering " + UnixStatsCollector.class.getSimpleName()
                + ".runCommand()");
        float memLoad;

        // use /bin/sh to execute the script
        String[] executableCommand = new String[command.length + 1];
        executableCommand[0] = "/bin/sh";
        for (int i = 0; i < command.length; i++) {
            executableCommand[i + 1] = command[i];
        }

        ProcessBuilder pb = new ProcessBuilder(executableCommand);
        try {
            Process cmd = pb.start();
            InputStream stdout = cmd.getInputStream();
            InputStream stderr = cmd.getErrorStream();
            /*
             * Endless loop until child process has finished. Keep looping, even
             * if the wait is inerrupted.
             */
            while (true) {
                try {
                    cmd.waitFor();
                    // Finished waiting the way we were supposed to. Exit loop.
                    break;
                } catch (InterruptedException e) {
                    /*
                     * interrupted while waiting - do nothing and enter next
                     * loop
                     */
                }
            }

            if (cmd.exitValue() == 0) {
                BufferedReader outputReader = new BufferedReader(
                        new InputStreamReader(stdout));
                try {
                    memLoad = new Float(outputReader.readLine());
                } catch (Exception e) {
                    log
                            .error("Error parsing output of script "
                                    + command[0], e);
                    memLoad = -1;
                }

            } else {
                StringBuilder errorMsg = new StringBuilder(stderr.available());
                InputStreamReader errorReader = new InputStreamReader(stderr);

                int character;
                while ((character = errorReader.read()) != -1) {
                    errorMsg.append((char) character);
                }

                memLoad = -1;
                log.error("Couldn't get load. Error message, if any: "
                        + errorMsg);
            }

            stdout.close();
            stderr.close();
        } catch (IOException e) {
            log.error("Couldn't get load", e);
            memLoad = -1;
        }
        log.trace("Leaving " + UnixStatsCollector.class.getSimpleName()
                + ".runCommand()");
        return memLoad;
    }
}
