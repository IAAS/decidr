package de.decidr.model.commands.workflowmodel;

import org.hibernate.Session;

import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.WorkflowModelPermission;

/**
 * 
 * Base class for commands that modify workflow models.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public abstract class WorkflowModelCommand extends AclEnabledCommand {

    protected Long workflowModelId = null;

    /**
     * Constructor
     * 
     * @param role
     * @param workflowModelId
     *            the workflow model that is read / modified. A corresponding
     *            permission is automatically created.
     */
    public WorkflowModelCommand(Role role, Long workflowModelId) {
        super(role, new WorkflowModelPermission(workflowModelId));
        this.workflowModelId = workflowModelId;
    }

    /**
     * @return the workflowModelId
     */
    public Long getWorkflowModelId() {
        return workflowModelId;
    }

    /**
     * Fetches the workflow model from the database using the previously set
     * workflow model id.
     * 
     * @param session
     *            the Hibernate session used to fetch the workflow model.
     * @return the corresponding workflow model entity
     * @throws EntityNotFoundException
     *             if there is no workflow model that corresponds with the
     *             workflow model id that was previously set
     */
    public WorkflowModel fetchWorkflowModel(Session session)
            throws EntityNotFoundException {

        WorkflowModel workflowModel = (WorkflowModel) session.get(
                WorkflowModel.class, workflowModelId);

        if (workflowModel == null) {
            throw new EntityNotFoundException(WorkflowModel.class,
                    workflowModelId);
        }

        return workflowModel;
    }
}