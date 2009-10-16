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
 * Gets OS-dependent stats on Microsoft Windows. As we currently don't quite
 * know how to do that, we just return <code>-1</code> to indicate an error.
 * 
 * @author Reinhold
 */
public class WindowsStatsCollector extends AbstractOSStatsCollector {

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.odemonitor.client.AbstractOSStatsCollector#getCPULoad()
     */
    @Override
    public int getCPULoad() {
        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.odemonitor.client.AbstractOSStatsCollector#getMemLoad()
     */
    @Override
    public int getMemLoad() {
        return -1;
    }
}