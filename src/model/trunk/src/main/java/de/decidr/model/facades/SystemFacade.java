package de.decidr.model.facades;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import de.decidr.model.annotations.AllowedRole;
import de.decidr.model.commands.system.AddServerCommand;
import de.decidr.model.commands.system.GetLogCommand;
import de.decidr.model.commands.system.GetServerStatisticsCommand;
import de.decidr.model.commands.system.GetSystemSettingsCommand;
import de.decidr.model.commands.system.LockServerCommand;
import de.decidr.model.commands.system.RemoveServerCommand;
import de.decidr.model.commands.system.SetSystemSettingsCommand;
import de.decidr.model.commands.system.UnLockServerCommand;
import de.decidr.model.commands.system.UpdateServerLoadCommand;
import de.decidr.model.entities.Log;
import de.decidr.model.entities.ServerLoadView;
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
 * 
 * @version 0.1
 */
public class SystemFacade extends AbstractFacade {

    /**
     * Creates a new system facade. All operations are executed by the given
     * actor.
     * 
     * @param actor
     */
    public SystemFacade(Role actor) {
        super(actor);
    }

    /**
     * Returns the current system settings as item with the following
     * properties:<br>
     * - logLevel<br>
     * - autoAcceptNewTenants<br>
     * <br>
     * 
     * @return system settings as item
     * @throws TransactionException
     */
    @AllowedRole(SuperAdminRole.class)
    public Item getSettings() throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();

        GetSystemSettingsCommand command = new GetSystemSettingsCommand(actor);
        String[] properties = { "logLevel", "autoAcceptNewTenants" };

        tac.runTransaction(command);

        return new BeanItem(command.getResult(), properties);

    }

    /**
     * 
     * Sets the given system settings.<br>
     * <br>
     * <i>AutoAcceptNewTenants</i> - new tenants will be automatically accepted
     * by the system and must not be approved manually by the super admin<br>
     * <br>
     * <i>LogLever</i> - System wide log level setting
     * 
     * 
     * @throws TransactionException
     */
    @AllowedRole(SuperAdminRole.class)
    public void setSettings(Boolean AutoAcceptNewTenants, Level loglevel)
            throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        SetSystemSettingsCommand command = new SetSystemSettingsCommand(actor,
                loglevel, AutoAcceptNewTenants);

        tac.runTransaction(command);

    }

    /**
     * Adds an server to the Database. This command will not set up an new
     * Server. The added server is only a representation of a real one.
     * 
     * @param location
     *            the location of the real server to which representative should
     *            be created<br>
     * 
     * @throws TransactionException
     */
    @AllowedRole(SuperAdminRole.class)
    public void addServer(String location) throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        AddServerCommand command = new AddServerCommand(actor, location);

        tac.runTransaction(command);
    }

    /**
     * Removes the server from the database.This command will not shut down the
     * server. The server is only a representation of a real one and only the
     * representative will be deleted.<br>
     * <br>
     * If the server doesn't exist the command will be ignored.
     * 
     * @param location
     *            location of the server representative which should be deleted
     * 
     * @throws TransactionException
     * 
     */
    @AllowedRole(SuperAdminRole.class)
    public void removeServer(String location) throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        RemoveServerCommand command = new RemoveServerCommand(actor, location);

        tac.runTransaction(command);

    }

    /**
     * 
     * Updates the load of the representative object of the server at the
     * database. The load must be in the range of 0 to 100. If given server does
     * not exists nothing will happen.
     * 
     * @param location
     *            location of the server representative which should be deleted
     * @param load
     *            new load server load as byte [0...100]
     * 
     * @throws TransactionException
     */
    @AllowedRole( { SuperAdminRole.class, ServerLoadUpdaterRole.class })
    public void updateServerLoad(String location, byte load)
            throws TransactionException {

        if ((load < 0) || (load > 100)) {
            throw new IllegalArgumentException(
                    "The load must be in the range of 0 to 100.");
        } else {
            TransactionCoordinator tac = HibernateTransactionCoordinator
                    .getInstance();
            UpdateServerLoadCommand command = new UpdateServerLoadCommand(
                    actor, location, load);

            tac.runTransaction(command);
        }

    }

    /**
     * 
     * Sets the lock flag of the representative object for the given server. If
     * Server does not exist, nothing will happen.
     * 
     * @param location
     *            location of the server representative which should be deleted
     * 
     * @throws TransactionException
     */
    @AllowedRole(SuperAdminRole.class)
    public void lockServer(String location) throws TransactionException {
        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        LockServerCommand command = new LockServerCommand(actor, location);

        tac.runTransaction(command);
    }

    /**
     * 
     * Releases the lock flag of the representative object for the given server.
     * If Server does not exist, nothing will happen.
     * 
     * @param location
     *            location of the server representative which should be deleted
     * 
     * @throws TransactionException
     */
    @AllowedRole(SuperAdminRole.class)
    public void unlockServer(String location) throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        UnLockServerCommand command = new UnLockServerCommand(actor, location);

        tac.runTransaction(command);
    }

    /**
     * 
     * Returns the system logs as a list of items. The List can be filtered by
     * using filters and an optional paginator.
     * 
     * 
     * @param filters
     * @param paginator
     * @return List<Item> Logs as items
     * @throws TransactionException
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
     * Returns a list of the existing unlocked servers as Item list. Each Item
     * has the following properties:<br>
     * - id<br>
     * - location<br>
     * - load<br>
     * - numInstances<br>
     * - dynamicallyAdded
     * 
     * @return ServerStatistics as a List of Items
     * @throws TransactionException
     */
    @SuppressWarnings("unchecked")
    @AllowedRole(SuperAdminRole.class)
    public List<Item> getServerStatistics() throws TransactionException {

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetServerStatisticsCommand command = new GetServerStatisticsCommand(
                actor);

        List<Item> outList = new ArrayList<Item>();
        List<ServerLoadView> inList = new ArrayList();
        @SuppressWarnings("unused")
        String[] properties = { "id", "location", "load", "numInstances",
                "dynamicallyAdded" };

        tac.runTransaction(command);
        inList = command.getResult();

        for (ServerLoadView server : inList) {
            outList.add(new BeanItem(server, properties));
        }

        return outList;
    }
}