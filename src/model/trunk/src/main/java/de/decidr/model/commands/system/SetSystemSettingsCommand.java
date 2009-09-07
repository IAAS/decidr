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

import de.decidr.model.DecidrGlobals;
import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Sets the system settings.
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
     * 
     * @param actor
     *            the user/system which executes the command
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
