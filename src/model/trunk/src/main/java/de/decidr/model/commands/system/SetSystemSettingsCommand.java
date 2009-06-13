package de.decidr.model.commands.system;

import java.util.List;

import org.apache.log4j.Level;
import org.hibernate.Query;

import de.decidr.model.entities.SystemSettings;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Sets the System Settings.
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class SetSystemSettingsCommand extends SystemCommand {

    private Level loglevel;
    private Boolean autoAcceptNewTenants;
       
/**
 * Sets the system settings loglevel and autoAcceptNewTenants.
 * 
 * @param role the user which executes the command
 * @param loglevel
 * @param autoAcceptNewTenants
 */
    public SetSystemSettingsCommand(Role role, Level loglevel, Boolean autoAcceptNewTenants) {
        super(role, null);
        
        this.loglevel=loglevel;
        this.autoAcceptNewTenants=autoAcceptNewTenants;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt) throws TransactionException {
        
        Query q = evt.getSession().createQuery("from SystemSettings");
        List<SystemSettings> results = q.list();
        SystemSettings newSettings = null;
        
        if(results.size()>1){
            throw new TransactionException("More than one SystemSettings found, but SystemSettings must be unique");
        }
        else if(results.size()==0){
            throw new EntityNotFoundException(SystemSettings.class);
        }else{
            newSettings = results.get(0);
            newSettings.setAutoAcceptNewTenants(autoAcceptNewTenants);
            newSettings.setLogLevel(loglevel.toString());
            evt.getSession().update(newSettings);
        }  

       
        
    }

}
