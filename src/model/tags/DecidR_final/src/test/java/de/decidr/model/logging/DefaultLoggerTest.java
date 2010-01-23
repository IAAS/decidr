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

package de.decidr.model.logging;

import static junit.framework.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Test;

import de.decidr.model.testing.DecidrOthersTest;

/**
 * Tests whether logging is possible at all using the default logger.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class DefaultLoggerTest extends DecidrOthersTest {

    @Test
    public void testGetLogger() {
        Logger logger = DefaultLogger.getLogger(DefaultLoggerTest.class);

        assertNotNull(logger);

        // make sure no exceptions are thrown
        try {
            logger.trace("Trace");
            logger.debug("Debug");
            logger.info("Info");
            logger.warn("Warn");
            logger.error("Error");
            logger.fatal("Fatal");
        } catch (Exception e) {
            fail("logging failed");
        }
    }

}
