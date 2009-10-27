package de.decidr.model.commands.workitem;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBException;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.soap.types.TaskItem;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;
import de.decidr.model.workflowmodel.humantask.TTaskItem;

/**
 * DH FIXME this needs to be revised: we have to examine the new properties
 * (from XML) and persist any files that have been uploaded. ~dh
 * 
 * Overwrites the data of an <strong>existing</strong> work item.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class SetDataCommand extends WorkItemCommand {

    private THumanTaskData data;

    /**
     * Creates a new SetDataCommand that overwrites the data of an existing work
     * item.
     * 
     * @param role
     *            user / system who executes the command
     * @param workItemId
     *            the ID of the workitem whose data should be set
     * @param data
     *            the new data
     * @throws IllegalArgumentException
     *             if workIdId or data is <code>null</code>
     */
    public SetDataCommand(Role role, Long workItemId, THumanTaskData data) {
        super(role, workItemId);
        if (workItemId == null || data == null) {
            throw new IllegalArgumentException(
                    "Work item ID and data must not be null.");
        }
        this.data = data;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        WorkItem workItem = fetchWorkItem(evt.getSession());

        // persist all uploaded files
        Set<Long> fileIds = new HashSet<Long>();

        for (Object o : data.getTaskItemOrInformation()) {
            if (o instanceof TTaskItem) {
                TTaskItem taskItem = (TTaskItem) o;
                // DH how do I find out if this task item contains an uploaded
                // file? ~dh
            }
        }
        if (!fileIds.isEmpty()) {
            String hql = "update File set temporary = false where id in (:fileIds)";
            evt.getSession().createQuery(hql).setParameterList("fileIds",
                    fileIds).executeUpdate();
        }

        // save human task data as blob
        try {
            workItem.setData(TransformUtil.getBytes(data));
        } catch (JAXBException e) {
            throw new TransactionException(e);
        }
        evt.getSession().update(workItem);
    }
}
