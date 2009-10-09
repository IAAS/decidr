package de.decidr.model.storage.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.HibernateEntityStorageProvider;
import de.decidr.model.transactions.TransactionEvent;

public class PutFileTestFaultyCommand extends AbstractTransactionalCommand {

    java.io.File basicFile;
    Long fId;
    HibernateEntityStorageProvider storageProvider;
    boolean result;

    /**
     * Creates a new {@link PutFileTestFaultyCommand}
     * 
     * @param role
     * @param fileId
     */
    public PutFileTestFaultyCommand(Long fileId, File file,
            HibernateEntityStorageProvider provider) {
        
        basicFile = file;
        fId = fileId;
        storageProvider = provider;
    }

    public boolean getResult(){
        return result;
    }
    
    @Override
    public void transactionStarted(TransactionEvent evt)
            throws TransactionException {

        try {
            storageProvider.putFile((FileInputStream)null, fId, basicFile.length());
            result=false;
        } catch (IllegalArgumentException e) {
            //nothing to do
        } catch (StorageException e) {
            result=false;
        }
        
        
        
        try {
            storageProvider.putFile(new FileInputStream(basicFile.getAbsolutePath()), (Long)null, basicFile.length());
            result=false;
        } catch (IllegalArgumentException e) {
            //nothing to do
        } catch (StorageException e) {
            result=false;
        } catch (FileNotFoundException e) {
            result=false;
        }
        
        
        try {
            storageProvider.putFile(new FileInputStream(basicFile.getAbsolutePath()), fId, (Long)null);
            result=false;
        } catch (IllegalArgumentException e) {
            //nothing to do
        } catch (StorageException e) {
            result=false;
        } catch (FileNotFoundException e) {
            result=false;
        }

    }

}
