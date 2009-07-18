package de.decidr.model.commands.user;

/**
 * Confirms the given
 * invitation and sets the relation between the object given by the invitation.
 */
import org.hibernate.Query;

import de.decidr.model.LifetimeValidator;
import de.decidr.model.entities.Invitation;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserAdministratesWorkflowModel;
import de.decidr.model.entities.UserIsMemberOfTenant;
import de.decidr.model.entities.UserParticipatesInWorkflow;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class ConfirmInviationCommand extends UserCommand {

    private Long invitationId;

    /**
     * 
     * Creates a new ConfirmInviationCommand. This Command confirms the given
     * invitation and sets the relation between the object given by the
     * invitation.
     * 
     * @param role
     * @param invitationId
     */
    public ConfirmInviationCommand(Role role, Long invitationId) {
        super(role, null);

        this.invitationId = invitationId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        
        Invitation i = (Invitation) evt.getSession().load(Invitation.class, invitationId);
        
        LifetimeValidator.isInvitationValid(i);
        
        if (i.getAdministrateWorkflowModel() != null){
            
            // add to tenant if user isn't tenant member
            User user = i.getReceiver();
            Tenant tenant = i.getAdministrateWorkflowModel().getTenant();
         
            Query q = evt.getSession().createQuery("COUNT(*) from UserIsMemberOfTenant as a where (a.tenant.id = :tenantId AND a.user.id = :userId) OR a.tenant.admin.id = :userId");
            q.setLong("tenantId", tenant.getId());
            q.setLong("userId", user.getId());
            
            if( q.uniqueResult() == null){
                
                 UserIsMemberOfTenant relation = new UserIsMemberOfTenant();
                
                relation.setUser(user);
                relation.setTenant(tenant);
                
                evt.getSession().save(relation);
            }

            // Add as WorkflowAdmin
            UserAdministratesWorkflowModel relation = new UserAdministratesWorkflowModel();
            relation.setUser(user);
            relation.setWorkflowModel(i.getAdministrateWorkflowModel());
            evt.getSession().save(relation);
            
        }
        else if(i.getJoinTenant() != null){
            
         // add to tenant if user isn't tenant member
            User user = i.getReceiver();
            Tenant tenant = i.getJoinTenant();
         
            Query q = evt.getSession().createQuery("COUNT(*) from UserIsMemberOfTenant as a where (a.tenant.id = :tenantId AND a.user.id = :userId) OR a.tenant.admin.id = :userId");
            q.setLong("tenantId", tenant.getId());
            q.setLong("userId", user.getId());
            
            if( q.uniqueResult() == null){
                
                 UserIsMemberOfTenant relation = new UserIsMemberOfTenant();
                
                relation.setUser(user);
                relation.setTenant(tenant);
                
                evt.getSession().save(relation);
            }

            
        }
        else if(i.getParticipateInWorkflowInstance() != null){
            
         // add to tenant if user isn't tenant member
            User user = i.getReceiver();
            Tenant tenant = i.getParticipateInWorkflowInstance().getDeployedWorkflowModel().getTenant();
         
            Query q = evt.getSession().createQuery("COUNT(*) from UserIsMemberOfTenant as a where (a.tenant.id = :tenantId AND a.user.id = :userId) OR a.tenant.admin.id = :userId");
            q.setLong("tenantId", tenant.getId());
            q.setLong("userId", user.getId());
            
            if( q.uniqueResult() == null){
                
                 UserIsMemberOfTenant relation = new UserIsMemberOfTenant();
                
                relation.setUser(user);
                relation.setTenant(tenant);
                
                evt.getSession().save(relation);
            }
            
            //Add as WorkflowParticipant
            UserParticipatesInWorkflow relation = new UserParticipatesInWorkflow();
            relation.setUser(user);
            relation.setWorkflowInstance(i.getParticipateInWorkflowInstance());
            evt.getSession().save(relation);

            
        }
        else{
            // nothing to do
        }

    }
}
