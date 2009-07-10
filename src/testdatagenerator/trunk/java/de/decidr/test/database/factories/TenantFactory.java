package de.decidr.test.database.factories;

import java.util.List;

import org.hibernate.Session;

import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;

/**
 * This factory creates tenants for testing purposes. It is assumed that some
 * users are already available in the according database.
 * 
 * 
 */
public class TenantFactory {

    private Session session;

    public TenantFactory(Session session) {
        this.session = session;
    }

    public  List<Tenant> createRandomTenants(int numTenants) {
        for (int i = 0; i < numTenants; i++) {
            Tenant tenant = new Tenant();
            
            tenant.setAdmin(getRandomAdmin());
            //TODO continue here
        
        }
        return null;
    }

    public User getRandomAdmin() {
        String hql = "select u.id from User u"
                + "where (u.disabledSince is null) and "
                + "(u.unavailableSince is null) and "
                + "(u.userProfile is not null) order by rand() limit 1";

        User user = (User) session.createQuery(hql).uniqueResult();

        return user;
    }
}
