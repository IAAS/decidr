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

package de.decidr.model.commands.workitem;

import org.hibernate.Query;

import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Deletes the given work item from the database. If the given work item doesn't
 * exist, no exception is thrown.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class DeleteWorkItemCommand extends WorkItemCommand {

    /**
     * Constructor TODO document
     * 
     * @param role
     *            TODO document
     * @param workItemId
     *            TODO document
     */
    public DeleteWorkItemCommand(Role role, Long workItemId) {
        super(role, workItemId);
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {
        Query q = evt.getSession().createQuery(
                "delete from WorkItem w where w.id = :workItemId");
        q.setLong("workItemId", workItemId);
        q.executeUpdate();
    }
}
