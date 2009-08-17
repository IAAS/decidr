package de.decidr.model.commands.system;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Sets the System Settings.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class SetSystemSettingsCommand extends SystemCommand {

    private SystemSettings newSettings;

    /**
     * Sets the system settings.
     * 
     * @param actor
     *            the user which executes the command
     * @param newSettings
     *            the new system settings
     */
    public SetSystemSettingsCommand(Role actor, SystemSettings newSettings) {
        super(actor, null);
        this.newSettings = newSettings;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        SystemSettings currentSettings = (SystemSettings) evt.getSession()
                .createQuery("from SystemSettings").setMaxResults(1)
                .uniqueResult();

        if (currentSettings == null) {
            throw new EntityNotFoundException(SystemSettings.class);
        }

        currentSettings.setAutoAcceptNewTenants(newSettings
                .isAutoAcceptNewTenants());
        currentSettings.setChangeEmailRequestLifetimeSeconds(newSettings
                .getChangeEmailRequestLifetimeSeconds());
        currentSettings.setDomain(newSettings.getDomain());
        currentSettings.setInvitationLifetimeSeconds(newSettings
                .getInvitationLifetimeSeconds());
        currentSettings.setLogLevel(newSettings.getLogLevel());
        currentSettings.setMaxAttachmentsPerEmail(newSettings
                .getMaxAttachmentsPerEmail());
        currentSettings.setMaxUploadFileSizeBytes(newSettings
                .getMaxUploadFileSizeBytes());
        currentSettings.setMtaHostname(newSettings.getMtaHostname());
        currentSettings.setMtaPassword(newSettings.getMtaPassword());
        currentSettings.setMtaPort(newSettings.getMtaPort());
        currentSettings.setMtaUsername(newSettings.getMtaUsername());
        currentSettings.setMtaUseTls(newSettings.isMtaUseTls());
        currentSettings.setPasswordResetRequestLifetimeSeconds(newSettings
                .getPasswordResetRequestLifetimeSeconds());
        currentSettings.setRegistrationRequestLifetimeSeconds(newSettings
                .getRegistrationRequestLifetimeSeconds());
        currentSettings.setSystemEmailAddress(newSettings
                .getSystemEmailAddress());
        currentSettings.setSystemName(newSettings.getSystemName());

        evt.getSession().update(currentSettings);
    }
}
