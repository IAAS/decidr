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

import static org.junit.Assert.fail;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author Reinhold
 * @version 0.1
 */
public class PasswordTest {

    /**
     * Test method for {@link Password#getHash(String, String)}.
     */
    @Test
    public void testGetHash() {
        // <Password, Salt> mappings
        final Map<String, String> testPWDs = new HashMap<String, String>();
        testPWDs.put("", "");

        fail("Not yet implemented"); // RR getHash
    }

    private String hashHelper(String string) throws NoSuchAlgorithmException {
        String result = null;

        MessageDigest digest= MessageDigest.getInstance(Password.getHashAlgorithm());
        digest.reset();
//        result=digest.
        
        return result;
    }

    /**
     * Test method for {@link Password#getRandomAuthKey()}.
     */
    @Test
    public void testGetRandomAuthKey() {
        fail("Not yet implemented"); // RR getRandomAuthKey
    }

    /**
     * Test method for {@link Password#getRandomSalt()}.
     */
    @Test
    public void testGetRandomSalt() {
        fail("Not yet implemented"); // RR getRandomSalt
    }

    /**
     * Test method for {@link Password#generateRandomPassword()}.
     */
    @Test
    public void testGenerateRandomPassword() {
        fail("Not yet implemented"); // RR generateRandomPassword
    }
}
