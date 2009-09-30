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

package de.decidr.model;

import static org.junit.Assert.fail;

import org.junit.Assert;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.transactions.HibernateTransactionCoordinator;

/**
 * Abstract base class for tests using Transactions.
 * 
 * @author Reinhold
 */
public abstract class CommandsTest extends LowLevelDatabaseTest {

    /**
     * Makes sure that a <code>{@link TransactionException}</code> is thrown.
     * 
     * @param failmsg
     *            The message passed to <code>{@link Assert#fail(String)}</code>
     *            , should a command succeed.
     * @param cmds
     *            the <code>{@link TransactionalCommand commands}</code> to
     *            execute.
     */
    public static void assertTransactionException(String failmsg,
            TransactionalCommand... cmds) {
        for (TransactionalCommand cmd : cmds) {
            assertTransactionException(failmsg, cmd);
        }
    }

    /**
     * Makes sure that a <code>{@link TransactionException}</code> is thrown.
     * 
     * @param failmsg
     *            The message passed to <code>{@link Assert#fail(String)}</code>
     *            , should the command succeed.
     * @param cmd
     *            the <code>{@link TransactionalCommand command}</code> to
     *            execute.
     */
    public static void assertTransactionException(String failmsg,
            TransactionalCommand cmd) {
        try {
            HibernateTransactionCoordinator.getInstance().runTransaction(cmd);
            fail(failmsg);
        } catch (TransactionException e) {
            // is supposed to be thrown
        }
    }
}
