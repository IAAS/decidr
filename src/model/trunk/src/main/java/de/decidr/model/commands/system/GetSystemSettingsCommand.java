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

import java.util.List;

import org.hibernate.Criteria;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Retrieves the current system settings from the database.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class GetSystemSettingsCommand extends SystemCommand {

    private SystemSettings result;

    /**
     * Creates a new GetSystemSettingsCommand. The command loads the System
     * Settings from the database and copies it in the variable result.
     * 
     * @param role
     *            user which executes the command
     */
    public GetSystemSettingsCommand(Role role) {
        super(role, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {

        Criteria c = evt.getSession().createCriteria(SystemSettings.class);
        List<SystemSettings> results = c.list();

        if (results.size() > 0) {
            throw new TransactionException(
                    "More than one system settings row found, but system settings should be unique.");
        } else if (results.size() == 0) {
            throw new EntityNotFoundException(SystemSettings.class);
        } else {
            result = results.get(0);
        }
    }

    /**
     * @return the result
     */
    public SystemSettings getResult() {
        return result;
    }
}
