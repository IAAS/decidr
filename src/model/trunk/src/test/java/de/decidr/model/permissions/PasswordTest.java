package de.decidr.model.permissions;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import static junit.framework.Assert.*;

public class PasswordTest {

    /**
     * 
     * not a real test case, just needed to create hash and salt for test data
     * 
     */
    @Test
    public void getPasswordForSuperadminInTestDatas() {
        
        String salt ="";
        String hash ="";
        
        try {
            salt = Password.getRandomSalt();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            hash = Password.getHash("user5", salt);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("Salt: "+ salt);
        System.out.println("Hash: "+ hash);
        
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
