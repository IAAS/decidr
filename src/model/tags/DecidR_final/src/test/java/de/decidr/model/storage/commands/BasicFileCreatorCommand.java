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

package de.decidr.model.storage.commands;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import javax.activation.MimetypesFileTypeMap;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.entities.File;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

public class BasicFileCreatorCommand extends AbstractTransactionalCommand {
    /*
     * converts file into ByteArrayOutputStream
     */
    private static ByteArrayOutputStream readFile(java.io.File file)
            throws Exception {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        FileInputStream fileInputStream = new FileInputStream(file
                .getAbsolutePath());

        byte[] buffer = new byte[16384];

        for (int len = fileInputStream.read(buffer); len > 0; len = fileInputStream
                .read(buffer)) {
            byteArrayOutputStream.write(buffer, 0, len);
        }

        fileInputStream.close();

        return byteArrayOutputStream;
    }

    File dataFile;

    java.io.File basicFile;

    public final java.io.File getBasicFile() {
        return basicFile;
    }

    public final File getDataFile() {
        return dataFile;
    }

    @Override
    public void transactionStarted(TransactionEvent evt)
            throws TransactionException {
        basicFile = new java.io.File("./src/test/resources/decidr.jpg");
        assertTrue(basicFile.exists());

        dataFile = new de.decidr.model.entities.File();

        dataFile.setFileName("decidr.jpg");
        dataFile.setMimeType(new MimetypesFileTypeMap()
                .getContentType(basicFile));
        dataFile.setFileSizeBytes(basicFile.length());
        try {
            dataFile.setData(readFile(basicFile).toByteArray());
        } catch (Exception e) {
            throw new TransactionException(e);
        }
        dataFile.setId(123456l);
        dataFile.setCreationDate(DecidrGlobals.getTime().getTime());

        evt.getSession().save(dataFile);
    }
}