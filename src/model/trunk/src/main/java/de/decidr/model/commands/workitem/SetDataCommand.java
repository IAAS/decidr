package de.decidr.model.commands.workitem;

import java.util.Set;

import javax.xml.bind.JAXBException;

import de.decidr.model.XmlTools;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.file.AssociateFileWithWorkItemCommand;
import de.decidr.model.entities.WorkItem;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.humantask.THumanTaskData;

/**
 * Overwrites the data of an <strong>existing</strong> work item. Any files that
 * have been uploaded in the context of the work item are persisted and
 * associated with the work item.
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
        requireWorkItemId();
        if (data == null) {
            throw new IllegalArgumentException(
                    "Work item data must not be null.");
        }
        this.data = data;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        WorkItem workItem = fetchWorkItem(evt.getSession());

        // persist all uploaded files and associate with work item.
        Set<Long> fileIds = XmlTools.getFileIds(data);
        for (Long fileId : fileIds) {
            HibernateTransactionCoordinator.getInstance().runTransaction(
                    new AssociateFileWithWorkItemCommand(role, fileId, workItem
                            .getId()));
        }

        // save human task data as blob
        try {
            workItem.setData(TransformUtil.humanTaskToByte(data));
        } catch (JAXBException e) {
            throw new TransactionException(e);
        }
        evt.getSession().update(workItem);
    }

}