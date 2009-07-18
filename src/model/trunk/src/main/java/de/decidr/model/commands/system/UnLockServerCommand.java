package de.decidr.model.commands.system;

import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

/**
 * 
 * Sets the server of the given server to locked or unlocked. If the server does
 * not exist, nothing will happen.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class UnLockServerCommand extends SystemCommand {

    private Boolean lock = false;
    private Long serverId = null;

    /**
     * Creates a new UnLockServerCommand. The command unlocks the given server.
     * If server does not exist, nothing will happen.
     * 
     * @param role
     *            user, who wants to execute the command
     * @param serverId
     *            id of server to unlock
     * @param whether
     *            the server should be locked. If false, the server is unlocked.
     */
    public UnLockServerCommand(Role role, Long serverId, Boolean lock) {
        super(role, null);
        this.serverId = serverId;
        this.lock = lock;
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {
        evt.getSession().createQuery(
                "update Server set locked = :newLock where id = :serverId")
                .setBoolean("newLock", lock).setLong("serverId", serverId)
                .executeUpdate();
    }

}
