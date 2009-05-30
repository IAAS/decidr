package de.decidr.model.permissions.asserters;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import de.decidr.model.commands.TransactionalCommand;
import de.decidr.model.entities.File;
import de.decidr.model.entities.UserHasFileAccess;
import de.decidr.model.permissions.Asserter;
import de.decidr.model.permissions.FileDeletePermission;
import de.decidr.model.permissions.FilePermission;
import de.decidr.model.permissions.FileReadPermission;
import de.decidr.model.permissions.FileWritePermission;
import de.decidr.model.permissions.Permission;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.UserRole;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionAbortedEvent;
import de.decidr.model.transactions.TransactionCoordinator;
import de.decidr.model.transactions.TransactionEvent;

public class HasFileAccessTo implements Asserter, TransactionalCommand {

    private Long userid = null;
    private Long fileid = null;
    private File file = null;
    private Permission permission = null;
    Boolean result = false;  
    

    @Override
    public Boolean assertRule(Role role, Permission permission) {

        if ((role instanceof UserRole) && (permission instanceof FilePermission)){
            
            userid = role.getActorId();
            fileid = ((FilePermission)permission).getId();
            this.permission = permission;
            
            TransactionCoordinator htac = HibernateTransactionCoordinator
                    .getInstance();
    
            htac.runTransaction(this);
        }

        return result;
    }
    
    @Override
    public void transactionAborted(TransactionAbortedEvent evt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void transactionCommitted(TransactionEvent evt) {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionStarted(TransactionEvent evt) {
        
        Session session = evt.getSession();
        file = (File)session.load(File.class, fileid);
        
        // FIXME
        if (permission instanceof FileReadPermission){
        result = file.isMayPublicRead();
        }
        
        if (!result){
            String qtext = "from UserHasFileAccess as obj where obj.user.id =\""+userid+"\"";
    
            Query q = session.createQuery(qtext);
            List<UserHasFileAccess> qResult = q.list();
    
            for (UserHasFileAccess access:qResult){
              
                if ((access.getFile().getId()==fileid)){
                  
                  if (permission instanceof FileReadPermission){
                      result = access.isMayRead();
                  }
                  else if (permission instanceof FileWritePermission){
                      result = access.isMayReplace();
                  }
                  else if (permission instanceof FileDeletePermission){
                      result = access.isMayDelete();
                  }
                  
                  result = true;
                  break;
                }
            }
        }
    }
        
        
}
