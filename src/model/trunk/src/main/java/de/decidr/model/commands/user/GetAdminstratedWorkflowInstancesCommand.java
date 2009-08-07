package de.decidr.model.commands.user;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserAdministratesWorkflowInstance;
import de.decidr.model.entities.WorkflowInstance;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Saves all administrated WorkflowInstances of the given user in the result
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
@SuppressWarnings("unchecked")
public class GetAdminstratedWorkflowInstancesCommand extends UserCommand {

    List<WorkflowInstance> result = new ArrayList();

    /**
     * Creates a new GetAdminstratedWorkflowInstancesCommand. This Command saves
     * all administrated WorkflowInstances of the given user in the result
     * variable.
     * 
     * @param role
     *            user which executes the command
     * @param userId
     *            the ID of the user whose administrated workflow instances should be requested
     */
    public GetAdminstratedWorkflowInstancesCommand(Role role, Long userId) {
        super(role, userId);
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        User user = new User();
        List<UserAdministratesWorkflowInstance> list = new ArrayList();

        user.setId(getUserId());

        Criteria c = evt.getSession().createCriteria(
                UserAdministratesWorkflowInstance.class).createCriteria(
                "deployedWorkflowModel", CriteriaSpecification.LEFT_JOIN);
        c.add(Restrictions.eq("user", user));

        list = c.list();

        for (UserAdministratesWorkflowInstance item : list) {
            result.add(item.getWorkflowInstance());
        }
    }

    /**
     * @return List of WorkflowInstances which are administrated by the given user
     */
    public List<WorkflowInstance> getResult() {
        return result;
    }
}
