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

package de.decidr.modelingtool.client.browserspecific;

/**
 * Class for browser specific tools. If any browser needs other values than the
 * standard values, a new sub class of this has to be created for the browser
 * with the corrected values.
 *
 * @author Johannes Engelhardt
 */
public class BrowserSpecificTools {

    /**
     * Method to correct the border offset at setting the size of a bordered
     * object with 1px border.
     *
     * @param value The original value.
     * @return The corrected value.
     */
    public int correctBorderOffset(int value) {
        return value;
    }

}
