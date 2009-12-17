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
package de.decidr.model.facades;

import java.util.List;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.ServerLoadUpdaterRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.system.AddServerCommand;
import de.decidr.model.commands.system.GetLogCommand;
import de.decidr.model.commands.system.GetServerStatisticsCommand;
import de.decidr.model.commands.system.GetServersCommand;
import de.decidr.model.commands.system.LockServerCommand;
import de.decidr.model.commands.system.RemoveServerCommand;
import de.decidr.model.commands.system.SetSystemSettingsCommand;
import de.decidr.model.commands.system.UpdateServerLoadCommand;
import de.decidr.model.entities.Log;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

/**
 * The system facade contains all functions which are available to modify system
 * data/settings.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class SystemFacade extends AbstractFacade {

    /**
     * Creates a new system facade. All operations are executed by the given
     * actor.
     * 
     * @param actor
     *            user who will execute the commands of the created facade
     */
    public SystemFacade(Role actor) {
        super(actor);
    }

    /**
     * Sets the given system settings. The "modified date" is set to the current
     * time, ignoring the modifiedDate property of the given SystemSettings
     * object.
     * 
     * @param newSettings
     *            a transient entity containing the new system settings. The id
     *            and modifiedDate properties of this entity will be ignored.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if newSettings is <code>null</code>
     */
    @AllowedRole(SuperAdminRole.class)
    public void setSettings(SystemSettings newSettings)
            throws TransactionException {
        SetSystemSettingsCommand command = new SetSystemSettingsCommand(actor,
                newSettings);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Adds a server to the database. This command does not actually set up a
     * new server, it only adds a reference to the server to the database.
     * 
     * @param type
     *            server type
     * @param location
     *            server "location" (e.g. a URL or a hostname)
     * @param initialLoad
     *            initial server load
     * @param locked
     *            whether or not the new server should be flagged as locked
     * @param dynamicallyAdded
     *            whether or not the new server should be flagged as
     *            "dynamically added"
     * @return the new server
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if type or location is <code>null</code> or if location is
     *             empty.
     */
    @AllowedRole(SuperAdminRole.class)
    public Server addServer(ServerTypeEnum type, String location,
            byte initialLoad, boolean locked, boolean dynamicallyAdded)
            throws TransactionException {
        AddServerCommand cmd = new AddServerCommand(actor, type, location,
                initialLoad, locked, dynamicallyAdded);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getNewServer();
    }

    /**
     * Removes the server from the database. This command will not shut down the
     * server. If the server doesn't exist no exception is thrown. <br>
     * Please note that removing a server causes all workflow instances that
     * have been deployed on this server to be terminated and removed. Workflow
     * models that have been deployed on the affected server are undeployed.
     * 
     * @param serverId
     *            ID of the server which should be removed
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if serverId is <code>null</code>
     */
    @AllowedRole(SuperAdminRole.class)
    public void removeServer(Long serverId) throws TransactionException {
        RemoveServerCommand command = new RemoveServerCommand(actor, serverId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Updates the load of a server entity. The load must be in the range of -1
     * to 100. If given server does not exist no exception will be raised.
     * 
     * @param serverId
     *            id of the server to update
     * @param load
     *            new load server load as byte
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if serverId is null or if the load is out of range.
     */
    @AllowedRole( { SuperAdminRole.class, ServerLoadUpdaterRole.class })
    public void updateServerLoad(Long serverId, byte load)
            throws TransactionException {
        UpdateServerLoadCommand command = new UpdateServerLoadCommand(actor,
                serverId, load);

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Sets or releases the lock flag of the given server. If server does not
     * exist, no exception is thrown.
     * 
     * @param serverId
     *            id of server to lock or unlock
     * @param lock
     *            whether the server should be locked. If false, the server will
     *            be unlocked
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     * @throws IllegalArgumentException
     *             if serverId is <code>null</code>
     */
    @AllowedRole(SuperAdminRole.class)
    public void setServerLock(Long serverId, boolean lock)
            throws TransactionException {
        LockServerCommand command = new LockServerCommand(actor, serverId, lock);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Returns a list of system log entries (only relevant when logging to the
     * database using {@link DefaultLogger}.
     * 
     * @param filters
     *            filters the result by the given criteria
     * @param paginator
     *            splits the result in several pages
     * @return system log entries
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(SuperAdminRole.class)
    public List<Log> getLog(List<Filter> filters, Paginator paginator)
            throws TransactionException {
        GetLogCommand command = new GetLogCommand(actor, filters, paginator);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Returns a list of servers that are known to the system.
     * 
     * @return list of servers including load information.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(SuperAdminRole.class)
    public List<ServerLoadView> getServerStatistics()
            throws TransactionException {
        GetServerStatisticsCommand command = new GetServerStatisticsCommand(
                actor);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return command.getResult();
    }

    /**
     * Retrieves server metainformation from the database. You can specify which
     * types of servers the result should include.
     * 
     * @param serverTypes
     *            server types to include. If <code>null</code> or empty, all
     *            server types are included
     * @return list of servers that match one of the given server types.
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(ServerLoadUpdaterRole.class)
    public List<Server> getServers(ServerTypeEnum... serverTypes)
            throws TransactionException {
        GetServersCommand cmd = new GetServersCommand(actor, serverTypes);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getResult();
    }
}