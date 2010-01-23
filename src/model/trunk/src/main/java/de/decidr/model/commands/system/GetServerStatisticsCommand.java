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

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.transactions.TransactionStartedEvent;

/**
 * Writes a list of the existing servers to the result variable.
 * 
 * @author Markus Fischer
 * @version 0.1
 */
public class GetServerStatisticsCommand extends SystemCommand {

    List<ServerLoadView> result;

    /**
     * Creates a new GetServerStatisticsCommand. The command saves all servers
     * in the result variable.
     * 
     * @param role
     *            the user who wants to execute the command
     */
    public GetServerStatisticsCommand(Role role) {
        super(role, null);
    }

    /**
     * @return the result
     */
    public List<ServerLoadView> getResult() {
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionStartedEvent evt) {
        result = evt.getSession().createQuery("from ServerLoadView").list();
    }
}