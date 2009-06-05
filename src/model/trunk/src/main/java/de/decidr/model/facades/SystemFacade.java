package de.decidr.model.facades;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import de.decidr.model.commands.system.AddServerCommand;
import de.decidr.model.commands.system.GetServerStatisticsCommand;
import de.decidr.model.commands.system.GetSystemSettingsCommand;
import de.decidr.model.commands.system.LockServerCommand;
import de.decidr.model.commands.system.RemoveServerCommand;
import de.decidr.model.commands.system.SetSystemSettingsCommand;
import de.decidr.model.commands.system.UnLockServerCommand;
import de.decidr.model.commands.system.UpdateServerLoadCommand;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;
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
     * 
     * Creates a new system facade. All operations are executed by the given
     * actor.
     * 
     * @param actor
     */
    public SystemFacade(Role actor) {
        super(actor);
    }

    /**
     * 
     * Returns the current system settings as item with the following
     * properties: - logLevel - autoAcceptNewTenants
     * 
     * Only the super admin is allowed to use this command.
     * 
     * @return
     */
    public Item getSettings() throws TransactionException{

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetSystemSettingsCommand command = new GetSystemSettingsCommand(actor);
        String[] properties = { "logLevel", "autoAcceptNewTenants" };

        tac.runTransaction(command);

        return new BeanItem(command.getResult(), properties);

    }

    /**
     * 
     * Sets the given system settings. The settings MUST be passed as item with
     * the following properties: - logLevel - autoAcceptNewTenants
     * 
     * Only the super admin is allowed to use this command.
     * 
     * @return
     */
    public void setSettings(Item settings) throws TransactionException{

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        SetSystemSettingsCommand command = new SetSystemSettingsCommand(actor,
                settings);

        tac.runTransaction(command);

    }

    /**
     * Adds an server to the Database. This command will not set up an new
     * Server. The added server is only a representation of a real one.
     * 
     * @param location
     *            the location of the server representative, which should be
     *            created
     */
    public void addServer(String location) throws TransactionException{

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        AddServerCommand command = new AddServerCommand(actor, location);

        tac.runTransaction(command);
    }

    /**
     * Removes the server from the database.This command will not shut down the
     * server. The server is only a representation of a real one and only the
     * representative will be deleted.
     * 
     * @param location location of the server representative which should be deleted
     */
    public void removeServer(String location) throws TransactionException{
        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        RemoveServerCommand command = new RemoveServerCommand(actor, location);

        tac.runTransaction(command);
    }

    
    /**
     * 
     * Updates the load of the representative object of the server at the database.
     * 
     * @param location location of the server representative which should be deleted
     * @param load new load
     */
    public void updateServerLoad(String location, byte load) throws TransactionException{
        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        UpdateServerLoadCommand command = new UpdateServerLoadCommand(actor,
                location, load);

        tac.runTransaction(command);
    }

    
    /**
     * 
     * Sets the lock flag of the representative object for the given server.
     * 
     * @param location location of the server representative which should be deleted
     */

    public void lockServer(String location) throws TransactionException{
        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        LockServerCommand command = new LockServerCommand(actor, location);

        tac.runTransaction(command);
    }

    /**
     * 
     * Releases the lock flag of the representative object for the given server.
     * 
     * @param location location of the server representative which should be deleted
     */
    public void unlockServer(String location) throws TransactionException{
        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        UnLockServerCommand command = new UnLockServerCommand(actor, location);

        tac.runTransaction(command);
    }

    public List<Item> getLog(List<Filter> filters, Paginator paginator) throws TransactionException{
        // FIXME
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a list of the existing unlocked servers as Item list.
     * Each Item has the following properties:
     * 
     * - id
     * - location
     * - load
     * - numInstances
     * - dynamicallyAdded
     * 
     * @return ServerStatistics as a List of Items
     */
    public List<Item> getServerStatistics() throws TransactionException{

        TransactionCoordinator tac = HibernateTransactionCoordinator
                .getInstance();
        GetServerStatisticsCommand command = new GetServerStatisticsCommand(
                actor);

        List<Item> outList = new ArrayList<Item>();
        List<ServerLoadView> inList;
        @SuppressWarnings("unused")
        String[] properties = { "id", "location", "load", "numInstances",
                "dynamicallyAdded" };

        tac.runTransaction(command);
        inList = command.getResult();

        for (ServerLoadView server : inList) {
            outList.add(new BeanItem(server));
        }

        return outList;
    }
}