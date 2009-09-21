package de.decidr.model.commands.tenant;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.File;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.storage.StorageProviderFactory;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Sets the advanced color scheme of the given tenant.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class SetAdvancedColorSchemeCommand extends TenantCommand {

    FileInputStream advancedColorScheme;
    String mimeType;
    String fileName;

    /**
     * Creates a new SetAdvancedColorSchemeCommand. This command sets the
     * advanced color scheme of the given tenant.
     * 
     * @param role
     *            user which executes the command
     * @param tenantId
     *            the id of the tenant where the advanced color scheme should be
     *            set
     * @param simpleColorScheme
     *            the color scheme file
     * @param mimeType
     *            mime type of the file
     * @param fileName
     *            name of the file
     */
    public SetAdvancedColorSchemeCommand(Role role, Long tenantId,
            FileInputStream advancedColorScheme, String mimeType,
            String fileName) {

        super(role, tenantId);
        this.advancedColorScheme = advancedColorScheme;
        this.mimeType = mimeType;
        this.fileName = fileName;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        StorageProviderFactory factory;
        File schemeFile;

        Tenant tenant = (Tenant) evt.getSession().load(Tenant.class,
                this.getTenantId());

        if (tenant.getAdvancedColorScheme() == null) {
            schemeFile = new File();
        } else {
            schemeFile = tenant.getAdvancedColorScheme();
        }

        schemeFile.setMimeType(mimeType);
        schemeFile.setMayPublicRead(true);
        schemeFile.setFileName(fileName);

        evt.getSession().saveOrUpdate(schemeFile);

        tenant.setAdvancedColorScheme(schemeFile);
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
            factory.getStorageProvider().putFile(advancedColorScheme,
                    schemeFile.getId());
        } catch (StorageException e) {
            throw new TransactionException(e);
        } catch (IncompleteConfigurationException e) {
            throw new TransactionException(e);
        }
    }
}