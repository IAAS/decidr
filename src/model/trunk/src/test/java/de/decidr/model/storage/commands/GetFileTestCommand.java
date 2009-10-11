package de.decidr.model.storage.commands;

import java.io.FileInputStream;

import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.HibernateEntityStorageProvider;
import de.decidr.model.transactions.TransactionEvent;

public class GetFileTestCommand extends AbstractTransactionalCommand {

    Long fId;
    HibernateEntityStorageProvider storageProvider;
    FileInputStream resultStream;
    

    /**
     * Creates a new {@link GetFileTestCommand}
     * 
     * @param role
     * @param fileId
     */
    public GetFileTestCommand(Long fileId, HibernateEntityStorageProvider provider) {
        fId = fileId;
        storageProvider = provider;
    }
    
    public FileInputStream getResultStrem(){
        return resultStream;
    }

    @Override
    public void transactionStarted(TransactionEvent evt)
            throws TransactionException {

        try {
            resultStream = (FileInputStream) storageProvider.getFile(fId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new TransactionException(e);
        }

    }

}
