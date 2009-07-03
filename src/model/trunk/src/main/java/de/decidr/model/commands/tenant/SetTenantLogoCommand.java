package de.decidr.model.commands.tenant;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import de.decidr.model.entities.File;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.permissions.Role;
import de.decidr.model.storage.StorageProviderFactory;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Sets the given logo as tenant logo to the given tenant. The logo will be
 * saved on permanent storage an a file entity will be created. The id of the
 * entity and the id of the file in the storage service for will be the same.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class SetTenantLogoCommand extends TenantCommand {

    FileInputStream logo;
    String mimeType;
    String fileName;

    /**
     * 
     * Creates a new SetTenantLogoCommand. This commands sets the given logo as
     * tenant logo to the given tenant. The logo will be saved on permanent
     * storage an a file entity will be created. The id of the entity and the id
     * of the file in the storage service for will be the same.
     * 
     * @param role
     *            user which executes the command
     * @param tenantId
     *            the id of the tenant where the logo will be set
     * @param logo
     *            the logo as FileInputStream
     * @param mimeType
     *            the mimetype of the logo file
     * @param fileName
     *            the file name of the logo file
     */
    public SetTenantLogoCommand(Role role, Long tenantId, FileInputStream logo,
            String mimeType, String fileName) {
        super(role, tenantId);
        this.logo = logo;
        this.mimeType = mimeType;
        this.fileName = fileName;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        StorageProviderFactory factory;

        File logoFile = new File();
        logoFile.setMimeType(mimeType);
        logoFile.setMayPublicRead(true);
        logoFile.setFileName(fileName);

        evt.getSession().save(logoFile);

        Tenant tenant = (Tenant) evt.getSession().load(Tenant.class,
                getTenantId());
        tenant.setLogo(logoFile);
        evt.getSession().update(tenant);

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
            factory.getStorageProvider().putFile(logo, logoFile.getId());
        } catch (StorageException e) {
            throw new TransactionException(e);
        } catch (IncompleteConfigurationException e) {
            throw new TransactionException(e);
        }

    }

}
