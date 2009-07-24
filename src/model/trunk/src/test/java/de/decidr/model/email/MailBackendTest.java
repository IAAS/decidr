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

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeBodyPart;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for <code>{@link MailBackend}</code>.
 * 
 * @author Reinhold
 */
public class MailBackendTest {
    MailBackend testMail;

    @Before
    public void setUpBeforeEachTest() {
        testMail = new MailBackend("new@new.de", null, "new@new.de", null);
    }

    /**
     * Test method for {@link MailBackend#validateAddresses(String)}.
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
     * Test method for {@link MailBackend#validateAddresses(List)}.
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
     * Test method for {@link MailBackend#addBCC(String)}.
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

    /**
     * Test method for
     * {@link MailBackend#MailBackend(String, String, String, String)}.
     */
    @Test
    public void testMailBackendStringStringStringString() {
        // should *not* throw exceptions
        new MailBackend("a@bc.de", "AAA", "a@bc.de", "");
        new MailBackend("a@bc.de", "AAA", "a@bc.de", null);
        new MailBackend("a@bc.de", "", "a@bc.de", "");
        new MailBackend("a@bc.de", null, "a@bc.de", null);

        // RR should throw exceptions
        try {
            new MailBackend("", "", "", "");
            fail("all parameters empty should fail");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }
        try {
            new MailBackend((String) null, "AAA", "a@bc.de", "sub");
            fail("To: null should be invalid");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }
        try {
            new MailBackend("", "AAA", "a@bc.de", "sub");
            fail("Empty To: should be invalid");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }
        try {
            new MailBackend("a@bc.de", "AAA", "", "sub");
            fail("Empty From: should be invalid");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }
        try {
            new MailBackend("a@bc.de", "AAA", null, "sub");
            fail("From: null should be invalid");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }
    }

    /**
     * Test method for
     * {@link MailBackend#MailBackend(List, String, String, String)}.
     */
    @Test
    public void testMailBackendListOfStringStringStringString() {
        // RR use lists, not strings
        List<String> validEmails = new ArrayList<String>();
        List<String> invalidEmails = new ArrayList<String>();

        // should *not* throw exceptions
        new MailBackend("a@bc.de", "AAA", "a@bc.de", "");
        new MailBackend("a@bc.de", "AAA", "a@bc.de", null);
        new MailBackend("a@bc.de", "", "a@bc.de", "");
        new MailBackend("a@bc.de", null, "a@bc.de", null);

        // RR should throw exceptions
        try {
            new MailBackend("", "", "", "");
            fail("all parameters empty should fail");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }
        try {
            new MailBackend((String) null, "AAA", "a@bc.de", "sub");
            fail("To: null should be invalid");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }
        try {
            new MailBackend("", "AAA", "a@bc.de", "sub");
            fail("Empty To: should be invalid");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }
        try {
            new MailBackend("a@bc.de", "AAA", "", "sub");
            fail("Empty From: should be invalid");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }
        try {
            new MailBackend("a@bc.de", "AAA", null, "sub");
            fail("From: null should be invalid");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }
    }

    /**
     * Test method for {@link MailBackend#addCC(String)}.
     */
    @Test
    public void testAddCC() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#addFile(File)}.
     */
    @Test
    public void testAddFileFile() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#addFile(URI)}.
     */
    @Test
    public void testAddFileURI() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#addFile(URL)}.
     */
    @Test
    public void testAddFileURL() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#addFile(InputStream)}.
     */
    @Test
    public void testAddFileInputStream() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#addHeader(String, String)}.
     */
    @Test
    public void testAddHeader() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#addHeaders(Map)}.
     */
    @Test
    public void testAddHeaders() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#addMimePart(MimeBodyPart)}.
     */
    @Test
    public void testAddMimePart() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#addMimeParts(List)}.
     */
    @Test
    public void testAddMimeParts() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#addReceiver(String)}.
     */
    @Test
    public void testAddReceiver() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#getAdditionalHeaderMap()}.
     */
    @Test
    public void testGetAdditionalHeaderMap() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#getHeaderBCC()}.
     */
    @Test
    public void testGetHeaderBCC() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#getHeaderCC()}.
     */
    @Test
    public void testGetHeaderCC() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#getHeaderFromName()}.
     */
    @Test
    public void testGetHeaderFromName() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#getHeaderFromMail()}.
     */
    @Test
    public void testGetHeaderFromMail() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#getHeaderSubject()}.
     */
    @Test
    public void testGetHeaderSubject() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#getHeaderTo()}.
     */
    @Test
    public void testGetHeaderTo() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#getHeaderXMailer()}.
     */
    @Test
    public void testGetHeaderXMailer() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#getMessageParts()}.
     */
    @Test
    public void testGetMessageParts() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#getPassword()}.
     */
    @Test
    public void testGetPassword() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#getSMTPPortNum()}.
     */
    @Test
    public void testGetSMTPPortNum() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#getSMTPServerHost()}.
     */
    @Test
    public void testGetSMTPServerHost() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#getUsername()}.
     */
    @Test
    public void testGetUsername() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#isUseTLS()}.
     */
    @Test
    public void testIsUseTLS() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#removeHeader(String)}.
     */
    @Test
    public void testRemoveHeader() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#removeHeaders(List)}.
     */
    @Test
    public void testRemoveHeaders() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#removeMimePart(MimeBodyPart)}.
     */
    @Test
    public void testRemoveMimePart() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#removeMimeParts(List)}.
     */
    @Test
    public void testRemoveMimeParts() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#sendMessage()}.
     */
    @Test
    public void testSendMessage() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setAuthInfo(String, String)} .
     */
    @Test
    public void testSetAuthInfo() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setBCC(List)}.
     */
    @Test
    public void testSetBCCListOfString() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setBCC(String)}.
     */
    @Test
    public void testSetBCCString() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setBodyText(String)}.
     */
    @Test
    public void testSetBodyText() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setBodyHTML(String)}.
     */
    @Test
    public void testSetBodyHTML() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setCC(List)}.
     */
    @Test
    public void testSetCCListOfString() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setCC(String)}.
     */
    @Test
    public void testSetCCString() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setHostname(String)}.
     */
    @Test
    public void testSetHostname() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setPassword(String)}.
     */
    @Test
    public void testSetPassword() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setPortNum(int)}.
     */
    @Test
    public void testSetPortNum() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setReceiver(List)}.
     */
    @Test
    public void testSetReceiverListOfString() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setReceiver(String)}.
     */
    @Test
    public void testSetReceiverString() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setSender(String, String)}.
     */
    @Test
    public void testSetSender() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setServerInfo(String, int)}.
     */
    @Test
    public void testSetServerInfo() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setSubject(String)}.
     */
    @Test
    public void testSetSubject() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setUsername(String)}.
     */
    @Test
    public void testSetUsername() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#setXMailer(String)}.
     */
    @Test
    public void testSetXMailer() {
        fail("Not yet implemented"); // RR
    }

    /**
     * Test method for {@link MailBackend#useTLS(boolean)}.
     */
    @Test
    public void testUseTLS() {
        fail("Not yet implemented"); // RR
    }
}
