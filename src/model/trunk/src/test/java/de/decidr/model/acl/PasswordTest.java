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

package de.decidr.model.acl;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.decidr.model.logging.DefaultLogger;

/**
 * Test cases for <code>{@link Password}</code>.
 * 
 * @author Reinhold
 * @version 0.1
 */
public class PasswordTest {

    /**
     * Test method for {@link Password#getHash(String, String)}.
     */
    @Test
    public void testGetHash() throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        final List<String> testPWDs = new ArrayList<String>();
        final List<String> testSalts = new ArrayList<String>();
        fillTestPWDs(testPWDs);

        final Map<String, String> evilPWDs = new HashMap<String, String>();
        evilPWDs.put(null, null);
        evilPWDs.put("", null);
        evilPWDs.put(null, "");

        String resultHash1;
        String resultHash2;
        for (String pwd : testPWDs) {
            for (String salt : testSalts) {
                DefaultLogger.getLogger(PasswordTest.class).info(
                        "Testing password \"" + pwd + "\" with salt \"" + salt
                                + "\"");
                resultHash1 = Password.getHash(pwd, salt);
                resultHash2 = Password.getHash(pwd, salt);

                assertNotNull(resultHash1);
                assertNotNull(resultHash2);

                assertEquals(resultHash1, resultHash2);
                assertEquals(hashHelper(pwd, salt), resultHash1);
            }
        }

        for (String pwd : evilPWDs.keySet()) {
            try {
                Password.getHash(pwd, evilPWDs.get(pwd));
                fail("Hashing \"" + pwd + "\" with salt \"" + evilPWDs.get(pwd)
                        + "\" succeeded!");
            } catch (Exception e) {
                // needs to be thrown
            }
        }

    }

    private void fillTestPWDs(final List<String> testPWDs) {
        testPWDs.add("");
        testPWDs.add("!");
        // ü
        testPWDs.add("\u00fc");
        // ö
        testPWDs.add("\u00f6");
        // ä
        testPWDs.add("\u00e4");
        // ß
        testPWDs.add("\u00df");
        // Ü
        testPWDs.add("\u00dc");
        // Ö
        testPWDs.add("\u00d6");
        // Ä
        testPWDs.add("\u00c4");
        testPWDs.add("\"");
        testPWDs.add("\\");
        testPWDs.add("§");
        testPWDs.add("$");
        testPWDs.add("%");
        testPWDs.add("&");
        testPWDs.add("/");
        testPWDs.add("(");
        testPWDs.add(")");
        testPWDs.add("=");
        testPWDs.add("?");
        testPWDs.add("`");
        testPWDs.add("'");
        testPWDs.add("*");
        testPWDs.add("+");
        testPWDs.add("#");
        testPWDs.add("~");
        testPWDs.add("¸");
        testPWDs.add("]");
        testPWDs.add("}");
        testPWDs.add("[");
        testPWDs.add("{");
        testPWDs.add("¬");
        testPWDs.add("½");
        testPWDs.add("¼");
        testPWDs.add("³");
        testPWDs.add("²");
        testPWDs.add("¹");
        testPWDs.add("^");
        testPWDs.add("°");
        testPWDs.add("@");
        testPWDs.add("€");
        testPWDs.add("µ");
        testPWDs.add("<");
        testPWDs.add(">");
        testPWDs.add("|");
        testPWDs.add(",");
        testPWDs.add(".");
        testPWDs.add(";");
        testPWDs.add(":");
        testPWDs.add("-");
        testPWDs.add("_");
        testPWDs.add("ł");
        testPWDs.add("¶");
        testPWDs.add("ŧ");
        testPWDs.add("←");
        testPWDs.add("↓");
        testPWDs.add("→");
        testPWDs.add("ø");
        testPWDs.add("þ");
        testPWDs.add("¨");
        testPWDs.add("æ");
        testPWDs.add("ſ");
        testPWDs.add("ð");
        testPWDs.add("đ");
        testPWDs.add("ŋ");
        testPWDs.add("ħ");
        testPWDs.add("˝");
        testPWDs.add("»");
        testPWDs.add("«");
        testPWDs.add("¢");
        testPWDs.add("„");
        testPWDs.add("“");
        testPWDs.add("”");
        testPWDs.add("·");
        testPWDs.add("…");
        testPWDs.add("Ω");
        testPWDs.add("Ł");
        testPWDs.add("®");
        testPWDs.add("Ŧ");
        testPWDs.add("¥");
        testPWDs.add("↑");
        testPWDs.add("ı");
        testPWDs.add("Ø");
        testPWDs.add("Þ");
        testPWDs.add("¯");
        testPWDs.add("Æ");
        testPWDs.add("ẞ");
        testPWDs.add("Ð");
        testPWDs.add("ª");
        testPWDs.add("Ŋ");
        testPWDs.add("Ħ");
        testPWDs.add("˙");
        testPWDs.add("¦");
        testPWDs.add("›");
        testPWDs.add("‹");
        testPWDs.add("©");
        testPWDs.add("‘");
        testPWDs.add("º");
        testPWDs.add("×");
        testPWDs.add("÷");
        testPWDs.add("˙");
        testPWDs.add("⅛");
        testPWDs.add("¡");
        testPWDs.add("£");
        testPWDs.add("¤");
        testPWDs.add("⅜");
        testPWDs.add("⅝");
        testPWDs.add("⅞");
        testPWDs.add("™");
        testPWDs.add("±");
        testPWDs.add("¿");
        testPWDs.add("\t");
        testPWDs.add("\n");
        testPWDs.add("\f");
        testPWDs.add("\r");
        testPWDs.add("\b");
        testPWDs.add("\'");
        testPWDs.add("ё");
        testPWDs.add("Ё");
        testPWDs.add("й");
        testPWDs.add("Й");
        testPWDs.add("ц");
        testPWDs.add("Ц");
        testPWDs.add("у");
        testPWDs.add("У");
        testPWDs.add("к");
        testPWDs.add("К");
        testPWDs.add("е");
        testPWDs.add("Е");
        testPWDs.add("Н");
        testPWDs.add("н");
        testPWDs.add("г");
        testPWDs.add("Г");
        testPWDs.add("ш");
        testPWDs.add("Ш");
        testPWDs.add("щ");
        testPWDs.add("Щ");
        testPWDs.add("з");
        testPWDs.add("З");
        testPWDs.add("х");
        testPWDs.add("Х");
        testPWDs.add("ъ");
        testPWDs.add("Ъ");
        testPWDs.add("ф");
        testPWDs.add("Ф");
        testPWDs.add("ы");
        testPWDs.add("Ы");
        testPWDs.add("в");
        testPWDs.add("В");
        testPWDs.add("а");
        testPWDs.add("А");
        testPWDs.add("п");
        testPWDs.add("П");
        testPWDs.add("р");
        testPWDs.add("Р");
        testPWDs.add("о");
        testPWDs.add("О");
        testPWDs.add("л");
        testPWDs.add("Л");
        testPWDs.add("д");
        testPWDs.add("Д");
        testPWDs.add("ж");
        testPWDs.add("Ж");
        testPWDs.add("э");
        testPWDs.add("Э");
        testPWDs.add("я");
        testPWDs.add("Я");
        testPWDs.add("ч");
        testPWDs.add("Ч");
        testPWDs.add("С");
        testPWDs.add("с");
        testPWDs.add("м");
        testPWDs.add("М");
        testPWDs.add("и");
        testPWDs.add("И");
        testPWDs.add("т");
        testPWDs.add("Т");
        testPWDs.add("ь");
        testPWDs.add("Ь");
        testPWDs.add("б");
        testPWDs.add("Б");
        testPWDs.add("ю");
        testPWDs.add("Ю");
        testPWDs.add("asdf");
        testPWDs.add("AsDF");
        testPWDs.add("123");
        testPWDs.add("asdf123");
        testPWDs.add("aSdF321");
        testPWDs.add("adsf456§$%&");
        testPWDs.add("!saf34§/&");
        testPWDs.add("4ghf)&%");
        testPWDs.add("aDF3Fg%43&fDDeg!}");
        testPWDs.add("фаил");
        testPWDs.add("ФаИл");
    }

    private String hashHelper(String passwd, String salt)
            throws NoSuchAlgorithmException {
        String result = null;

        MessageDigest digest = MessageDigest.getInstance(Password
                .getHashAlgorithm());

        digest.reset();
        result = new String(digest.digest((salt + passwd).getBytes()));

        digest.reset();
        result = new String(digest.digest((result).getBytes()));

        return result;
    }

    /**
     * Test method for {@link Password#getRandomAuthKey()}.
     */
    @Test
    public void testGetRandomAuthKey() throws UnsupportedEncodingException,
            NoSuchAlgorithmException {
        String authKey = Password.getRandomAuthKey();
        String compareKey;

        assertNotNull(authKey);
        assertTrue(authKey.length() <= 64);

        for (int i = 0; i < 1000; i++) {
            compareKey = Password.getRandomAuthKey();

            assertNotNull(compareKey);
            assertTrue(compareKey.length() <= 64);
            assertFalse(compareKey.equals(authKey));
        }
    }

    /**
     * Test method for {@link Password#getRandomSalt()}.
     */
    @Test
    public void testGetRandomSalt() throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        String salt = Password.getRandomSalt();
        String compareSalt;

        assertNotNull(salt);
        assertTrue(salt.length() <= 128);

        for (int i = 0; i < 1000; i++) {
            compareSalt = Password.getRandomSalt();

            assertNotNull(compareSalt);
            assertTrue(compareSalt.length() <= 128);
            assertFalse(compareSalt.equals(salt));
        }
    }

    /**
     * Test method for {@link Password#generateRandomPassword()}.
     */
    @Test
    public void testGenerateRandomPassword() {
        String passwd = Password.generateRandomPassword();
        String comparePasswd;

        assertNotNull(passwd);
        // RR length and other constraints
        assertTrue(passwd.length() <= 12);

        for (int i = 0; i < 1000; i++) {
            comparePasswd = Password.generateRandomPassword();

            assertNotNull(comparePasswd);
            assertTrue(comparePasswd.length() <= 12);
            assertFalse(comparePasswd.equals(passwd));
        }
    }
    
    /**
     * Test method for {@link Password#getDigestNotation(byte[], int, char[])}
     */
    public void testgetDigestNotation() {
        //RR
    }
}
