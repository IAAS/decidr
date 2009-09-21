package de.decidr.model.commands.workitem;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Overwrites the raw XML data of a work item with the given raw bytes.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class SetDataCommand extends WorkItemCommand {

    private byte[] data;

    /**
     * Creates a new instance of the SetDataCommand
     * 
     * @param role
     *            user who executes the command
     * @param workItemId
     *            the ID of the workitem whose data should be set
     * @param data
     *            the new data
     */
    public SetDataCommand(Role role, Long workItemId, byte[] data) {
        super(role, workItemId);
        this.data = data;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        WorkItem workItem = (WorkItem) evt.getSession().get(WorkItem.class,
                workItemId);

        if (workItem != null) {
            workItem.setData(data);
            evt.getSession().saveOrUpdate(workItem);
        } else {
            throw new EntityNotFoundException(WorkItem.class, workItemId);
        }
    }
}