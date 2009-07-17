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

/**
 * Gets OS-dependent stats on MacOS X.
 * 
 * @author Reinhold
 */
public class MacStatsCollector extends UnixStatsCollector {

    // TODO implement script
    private static final String SCRIPTS_MEMORY_USAGE = "scripts/mac/getMemoryUsage.sh";

    // TODO implement script
    private static final String SCRIPTS_CPU_USAGE = "scripts/mac/getCPUUsage.sh";

    @Override
    public int getCPULoad() {
        log.trace("Entering " + MacStatsCollector.class.getSimpleName()
                + ".getCPULoad()");
        float cpuLoad = runCommand(SCRIPTS_CPU_USAGE, "1", "2");

        log.trace("Leaving " + MacStatsCollector.class.getSimpleName()
                + ".getCPULoad()");
        return Math.round(cpuLoad);
    }

    @Override
    public int getMemLoad() {
        log.trace("Entering " + MacStatsCollector.class.getSimpleName()
                + ".getMemLoad()");
        float memLoad = runCommand(SCRIPTS_MEMORY_USAGE, "2");

        log.trace("Leaving " + MacStatsCollector.class.getSimpleName()
                + ".getMemLoad()");
        return Math.round(memLoad);
    }
}
