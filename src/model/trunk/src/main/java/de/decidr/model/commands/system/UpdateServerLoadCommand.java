package de.decidr.model.commands.system;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * Updates the server load of a given server at the database.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class UpdateServerLoadCommand extends SystemCommand {

    private Long serverId = null;
    private byte load;

    /**
     * Updates the server load of a given server at the database. If given
     * server does not exists nothing will happen.
     * 
     * @param role
     *            the user who wants to execute the command
     * @param serverid
     *            the id of the server to update
     * @param load
     *            the new load
     */
    public UpdateServerLoadCommand(Role role, Long serverId, byte load) {
        super(role, null);
        this.serverId = serverId;
        this.load = load;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {

        evt.getSession().createQuery(
                "update Server set load = :newLoad where id = :serverId")
                .setByte("newLoad", load).setLong("serverId", serverId)
                .executeUpdate();
    }
}
