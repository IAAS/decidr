package de.decidr.model.commands.workflowinstance;

import org.hibernate.Query;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Deletes all work items of the given workflow instance.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class RemoveAllWorkItemsCommand extends WorkflowInstanceCommand {

    private String odePid;
    private Long deployedWorkflowModelId;

    /**
     * 
     * Creates a new RemoveAllWorkItemsCommand. This command deletes all work
     * items of the given workflow instance.
     * 
     * @param role
     * @param odePid
     * @param deployedWorkflowModelId
     */
    public RemoveAllWorkItemsCommand(Role role, String odePid,
            Long deployedWorkflowModelId) {
        super(role, null);

        this.odePid = odePid;
        this.deployedWorkflowModelId = deployedWorkflowModelId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Query q = evt
                .getSession()
                .createQuery(
                        "DELETE from WorkItem item WHERE item.workflowinstance.odePid = :pid AND item.workflowinstance.deployedWorkflowModel.id = :wid");

        q.setString("pid", odePid);
        q.setLong("wid", deployedWorkflowModelId);

        q.executeUpdate();

    }

}