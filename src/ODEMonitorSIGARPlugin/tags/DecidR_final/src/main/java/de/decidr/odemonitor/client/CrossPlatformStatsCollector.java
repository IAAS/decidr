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

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 * Uses the <a href="http://www.hyperic.com/products/sigar.html">SIGAR API</a>
 * to collect CPU and memory statistics in a platform-independent manner.
 * 
 * @author Reinhold
 */
public class CrossPlatformStatsCollector extends AbstractOSStatsCollector {

    Sigar sigar = new Sigar();

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.odemonitor.client.AbstractOSStatsCollector#getCPULoad()
     */
    @Override
    public int getCPULoad() {
        int load = -1;

        try {
            load = (int) Math.round(sigar.getCpuPerc().getCombined() * 100);
        } catch (SigarException e) {
            // failed getting CPU load => leave load at -1
        }

        return load;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.odemonitor.client.AbstractOSStatsCollector#getMemLoad()
     */
    @Override
    public int getMemLoad() {
        int load = -1;

        try {
            long total = sigar.getMem().getTotal() + sigar.getSwap().getTotal();
            long free = sigar.getMem().getActualFree()
                    + sigar.getSwap().getFree();
            load = (int) Math.round(((total - free) / (double) total) * 100);
        } catch (SigarException e) {
            // failed getting memory load => leave load at -1
        }

        return load;
    }
}
