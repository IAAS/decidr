package de.decidr.model.storage.commands;

import java.io.FileInputStream;

import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.HibernateEntityStorageProvider;
import de.decidr.model.transactions.TransactionEvent;

public class RemoveFileTestCommand extends AbstractTransactionalCommand {

    Long fId;
    HibernateEntityStorageProvider storageProvider;
    FileInputStream resultStream;
    

    /**
     * Creates a new {@link RemoveFileTestCommand}
     * 
     * @param role
     * @param fileId
     */
    public RemoveFileTestCommand(Long fileId, HibernateEntityStorageProvider provider) {
        fId = fileId;
        storageProvider = provider;
    }
    
    @Override
    public void transactionStarted(TransactionEvent evt)
            throws TransactionException {

        try {
            storageProvider.removeFile(fId);
        } catch (StorageException e) {
            throw new TransactionException("failure occurred by deleting a file");
        }
        

    }

}
