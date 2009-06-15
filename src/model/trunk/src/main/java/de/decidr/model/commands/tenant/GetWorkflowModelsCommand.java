package de.decidr.model.commands.tenant;

import java.util.List;
import org.hibernate.criterion.Restrictions;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.WorkflowModel;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Filters;
import de.decidr.model.filters.PaginatingCriteria;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Saves the result in the variable result as List<WorkflowModel>.
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class GetWorkflowModelsCommand extends TenantCommand {

    private List<Filter> filters;
    private Paginator paginator;
    private List<WorkflowModel> result;
    
    /**
     * 
     * Creates a new GetWorkflowModelsCommand. The command saves the result in the
     * variable result as List<WorkflowModel>.
     * 
     * @param role
     * @param tenantId
     * @param filters
     * @param paginator
     */
    public GetWorkflowModelsCommand(Role role, Long tenantId, List<Filter> filters,
            Paginator paginator) {
        super(role, tenantId);
        
        this.filters=filters;
        this.paginator=paginator;
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        
        Tenant t = new Tenant();
        t.setId(getTenantId());
        
        PaginatingCriteria c = new PaginatingCriteria(WorkflowModel.class,evt.getSession());
        
        c.add(Restrictions.eq("tenant", t));
        
        Filters.apply(c, filters, paginator);
        
        result = c.list();
        
    }

   /**
    * 
    * @return the result
    */
    public List<WorkflowModel> getResult() {
        return result;
    }

}