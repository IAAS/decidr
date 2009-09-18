package de.decidr.model.transactions;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.hibernate.cfg.Configuration;
import org.junit.Test;

import de.decidr.model.CommandsTest;
import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;

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
        public void transactionStarted(TransactionEvent evt) {
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
        public void transactionStarted(TransactionEvent evt) {
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
            // RR it seems that a blank new configuration does not have all
            // required properties set. Try calling the configure() method or set
            // the required properties manually. ~dh
            htc.setConfiguration(cfg);

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

    @Test
    public void testRunTransactionTransactionalCommandCollection()
            throws TransactionException {
        TransactionCoordinator htc = HibernateTransactionCoordinator
                .getInstance();
        TestCommandCommit c = new TestCommandCommit();
        TestCommandCommit c2 = new TestCommandCommit();
        Collection<TransactionalCommand> commands = new ArrayList<TransactionalCommand>();
        commands.add(c);
        commands.add(c2);

        htc.runTransaction(commands);

        assertTrue(c.started);
        assertTrue(c.committed);
        assertFalse(c.aborted);

        assertTrue(c2.started);
        assertTrue(c2.committed);
        assertFalse(c2.aborted);

        try {
            htc.runTransaction((Collection<TransactionalCommand>) null);
            fail("can't run empty transaction");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
        try {
            htc.runTransaction(new ArrayList<TransactionalCommand>());
            fail("can't run empty transaction");
        } catch (TransactionException e) {
            // supposed to be thrown
        }
    }
}
