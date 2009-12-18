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

import de.decidr.model.entities.SystemSettings;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.testing.DecidrOthersTest;

/**
 * This class tests the constraints of the return values of the
 * <code>{@link DecidrGlobals}</code> class' methods.
 * 
 * @author Reinhold
 */
public class DecidrGlobalsTest extends DecidrOthersTest {

    /**
     * Test method for {@link DecidrGlobals#getTime()}.
     */
    @Test
    public void testGetTime() {
        // heuristically assumes that the time between the two calls cannot be
        // greater than 10 seconds
        assertTrue(Math.abs(Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                .getTimeInMillis()
                - DecidrGlobals.getTime().getTimeInMillis()) < 10000);

        // truncating the milliseconds shouldn't affect any other field
        Calendar timeWithMillis;
        Calendar timeWithoutMillis;
        int millis;
        int safety = 100000;
        // heuristically assumes that the time between the two calls to
        // DecidrGlobals.getTime() cannot be greater than 99 milliseconds
        do {
            safety--;
            timeWithMillis = DecidrGlobals.getTime(false);
            timeWithoutMillis = DecidrGlobals.getTime(true);
            millis = timeWithMillis.get(Calendar.MILLISECOND);
        } while ((millis == 0 || millis > 900) && safety > 0);

        if (safety <= 0) {
            fail("Couldn't get a time that has more than zero "
                    + "and less than 900 milliseconds within 100000 iterations.");
        }

        // everything except the milliseconds must be the same and the
        // milliseconds must be zero for timeWithoutMillis.
        assertEquals(timeWithMillis.get(Calendar.YEAR), timeWithoutMillis
                .get(Calendar.YEAR));
        assertEquals(timeWithMillis.get(Calendar.MONTH), timeWithoutMillis
                .get(Calendar.MONTH));
        assertEquals(timeWithMillis.get(Calendar.DAY_OF_MONTH),
                timeWithoutMillis.get(Calendar.DAY_OF_MONTH));
        assertEquals(timeWithMillis.get(Calendar.HOUR_OF_DAY),
                timeWithoutMillis.get(Calendar.HOUR_OF_DAY));
        assertEquals(timeWithMillis.get(Calendar.MINUTE), timeWithoutMillis
                .get(Calendar.MINUTE));
        assertEquals(timeWithMillis.get(Calendar.SECOND), timeWithoutMillis
                .get(Calendar.SECOND));
        if (timeWithMillis.get(Calendar.MILLISECOND) == timeWithoutMillis
                .get(Calendar.MILLISECOND)) {
            fail("Milliseconds are equal.");
        }
        assertEquals(timeWithoutMillis.get(Calendar.MILLISECOND), 0);

        // since only the milliseconds are removed, the timestamp distance
        // cannot be greater than 1000
        long distance = Math.abs(timeWithMillis.getTime().getTime()
                - timeWithoutMillis.getTime().getTime());
        if (distance > 1000) {
            fail("Distance was more than a second.");
        }
    }

    /**
     * Test method for {@link DecidrGlobals#getSettings()}.
     */
    @Test
    public void testGetSettings() {
        SystemSettings settings = DecidrGlobals.getSettings();
        assertNotNull(settings);
        assertNotNull(settings.getBaseUrl());
        assertNotNull(settings.getId());
        assertNotNull(settings.getLogLevel());
        assertNotNull(settings.getModifiedDate());
        assertNotNull(settings.getMtaHostname());
        assertNotNull(settings.getMtaPassword());
        assertNotNull(settings.getMtaUsername());
        assertNotNull(settings.getSuperAdmin());
        assertNotNull(settings.getSystemName());
        assertNotNull(settings.getSystemEmailAddress());
        assertTrue(settings.getChangeEmailRequestLifetimeSeconds() >= 0);
        assertTrue(settings.getInvitationLifetimeSeconds() >= 0);
        assertTrue(settings.getMaxAttachmentsPerEmail() >= 0);
        assertTrue(settings.getMaxServerLoadForShutdown() >= 0);
        assertTrue(settings.getMaxServerLoadForUnlock() >= 0);
        assertTrue(settings.getMaxUploadFileSizeBytes() >= 0);
        assertTrue(settings.getMaxWorkflowInstancesForShutdown() >= 0);
        assertTrue(settings.getMaxWorkflowInstancesForUnlock() >= 0);
        assertTrue(settings.getMinServerLoadForLock() >= 0);
        assertTrue(settings.getMinUnlockedServers() >= 0);
        assertTrue(settings.getMinWorkflowInstancesForLock() >= 0);
        assertTrue(settings.getMonitorAveragingPeriodSeconds() >= 0);
        assertTrue(settings.getMonitorUpdateIntervalSeconds() >= 0);
        assertTrue(settings.getMtaPort() >= -1);
        assertTrue(settings.getPasswordResetRequestLifetimeSeconds() >= 0);
        assertTrue(settings.getRegistrationRequestLifetimeSeconds() >= 0);
        assertTrue(settings.getServerPoolInstances() >= 0);
        assertTrue(settings.getMaxServerLoadForShutdown() <= 100);
        assertTrue(settings.getMaxServerLoadForUnlock() <= 100);
        assertTrue(settings.getMinServerLoadForLock() <= 100);
    }

    /**
     * Test method for {@link DecidrGlobals#getEsb()}.
     */
    @Test
    public void testGetEsb() {
        assertNotNull(DecidrGlobals.getEsb());
        assertNotNull(DecidrGlobals.getEsb().getLocation());
        assertEquals(ServerTypeEnum.Esb.toString(), DecidrGlobals.getEsb()
                .getServerType().getName());
    }

    /**
     * Test method for {@link DecidrGlobals#getDefaultTenant()}.
     */
    @Test
    public void testGetDefaultTenant() {
        assertNotNull(DecidrGlobals.getDefaultTenant());
        assertNotNull(DecidrGlobals.getDefaultTenant().getAdmin());
    }

    /**
     * Test method for {@link DecidrGlobals#getWebServiceUrl(String)}.
     */
    @Test
    public void testGetWebServiceUrl() {
        assertNotNull(DecidrGlobals.getWebServiceUrl("MyWS"));
        assertTrue(DecidrGlobals.getWebServiceUrl("MyWS").contains("MyWS"));

        try {
            DecidrGlobals.getWebServiceUrl(null);
            fail("null web service name is incorrect");
        } catch (IllegalArgumentException e) {
            // this is supposed to fail
        }
        try {
            DecidrGlobals.getWebServiceUrl("");
            fail("empty web service name is incorrect");
        } catch (IllegalArgumentException e) {
            // this is supposed to fail
        }
    }

    /**
     * Test method for {@link DecidrGlobals#getWebServiceWsdlUrl(String)}.
     */
    @Test
    public void testGetWebServiceWsdlUrl() {
        assertNotNull(DecidrGlobals.getWebServiceWsdlUrl("MyWS"));
        assertTrue(DecidrGlobals.getWebServiceWsdlUrl("MyWS").contains("MyWS"));

        try {
            DecidrGlobals.getWebServiceWsdlUrl(null);
            fail("null web service name is incorrect");
        } catch (IllegalArgumentException e) {
            // this is supposed to fail
        }
        try {
            DecidrGlobals.getWebServiceWsdlUrl("");
            fail("empty web service name is incorrect");
        } catch (IllegalArgumentException e) {
            // this is supposed to fail
        }
    }
}
