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

package de.decidr.model.email;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import de.decidr.model.email.MailBackend;
import de.decidr.model.logging.DefaultLogger;

/**
 * Test cases for <code>{@link MailBackend}</code>.
 * 
 * @author Reinhold
 */
public class MailBackendTest {
    MailBackend testMail;
    static {
        // make sure that DefaultLogger doesn't override the settings in out
        // beautiful constructor
        new DefaultLogger();
    }

    public MailBackendTest() {
        Logger.getRootLogger().setLevel(Level.TRACE);
    }

    @Before
    public void setUpBeforeEachTest() {
        testMail = new MailBackend("new@new.de", null, "new@new.de", null);
    }

    /**
     * Test method for
     * {@link de.decidr.model.email.MailBackend#validateAddresses(java.lang.String)}
     * .
     */
    @Test
    public void testValidateAddressesString() {
        assertTrue(MailBackend.validateAddresses("a.b@c.de"));
        assertTrue(MailBackend.validateAddresses("a_b@c.de"));
        assertTrue(MailBackend.validateAddresses("ab@cde"));
        assertTrue(MailBackend.validateAddresses("ab@[127.0.0.1]"));
        assertTrue(MailBackend.validateAddresses("ab@c.de"));
        assertTrue(MailBackend.validateAddresses("a@b.c.de"));
        assertTrue(MailBackend.validateAddresses("<a.b@c.de>"));
        assertTrue(MailBackend.validateAddresses("\"aaabbb\" <a.b@c.de>"));
        assertTrue(MailBackend.validateAddresses("asdfg"));

        assertFalse(MailBackend.validateAddresses((String) null));
        assertFalse(MailBackend.validateAddresses(""));
        assertFalse(MailBackend.validateAddresses("a@b@c.de"));
        assertFalse(MailBackend.validateAddresses("a b@c.de"));
        // XXX These seem to be JavaMail implementation bugs
        // assertFalse(MailBackend.validateAddresses("<aaabbb> \"a.b@c.de\""));
        // assertFalse(MailBackend.validateAddresses("ab@[127.0.1]"));
        // assertFalse(MailBackend.validateAddresses("ab@c.de, "));

        assertTrue(MailBackend.validateAddresses("a.b@c.de, abc@ddd.de"));
        assertTrue(MailBackend
                .validateAddresses("\"aaabbb\" <a.b@c.de>, \"aaabbb\" <a.b@c.de>"));

        assertFalse(MailBackend.validateAddresses("a.b@c.de abc@ddd.de"));
        // XXX These seem to be JavaMail implementation bugs
        // assertFalse(MailBackend.validateAddresses("ab@c.de, , a@bc.de"));
        // assertFalse(MailBackend.validateAddresses(", "));
        // assertFalse(MailBackend.validateAddresses(", , "));
    }

    /**
     * Test method for
     * {@link de.decidr.model.email.MailBackend#validateAddresses(java.util.List)}
     * .
     */
    @Test
    public void testValidateAddressesListOfString() {
        List<String> strList = new ArrayList<String>(1001);
        for (int i = 0; i <= 1000; i++) {
            strList.add("ab" + i + "@c.de");
        }
        assertTrue("1000 addresses test", MailBackend
                .validateAddresses(strList));

        assertFalse(MailBackend.validateAddresses((List<String>) null));
        strList.add("");
        // XXX This seems to be a JavaMail implementation bug
        // assertFalse(MailBackend.validateAddresses(strList));
        strList.add("abc@d.de");
        // XXX This seems to be a JavaMail implementation bug
        // assertFalse(MailBackend.validateAddresses(strList));

        strList.clear();
        strList.add("");
        strList.add("");
        // XXX This seems to be a JavaMail implementation bug
        // assertFalse(MailBackend.validateAddresses(strList));
    }

    /**
     * Test method for
     * {@link de.decidr.model.email.MailBackend#addBCC(String)} .
     */
    @Test
    public void testAddBCC() {
        final String bcc = "new@new.de";
        String ccBefore, toBefore, fromBefore;
        testMail.setBCC(bcc);
        assertEquals("please test setBCC(String) - it seems to fail", bcc,
                testMail.getHeaderBCC());

        ccBefore = testMail.getHeaderCC();
        toBefore = testMail.getHeaderTo();
        fromBefore = testMail.getHeaderFromMail();
        testMail.addBCC("ab@c.de");
        assertEquals(bcc + ", ab@c.de", testMail.getHeaderBCC());
        assertEquals(ccBefore, testMail.getHeaderCC());
        assertEquals(toBefore, testMail.getHeaderTo());
        assertEquals(fromBefore, testMail.getHeaderFromMail());

        testMail.addBCC("ab@c.de");
        assertEquals(bcc + ", ab@c.de, ab@c.de", testMail.getHeaderBCC());
        assertEquals(ccBefore, testMail.getHeaderCC());
        assertEquals(toBefore, testMail.getHeaderTo());
        assertEquals(fromBefore, testMail.getHeaderFromMail());

        try {
            testMail.addBCC("invalid@email@address");
            fail("Didn't catch invalid email address.");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }

        testMail.setBCC("");
        assertEquals("please test setBCC(String) - it seems to fail", "",
                testMail.getHeaderBCC());
        testMail.addBCC(null);
        assertNotNull(testMail.getHeaderBCC());
        assertEquals("", testMail.getHeaderBCC());

        testMail.setBCC("");
        assertEquals("please test setBCC(String) - it seems to fail", "",
                testMail.getHeaderBCC());
        testMail.addBCC("");
        assertNotNull(testMail.getHeaderBCC());
        assertEquals("", testMail.getHeaderBCC());
    }
}
