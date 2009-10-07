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

package de.decidr.model.storage;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import javax.activation.MimetypesFileTypeMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.decidr.model.entities.File;
import de.decidr.model.exceptions.StorageException;

/**
 * MF: add comment
 * 
 * @author Markus Fischer
 */
public class LocalStorageProviderTest {
  
    static LocalStorageProvider StorageProvider;
    static File DataFile;
    static java.io.File BasicFile;
    
    /*
     * reads a test file
     * 
     */
    @SuppressWarnings("unused")
    private static byte[] readFile(java.io.File file) throws Exception {
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        
        FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
 
        byte[] buffer = new byte[16384];
 
        for (int len = fileInputStream.read(buffer); len > 0; len = fileInputStream
                .read(buffer)) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
 
        fileInputStream.close();
 
        return byteArrayOutputStream.toByteArray();
    }
    
    @BeforeClass
    public static void disable() {
        //fail("This test class has not yet been implemented");
    }

   @BeforeClass
    public static void setUpBeforeClass() throws Exception {
       
       BasicFile = new java.io.File("./src/test/java/decidr.jpg");
       
       DataFile = new de.decidr.model.entities.File();
       
       DataFile.setFileName("decidr.jpg");
       DataFile.setMimeType(new MimetypesFileTypeMap().getContentType(BasicFile));
       DataFile.setFileSizeBytes(BasicFile.length());
       DataFile.setData(readFile(BasicFile));
       DataFile.setId(123456l);
       
       StorageProvider = new LocalStorageProvider();
       
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // MF implement
    }

    @Before
    public void setUp() throws Exception {
        // MF implement
    }

    @After
    public void tearDown() throws Exception {
        // MF implement
    }

    /**
     * Test method for {@link LocalStorageProvider#LocalStorageProvider()}.
     */
    @Test
    public void testLocalStorageProvider() {
        //fail("Not yet implemented"); // MF LocalStorageProvider
    }

    /**
     * Test method for {@link LocalStorageProvider#applyConfig(Properties)}.
     */
    @Test
    public void testApplyConfig() {
        //fail("Not yet implemented"); // MF applyConfig
    }

    /**
     * Test method for {@link LocalStorageProvider#getFile(Long)}.
     */
    @Test
    public void testGetFile() {
        //fail("Not yet implemented"); // MF getFile
    }

    /**
     * Test method for {@link LocalStorageProvider#isApplicable(Properties)}.
     */
    @Test
    public void testIsApplicable() {
        //fail("Not yet implemented"); // MF isApplicable
    }

    /**
     * Test method for
     * {@link LocalStorageProvider#putFile(FileInputStream, Long)}. 
     */
    @Test
    public void testPutFile() throws FileNotFoundException, StorageException {
        
        StorageProvider.putFile(new FileInputStream(BasicFile.getAbsolutePath()), 123456l);
        
        // MF dummes assert, da es nicht die Inhalte der Objekte prüft
        assertEquals(new FileInputStream("/src/test/java/decidr.jpg"), StorageProvider.getFile(123456l));
        
    } 

    /**
     * Test method for {@link LocalStorageProvider#removeFile(Long)}.
     */
    @Test
    public void testRemoveFile() {
        //fail("Not yet implemented"); // MF removeFile
    }
}
