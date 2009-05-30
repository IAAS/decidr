package de.decidr.model.commands.system;

import java.util.List;
import org.hibernate.Query;
import com.vaadin.data.Item;
import de.decidr.model.entities.SystemSettings;
import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class SetSystemSettingsCommand extends SystemCommand {

    Item item = null;
       
    public SetSystemSettingsCommand(Role role, Item item) {
        super(role, null);
        this.item=item;
        // TODO Auto-generated constructor stub
    }

    @SuppressWarnings("unchecked")
    @Override
    public void transactionAllowed(TransactionEvent evt) {
        
        String qtext = "from SystemSettings";
        
        Query q = evt.getSession().createQuery(qtext);
        List<SystemSettings> results = q.list();
        
        SystemSettings settings = results.get(0);
        
        settings.setLogLevel(item.getItemProperty("loglevel").getValue().toString());
        settings.setAutoAcceptNewTenants(Boolean.parseBoolean(item.getItemProperty("autoAcceptNewTenants").getValue().toString()));
        
        evt.getSession().saveOrUpdate(settings);
    }

}
