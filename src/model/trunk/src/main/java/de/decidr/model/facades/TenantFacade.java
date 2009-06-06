package de.decidr.model.facades;

import java.io.InputStream;
import java.util.List;
import com.vaadin.data.Item;

import de.decidr.model.commands.tenant.ApproveTenantsCommand;
import de.decidr.model.commands.tenant.CreateTenantCommand;
import de.decidr.model.commands.tenant.CreateWorkflowModelCommand;
import de.decidr.model.commands.tenant.DeleteTenantsCommand;
import de.decidr.model.commands.tenant.DisapproveTenantsCommand;
import de.decidr.model.commands.tenant.GetTenantIdCommand;
import de.decidr.model.commands.tenant.GetTenantLogoCommand;
import de.decidr.model.commands.tenant.RemoveTenentMemberCommand;
import de.decidr.model.commands.tenant.RemoveWorkflowModelCommand;
import de.decidr.model.commands.tenant.SetTenantDescriptionCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionCoordinator;

/**
 * 
 * The tenant facade contains all functions which are available to modify tenant
 * data/settings.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class TenantFacade extends AbstractFacade {

    /**
     * 
     * Creates a new tenant facade. All operations are executed by the given
     * actor.
     * 
     * @param actor
     */
    public TenantFacade(Role actor) {
        super(actor);
    }

    /**
     * 
     * Creates a new Tenant.
     * 
     * @param name
     * @param description
     * @param adminId the id of the admin user
     * @return id of the new tenant
     */
    public Long createTenant(String name, String description, Long adminId) throws TransactionException{
        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        CreateTenantCommand command = new CreateTenantCommand(actor,name,description,adminId);
        
        tac.runTransaction(command);
        
        return command.getTenantId();
    }

    /**
     * 
     * Sets the description of an tenant
     * 
     * @param tenantId
     * @param description
     */
    public void setDescription(Long tenantId, String description) throws TransactionException{
        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        SetTenantDescriptionCommand command = new SetTenantDescriptionCommand(actor,tenantId,description);

        tac.runTransaction(command);

    }

    /**
     * 
     * Returns the tenant logo as InputStream;
     * 
     * @param tenantId
     * @return tenent logo
     */
    public InputStream getLogo(Long tenantId) throws TransactionException{
        
        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        GetTenantLogoCommand command = new GetTenantLogoCommand(actor,tenantId);

        tac.runTransaction(command);
        
        return command.getLogoStream();

    }

    //FIXME FileHandling fehlt noch
    public void setLogo(Long tenantId, InputStream logo) {
        throw new UnsupportedOperationException();
    }

    //FIXME FileHandling fehlt noch
    public void setSimpleColorScheme(InputStream simpleColorScheme,
            Long tenantId) {
        throw new UnsupportedOperationException();
    }

    //FIXME FileHandling fehlt noch
    public void setAdvancedColorScheme(InputStream advancedColorScheme,
            Long tenantId) {
        throw new UnsupportedOperationException();
    }

    //FIXME FileHandling fehlt noch
    public void getCurrentColorScheme() {
        throw new UnsupportedOperationException();
    }

    //FIXME FileHandling fehlt noch
    public void setCurrentColorScheme(InputStream currentColorScheme,
            Long tenantId) {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * Adds a user as member of a tenant.
     * 
     * @param tenantId
     * @param memberId
     */
    public void addTenantMember(Long tenantId, Long memberId) {
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * Creates a new workflow model.
     * 
     * @param tenantId
     * @param name
     * @return id of the new workflow model
     */
    public Long createWorkflowModel(Long tenantId, String name) throws TransactionException{

        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        CreateWorkflowModelCommand command = new CreateWorkflowModelCommand(actor,tenantId,name);

        tac.runTransaction(command);
        
        return command.getWorkflowModelId();
       
    }

    /**
     * 
     * Removes the tenant member relation.
     * 
     * @param tenantId
     * @param userId
     */
    public void removeTenantMember(Long tenantId, Long userId) throws TransactionException{
        
        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        RemoveTenentMemberCommand command = new RemoveTenentMemberCommand(actor,tenantId,userId);

        tac.runTransaction(command);
    }

    /**
     * 
     * Removes the relation between the given workflow model and the tenant.
     * 
     * @param tenantId
     * @param workflowModelId
     */
    public void removeWorkflowModel(Long tenantId, Long workflowModelId) throws TransactionException{

        
        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        RemoveWorkflowModelCommand command = new RemoveWorkflowModelCommand(actor,workflowModelId);

        tac.runTransaction(command);
 
        
    }

    /**
     * 
     * Approves the given tenants.
     * 
     * @param tenantIds
     */
    public void approveTenants(List<Long> tenantIds) throws TransactionException{

        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        ApproveTenantsCommand command = new ApproveTenantsCommand(actor,tenantIds);

        tac.runTransaction(command);
        
    }

    /**
     * 
     * Disapproves all given tenants and sends a notification mail to the tenant owners.
     * 
     * @param tenantIds
     * @throws TransactionException
     */
    public void disapproveTenants(List<Long> tenantIds) throws TransactionException{
        
        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        DisapproveTenantsCommand command = new DisapproveTenantsCommand(actor,tenantIds);

        tac.runTransaction(command);
        
        //FIXME sent notification mail
    }

    /**
     * 
     * Deletes all given tenant. Non-existing tenants will be ignored.
     * 
     * @param tenantIds
     */
    public void deleteTenants(List<Long> tenantIds) throws TransactionException{
        
        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        DeleteTenantsCommand command = new DeleteTenantsCommand(actor,tenantIds);

        tac.runTransaction(command);
  
    }

    /**
     * 
     * Returns the tenantId of the tenant which corresponds to the given name.
     * 
     * @param tenantName
     * @return tenandId, if tenant not exists an exception is thrown
     * @throws TransactionException
     */
    public Long getTenantId(String tenantName) throws TransactionException{
        
        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        GetTenantIdCommand command = new GetTenantIdCommand(actor,tenantName);

        tac.runTransaction(command);
        
        return command.getTenantId();
  
    }
 
    

    //FIXME paginator not yet implemented
    public List<Item> getUsersOfTenant(Long tenantId, Paginator paginator) {
        throw new UnsupportedOperationException();
    }
 
    //FIXME paginator not yet implemented
    public List<Item> getWorkflowInstances(Long tenantId, Paginator paginator) {
        throw new UnsupportedOperationException();
    }

    
    //FIXME paginator not yet implemented
    public List<Item> getTenantsToApprove(List<Filter> filters,
            Paginator paginator) {
        throw new UnsupportedOperationException();
    }

    //FIXME paginator not yet implemented
    public List<Item> getAllTenants(List<Filter> filters, Paginator paginator) {
        throw new UnsupportedOperationException();
    }

 
    //FIXME paginator not yet implemented
    public List<Item> getWorkflowModels(Long tenantId, List<Filter> filters,
            Paginator paginator) {
        throw new UnsupportedOperationException();
    }

    //FIXME do it
    public void inviteUsersAsMembers(Long tenantId,
            List<String> emailOrUsernames) {
        throw new UnsupportedOperationException();
    }
    
    //FIXME do it
    public void importPublishedWorkflowModels(Long tenantId,
            List<Long> workflowModelIds) {
        throw new UnsupportedOperationException();
    }

    
}