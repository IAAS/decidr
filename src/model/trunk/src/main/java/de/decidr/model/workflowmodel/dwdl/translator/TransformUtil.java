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

package de.decidr.model.workflowmodel.dwdl.translator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.wsdl.Definition;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import de.decidr.model.workflowmodel.dwdl.Workflow;

/**
 * A simple utility class for standard transformations
 *
 * @author Modood Alvi
 * @version 0.1
 */
public class TransformUtil {
    
    public static byte[] fileToBytes(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
    
    public static Workflow bytesToDWDLWorkflow(byte[] dwdl) throws JAXBException{
        JAXBContext dwdlCntxt = JAXBContext.newInstance(Workflow.class);
        Unmarshaller dwdlUnmarshaller = dwdlCntxt.createUnmarshaller();
        JAXBElement<Workflow> dwdlElement = dwdlUnmarshaller.unmarshal(
                new StreamSource(new ByteArrayInputStream(dwdl)),
                Workflow.class);
        return dwdlElement.getValue();
    }
    
    public static Definition bytesToWSDLDefinition(byte[] wsdl){
        return null;
    }

}
