package de.decidr.model.facades;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.tenant.AddTenantMemberCommand;
import de.decidr.model.commands.tenant.ApproveTenantsCommand;
import de.decidr.model.commands.tenant.CreateTenantCommand;
import de.decidr.model.commands.tenant.CreateWorkflowModelCommand;
import de.decidr.model.commands.tenant.DeleteTenantsCommand;
import de.decidr.model.commands.tenant.DisapproveTenantsCommand;
import de.decidr.model.commands.tenant.GetTenantIdCommand;
import de.decidr.model.commands.tenant.GetTenantLogoCommand;
import de.decidr.model.commands.tenant.GetUsersOfTenantCommand;
import de.decidr.model.commands.tenant.RemoveTenentMemberCommand;
import de.decidr.model.commands.tenant.RemoveWorkflowModelCommand;
import de.decidr.model.commands.tenant.SetTenantDescriptionCommand;
import de.decidr.model.entities.Log;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionCoordinator;
import de.decidr.model.permissions.UserRole;
import de.decidr.model.permissions.TenantAdminRole;
import de.decidr.model.permissions.SuperAdminRole;
import de.decidr.model.filters.Paginator;

/**
 * 
 * The tenant facade contains all functions which are available to modify tenant
 * data/settings.
 * 
 * @author Markus Fischer
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
     * Creates a new Tenant with the given properties.
     * 
     * @param name name of the tenant
     * @param description description of the tenant
     * @param adminId the id of the tenant admin user
     * @return id of the new tenant
     * @throws TransactionException
     */
    @AllowedRole(UserRole.class)
    public Long createTenant(String name, String description, Long adminId) throws TransactionException{
        
        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        
        CreateTenantCommand command = new CreateTenantCommand(actor,name,description,adminId);
        
        tac.runTransaction(command);
        
        return command.getTenantId();
    }

    
    /**
     * 
     * Sets the description of the given tenant. If the given tenant does not exists
     * an Exception will be thrown.
     * 
     * @param tenantId the id of the tenant which should be changed
     * @param description the new description
     * @throws TransactionException
     */
    @AllowedRole(TenantAdminRole.class)
    public void setDescription(Long tenantId, String description) throws TransactionException{
        
        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        SetTenantDescriptionCommand command = new SetTenantDescriptionCommand(actor,tenantId,description);

        tac.runTransaction(command);

    }

    /**
     * 
     * Returns the tenant logo as InputStream;
     * // FIXME FileHandling not yet implemented
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
     * Adds a user as member of a tenant. Both IDs mustn't be null;
     * 
     * @param tenantId
     * @param memberId
     * @throws TransactionException
     */
    @AllowedRole(TenantAdminRole.class)
    public void addTenantMember(Long tenantId, Long memberId) throws TransactionException{
        
        if(tenantId == null){
            throw new NullPointerException("tenantId mustn't be null");
        }
        else if (memberId == null){
            throw new NullPointerException("memberID mustn't be null");
        }
        else{
            TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
            AddTenantMemberCommand command = new AddTenantMemberCommand(actor,tenantId,memberId);
          
            tac.runTransaction(command);
        }
        
    }

    /**
     * 
     * Creates a new workflow model for the given tenant. If the given tenant does not exist an exception
     * will be thrown. TenantId and name mustn't be null.
     * 
     * @param tenantId
     * @param name
     * @return id of the new workflow model
     * @throws TransactionException
     */
    @AllowedRole(TenantAdminRole.class)
    public Long createWorkflowModel(Long tenantId, String name) throws TransactionException{

        if(tenantId == null){
            throw new NullPointerException("tenantId mustn't be null");
        }
        else if((name == null) || ("".equals(name))){
            throw new IllegalArgumentException("name must have a content");
        }
        else{
            TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
            CreateWorkflowModelCommand command = new CreateWorkflowModelCommand(actor,tenantId,name);

            tac.runTransaction(command);
            return command.getWorkflowModelId();
        }
        
       
    }

    /**
     * 
     * Removes the tenant member relation between the given tenant and the given user.
     * If the relation/userId/tenantId doesn't exist nothing will happen.
     * 
     * @param tenantId
     * @param userId
     * @throws TransactionException
     */
    @AllowedRole(TenantAdminRole.class)
    public void removeTenantMember(Long tenantId, Long userId) throws TransactionException{
        
        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        RemoveTenentMemberCommand command = new RemoveTenentMemberCommand(actor,tenantId,userId);

        tac.runTransaction(command);
    }

    /**
     * 
     * Removes the relation between the given workflow model and the tenant.
     * If the given workflow model doesn't exist nothing will happen.
     * 
     * @param tenantId
     * @param workflowModelId
     * @throws TransactionException
     */
    @AllowedRole(TenantAdminRole.class)
    public void removeWorkflowModel(Long workflowModelId) throws TransactionException{

        
        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        RemoveWorkflowModelCommand command = new RemoveWorkflowModelCommand(actor,workflowModelId);

        tac.runTransaction(command);
 
        
    }

    /**
     * 
     * Approves the given tenants. Not existing and already approved/disapproved tenants will we ignored.
     * 
     * @param tenantIds
     */
    @AllowedRole(TenantAdminRole.class)
    public void approveTenants(List<Long> tenantIds) throws TransactionException{

        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        ApproveTenantsCommand command = new ApproveTenantsCommand(actor,tenantIds);

        tac.runTransaction(command);
        
    }

    /**
     * 
     * Disapproves all given tenants and sends a notification mail to the tenant owners.
     * Already approved/disapproved tenants will be ignored.
     * 
     * @param tenantIds
     * @throws TransactionException
     */
    @AllowedRole(TenantAdminRole.class)
    public void disapproveTenants(List<Long> tenantIds) throws TransactionException{
        
        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        DisapproveTenantsCommand command = new DisapproveTenantsCommand(actor,tenantIds);

        tac.runTransaction(command);
        
        //FIXME sent notification mail (to fix in command) (use notification class)
    }

    /**
     * 
     * Deletes all given tenant. Non-existing tenants will be ignored.
     * 
     * @param tenantIds
     */
    @AllowedRole(SuperAdminRole.class)
    public void deleteTenants(List<Long> tenantIds) throws TransactionException{
        
        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        DeleteTenantsCommand command = new DeleteTenantsCommand(actor,tenantIds);

        tac.runTransaction(command);
  
    }

    /**
     * 
     * Returns the tenantId of the tenant which corresponds to the given name.
     * If the tenantName doesn't exists, an exception will be thrown.
     * 
     * @param tenantName
     * @return tenandId, if tenant not exists an exception is thrown
     * @throws TransactionException if zero or more than one result is found (tenant name must be unique)
     */
    @AllowedRole(UserRole.class)
    public Long getTenantId(String tenantName) throws TransactionException{
        
        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        GetTenantIdCommand command = new GetTenantIdCommand(actor,tenantName);

        tac.runTransaction(command);
        
        return command.getTenantId();
  
    }
 
    
    //FIXME paginator not yet implemented
    @SuppressWarnings("unchecked")
    public List<Item> getUsersOfTenant(Long tenantId, Paginator paginator) throws TransactionException{

        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
        GetUsersOfTenantCommand command = new GetUsersOfTenantCommand(actor,tenantId, paginator);
     
        List<Item> outList = new ArrayList<Item>();
        List<User> inList = new ArrayList();

        tac.runTransaction(command);
        inList = command.getResult();

        for (User user : inList) {
            outList.add(new BeanItem(user));
        }

        return outList;
        
        
          
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

    //FIXME notifier not yet implemented
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