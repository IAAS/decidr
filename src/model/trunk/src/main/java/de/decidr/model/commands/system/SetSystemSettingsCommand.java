package de.decidr.model.commands.system;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.util.ReflectHelper;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.HibernateTools;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Sets the System settings.
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

        Long settingsId = currentSettings.getId();
        // copy each property of the new settings except for id and modified
        // date
        try {
            BeanUtils.copyProperties(currentSettings, newSettings);
        } catch (IllegalAccessException e) {
            throw new TransactionException(e);
        } catch (InvocationTargetException e) {
            throw new TransactionException(e);
        }

        currentSettings.setId(settingsId);
        currentSettings.setModifiedDate(DecidrGlobals.getTime().getTime());

        evt.getSession().update(currentSettings);
    }
}
