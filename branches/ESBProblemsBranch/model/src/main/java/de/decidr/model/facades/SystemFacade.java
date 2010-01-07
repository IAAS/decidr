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

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.acl.roles.ServerLoadUpdaterRole;
import de.decidr.model.acl.roles.SuperAdminRole;
import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.system.AddServerCommand;
import de.decidr.model.commands.system.GetLogCommand;
import de.decidr.model.commands.system.GetServerStatisticsCommand;
import de.decidr.model.commands.system.GetServersCommand;
import de.decidr.model.commands.system.GetSystemSettingsCommand;
import de.decidr.model.commands.system.LockServerCommand;
import de.decidr.model.commands.system.RemoveServerCommand;
import de.decidr.model.commands.system.SetSystemSettingsCommand;
import de.decidr.model.commands.system.UpdateServerLoadCommand;
import de.decidr.model.entities.Log;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.entities.User;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionCoordinator;

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
     * If you need a SystemSettings object instead, please use
     * <code>DecidrGlobals.getSettings()</code> instead!
     * 
     * Returns the current system settings as a Vaadin item with the following
     * properties:
     * <ul>
     * <li>autoAcceptNewTenants: <code>{@link Boolean}</code> - whether new
     * tenants don't have to be approved by the super admin</li>
     * <li>systemName: <code>{@link String}</code> - name of the system (usually
     * "DecidR")</li>
     * <li>domain: <code>{@link String}</code> - domain where the system can be
     * reached ("decidr.de"). Used for URL generation.</li>
     * <li>systemEmailAddress: <code>{@link String}</code> - from-Address to use
     * when sending notifications to users</li>
     * <li>logLevel: <code>{@link String}</code> - current global log level</li>
     * <li>superAdmin: <code>{@link User}</code> - the super admin</li>
     * <li>passwordResetRequestLifetimeSeconds: <code>{@link Integer}</code> -
     * the user has this many seconds to confirm his password reset.</li>
     * <li>registrationRequestLifetimeSeconds: <code>{@link Integer}</code> -
     * the user has this many seconds to confirm his registration.</li>
     * <li>changeEmailRequestLifetimeSeconds: <code>{@link Integer}</code> - the
     * user has this many seconds to confirm his new email address</li>
     * <li>invitationLifetimeSeconds: <code>{@link Integer}</code> - the user
     * has this many seconds to confirm or reject an invitation.</li>
     * <li>mtaHostname: <code>{@link String}</code> - mail transfer agent
     * hostname.</li>
     * <li>mtaPort: <code>{@link Integer}</code> - mail transfer agent port.</li>
     * <li>mtaUseTls: <code>{@link Boolean}</code> - whether transport layer
     * security should be used when sending emails.</li>
     * <li>mtaUsername: <code>{@link String}</code> - username to use when
     * sending email.</li>
     * <li>mtaPassword: <code>{@link String}</code> - password to use when
     * seding email</li>
     * <li>maxUploadFileSizeBytes: <code>{@link Long}</code> - maximum filesize
     * for any file uploads</li>
     * <li>maxAttachmentsPerEmail: <code>{@link Integer}</code> - maximum number
     * of attachments an email sent by the system can have.</li>
     * <li>monitorUpdateIntervalSeconds: {@link Integer} - time between to ODE
     * monitor status updates in seconds</li>
     * <li>monitorAveragingPeriodSeconds: {@link Integer} - time span over which
     * collected data is averaged in seconds.</li>
     * <li>serverPoolInstances: {@link Integer} - number of server instances
     * that should never be shut down automatically by the monitor service</li>
     * <li>minServerLoadForLock: {@link Byte} - if server load goes above this
     * value, the server will be locked.</li>
     * <li>minServerLoadForUnlock: {@link Byte} - if server load goes below this
     * value, the server will be unlocked</li>
     * <li>maxServerLoadForShutdown: {@link Byte} - if server load goes below
     * this value, the load monitor will consider shutting down the affected
     * server.</li>
     * <li>minUnlockedServers: {@link Integer} - number of servers that should
     * remain unlocked even if their load is above minServerLoadForLock</li>
     * <li>minWorkflowInstancesForLock: {@link Integer} - if a server has more
     * than minWorkflowInstancesForLock workflow instances it will be locked by
     * the load monitor</li>
     * <li>maxWorkflowInstanceForUnlock: {@link Integer} - if a server has less
     * than maxWorkflowInstancesForLock workflow instances it will be unlocked
     * by the load monitor</li>
     * <li>maxWorkflowInstancesForShutdown: {@link Integer} - if a server has
     * less than maxWorkflowInstancesForShutdown workflow instances, the load
     * monitor will consider shutting it down.</li>
     * </ul>
     * 
     * @return system settings as <code>{@link Item}</code>
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(SuperAdminRole.class)
    public Item getSettings() throws TransactionException {
        GetSystemSettingsCommand command = new GetSystemSettingsCommand(actor);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return new BeanItem(command.getResult());
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
     * Returns the system logs as a list of vaadin items. The list can be
     * filtered by using filters and an optional paginator.
     * 
     * @param filters
     *            filters the result by the given criteria
     * @param paginator
     *            splits the result in several pages
     * @return <code>{@link List<Item>}</code> Logs as items
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(SuperAdminRole.class)
    public List<Item> getLog(List<Filter> filters, Paginator paginator)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetLogCommand command = new GetLogCommand(actor, filters, paginator);

        List<Item> outList = new ArrayList<Item>();
        List<Log> inList = new ArrayList<Log>();

        tac.runTransaction(command);
        inList = command.getResult();

        for (Log log : inList) {
            outList.add(new BeanItem(log));
        }

        return outList;
    }

    /**
     * Returns a list of the existing servers as a list of Vaadin items. Each
     * item has the following properties:
     * <ul>
     * <li>id: <code>{@link Long}</code> - the server id</li>
     * <li>location: <code>{@link String}</code> - server location (url or
     * hostname)</li>
     * <li>load: <code>{@link Byte}</code> - the server load in percent</li>
     * <li>numInstances: <code>{@link Long}</code> - the number of workflow
     * instances on that server</li>
     * <li>dynamicallyAdded: <code>{@link Boolean}</code> - whether the server
     * was dynamically added</li>
     * <li>serverType: <code>{@link String}</code> - type of server</li>
     * </ul>
     * 
     * @return ServerStatistics as a list of Vaadin
     *         <code>{@link Item items}</code>
     * @throws TransactionException
     *             iff the transaction is aborted for any reason.
     */
    @AllowedRole(SuperAdminRole.class)
    public List<Item> getServerStatistics() throws TransactionException {
        GetServerStatisticsCommand command = new GetServerStatisticsCommand(
                actor);

        String[] properties = { "id", "location", "load", "numInstances",
                "dynamicallyAdded", "serverType" };

        HibernateTransactionCoordinator.getInstance().runTransaction(command);

        List<Item> result = new ArrayList<Item>();
        for (ServerLoadView server : command.getResult()) {
            result.add(new BeanItem(server, properties));
        }
        return result;
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