package de.decidr.model.commands.workflowmodel;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import de.decidr.model.acl.access.WorkflowModelAccess;
import de.decidr.model.acl.permissions.Permission;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.commands.AclEnabledCommand;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.EntityNotFoundException;

/**
 * 
 * Base class for commands that modify workflow models.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public abstract class WorkflowModelCommand extends AclEnabledCommand implements WorkflowModelAccess{

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
        super(role, (Permission)null);
        this.workflowModelId = workflowModelId;
    }

    /**
     * @return the workflowModelId
     */
    public Long getWorkflowModelId() {
        return workflowModelId;
    }
    
    /**
     * @return the workflowModelIds
     */
    public Long[] getWorkflowModelIds() {
        Long[] result = {workflowModelId};
        return result;
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

    /**
     * Fetches the current deployed version of the workflow model.
     * 
     * @param session
     * @return the current deployed version of the workflow model or null if
     *         there is no current deplyoed version.
     */
    public DeployedWorkflowModel fetchCurrentDeployedWorkflowModel(
            Session session) {
        WorkflowModel model = new WorkflowModel();
        model.setId(getWorkflowModelId());

        Criteria crit = session.createCriteria(DeployedWorkflowModel.class,
                "dwm");

        crit.createCriteria("originalWorkflowModel", "owm").add(
                Restrictions.eqProperty("owm.version", "dwm.version"));

        crit.add(Restrictions.eq("originalModel", model));

        return (DeployedWorkflowModel) crit.uniqueResult();
    }
}