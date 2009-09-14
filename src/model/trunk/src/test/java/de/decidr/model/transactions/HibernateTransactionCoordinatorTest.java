package de.decidr.model.transactions;

import static org.junit.Assert.*;

import org.junit.Test;

import de.decidr.model.CommandsTest;
import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;

public class HibernateTransactionCoordinatorTest extends CommandsTest {

    class TestCommandCommit extends AbstractTransactionalCommand {

        private Boolean started = false;
        private Boolean committed = false;
        private Boolean aborted = false;

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

        /**
         * @return the started
         */
        public Boolean getStarted() {
            return started;
        }

        /**
         * @return the committed
         */
        public Boolean getCommitted() {
            return committed;
        }

        /**
         * @return the aborted
         */
        public Boolean getAborted() {
            return aborted;
        }
    }

    class TestCommandAbort extends AbstractTransactionalCommand {

        private Boolean started = false;
        private Boolean committed = false;
        private Boolean aborted = false;

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

        /**
         * @return the started
         */
        public Boolean getStarted() {
            return started;
        }

        /**
         * @return the committed
         */
        public Boolean getCommitted() {
            return committed;
        }

        /**
         * @return the aborted
         */
        public Boolean getAborted() {
            return aborted;
        }
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

        assertTrue(c.getStarted());
        assertTrue(c.getCommitted());
        assertFalse(c.getAborted());

        try {
            htc.runTransaction(c2);
            fail("transaction should have aborted but didn't");
        } catch (TransactionException e) {
            // supposed to be thrown
        }

        assertTrue(c2.getStarted());
        assertFalse(c2.getCommitted());
        assertTrue(c2.getAborted());
    }

    @Test
    public void testRunTransactionTransactionalCommandCollection() {
        fail("Not yet implemented");
        // RR runTransactionTransactionalCommandCollection
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

        assertTrue(c.getStarted());
        assertTrue(c.getCommitted());
        assertFalse(c.getAborted());

        assertTrue(c2.getStarted());
        assertTrue(c2.getCommitted());
        assertFalse(c2.getAborted());

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
    public void testGetConfiguration() {
        fail("Not yet implemented"); // RR getConfiguration
    }

    @Test
    public void testSetConfiguration() {
        fail("Not yet implemented"); // RR setConfiguration
    }

    @Test
    public void testGetCurrentSession() {
        fail("Not yet implemented"); // RR getCurrentSession
    }
}
