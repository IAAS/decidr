/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.decidr.model.commands.system;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Level;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Sets the system settings. The "modified date" is set to the current time,
 * ignoring the modifiedDate property of the given SystemSettings object.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class SetSystemSettingsCommand extends SystemCommand {

    private SystemSettings newSettings;

    /**
     * Creates a new SetSystemSettingsCommand that updates the system settings.
     * The "modified date" is set to the current time, ignoring the modifiedDate
     * property of the given SystemSettings object.
     * 
     * @param actor
     *            the user/system which executes the command
     * @param newSettings
     *            the new system settings
     */
    public SetSystemSettingsCommand(Role actor, SystemSettings newSettings) {
        super(actor, null);
        if (newSettings == null) {
            throw new IllegalArgumentException(
                    "New system settings must not be null.");
        }
        this.newSettings = newSettings;
    }

    /**
     * @throws TransactionException
     *             if the settings are invalid.
     */
    protected void validateNewSettings() throws TransactionException {

        // Data consistency *should* be checked by the database, not the
        // application, but this approach required less effort.
        if (Level.toLevel(newSettings.getLogLevel(), null) == null) {
            throw new TransactionException("Invalid log level.");
        }

        String simpleEmailPattern = "[\\p{L}_-]+@[a-z0-9\\.\\-_]\\.[a-z]{2,4}";

        if (newSettings.getSystemEmailAddress() == null
                || !newSettings.getSystemEmailAddress().matches(
                        simpleEmailPattern)) {
            throw new TransactionException(
                    "System email address must not be null or empty.");
        }
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        validateNewSettings();

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
