package de.decidr.model.commands.tenant;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.storage.StorageProviderFactory;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Deletes all tenants which corresponds to the given ids including all
 * corresponding WorkflowInstances and workitems.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class DeleteTenantCommand extends TenantCommand {

    private Long tenantId;

    /**
     * 
     * Creates a new DeleteTenantsCommand. This Command will delete all tenants
     * which corresponds to the given ids including all corresponding
     * WorkflowInstances and workitems. all .
     * 
     * @param role
     *            the user which executes the command
     * @param tenantId
     */
    public DeleteTenantCommand(Role role, Long tenantId) {
        super(role, null);

        this.tenantId = tenantId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Tenant tenant = (Tenant) evt.getSession().get(Tenant.class, tenantId);

        if (tenant != null) {

            StorageProviderFactory factory;
            
            try {
                factory = StorageProviderFactory.getDefaultFactory();
            } catch (InvalidPropertiesFormatException e) {
                throw new TransactionException(e);
            } catch (FileNotFoundException e) {
                throw new TransactionException(e);
            } catch (IOException e) {
                throw new TransactionException(e);
            }
            
            
            
            // delete File logo (permanent storage)
            try {
                factory.getStorageProvider().removeFile(tenant.getLogo().getId());
            } catch (StorageException e) {
                throw new TransactionException(e);
            } catch (IncompleteConfigurationException e) {
                throw new TransactionException(e);
            }
            
            // delete File logo (db representation)
            evt.getSession().delete(tenant.getLogo());
            
            
            
            
            // delete File simpleColorScheme (permanent storage)
            try {
                factory.getStorageProvider().removeFile(tenant.getSimpleColorScheme().getId());
            } catch (StorageException e) {
                throw new TransactionException(e);
            } catch (IncompleteConfigurationException e) {
                throw new TransactionException(e);
            }
            
            // delete File simplecolorScheme (db representation)
            evt.getSession().delete(tenant.getSimpleColorScheme());
            
            
            
            
            // delete File advancedColorScheme (permanent storage)
            try {
                factory.getStorageProvider().removeFile(tenant.getAdvancedColorScheme().getId());
            } catch (StorageException e) {
                throw new TransactionException(e);
            } catch (IncompleteConfigurationException e) {
                throw new TransactionException(e);
            }
            
            // delete File advancedColorScheme (db representation)
            evt.getSession().delete(tenant.getAdvancedColorScheme());
            
            
            
            // delete File currentColorScheme (permanent storage)
            try {
                factory.getStorageProvider().removeFile(tenant.getCurrentColorScheme().getId());
            } catch (StorageException e) {
                throw new TransactionException(e);
            } catch (IncompleteConfigurationException e) {
                throw new TransactionException(e);
            }
            
            // delete File currentColorScheme (db representation)
            evt.getSession().delete(tenant.getCurrentColorScheme());
            
            
            for (DeployedWorkflowModel model : tenant
                    .getDeployedWorkflowModels()) {

                // SECIT Say Modood that he should undeploy it

                /*
                 * corresponding WorkflowInstances and Workitems will be
                 * automatically deleted by the database
                 */
                evt.getSession().delete(model);

            }
            
            // finally delete tenant object
            evt.getSession().delete(tenant);

        }

    }

}
