package de.decidr.model.permissions;

import static junit.framework.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.acl.Password;

public class PasswordTest {

    /**
     * not a real test case, just needed to create hash and salt for test data<br>
     * 
     */
    @BeforeClass
    public static void getPasswordForSuperadminInTestDatas() {

        String salt = "";
        String hash = "";

        try {
            salt = Password.getRandomSalt();
        } catch (NoSuchAlgorithmException e) {
            fail("Problem by getting Salt");
        } catch (UnsupportedEncodingException e) {
            fail("Problem by getting Salt");
        }
        try {
            hash = Password.getHash("user5", salt);
        } catch (NoSuchAlgorithmException e) {
            fail("Problem by getting Hash");
        } catch (UnsupportedEncodingException e) {
            fail("Problem by getting Hash");
        }

        System.out.println("Salt: " + salt);
        System.out.println("Hash: " + hash);
    }

    @Test
    public void testGetRandomAuthKey() {
        fail("not implemented");
    }

    @Test
    public void testGetRandomSalt() {
        fail("not implemented");
    }
}
