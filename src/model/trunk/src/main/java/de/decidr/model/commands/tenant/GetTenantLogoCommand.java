package de.decidr.model.commands.tenant;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;

import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.storage.StorageProviderFactory;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Returns the LogoData TODO document
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetTenantLogoCommand extends TenantCommand {

    private Long tenantId;
    private InputStream logoStream;

    /**
     * Creates a new GetTenantLogoCommand. This command load the logo from the
     * storage and saves it in result variable.
     * 
     * @param role
     *            TODO document
     * @param tenantId
     *            TODO document
     */
    public GetTenantLogoCommand(Role role, Long tenantId) {
        super(role, null);
        this.tenantId = tenantId;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Tenant tenant = (Tenant) evt.getSession().load(Tenant.class, tenantId);
        Long logoid = tenant.getLogo().getId();
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

        try {
            logoStream = factory.getStorageProvider().getFile(logoid);
        } catch (StorageException e) {
            throw new TransactionException(e);
        } catch (IncompleteConfigurationException e) {
            throw new TransactionException(e);
        }
    }

    public InputStream getLogoStream() {
        return logoStream;
    }
}
