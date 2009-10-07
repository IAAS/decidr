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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.Properties;

import javax.activation.MimetypesFileTypeMap;

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
     * converts file into byte[]
     * 
     */
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
    
    
    /*
     * converts InputStream into byte[]
     * 
     */
    private static byte[] readInputStream(FileInputStream stream) throws Exception {
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        
        FileInputStream fileInputStream = stream;
 
        byte[] buffer = new byte[16384];
 
        for (int len = fileInputStream.read(buffer); len > 0; len = fileInputStream
                .read(buffer)) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
 
        fileInputStream.close();
 
        return byteArrayOutputStream.toByteArray();
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


    /**
     * Test method for
     * {@link LocalStorageProvider#putFile(FileInputStream, Long)}. 
     * @throws Exception 
     */
    @Test
    public void testPutFileGetFile() throws Exception {
        
        StorageProvider.putFile(new FileInputStream(BasicFile.getAbsolutePath()), 123456l,BasicFile.length());
        
        FileInputStream stream = new FileInputStream("./src/test/java/decidr.jpg");
        
        byte[] In = readInputStream(stream);
        byte[] Out = readInputStream((FileInputStream)StorageProvider.getFile(123456l));
        
        assertTrue(java.util.Arrays.equals(In, Out));
        
        
        try{
            StorageProvider.putFile((FileInputStream)null,123l,BasicFile.length());
            fail("IllegalArgumentException expected");
        }
        catch(IllegalArgumentException e){
            //nothing to do
        }
        
        
        try{
            StorageProvider.putFile(stream,(Long)null,BasicFile.length());
            fail("IllegalArgumentException expected");
        }
        catch(IllegalArgumentException e){
            //nothing to do
        }
        
        try{
            StorageProvider.putFile(stream,123l,(Long)null);
            fail("IllegalArgumentException expected");
        }
        catch(IllegalArgumentException e){
            //nothing to do
        }
        
    } 


    /**
     * Test method for {@link LocalStorageProvider#removeFile(Long)}.
     * @throws StorageException 
     */
    @Test
    public void testRemoveFile() throws StorageException {
        
        StorageProvider.removeFile(123456l);
        
        try{
            StorageProvider.getFile(123456l);
            fail("StorageException expected");
        }
        catch (StorageException e){
            //nothing to do
        }
        
              
        try{
            StorageProvider.removeFile(999999l);        // this file does'nt exist
        }
        catch(Exception e){
            fail("No Exception should be thrown when an non existing file should be deleted");
        }
        
        
        try{
            StorageProvider.removeFile((Long)null);
            fail("IllegalArgumentException expected");
        }
        catch(IllegalArgumentException e){
            //nothing to do
        } 

        
    }
    
    
    
    /**
     * Test method for {@link LocalStorageProvider#LocalStorageProvider()}.
     */
    @Test
    public void testLocalStorageProvider() {
        fail("Not yet implemented"); // MF LocalStorageProvider
    }

    /**
     * Test method for {@link LocalStorageProvider#applyConfig(Properties)}.
     */
    @Test
    public void testApplyConfig() {
        fail("Not yet implemented"); // MF applyConfig
    }

 
    /**
     * Test method for {@link LocalStorageProvider#isApplicable(Properties)}.
     */
    @Test
    public void testIsApplicable() {
        fail("Not yet implemented"); // MF isApplicable
    }

    
    
}
