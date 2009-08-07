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

import java.util.ArrayList;
import java.util.List;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Server;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Retrieves server metainformation from the database. You can specify which
 * server types the result should include.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class GetServersCommand extends SystemCommand {

    private ServerTypeEnum[] serverTypes = null;

    private List<Server> result = null;

    /**
     * 
     * Creates a new GetServersCommand that retrieves server metainformation
     * from the database
     * 
     * @param actor
     *            user or system that executes this command
     * @param serverTypes
     *            only servers that have one of the given types will be
     *            included. If this parameter is null, all severs will be
     *            included.
     */
    public GetServersCommand(Role actor, ServerTypeEnum... serverTypes) {
        super(actor, null);
        this.serverTypes = serverTypes;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        String hql;
        // convert enum names to string
        List<String> allowedTypes = new ArrayList<String>();
        if (serverTypes != null) {
            for (ServerTypeEnum allowedType : serverTypes) {
                allowedTypes.add(allowedType.toString());
            }
        }
        // if no server types are given, fetch all server
        if (serverTypes == null) {
            hql = "from Server s join fetch s.serverType";
            result = evt.getSession().createQuery(hql).list();
        } else {
            hql = "from Server s join fetch s.serverType where s.serverType in (:allowedTypes)";
            result = evt.getSession().createQuery(hql).setParameterList(
                    "allowedTypes", allowedTypes).list();
        }
    }

    /**
     * @return the server metainformation
     */
    public List<Server> getResult() {
        return result;
    }

}
