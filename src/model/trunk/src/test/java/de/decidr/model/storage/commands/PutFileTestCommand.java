package de.decidr.model.storage.commands;

import java.io.File;
import java.io.FileInputStream;

import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.HibernateEntityStorageProvider;
import de.decidr.model.transactions.TransactionEvent;

public class PutFileTestCommand extends AbstractTransactionalCommand {

    java.io.File basicFile;
    Long fId;
    HibernateEntityStorageProvider storageProvider;

    /**
     * Creates a new {@link PutFileTestCommand}
     */
    public PutFileTestCommand(Long fileId, File file,
            HibernateEntityStorageProvider provider) {
        
        basicFile = file;
        fId = fileId;
        storageProvider = provider;
    }

    @Override
    public void transactionStarted(TransactionEvent evt)
            throws TransactionException {

        try {
            storageProvider.putFile(new FileInputStream(basicFile
                    .getAbsolutePath()), fId, basicFile.length());
        } catch (Exception e) {
            throw new TransactionException(e);
        }
    }
}
