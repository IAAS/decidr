package de.decidr.model.transactions;

import static org.junit.Assert.*;

import org.junit.Test;

import de.decidr.model.commands.AbstractTransactionalCommand;
import de.decidr.model.exceptions.TransactionException;

public class HibernateTransactionCoordinatorTest {

    private class testCommandCommit extends AbstractTransactionalCommand{

        private Boolean started=false;
        private Boolean committed=false;
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
    
    private class testCommandAboart extends AbstractTransactionalCommand{

        private Boolean started=false;
        private Boolean committed=false;
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
        public void transactionStarted(TransactionEvent evt){
     
            started = true;
            
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
        public Boolean getAboarted() {
            return aborted;
        }
        
    }
    
    @Test
    public void testGetInstance() {
        TransactionCoordinator htc = HibernateTransactionCoordinator.getInstance();
        assertNotNull(htc);
    }
    
    @Test
    public void testRunTransactionTransactionalCommand() {
        
        TransactionCoordinator htc = HibernateTransactionCoordinator.getInstance();
        testCommandCommit c = new testCommandCommit();
        testCommandAboart c2 = new testCommandAboart();
        Boolean transactionThrown = false;
        
        try{
            htc.runTransaction(c);
        }
        catch (TransactionException e){
            fail(e.getMessage());
        }
        
        assertTrue(c.getStarted());
        assertTrue(c.getCommitted());
        assertFalse(c.getAboarted());  
        
        try{
            htc.runTransaction(c2);
        }
        catch (TransactionException e){
            transactionThrown = true;
        }
        
        assertTrue(c.getStarted());
        assertFalse(c.getCommitted());
        assertTrue(c.getAboarted()); 
        assertTrue(transactionThrown);
    }
  
    
    @Test
    public void testRunTransactionTransactionalCommandArray() {
        
        TransactionCoordinator htc = HibernateTransactionCoordinator.getInstance();
        testCommandCommit c = new testCommandCommit();
        testCommandCommit c2 = new testCommandCommit();
        testCommandCommit[] commands = {c,c2};
        
        try{
            htc.runTransaction(commands);
        }
        catch (Exception e){
            fail("someting bad happend");
        }
        
        assertTrue(c.getStarted());
        assertTrue(c.getCommitted());
        assertFalse(c.getAboarted());
        
        assertTrue(c2.getStarted());
        assertTrue(c2.getCommitted());
        assertFalse(c2.getAboarted()); 
        
    }
    
    // TK @Tom: finish this test
}
