package de.decidr.model.facades;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import de.decidr.model.commands.system.AddServerCommand;
import de.decidr.model.commands.system.GetServerStatisticsCommand;
import de.decidr.model.commands.system.GetSystemSettingsCommand;
import de.decidr.model.commands.system.LockServerCommand;
import de.decidr.model.commands.system.RemoveServerCommand;
import de.decidr.model.commands.system.SetSystemSettingsCommand;
import de.decidr.model.commands.system.UnLockServerCommand;
import de.decidr.model.commands.system.UpdateServerLoadCommand;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.HibernateTransactionCoordinator;
import de.decidr.model.transactions.TransactionCoordinator;

public class SystemFacade extends AbstractFacade {

	public SystemFacade(Role actor) {
	        super(actor);
	}

	public Item getSettings() {
		
	        TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
		GetSystemSettingsCommand command = new GetSystemSettingsCommand(actor);
		String[] properties = {"logLevel","autoAcceptNewTenants"};
		
		tac.runTransaction(command);
		
		return new BeanItem(command.getResult(),properties);
		
	}

	public void setSettings(Item settings) {
	       
	    TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
               SetSystemSettingsCommand command = new SetSystemSettingsCommand(actor, settings);
                             
               tac.runTransaction(command);
       
	}

	public void addServer(String location) {
	    
	    TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
            AddServerCommand command = new AddServerCommand(actor, location);
                          
            tac.runTransaction(command);
	}

	public void removeServer(String location) {
	    TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
            RemoveServerCommand command = new RemoveServerCommand(actor, location);
                          
            tac.runTransaction(command);
	}

	public void updateServerLoad(String location, byte load) {
	    TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
            UpdateServerLoadCommand command = new UpdateServerLoadCommand(actor, location, load);
                          
            tac.runTransaction(command);
	}

	public void lockServer(String location) {
	    TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
            LockServerCommand command = new LockServerCommand(actor, location);
                          
            tac.runTransaction(command);
	}

	public void unlockServer(String location) {
	    TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
            UnLockServerCommand command = new UnLockServerCommand(actor, location);
                          
            tac.runTransaction(command);
	}

	public List<Item> getLog(List<Filter> filters, Paginator paginator) {
	    // FIXME
	    throw new UnsupportedOperationException();
	}

	public List<Item> getServerStatistics() {
		
	    TransactionCoordinator tac = HibernateTransactionCoordinator.getInstance();
            GetServerStatisticsCommand command = new GetServerStatisticsCommand(actor);
            
            List<Item> outList = new ArrayList<Item>();
            List<ServerLoadView> inList;
            @SuppressWarnings("unused")
            String[] properties = {"id","location","load","numInstances","dynamicallyAdded"};
            
            tac.runTransaction(command);
            inList = command.getResult();
	        
                for (ServerLoadView server:inList){
	            outList.add(new BeanItem(server));
	        }
             
                return outList;
	}
}