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

package de.decidr.model;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

import de.decidr.model.enums.ServerTypeEnum;

/**
 * RR: add comment
 * 
 * @author Reinhold
 */
public class DecidrGlobalsTest extends AbstractDatabaseTest {

    /**
     * Test method for {@link de.decidr.model.DecidrGlobals#getTime()}.
     */
    @Test
    public void testGetTime() {
        // heuristically assumes that the time between the two calls cannot be
        // greater than 10 seconds
        assertTrue(Math.abs(Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                .getTimeInMillis()
                - DecidrGlobals.getTime().getTimeInMillis()) < 10000);
    }

    /**
     * Test method for {@link de.decidr.model.DecidrGlobals#getSettings()}.
     */
    @Test
    public void testGetSettings() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link de.decidr.model.DecidrGlobals#getEsb()}.
     */
    @Test
    public void testGetEsb() {
        assertNotNull(DecidrGlobals.getEsb());
        assertNotNull(DecidrGlobals.getEsb().getLocation());
        assertEquals(ServerTypeEnum.Esb.toString(), DecidrGlobals.getEsb()
                .getServerType().getName());
    }

    /**
     * Test method for
     * {@link de.decidr.model.DecidrGlobals#getWebServiceUrl(java.lang.String)}.
     */
    @Test
    public void testGetWebServiceUrl() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link de.decidr.model.DecidrGlobals#getWebServiceWsdlUrl(java.lang.String)}
     * .
     */
    @Test
    public void testGetWebServiceWsdlUrl() {
        fail("Not yet implemented"); // TODO
    }

}
