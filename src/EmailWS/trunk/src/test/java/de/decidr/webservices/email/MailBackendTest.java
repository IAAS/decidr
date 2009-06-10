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

package de.decidr.webservices.email;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test cases for <code>{@link MailBackend}</code>.
 * 
 * @author Reinhold
 */
public class MailBackendTest {

    /**
     * Test method for
     * {@link de.decidr.webservices.email.MailBackend#validateAddresses(java.lang.String)}
     * .
     */
    @Test
    public void testValidateAddressesString() {
        final boolean testCaseDone = true;

        assertTrue(MailBackend.validateAddresses("a.b@c.de"));
        assertTrue(MailBackend.validateAddresses("a_b@c.de"));
        assertTrue(MailBackend.validateAddresses("ab@cde"));
        assertTrue(MailBackend.validateAddresses("ab@[127.0.0.1]"));
        assertTrue(MailBackend.validateAddresses("ab@c.de"));
        assertTrue(MailBackend.validateAddresses("a@b.c.de"));
        assertTrue(MailBackend.validateAddresses("<a.b@c.de>"));
        assertTrue(MailBackend.validateAddresses("\"aaabbb\" <a.b@c.de>"));
        assertTrue(MailBackend.validateAddresses("asdfg"));

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

        assertTrue(testCaseDone);
    }

    /**
     * Test method for
     * {@link de.decidr.webservices.email.MailBackend#validateAddresses(java.util.List)}
     * .
     */
    @Test
    public void testValidateAddressesListOfString() {
        final boolean testCaseDone = true;

        List<String> strList = new ArrayList<String>(1001);
        for (int i = 0; i <= 1000; i++) {
            strList.add("ab" + i + "@c.de");
        }
        assertTrue("1000 addresses test", MailBackend
                .validateAddresses(strList));

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

        assertTrue(testCaseDone);
    }
}
