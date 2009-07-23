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

import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.system.AddServerCommand;
import de.decidr.model.commands.system.GetLogCommand;
import de.decidr.model.commands.system.GetServerStatisticsCommand;
import de.decidr.model.commands.system.GetSystemSettingsCommand;
import de.decidr.model.commands.system.RemoveServerCommand;
import de.decidr.model.commands.system.SetSystemSettingsCommand;
import de.decidr.model.commands.system.LockServerCommand;
import de.decidr.model.commands.system.UpdateServerLoadCommand;
import de.decidr.model.entities.Log;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.enums.ServerTypeEnum;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.ServerLoadUpdaterRole;
import de.decidr.model.permissions.SuperAdminRole;
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
     * Returns the current system settings as a Vaadin item with the following
     * properties:
     * <ul>
     * <li>autoAcceptNewTenants: <code>{@link Boolean}</code> - whether new
     * tenants don't have to be approved by the super admin</li>
     * <li>systemName: <code>{@link }</code> - name of the system (usually
     * "DecidR")</li>
     * <li>domain: <code>{@link String}</code> - domain where the system can be
     * reached ("decidr.de"). Used for URL generation.</li>
     * <li>systemEmailAddress: <code>{@link String}</code> - from-Address to use
     * when sending notifications to users</li>
     * <li>logLevel: <code>{@link String}</code> - current global log level</li>
     * <li>passwordResetRequestLifeTimeSeconds: <code>{@link Integer}</code> -
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
     * <li>maxUploadFileSizeByte: <code>{@link Long}</code> - maximum filesize
     * for any file uploads</li>
     * <li>maxAttachmentsPerEmail: <code>{@link Integer}</code> - maximum number
     * of attachments an email sent by the system can have.</li>
     * </ul>
     * 
     * @return system settings as <code>{@link Item}</code>
     * @throws TransactionException
     *             if an error occurs during the transaction
     */
    @AllowedRole(SuperAdminRole.class)
    public Item getSettings() throws TransactionException {
        GetSystemSettingsCommand command = new GetSystemSettingsCommand(actor);
        String[] properties = { "autoAcceptNewTenants", "systemName", "domain",
                "systemEmailAddress", "logLevel",
                "passwordResetRequestLifeTimeSeconds",
                "registrationRequestLifetimeSeconds",
                "changeEmailRequestLifetimeSeconds",
                "invitationLifetimeSeconds", "mtaHostname", "mtaPort",
                "mtaUseTls", "mtaUsername", "mtaPassword",
                "maxUploadFileSizeByte", "maxAttachmentsPerEmail" };
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        return new BeanItem(command.getResult(), properties);
    }

    /**
     * Sets the given system settings.
     * 
     * @throws TransactionException
     *             if an error occurs during the transaction s
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
     *             if an error occurs during the transaction
     */
    @AllowedRole(SuperAdminRole.class)
    public Server addServer(ServerTypeEnum type, String location,
            Byte initialLoad, Boolean locked, Boolean dynamicallyAdded)
            throws TransactionException {
        AddServerCommand cmd = new AddServerCommand(actor, type, location,
                initialLoad, locked, dynamicallyAdded);
        HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
        return cmd.getNewServer();
    }

    /**
     * Removes the server from the database. This command will not shut down the
     * server. <br>
     * If the server doesn't exist the command will be ignored.
     * 
     * @param serverId
     *            TODO document
     * @throws TransactionException
     *             if an error occurs during the transaction
     */
    @AllowedRole(SuperAdminRole.class)
    public void removeServer(Long serverId) throws TransactionException {
        RemoveServerCommand command = new RemoveServerCommand(actor, serverId);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * FIXME how does our server load updater work??
     * 
     * Updates the load of the representative object of the server at the
     * database. The load must be in the range of 0 to 100. If given server does
     * not exist no exception will be raised.
     * 
     * @param location
     *            location of the server representative which should be deleted
     * @param load
     *            new load server load as byte [0...100]
     * @throws TransactionException
     *             if an error occurs during the transaction
     */
    @AllowedRole( { SuperAdminRole.class, ServerLoadUpdaterRole.class })
    public void updateServerLoad(String location, byte load)
            throws TransactionException {

        if ((load < 0) || (load > 100)) {
            throw new IllegalArgumentException(
                    "The load must be in the range from 0 to 100.");
        } else {
            TransactionCoordinator tac = HibernateTransactionCoordinator
                    .getInstance();
            UpdateServerLoadCommand command = new UpdateServerLoadCommand(
                    actor, location, load);

            tac.runTransaction(command);
        }
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
     *             if an error occurs during the transaction
     */
    @AllowedRole(SuperAdminRole.class)
    public void setServerLock(Long serverId, Boolean lock)
            throws TransactionException {
        LockServerCommand command = new LockServerCommand(actor, serverId, lock);
        HibernateTransactionCoordinator.getInstance().runTransaction(command);
    }

    /**
     * Returns the system logs as a list of items. The list can be filtered by
     * using filters and an optional paginator.
     * 
     * @param filters
     *            filters the result by the given criteria
     * @param paginator
     *            splits the result in several pages
     * @return <code>{@link List<Item>}</code> Logs as items
     * @throws TransactionException
     *             if an error occurs during the transaction
     */
    @SuppressWarnings("unchecked")
    @AllowedRole(SuperAdminRole.class)
    public List<Item> getLog(List<Filter> filters, Paginator paginator)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetLogCommand command = new GetLogCommand(actor, filters, paginator);

        List<Item> outList = new ArrayList<Item>();
        List<Log> inList = new ArrayList();

        tac.runTransaction(command);
        inList = command.getResult();

        for (Log log : inList) {
            outList.add(new BeanItem(log));
        }

        return outList;
    }

    /**
     * Returns a list of the existing unlocked servers as a list of Vaadin
     * items. Each item has the following properties:
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
     *             if an error occurs during the transaction
     * 
     */
    @SuppressWarnings("unchecked")
    @AllowedRole(SuperAdminRole.class)
    public List<Item> getServerStatistics() throws TransactionException {
        GetServerStatisticsCommand command = new GetServerStatisticsCommand(
                actor);

        List<ServerLoadView> servers = new ArrayList();
        String[] properties = { "id", "location", "load", "numInstances",
                "dynamicallyAdded", "serverType" };

        HibernateTransactionCoordinator.getInstance().runTransaction(command);
        servers = command.getResult();

        List<Item> result = new ArrayList<Item>();
        for (ServerLoadView server : servers) {
            result.add(new BeanItem(server, properties));
        }
        return result;
    }
}