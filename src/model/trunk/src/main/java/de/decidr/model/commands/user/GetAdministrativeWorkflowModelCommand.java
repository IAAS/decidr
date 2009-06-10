package de.decidr.model.commands.user;

import java.util.List;
import org.hibernate.Query;
import de.decidr.model.entities.UserAdministratesWorkflowModel;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class GetAdministrativeWorkflowModelCommand extends UserCommand {

    private Long userId; 
    private List<WorkflowModel> result;
    
   /**
    * 
    * Creates a new getAdministrativeWorkflowModelCommand. This command will
    * write all WorkflowModels which the user administrates as a list in the result
    * variable.
    * 
    * @param role user which executes the command
    * @param userId
    */
    public GetAdministrativeWorkflowModelCommand(Role role,Long userId) {
        super(role, null);
        
        this.userId=userId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        
        List<UserAdministratesWorkflowModel> relationSet;
        
        Query q = evt.getSession().createQuery("elect UserAdministratesWorkflowModel where user.id=:id");
        q.setLong("id", userId);
        relationSet = q.list();
        
        for(UserAdministratesWorkflowModel model:relationSet){
            result.add(model.getWorkflowModel());
        }
        
    }

    /**
     * @return the result
     */
    public List<WorkflowModel> getResult() {
        return result;
    }
    
    

}
