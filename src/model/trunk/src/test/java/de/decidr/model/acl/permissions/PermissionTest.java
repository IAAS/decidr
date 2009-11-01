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

package de.decidr.model.acl.permissions;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.testing.DecidrAclTest;

/**
 * TODO: add comment
 * 
 * @author GH
 */
public class PermissionTest extends DecidrAclTest {
    private static List<String> strings = new ArrayList<String>();

    @BeforeClass
    public static void setUpBeforeClass() {
        fillStringList();
    }

    @AfterClass
    public static void cleanUpAfterClass() {
        strings = null;
    }

    /**
     * Test method for {@link Permission#Permission(String)}.
     */
    @Test
    public void testPermissionString() {
        Permission perm = null;

        for (String s : strings) {
            perm = new Permission(s);
            assertNotNull(perm);
            assertTrue(perm.getName().equals(s));
        }

        try {
            perm = new Permission("");
            fail("Name cannot be empty");
        } catch (Exception e) {
            // needs to be thrown
        }

    }

    /**
     * Test method for {@link Permission#equals(Object)}.
     */
    @Test
    public void testEqualsObject() {
        FileDeletePermission filePerm = new FileDeletePermission(1l);
        FileDeletePermission filePerm2 = filePerm;
        FileReadPermission filePermRead = new FileReadPermission(1l);

        assertTrue(filePerm.equals(filePerm2));
        assertFalse(filePerm.equals(filePermRead));
        assertFalse(filePermRead.equals(filePerm));
        assertFalse(filePerm.equals(null));
    }

    /**
     * Test method for {@link Permission#getNextImplyingPermission()}.
     */
    @Test
    public void testGetNextImplyingPermission() {
//        // gh I don't think there's a lot you can test here using the existing
//        // permissions because
//        // you cannot make any assumptions about the internal naming of these
//        // permissions.
//        // A possible workaround is creating your own permissions:
//        // new Permission("Known.Permission.Identifier.*");
//        // Now you know what to expect when calling getNextImplyingPermission
//        // ~dh
//
//        FileDeletePermission filePerm1 = new FileDeletePermission(1l);
//        assertTrue(filePerm1.getNextImplyingPermission().getName()
//                .endsWith("*"));
//
//        FileReadPermission filePerm2 = new FileReadPermission(1l);
//        assertTrue(filePerm2.getNextImplyingPermission().getName()
//                .endsWith("*"));
//
//        // gh file delete permissions vs. file read permissions :
//        // File.Delete.[ID] vs. File.Read.[ID]
//        // so the next implying permissions are File.Delete.* vs. File.Read.*
//        // ~dh
//        assertTrue(filePerm1.getNextImplyingPermission().getName().equals(
//                filePerm2.getNextImplyingPermission().getName()));
//
//        // gh this test requires knowledge about the internal naming of the
//        // permission object. ~dh
//        assertTrue(filePerm1.getNextImplyingPermission().getName().equals(
//                "de.decidr.model.entities.*"));
    }

    private static void fillStringList() {
        strings.add("!");
        // ü
        strings.add("\u00fc");
        // ö
        strings.add("\u00f6");
        // ä
        strings.add("\u00e4");
        // ß
        strings.add("\u00df");
        // Ü
        strings.add("\u00dc");
        // Ö
        strings.add("\u00d6");
        // Ä
        strings.add("\u00c4");
        strings.add("\"");
        strings.add("\\");
        strings.add("§");
        strings.add("$");
        strings.add("%");
        strings.add("&");
        strings.add("/");
        strings.add("(");
        strings.add(")");
        strings.add("=");
        strings.add("?");
        strings.add("`");
        strings.add("'");
        strings.add("*");
        strings.add("+");
        strings.add("#");
        strings.add("~");
        strings.add("¸");
        strings.add("]");
        strings.add("}");
        strings.add("[");
        strings.add("{");
        strings.add("¬");
        strings.add("½");
        strings.add("¼");
        strings.add("³");
        strings.add("²");
        strings.add("¹");
        strings.add("^");
        strings.add("°");
        strings.add("@");
        strings.add("€");
        strings.add("µ");
        strings.add("<");
        strings.add(">");
        strings.add("|");
        strings.add(",");
        strings.add(".");
        strings.add(";");
        strings.add(":");
        strings.add("-");
        strings.add("_");
        strings.add("ł");
        strings.add("¶");
        strings.add("ŧ");
        strings.add("←");
        strings.add("↓");
        strings.add("→");
        strings.add("ø");
        strings.add("þ");
        strings.add("¨");
        strings.add("æ");
        strings.add("ſ");
        strings.add("ð");
        strings.add("đ");
        strings.add("ŋ");
        strings.add("ħ");
        strings.add("˝");
        strings.add("»");
        strings.add("«");
        strings.add("¢");
        strings.add("„");
        strings.add("“");
        strings.add("”");
        strings.add("·");
        strings.add("…");
        strings.add("Ω");
        strings.add("Ł");
        strings.add("®");
        strings.add("Ŧ");
        strings.add("¥");
        strings.add("↑");
        strings.add("ı");
        strings.add("Ø");
        strings.add("Þ");
        strings.add("¯");
        strings.add("Æ");
        strings.add("ẞ");
        strings.add("Ð");
        strings.add("ª");
        strings.add("Ŋ");
        strings.add("Ħ");
        strings.add("˙");
        strings.add("¦");
        strings.add("›");
        strings.add("‹");
        strings.add("©");
        strings.add("‘");
        strings.add("º");
        strings.add("×");
        strings.add("÷");
        strings.add("˙");
        strings.add("⅛");
        strings.add("¡");
        strings.add("£");
        strings.add("¤");
        strings.add("⅜");
        strings.add("⅝");
        strings.add("⅞");
        strings.add("™");
        strings.add("±");
        strings.add("¿");
        strings.add("\t");
        strings.add("\n");
        strings.add("\f");
        strings.add("\r");
        strings.add("\b");
        strings.add("\'");
        strings.add("ё");
        strings.add("Ё");
        strings.add("й");
        strings.add("Й");
        strings.add("ц");
        strings.add("Ц");
        strings.add("у");
        strings.add("У");
        strings.add("к");
        strings.add("К");
        strings.add("е");
        strings.add("Е");
        strings.add("Н");
        strings.add("н");
        strings.add("г");
        strings.add("Г");
        strings.add("ш");
        strings.add("Ш");
        strings.add("щ");
        strings.add("Щ");
        strings.add("з");
        strings.add("З");
        strings.add("х");
        strings.add("Х");
        strings.add("ъ");
        strings.add("Ъ");
        strings.add("ф");
        strings.add("Ф");
        strings.add("ы");
        strings.add("Ы");
        strings.add("в");
        strings.add("В");
        strings.add("а");
        strings.add("А");
        strings.add("п");
        strings.add("П");
        strings.add("р");
        strings.add("Р");
        strings.add("о");
        strings.add("О");
        strings.add("л");
        strings.add("Л");
        strings.add("д");
        strings.add("Д");
        strings.add("ж");
        strings.add("Ж");
        strings.add("э");
        strings.add("Э");
        strings.add("я");
        strings.add("Я");
        strings.add("ч");
        strings.add("Ч");
        strings.add("С");
        strings.add("с");
        strings.add("м");
        strings.add("М");
        strings.add("и");
        strings.add("И");
        strings.add("т");
        strings.add("Т");
        strings.add("ь");
        strings.add("Ь");
        strings.add("б");
        strings.add("Б");
        strings.add("ю");
        strings.add("Ю");
        strings.add("asdf");
        strings.add("AsDF");
        strings.add("123");
        strings.add("asdf123");
        strings.add("aSdF321");
        strings.add("adsf456§$%&");
        strings.add("!saf34§/&");
        strings.add("4ghf)&%");
        strings.add("aDF3Fg%43&fDDeg!}");
        strings.add("фаил");
        strings.add("ФаИл");
    }

}
