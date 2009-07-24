package de.decidr.model.commands.user;

import java.util.List;

import org.hibernate.Query;

import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Fetches all workflow models that are administrated by the given user from the
 * datbase.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class GetAdministratedWorkflowModelCommand extends UserCommand {

    private Long userId;
    private List<WorkflowModel> result;

    /**
     * Creates a new GetAdministratedWorkflowModelCommand. This command will
     * write all WorkflowModels which the user administrates as a list in the
     * result variable.
     * 
     * @param role
     *            user which executes the command
     * @param userId
     *            the id of the user whose administrated wokflow models should be requested
     */
    public GetAdministratedWorkflowModelCommand(Role role, Long userId) {
        super(role, null);

        this.userId = userId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        String hql = "select rel.workflowModel "
                + "from UserAdministratesWorkflowModel as rel"
                + "where rel.user.id = :userId";

        Query q = evt.getSession().createQuery(hql).setLong("userId", userId);

        result = q.list();
    }

    /**
     * @return List of workflow models which are administrated by the given user
     */
    public List<WorkflowModel> getResult() {
        return result;
    }
}
