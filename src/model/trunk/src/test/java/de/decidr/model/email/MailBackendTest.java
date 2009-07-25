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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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

        // should throw exceptions
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
        // construct a list of valid email adresses
        List<String> validEmails = new ArrayList<String>();
        validEmails.add("abc@de.uk");
        validEmails.add("ab.c@de.uk");
        validEmails.add("ab@de.co.uk");
        validEmails.add("ab_c@de.uk");
        validEmails.add("abc@[127.0.0.1]");

        // should *not* throw exceptions
        new MailBackend(validEmails, "AAA", "a@bc.de", "");
        new MailBackend(validEmails, "AAA", "a@bc.de", null);
        new MailBackend(validEmails, "", "a@bc.de", "");
        new MailBackend(validEmails, null, "a@bc.de", null);

        // should throw exceptions
        try {
            new MailBackend(new ArrayList<String>(), "", "", "");
            fail("all parameters empty should fail");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }
        try {
            new MailBackend((List<String>) null, "AAA", "a@bc.de", "sub");
            fail("To: null should be invalid");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }
        try {
            new MailBackend(new ArrayList<String>(), "AAA", "a@bc.de", "sub");
            fail("Empty To: should be invalid");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }
        try {
            new MailBackend(validEmails, "AAA", "", "sub");
            fail("Empty From: should be invalid");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }
        try {
            new MailBackend(validEmails, "AAA", null, "sub");
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
        final String cc = "new@new.de";
        String bccBefore, toBefore, fromBefore;
        testMail.setCC(cc);
        assertEquals("please test setCC(String) - it seems to fail", cc,
                testMail.getHeaderCC());

        bccBefore = testMail.getHeaderBCC();
        toBefore = testMail.getHeaderTo();
        fromBefore = testMail.getHeaderFromMail();
        testMail.addCC("ab@c.de");
        assertEquals(cc + ", ab@c.de", testMail.getHeaderCC());
        assertEquals(bccBefore, testMail.getHeaderBCC());
        assertEquals(toBefore, testMail.getHeaderTo());
        assertEquals(fromBefore, testMail.getHeaderFromMail());

        testMail.addCC("ab@c.de");
        assertEquals(cc + ", ab@c.de, ab@c.de", testMail.getHeaderCC());
        assertEquals(bccBefore, testMail.getHeaderBCC());
        assertEquals(toBefore, testMail.getHeaderTo());
        assertEquals(fromBefore, testMail.getHeaderFromMail());

        try {
            testMail.addCC("invalid@email@address");
            fail("Didn't catch invalid email address.");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }

        testMail.setCC("");
        assertEquals("please test setCC(String) - it seems to fail", "",
                testMail.getHeaderCC());
        testMail.addCC(null);
        assertNotNull(testMail.getHeaderCC());
        assertEquals("", testMail.getHeaderCC());

        testMail.setCC("");
        assertEquals("please test setCC(String) - it seems to fail", "",
                testMail.getHeaderCC());
        testMail.addCC("");
        assertNotNull(testMail.getHeaderCC());
        assertEquals("", testMail.getHeaderCC());
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
        final String HEADER_NAME = "X-DecidR-Test";
        final String HEADER_VALUE = "testval";
        final String VALID_MAIL = "me@me.me";

        testMail.addHeader("To", VALID_MAIL);
        assertEquals(VALID_MAIL, testMail.getHeaderTo());

        testMail.addHeader("CC", VALID_MAIL);
        assertEquals(VALID_MAIL, testMail.getHeaderCC());

        testMail.addHeader("BcC", VALID_MAIL);
        assertEquals(VALID_MAIL, testMail.getHeaderBCC());

        testMail.addHeader("FrOm", VALID_MAIL);
        assertEquals(VALID_MAIL, testMail.getHeaderFromMail());

        testMail.addHeader("sUbjECt", "my_sub");
        assertEquals("my_sub", testMail.getHeaderSubject());

        testMail.addHeader("X-MAILer", "mailer");
        assertEquals("mailer", testMail.getHeaderXMailer());
        testMail.addHeader("uSER-aGENT", "agent");
        assertEquals("agent", testMail.getHeaderXMailer());

        testMail.addHeader(HEADER_NAME, HEADER_VALUE);
        assertTrue(testMail.getAdditionalHeaderMap().containsKey(HEADER_NAME));
        assertTrue(testMail.getAdditionalHeaderMap()
                .containsValue(HEADER_VALUE));
        assertEquals(testMail.getAdditionalHeaderMap().remove(HEADER_NAME),
                HEADER_VALUE);

        try {
            testMail.addHeader("", HEADER_VALUE);
            fail("Empty name should fail");
        } catch (IllegalArgumentException e) {
            // The exception is supposed to be thrown
        }
        try {
            testMail.addHeader(HEADER_NAME, "");
            fail("Empty content should fail");
        } catch (IllegalArgumentException e) {
            // The exception is supposed to be thrown
        }
        try {
            testMail.addHeader(null, HEADER_VALUE);
            fail("Null name should fail");
        } catch (IllegalArgumentException e) {
            // The exception is supposed to be thrown
        }
        try {
            testMail.addHeader(HEADER_NAME, null);
            fail("Null content should fail");
        } catch (IllegalArgumentException e) {
            // The exception is supposed to be thrown
        }
    }

    /**
     * Test method for {@link MailBackend#addHeaders(Map)}.
     */
    @Test
    public void testAddHeaders() {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("X-A", "A");
        headerMap.put("X-B", "B");
        headerMap.put("X-C", "C");
        headerMap.put("X-D", "D");
        headerMap.put("X-E", "E");

        testMail.addHeaders(headerMap);
        assertTrue(testMail.getAdditionalHeaderMap().keySet().containsAll(
                headerMap.keySet()));
        assertTrue(testMail.getAdditionalHeaderMap().values().containsAll(
                headerMap.values()));
        assertTrue(testMail.getAdditionalHeaderMap().entrySet().containsAll(
                headerMap.entrySet()));
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
        final String recA = "a@a.a";
        final String recB = "b@b.b";
        final String recC = "c@c.c";

        testMail.setReceiver(recA);
        assertEquals(recA, testMail.getHeaderTo());
        testMail.addReceiver(recB);
        assertEquals(recA + ", " + recB, testMail.getHeaderTo());
        testMail.addReceiver(recC);
        assertEquals(recA + ", " + recB + ", " + recC, testMail.getHeaderTo());
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
        testMail.setSender("Anonymous Coward", "anonymous@coward.net");
        assertEquals("Anonymous Coward", testMail.getHeaderFromName());
        assertEquals("anonymous@coward.net", testMail.getHeaderFromMail());
        testMail.setSender("", "anonymous@coward.net");
        assertNull(testMail.getHeaderFromName());
        assertEquals("anonymous@coward.net", testMail.getHeaderFromMail());
        testMail.setSender(null, "anonymous@coward.net");
        assertNull(testMail.getHeaderFromName());
        assertEquals("anonymous@coward.net", testMail.getHeaderFromMail());

        try {
            testMail.setSender(null, "");
            fail("Empty sender is invalid");
        } catch (IllegalArgumentException e) {
            // The exception is supposed to be thrown
        }
        try {
            testMail.setSender(null, null);
            fail("null sender is invalid");
        } catch (IllegalArgumentException e) {
            // The exception is supposed to be thrown
        }
        try {
            testMail.setSender(null, "in@va@lid");
            fail("invalid format is invalid");
        } catch (IllegalArgumentException e) {
            // The exception is supposed to be thrown
        }
    }

    /**
     * Test method for {@link MailBackend#setServerInfo(String, int)}.
     */
    @Test
    public void testSetServerInfo() {
        testMail.setServerInfo("adelhei.de", 99);
        assertEquals("adelhei.de", testMail.getSMTPServerHost());
        assertEquals(99, testMail.getSMTPPortNum());
        testMail.setServerInfo("decidr.de", 99);
        assertEquals("decidr.de", testMail.getSMTPServerHost());
        assertEquals(99, testMail.getSMTPPortNum());
        testMail.setServerInfo("decidr.de", -100);
        assertEquals("decidr.de", testMail.getSMTPServerHost());
        assertEquals(-1, testMail.getSMTPPortNum());
        testMail.setServerInfo("decidr.de", 0);
        assertEquals("decidr.de", testMail.getSMTPServerHost());
        assertEquals(0, testMail.getSMTPPortNum());

        try {
            testMail.setServerInfo("", 0);
            fail("Empty hostname is invalid");
        } catch (IllegalArgumentException e) {
            // The exception is supposed to be thrown
        }
        try {
            testMail.setServerInfo(null, 0);
            fail("Null hostname is invalid");
        } catch (IllegalArgumentException e) {
            // The exception is supposed to be thrown
        }
    }

    /**
     * Test method for {@link MailBackend#setSubject(String)}.
     */
    @Test
    public void testSetSubject() {
        testMail.setSubject("DdeEcCiIdDRr");
        assertEquals("DdeEcCiIdDRr", testMail.getHeaderSubject());
        testMail.setSubject("decidr2");
        assertEquals("decidr2", testMail.getHeaderSubject());

        testMail.setSubject("");
        assertEquals(MailBackend.EMPTY_SUBJECT, testMail.getHeaderSubject());
        testMail.setSubject(null);
        assertEquals(MailBackend.EMPTY_SUBJECT, testMail.getHeaderSubject());
    }

    /**
     * Test method for {@link MailBackend#setUsername(String)}.
     */
    @Test
    public void testSetUsername() {
        testMail.setUsername("decidr");
        assertEquals("decidr", testMail.getUsername());
        testMail.setUsername("DeCiDr");
        assertEquals("DeCiDr", testMail.getUsername());

        testMail.setUsername("");
        assertNull(testMail.getUsername());
        testMail.setUsername(null);
        assertNull(testMail.getUsername());
    }

    /**
     * Test method for {@link MailBackend#setXMailer(String)}.
     */
    @Test
    public void testSetXMailer() {
        testMail.setXMailer("DecidR");
        assertEquals("DecidR", testMail.getHeaderXMailer());
        testMail.setXMailer("DecidR2");
        assertEquals("DecidR2", testMail.getHeaderXMailer());

        testMail.setXMailer("");
        assertNull(testMail.getHeaderXMailer());
        testMail.setXMailer(null);
        assertNull(testMail.getHeaderXMailer());
    }

    /**
     * Test method for {@link MailBackend#useTLS(boolean)}.
     */
    @Test
    public void testUseTLS() {
        testMail.useTLS(true);
        assertTrue(testMail.isUseTLS());
        testMail.useTLS(false);
        assertFalse(testMail.isUseTLS());
    }
}
