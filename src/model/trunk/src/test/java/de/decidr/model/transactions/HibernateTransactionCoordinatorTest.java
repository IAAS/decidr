package de.decidr.model.transactions;

import static org.junit.Assert.*;

import org.junit.Test;

import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.exceptions.TransactionException;

public class HibernateTransactionCoordinatorTest {

    class testCommandCommit extends AbstractTransactionalCommand {

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
        public Boolean getAboarted() {
            return aborted;
        }

    }

    class testCommandAbort extends AbstractTransactionalCommand {

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
        testCommandCommit c = new testCommandCommit();
        testCommandAbort c2 = new testCommandAbort();
        Boolean transactionThrown = false;

        htc.runTransaction(c);

        assertTrue(c.getStarted());
        assertTrue(c.getCommitted());
        assertFalse(c.getAboarted());

        try {
            htc.runTransaction(c2);
        } catch (TransactionException e) {
            transactionThrown = true;
        }

        assertTrue(c.getStarted());
        assertFalse(c.getCommitted());
        assertTrue(c.getAboarted());
        assertTrue(transactionThrown);
    }

    @Test
    public void testRunTransactionTransactionalCommandArray()
            throws TransactionException {

        TransactionCoordinator htc = HibernateTransactionCoordinator
                .getInstance();
        testCommandCommit c = new testCommandCommit();
        testCommandCommit c2 = new testCommandCommit();
        testCommandCommit[] commands = { c, c2 };

        htc.runTransaction(commands);

        assertTrue(c.getStarted());
        assertTrue(c.getCommitted());
        assertFalse(c.getAboarted());

        assertTrue(c2.getStarted());
        assertTrue(c2.getCommitted());
        assertFalse(c2.getAboarted());

        try {
            htc.runTransaction((TransactionalCommand) null);
            fail("can't run empty transaction");
        } catch (TransactionException e) {
            // This Exception is supposed to be thrown
        }
        try {
            htc.runTransaction();
            fail("can't run empty transaction");
        } catch (TransactionException e) {
            // This Exception is supposed to be thrown
        }
        try {
            htc.runTransaction(new TransactionalCommand[2]);
            fail("can't run empty transaction");
        } catch (TransactionException e) {
            // This Exception is supposed to be thrown
        }
    }

    // TK @Tom: finish this test
}
