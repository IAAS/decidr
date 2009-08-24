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
        TransactionCoordinator htc = HibernateTransactionCoordinator
                .getInstance();
        assertNotNull(htc);
    }

    @Test
    public void testRunTransactionTransactionalCommand()
            throws TransactionException {

        TransactionCoordinator htc = HibernateTransactionCoordinator
                .getInstance();
        TestCommandCommit c = new TestCommandCommit();
        TestCommandAbort c2 = new TestCommandAbort();
        Boolean transactionThrown = false;

        htc.runTransaction(c);

        assertTrue(c.getStarted());
        // DH this test fails ~rr
        assertTrue(c.getCommitted());
        assertFalse(c.getAborted());

        try {
            htc.runTransaction(c2);
        } catch (TransactionException e) {
            transactionThrown = true;
        }

        assertTrue(c.getStarted());
        assertFalse(c.getCommitted());
        assertTrue(c.getAborted());
        assertTrue(transactionThrown);
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
        // DH this test fails ~rr
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

    // RR finish this test
}
