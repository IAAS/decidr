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

import org.hibernate.Query;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerType;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.exceptions.EntityNotFoundException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Adds a server to the DecidR database. The server will not really be created.
 * It's only a representation of the real server.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class AddServerCommand extends SystemCommand {

    private ServerTypeEnum type = null;
    private String location = null;
    private Byte initialLoad = null;
    private Boolean locked = null;
    private Boolean dynamicallyAdded = null;

    private Server newServer = null;

    /**
     * Creates a new AddServerCommand. The created command adds a server to the
     * DecidR database. The server will not really be created. It's only a
     * representation of the real server.
     * 
     * @param actor
     *            the user which executes the command
     * @param type
     *            the type of the server
     * @param location
     *            the location of the real existing server
     * @param initialLoad
     *            the start load of the server (default is 0)
     * @param locked
     *            lock status of the server
     * @param dynamicallyAdded
     *            yes if server has been added automatically, else false
     * @throws IllegalArgumentException
     *             if the location is null or empty or if no type is given.
     */
    public AddServerCommand(Role actor, ServerTypeEnum type, String location,
            Byte initialLoad, Boolean locked, Boolean dynamicallyAdded) {
        super(actor, null);

        if (type == null || location == null || location.isEmpty()) {
            throw new IllegalArgumentException(
                    "Server type and location must not be null or empty.");
        }

        this.type = type;
        this.location = location;
        this.initialLoad = initialLoad;
        this.locked = locked;
        this.dynamicallyAdded = dynamicallyAdded;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt)
            throws TransactionException {
        String hql = "from ServerType s where s.name = :serverType";
        Query q = evt.getSession().createQuery(hql).setString("serverType",
                type.toString());
        ServerType serverType = (ServerType) q.uniqueResult();

        if (serverType == null) {
            throw new EntityNotFoundException(ServerType.class);
        }

        Server newServer = new Server();
        newServer.setDynamicallyAdded(dynamicallyAdded);
        newServer.setLoad(initialLoad);
        newServer.setLocation(location);
        newServer.setLocked(locked);
        newServer.setServerType(serverType);

        evt.getSession().save(newServer);

        this.newServer = newServer;
    }

    /**
     * @return the new server
     */
    public Server getNewServer() {
        return newServer;
    }
}
