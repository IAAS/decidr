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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for <code>{@link MailBackend}</code>.
 * 
 * @author Reinhold
 */
// TODO addFile(File) seems not to be tested
// TODO addFile(InputSteam) seems not to be tested
// TODO addFile(URI) seems not to be tested
// TODO addFile(URL) seems not to be tested
// TODO sendMessage() seems not to be tested
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
        assertTrue(MailBackend.validateAddresses("hallo123@c.de"));
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
        // assertFalse(MailBackend.validateAddresses("ab@[example.com]"));
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
     * Test method for {@link MailBackend#validateAddresses(Set)}.
     */
    @Test
    public void testValidateAddressesSetOfString() {
        Set<String> strList = new HashSet<String>(1001);
        for (int i = 0; i <= 1000; i++) {
            strList.add("ab" + i + "@c.de");
        }
        assertTrue("1000 addresses test", MailBackend
                .validateAddresses(strList));

        assertFalse(MailBackend.validateAddresses((Set<String>) null));
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
     * {@link MailBackend#MailBackend(Set, String, String, String)}.
     */
    @Test
    public void testMailBackendSetOfStringStringStringString() {
        // construct a list of valid email adresses
        Set<String> validEmails = new HashSet<String>();
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
            new MailBackend(new HashSet<String>(), "", "", "");
            fail("all parameters empty should fail");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }
        try {
            new MailBackend((HashSet<String>) null, "AAA", "a@bc.de", "sub");
            fail("To: null should be invalid");
        } catch (IllegalArgumentException e) {
            // This test is supposed to fail
        }
        try {
            new MailBackend(new HashSet<String>(), "AAA", "a@bc.de", "sub");
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
        testMail.getAdditionalHeaderMap().clear();
        assertTrue(testMail.getAdditionalHeaderMap().isEmpty());
        testMail.addHeaders(new HashMap<String, String>());

        try {
            testMail.addHeaders(null);
            fail("Null content should fail");
        } catch (IllegalArgumentException e) {
            // The exception is supposed to be thrown
        }
    }

    /**
     * Test method for {@link MailBackend#addMimePart(MimeBodyPart)}.
     */
    @Test
    public void testAddMimePart() {
        MimeBodyPart plainBodyPart = new MimeBodyPart();

        testMail.getMessageParts().clear();
        assertTrue(testMail.getMessageParts().isEmpty());

        testMail.addMimePart(plainBodyPart);
        assertTrue(testMail.getMessageParts().size() == 1);
        assertTrue(testMail.getMessageParts().contains(plainBodyPart));

        try {
            testMail.addMimePart(null);
            fail("Adding null mimepart should fail");
        } catch (IllegalArgumentException e) {
            // Exception is supposed to be thrown
        }
    }

    /**
     * Test method for {@link MailBackend#addMimeParts(List)}.
     */
    @Test
    public void testAddMimeParts() {
        Set<MimeBodyPart> parts = new HashSet<MimeBodyPart>(1);
        MimeBodyPart plainBodyPart = new MimeBodyPart();
        parts.add(plainBodyPart);

        testMail.getMessageParts().clear();
        assertTrue(testMail.getMessageParts().isEmpty());

        testMail.addMimeParts(parts);
        for (MimeBodyPart mimeBodyPart : parts) {
            assertTrue(testMail.getMessageParts().contains(mimeBodyPart));
        }

        testMail.getMessageParts().clear();
        assertTrue(testMail.getMessageParts().isEmpty());
        testMail.addMimeParts(new HashSet<MimeBodyPart>());
        assertTrue(testMail.getMessageParts().isEmpty());

        try {
            testMail.addMimeParts(null);
            fail("Adding null list should fail");
        } catch (IllegalArgumentException e) {
            // Exception is supposed to be thrown
        }
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
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("X-A", "A");
        headerMap.put("X-B", "B");
        headerMap.put("X-C", "C");
        headerMap.put("X-D", "D");
        headerMap.put("X-E", "E");

        Map<String, String> lesserMap = new HashMap<String, String>();
        lesserMap.put("X-A", "A");
        lesserMap.put("X-C", "C");
        lesserMap.put("X-D", "D");

        testMail.removeHeader("Non-Existent-Header");
        assertTrue(testMail.getAdditionalHeaderMap().isEmpty());

        testMail.getAdditionalHeaderMap().clear();
        assertTrue(testMail.getAdditionalHeaderMap().isEmpty());

        testMail.addHeaders(headerMap);
        assertTrue(testMail.getAdditionalHeaderMap().entrySet().containsAll(
                headerMap.entrySet()));

        testMail.removeHeader("X-E");
        assertFalse(testMail.getAdditionalHeaderMap().containsKey("X-E"));
        testMail.removeHeader("X-B");
        assertTrue(testMail.getAdditionalHeaderMap().entrySet().containsAll(
                lesserMap.entrySet()));
        assertFalse(testMail.getAdditionalHeaderMap().containsKey("X-B"));
        testMail.removeHeader("X-C");
        assertFalse(testMail.getAdditionalHeaderMap().containsKey("X-C"));
        testMail.removeHeader("X-D");
        assertFalse(testMail.getAdditionalHeaderMap().containsKey("X-D"));
        testMail.removeHeader("X-A");
        assertTrue(testMail.getAdditionalHeaderMap().isEmpty());
        testMail.removeHeader("Non-Existent-Header");
        assertTrue(testMail.getAdditionalHeaderMap().isEmpty());

        testMail.removeHeader("");
        testMail.removeHeader(null);
    }

    /**
     * Test method for {@link MailBackend#removeHeaders(Set)}.
     */
    @Test
    public void testRemoveHeaders() {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("X-A", "A");
        headerMap.put("X-B", "B");
        headerMap.put("X-C", "C");
        headerMap.put("X-D", "D");
        headerMap.put("X-E", "E");

        Set<String> lesserSet = new HashSet<String>();
        lesserSet.add("X-A");
        lesserSet.add("X-C");
        lesserSet.add("X-D");

        Map<String, String> diffMap = new HashMap<String, String>();
        diffMap.putAll(headerMap);
        for (String key : lesserSet) {
            diffMap.remove(key);
        }

        testMail.getAdditionalHeaderMap().clear();
        assertTrue(testMail.getAdditionalHeaderMap().isEmpty());

        testMail.addHeaders(headerMap);
        assertTrue(testMail.getAdditionalHeaderMap().entrySet().containsAll(
                headerMap.entrySet()));

        testMail.removeHeaders(lesserSet);
        assertTrue(testMail.getAdditionalHeaderMap().entrySet().containsAll(
                diffMap.entrySet()));

        testMail.removeHeaders(diffMap.keySet());
        assertTrue(testMail.getAdditionalHeaderMap().isEmpty());

        testMail.removeHeaders(headerMap.keySet());
        assertTrue(testMail.getAdditionalHeaderMap().isEmpty());

        testMail.removeHeaders(new HashSet<String>());
        testMail.removeHeaders(null);
    }

    /**
     * Test method for {@link MailBackend#removeMimePart(MimeBodyPart)}.
     */
    @Test
    public void testRemoveMimePart() {
        MimeBodyPart plainBodyPart = new MimeBodyPart();

        testMail.getMessageParts().clear();
        assertTrue(testMail.getMessageParts().isEmpty());

        testMail.addMimePart(plainBodyPart);
        assertTrue(testMail.getMessageParts().contains(plainBodyPart));

        testMail.removeMimePart(plainBodyPart);
        assertFalse(testMail.getMessageParts().contains(plainBodyPart));
        assertTrue(testMail.getMessageParts().isEmpty());

        try {
            testMail.removeMimePart(null);
            fail("Removing null mimepart should fail");
        } catch (IllegalArgumentException e) {
            // Exception is supposed to be thrown
        }
    }

    /**
     * Test method for {@link MailBackend#removeMimeParts(Set)}.
     */
    @Test
    public void testRemoveMimeParts() {
        Set<MimeBodyPart> parts = new HashSet<MimeBodyPart>(1);
        MimeBodyPart plainBodyPart = new MimeBodyPart();
        parts.add(plainBodyPart);

        testMail.getMessageParts().clear();
        assertTrue(testMail.getMessageParts().isEmpty());

        testMail.addMimeParts(parts);
        assertTrue(testMail.getMessageParts().containsAll(parts));

        testMail.removeMimeParts(parts);
        for (MimeBodyPart mimeBodyPart : parts) {
            assertFalse(testMail.getMessageParts().contains(mimeBodyPart));
        }
        assertTrue(testMail.getMessageParts().isEmpty());

        testMail.removeMimeParts(new HashSet<MimeBodyPart>());
        assertTrue(testMail.getMessageParts().isEmpty());

        try {
            testMail.removeMimePart(null);
            fail("Removing null mimepart should fail");
        } catch (IllegalArgumentException e) {
            // Exception is supposed to be thrown
        }
    }

    /**
     * Test method for {@link MailBackend#setAuthInfo(String, String)} .
     */
    @Test
    public void testSetAuthInfo() {
        testMail.setAuthInfo("decidr", "asd");
        assertEquals("decidr", testMail.getUsername());
        assertEquals("asd", testMail.getPassword());
        testMail.setAuthInfo("decidr", "SdfF");
        assertEquals("decidr", testMail.getUsername());
        assertEquals("SdfF", testMail.getPassword());
        testMail.setAuthInfo("decidr", "");
        assertEquals("decidr", testMail.getUsername());
        assertEquals("", testMail.getPassword());
        testMail.setAuthInfo("decidr", null);
        assertEquals("decidr", testMail.getUsername());
        assertNull(testMail.getPassword());

        testMail.setAuthInfo("DeCiDr", "asd");
        assertEquals("DeCiDr", testMail.getUsername());
        assertEquals("asd", testMail.getPassword());
        testMail.setAuthInfo("DeCiDr", "SdfF");
        assertEquals("DeCiDr", testMail.getUsername());
        assertEquals("SdfF", testMail.getPassword());
        testMail.setAuthInfo("DeCiDr", "");
        assertEquals("DeCiDr", testMail.getUsername());
        assertEquals("", testMail.getPassword());
        testMail.setAuthInfo("DeCiDr", null);
        assertEquals("DeCiDr", testMail.getUsername());
        assertNull(testMail.getPassword());

        testMail.setAuthInfo("", "asd");
        assertNull(testMail.getUsername());
        assertEquals("asd", testMail.getPassword());
        testMail.setAuthInfo("", "SdfF");
        assertNull(testMail.getUsername());
        assertEquals("SdfF", testMail.getPassword());
        testMail.setAuthInfo("", "");
        assertNull(testMail.getUsername());
        assertEquals("", testMail.getPassword());
        testMail.setAuthInfo("", null);
        assertNull(testMail.getUsername());
        assertNull(testMail.getPassword());

        testMail.setAuthInfo(null, "asd");
        assertNull(testMail.getUsername());
        assertEquals("asd", testMail.getPassword());
        testMail.setAuthInfo(null, "SdfF");
        assertNull(testMail.getUsername());
        assertEquals("SdfF", testMail.getPassword());
        testMail.setAuthInfo(null, "");
        assertNull(testMail.getUsername());
        assertEquals("", testMail.getPassword());
        testMail.setAuthInfo(null, null);
        assertNull(testMail.getUsername());
        assertNull(testMail.getPassword());
    }

    /**
     * Test method for {@link MailBackend#setBCC(Set)}.
     */
    @Test
    public void testSetBCCSetOfString() {
        Set<String> validSet = new HashSet<String>();
        Set<String> invalidSet = new HashSet<String>();

        validSet.add("asd@qw.ru");
        validSet.add("asd.efg@qw.ru");

        invalidSet.add("ads@asd@das");

        testMail.setBCC(validSet);
        assertTrue(testMail.getHeaderBCC().contains("asd@qw.ru"));
        assertTrue(testMail.getHeaderBCC().contains("asd.efg@qw.ru"));

        testMail.setBCC((Set<String>) null);
        assertEquals("", testMail.getHeaderBCC());
        testMail.setBCC(new HashSet<String>());
        assertEquals("", testMail.getHeaderBCC());

        try {
            testMail.setBCC(invalidSet);
            fail("Set of invalid addresses should fail");
        } catch (IllegalArgumentException e) {
            assertFalse(testMail.getHeaderBCC().contains("ads@asd@das"));
        }
    }

    /**
     * Test method for {@link MailBackend#setBCC(String)}.
     */
    @Test
    public void testSetBCCString() {
        testMail.setBCC("anonymous@coward.net");
        assertEquals("anonymous@coward.net", testMail.getHeaderBCC());
        testMail.setBCC("anonymousE@coward.net");
        assertEquals("anonymousE@coward.net", testMail.getHeaderBCC());

        testMail.setBCC("");
        assertEquals("", testMail.getHeaderBCC());
        testMail.setBCC((String) null);
        assertEquals("", testMail.getHeaderBCC());

        try {
            testMail.setBCC("in@va@lid");
            fail("invalid format is invalid");
        } catch (IllegalArgumentException e) {
            // The exception is supposed to be thrown
        }
    }

    /**
     * Test method for {@link MailBackend#setBodyText(String)}.
     */
    @Test
    public void testSetBodyText() throws MessagingException, IOException {
        testMail.setBodyText("Testbody");
        assertEquals("text/plain", testMail.getTextPart().getContentType());
        assertEquals("Testbody", testMail.getTextPart().getContent());
        testMail.setBodyText("");
        assertEquals("text/plain", testMail.getTextPart().getContentType());
        assertEquals("", testMail.getTextPart().getContent());

        testMail.setBodyText(null);
        assertNull(testMail.getTextPart());
    }

    /**
     * Test method for {@link MailBackend#setBodyHTML(String)}.
     */
    @Test
    public void testSetBodyHTML() throws MessagingException, IOException {
        testMail.setBodyHTML("<html>Testbody</html>");
        assertEquals("text/html", testMail.getHtmlPart().getContentType());
        assertEquals("<html>Testbody</html>", testMail.getHtmlPart()
                .getContent());
        testMail.setBodyHTML("<html></html>");
        assertEquals("text/html", testMail.getHtmlPart().getContentType());
        assertEquals("<html></html>", testMail.getHtmlPart().getContent());

        testMail.setBodyHTML(null);
        assertNull(testMail.getHtmlPart());
    }

    /**
     * Test method for {@link MailBackend#setCC(Set)}.
     */
    @Test
    public void testSetCCSetOfString() {
        Set<String> validSet = new HashSet<String>();
        Set<String> invalidSet = new HashSet<String>();

        validSet.add("asd@qw.ru");
        validSet.add("asd.efg@qw.ru");

        invalidSet.add("ads@asd@das");

        testMail.setCC(validSet);
        assertTrue(testMail.getHeaderCC().contains("asd@qw.ru"));
        assertTrue(testMail.getHeaderCC().contains("asd.efg@qw.ru"));

        testMail.setCC((Set<String>) null);
        assertEquals("", testMail.getHeaderCC());
        testMail.setCC(new HashSet<String>());
        assertEquals("", testMail.getHeaderCC());

        try {
            testMail.setCC(invalidSet);
            fail("Set of invalid addresses should fail");
        } catch (IllegalArgumentException e) {
            assertFalse(testMail.getHeaderCC().contains("ads@asd@das"));
        }
    }

    /**
     * Test method for {@link MailBackend#setCC(String)}.
     */
    @Test
    public void testSetCCString() {
        testMail.setCC("anonymous@coward.net");
        assertEquals("anonymous@coward.net", testMail.getHeaderCC());
        testMail.setCC("anonymousE@coward.net");
        assertEquals("anonymousE@coward.net", testMail.getHeaderCC());

        testMail.setCC("");
        assertEquals("", testMail.getHeaderCC());
        testMail.setCC((String) null);
        assertEquals("", testMail.getHeaderCC());

        try {
            testMail.setCC("in@va@lid");
            fail("invalid format is invalid");
        } catch (IllegalArgumentException e) {
            // The exception is supposed to be thrown
        }
    }

    /**
     * Test method for {@link MailBackend#setHostname(String)}.
     */
    @Test
    public void testSetHostname() {
        testMail.setHostname("decidr.de");
        assertEquals("decidr.de", testMail.getSMTPServerHost());
        testMail.setHostname("heise.de");
        assertEquals("heise.de", testMail.getSMTPServerHost());
        testMail.setHostname("");
        assertEquals("localhost", testMail.getSMTPServerHost());
        testMail.setHostname(null);
        assertEquals("localhost", testMail.getSMTPServerHost());
    }

    /**
     * Test method for {@link MailBackend#setPassword(String)}.
     */
    @Test
    public void testSetPassword() {
        testMail.setPassword("asd");
        assertEquals("asd", testMail.getPassword());
        testMail.setPassword("SdfF");
        assertEquals("SdfF", testMail.getPassword());
        testMail.setPassword("");
        assertEquals("", testMail.getPassword());
        testMail.setPassword(null);
        assertNull(testMail.getPassword());
    }

    /**
     * Test method for {@link MailBackend#setPortNum(int)}.
     */
    @Test
    public void testSetPortNum() {
        testMail.setPortNum(0);
        assertEquals(0, testMail.getSMTPPortNum());
        testMail.setPortNum(2020);
        assertEquals(2020, testMail.getSMTPPortNum());
        testMail.setPortNum(-1);
        assertEquals(-1, testMail.getSMTPPortNum());
        testMail.setPortNum(-1230);
        assertEquals(-1, testMail.getSMTPPortNum());
    }

    /**
     * Test method for {@link MailBackend#setReceiver(Set)}.
     */
    @Test
    public void testSetReceiverSetOfString() {
        Set<String> validSet = new HashSet<String>();
        Set<String> invalidSet = new HashSet<String>();

        validSet.add("asd@qw.ru");
        validSet.add("asd.efg@qw.ru");

        invalidSet.add("ads@asd@das");

        testMail.setReceiver(validSet);
        assertTrue(testMail.getHeaderTo().contains("asd@qw.ru"));
        assertTrue(testMail.getHeaderTo().contains("asd.efg@qw.ru"));

        try {
            testMail.setReceiver(invalidSet);
            fail("Set of invalid addresses should fail");
        } catch (IllegalArgumentException e) {
            assertFalse(testMail.getHeaderTo().contains("ads@asd@das"));
        }

        try {
            testMail.setReceiver((Set<String>) null);
            fail("Null Set should fail");
        } catch (IllegalArgumentException e) {
            // Exception should be thrown
        }
        try {
            testMail.setReceiver(new HashSet<String>());
            fail("Null Set should fail");
        } catch (IllegalArgumentException e) {
            // Exception should be thrown
        }
    }

    /**
     * Test method for {@link MailBackend#setReceiver(String)}.
     */
    @Test
    public void testSetReceiverString() {
        testMail.setReceiver("anonymous@coward.net");
        assertEquals("anonymous@coward.net", testMail.getHeaderTo());
        testMail.setReceiver("anonymousE@coward.net");
        assertEquals("anonymousE@coward.net", testMail.getHeaderTo());

        try {
            testMail.setReceiver("");
            fail("Empty receiver is invalid");
        } catch (IllegalArgumentException e) {
            // The exception is supposed to be thrown
        }
        try {
            testMail.setReceiver((String) null);
            fail("null receiver is invalid");
        } catch (IllegalArgumentException e) {
            // The exception is supposed to be thrown
        }
        try {
            testMail.setReceiver("in@va@lid");
            fail("invalid format is invalid");
        } catch (IllegalArgumentException e) {
            // The exception is supposed to be thrown
        }
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

        testMail.setServerInfo("", 0);
        assertEquals("localhost", testMail.getSMTPServerHost());
        assertEquals(0, testMail.getSMTPPortNum());
        testMail.setServerInfo(null, 0);
        assertEquals("localhost", testMail.getSMTPServerHost());
        assertEquals(0, testMail.getSMTPPortNum());
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
