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

package de.decidr.model.transactions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.testing.CommandsTest;

public class HibernateTransactionCoordinatorTest extends CommandsTest {

    class TestCommandAbort extends AbstractTransactionalCommand {

        public Boolean started = false;
        public Boolean committed = false;
        public Boolean aborted = false;

        @Override
        public void transactionAborted(TransactionAbortedEvent evt) {
            aborted = true;
        }

        @Override
        public void transactionCommitted(TransactionEvent evt) {
            committed = true;
        }

        @Override
        public void transactionStarted(TransactionStartedEvent evt) {
            started = true;
            // throw exception to provoke rollback
            throw new RuntimeException("MOEP");
        }
    }

    class TestCommandCommit extends AbstractTransactionalCommand {

        public Boolean started = false;
        public Boolean committed = false;
        public Boolean aborted = false;

        @Override
        public void transactionAborted(TransactionAbortedEvent evt) {
            aborted = true;
        }

        @Override
        public void transactionCommitted(TransactionEvent evt) {
            committed = true;
        }

        @Override
        public void transactionStarted(TransactionStartedEvent evt) {
            started = true;
        }
    }

    @Test
    public void testConfiguration() {
        Configuration origCfg;
        HibernateTransactionCoordinator htc = HibernateTransactionCoordinator
                .getInstance();

        assertNotNull(htc.getConfiguration());
        assertEquals(htc.getConfiguration(), htc.getConfiguration());
        assertSame(htc.getConfiguration(), htc.getConfiguration());

        try {
            htc.setConfiguration(null);
            fail(htc.getClass().getName()
                    + ".setConfiguration() accepted a null configuration");
        } catch (IllegalArgumentException e) {
            // supposed to be thrown
        }

        origCfg = htc.getConfiguration();

        try {
            Configuration cfg = new Configuration();
            try {
                htc.setConfiguration(cfg);
                fail("Accepted empty hibernate config");
            } catch (HibernateException e) {
                // supposed to be thrown
            }

            assertNotNull(origCfg.getProperties());
            cfg.setProperties((Properties) origCfg.getProperties().clone());
            htc.setConfiguration(cfg);
            assertNotNull(cfg.getProperties());
            assertEquals(origCfg.getProperties(), cfg.getProperties());
            assertNotSame(origCfg.getProperties(), cfg.getProperties());

            cfg.setProperty("testproperty", "testvalue");

            htc.setConfiguration(cfg);
            assertEquals("testvalue", htc.getConfiguration().getProperty(
                    "testproperty"));
        } finally {
            try {
                htc.setConfiguration(origCfg);
                assertEquals(origCfg, htc.getConfiguration());
                assertSame(origCfg, htc.getConfiguration());
            } catch (Exception e) {
                throw new Error("Cancelling tests as original hibernate "
                        + "configuration could not be restored!", e);
            }
        }
    }

    @Test
    public void testGetCurrentSession() {
        HibernateTransactionCoordinator.getInstance().getCurrentSession();
    }

    @Test
    public void testGetInstance() {
        assertNotNull(HibernateTransactionCoordinator.getInstance());
        assertNotNull(HibernateTransactionCoordinator.getInstance());
        assertSame(HibernateTransactionCoordinator.getInstance(),
                HibernateTransactionCoordinator.getInstance());
    }

    @Test
    public void testRunTransactionTransactionalCommand()
            throws TransactionException {

        TransactionCoordinator htc = HibernateTransactionCoordinator
                .getInstance();
        TestCommandCommit c = new TestCommandCommit();
        TestCommandAbort c2 = new TestCommandAbort();

        htc.runTransaction(c);

        assertTrue(c.started);
        assertTrue(c.committed);
        assertFalse(c.aborted);

        try {
            htc.runTransaction(c2);
            fail("transaction should have aborted but didn't");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        assertTrue(c2.started);
        assertFalse(c2.committed);
        assertTrue(c2.aborted);
    }

    @Test
    public void testRunTransactionTransactionalCommandArray()
            throws TransactionException {

        TransactionCoordinator htc = HibernateTransactionCoordinator
                .getInstance();
        TestCommandCommit c = new TestCommandCommit();
        TestCommandCommit c2 = new TestCommandCommit();
        TestCommandCommit[] commands = { c, c2 };

        htc.runTransaction(commands);

        assertTrue(c.started);
        assertTrue(c.committed);
        assertFalse(c.aborted);

        assertTrue(c2.started);
        assertTrue(c2.committed);
        assertFalse(c2.aborted);

        assertTransactionException("can't run null transaction",
                (TransactionalCommand) null);
        try {
            htc.runTransaction();
            fail("can't run empty transaction");
        } catch (TransactionException e) {
            // This Exception is supposed to be thrown
        }
        assertTransactionException("can't run empty transaction",
                new TransactionalCommand[2]);
    }

}
