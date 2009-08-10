package de.decidr.model.commands.workflowinstance;

import java.util.HashSet;
import java.util.Set;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserParticipatesInWorkflow;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Saves all participants of the given WorkflowInstance at the
 * result variable.
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class GetParticipatingUsersCommand extends WorkflowInstanceCommand {

    private Long workflowInstanceId;
    private Set<User> result;
    
    public GetParticipatingUsersCommand(Role role,
            Long WorkflowInstanceId) {
        super(role, null, WorkflowInstanceId);
        this.workflowInstanceId = WorkflowInstanceId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt) {
      
        Set<UserParticipatesInWorkflow> partUsers = new HashSet();
        result = new HashSet();
        
        WorkflowInstance instance = (WorkflowInstance)evt.getSession().load(WorkflowInstance.class, workflowInstanceId);
        
        partUsers = instance.getUserParticipatesInWorkflows();
        
        
        for(UserParticipatesInWorkflow partuser:partUsers){
            result.add(partuser.getUser());
        }
        
    }

    /**
     * @return the result
     */
    public Set<User> getResult() {
        return result;
    }


}
