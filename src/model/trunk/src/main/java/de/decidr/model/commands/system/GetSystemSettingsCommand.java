package de.decidr.model.commands.system;

import java.util.List;

import org.hibernate.Criteria;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Load the System Settings from the database and copies it in the variable
 * result.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetSystemSettingsCommand extends SystemCommand {

    private SystemSettings result;

    /**
     * Creates a new GetSystemSettingsCommand. The command loads the System
     * Settings from the database and copies it in the variable result.
     * 
     * @param role
     *            user which executes the command
     */
    public GetSystemSettingsCommand(Role role) {
        super(role, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt) throws TransactionException{

        Criteria c = evt.getSession().createCriteria(SystemSettings.class);
        List<SystemSettings> results = c.list();

        if (results.size()>0){
            throw new TransactionException("More than one system settings found, but system settings should be unique");
        }
        else if(results.size()==0){
            throw new EntityNotFoundException(SystemSettings.class);
        }else{
            result = results.get(0);
        }
        
    }

    /**
     * @return the result
     */
    public SystemSettings getResult() {
        return result;
    }
}
