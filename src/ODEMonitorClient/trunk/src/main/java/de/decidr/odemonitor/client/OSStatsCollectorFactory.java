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
 * Used to get an applicable <code>{@link AbstractOSStatsCollector}</code>.
 * 
 * @author Reinhold
 */
public class OSStatsCollectorFactory {
    private static final Logger log = DefaultLogger
            .getLogger(OSStatsCollectorFactory.class);
    private static AbstractOSStatsCollector collector;

    public static AbstractOSStatsCollector getCollector() {
        log.trace("Entering " + AbstractOSStatsCollector.class.getSimpleName()
                + ".getCollector()");
        if (collector == null) {
            useSIGARPlugin();
        }

        if (collector == null) {
            String osName = System.getProperty("os.name");
            if (osName.toLowerCase().contains("win")) {
                log.debug("detected Windows");
                collector = new WindowsStatsCollector();
            } else if (osName.toLowerCase().contains("linux")) {
                log.debug("detected Linux");
                collector = new LinuxStatsCollector();
            } else if (osName.toLowerCase().contains("mac")) {
                log.debug("detected Mac OS");
                collector = new MacStatsCollector();
            } else {
                log.error("Unrecognised OS: " + osName);
            }
        }

        log.trace("Leaving " + AbstractOSStatsCollector.class.getSimpleName()
                + ".getCollector()");
        return collector;
    }

    /**
     * Uses reflection to see whether the SIGAR plugin can be found. If so, use
     * that.
     */
    private static void useSIGARPlugin() {
        try {
            collector = (AbstractOSStatsCollector) Class.forName(
                    "de.decidr.odemonitor.client.CrossPlatformStatsCollector")
                    .newInstance();
            
            log.info("Found SIGAR plug-in");
        } catch (Throwable e) {
            // something bad happened, meaning that we can't use the SIGAR
            // plugin; leave collector as it was
        }
    }
}
