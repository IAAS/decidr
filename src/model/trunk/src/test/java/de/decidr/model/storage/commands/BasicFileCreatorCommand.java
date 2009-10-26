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
    File dataFile;
    java.io.File basicFile;

    public final File getDataFile() {
        return dataFile;
    }

    public final java.io.File getBasicFile() {
        return basicFile;
    }

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

    @Override
    public void transactionStarted(TransactionEvent evt)
            throws TransactionException {
        basicFile = new java.io.File("./src/test/java/decidr.jpg");
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