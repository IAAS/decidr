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

import org.apache.log4j.Logger;

import de.decidr.model.logging.DefaultLogger;

/**
 * Gets OS-dependent stats on Linux.
 * 
 * @author Reinhold
 */
public class LinuxStatsCollector extends UnixStatsCollector {

    private static final Logger log = DefaultLogger
            .getLogger(LinuxStatsCollector.class);
    private static final String SCRIPTS_MEMORY_USAGE = "scripts/linux/getMemoryUsage.sh";

    private static final String SCRIPTS_CPU_USAGE = "scripts/linux/getCPUUsage.sh";

    @Override
    public int getCPULoad() {
        log.trace("Entering " + LinuxStatsCollector.class.getSimpleName()
                + ".getCPULoad()");
        float cpuLoad = runCommand(SCRIPTS_CPU_USAGE, "1", "2");

        log.trace("Leaving " + LinuxStatsCollector.class.getSimpleName()
                + ".getCPULoad()");
        return Math.round(cpuLoad);
    }

    @Override
    public int getMemLoad() {
        log.trace("Entering " + LinuxStatsCollector.class.getSimpleName()
                + ".getMemLoad()");
        float memLoad = runCommand(SCRIPTS_MEMORY_USAGE, "2");

        log.trace("Leaving " + LinuxStatsCollector.class.getSimpleName()
                + ".getMemLoad()");
        return Math.round(memLoad);
    }
}
