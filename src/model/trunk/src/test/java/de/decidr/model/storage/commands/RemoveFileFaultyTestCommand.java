package de.decidr.model.storage.commands;

import java.io.FileInputStream;
import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.HibernateEntityStorageProvider;
import de.decidr.model.transactions.TransactionEvent;

public class RemoveFileFaultyTestCommand extends AbstractTransactionalCommand {

    Long fId;
    HibernateEntityStorageProvider storageProvider;
    FileInputStream resultStream;
    boolean error;
    

    /**
     * Creates a new {@link RemoveFileFaultyTestCommand}
     * 
     * @param role
     * @param fileId
     */
    public RemoveFileFaultyTestCommand(Long fileId, HibernateEntityStorageProvider provider) {
        fId = fileId;
        storageProvider = provider;
        error=true;
    }
    
    public boolean getResult(){
        return error;
    }
    
    @Override
    public void transactionStarted(TransactionEvent evt)
            throws TransactionException {

        try {
            storageProvider.removeFile((Long)null);
            error = false;
        } catch (IllegalArgumentException e) {
            //nothing to do
        } catch (StorageException e) {
            error = false;
        }
    }
}
