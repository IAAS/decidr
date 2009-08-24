package de.decidr.test.database.factories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.File;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.test.database.main.ProgressListener;

/**
 * Creates tenants for testing purposes. It is assumed that some users are
 * already available in the target database (see UserFactory).
 * 
 * @author Thomas Karsten
 * @author Daniel Huss
 * @version 0.1
 */
public class TenantFactory extends EntityFactory {

    private static char[] validTenantNameCharacters = { '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z' };

    private static int MAX_TENANT_NAME_LENTH = 50;

    private static int MIN_TENANT_NAME_LENGTH = 2;

    /**
     * Constructor
     * 
     * @param session
     */
    public TenantFactory(Session session) {
        super(session);
    }

    /**
     * @param session
     * @param progressListener
     */
    public TenantFactory(Session session, ProgressListener progressListener) {
        super(session, progressListener);
    }

    /**
     * Creates numTenants random persisted tenants.
     * 
     * @param numTenants
     * @return
     */
    public List<Tenant> createRandomTenants(int numTenants) {

        ArrayList<Tenant> result = new ArrayList<Tenant>(numTenants);

        Date now = DecidrGlobals.getTime().getTime();

        for (int i = 0; i < numTenants; i++) {
            Tenant tenant = new Tenant();

            tenant.setAdmin(getRandomAdmin());
            String tenantName = getRandomTenantName(i);
            tenant.setName(tenantName);
            tenant.setDescription(tenantName);

            // every 5th tenant hasn't been approved yet
            tenant.setApprovedSince(i % 5 == 0 ? null : now);

            // 50 % use the simple color scheme
            File advancedColorScheme = createDefaultColorScheme();
            File simpleColorScheme = createDefaultColorScheme();

            tenant.setAdvancedColorScheme(advancedColorScheme);
            tenant.setSimpleColorScheme(simpleColorScheme);
            tenant
                    .setCurrentColorScheme(rnd.nextBoolean() ? advancedColorScheme
                            : simpleColorScheme);

            tenant.setLogo(createDefaultLogo());
            session.save(tenant);
            result.add(tenant);
            
            fireProgressEvent(numTenants, i+1);
        }

        return result;
    }

    /**
     * Creates a new persisted file reference (to a nonexistent file) that can
     * be used as a tenant color scheme
     * 
     * @return
     */
    public File createDefaultColorScheme() {
        File result = new File();
        result.setFileName("uploaded.css");
        result.setMayPublicRead(true);
        result.setMimeType("text/css");
        session.save(result);
        return result;
    }

    /**
     * Creates a new persisted file reference (to a nonexistent file) that can
     * be used as a tenant logo
     * 
     * @return
     */
    public File createDefaultLogo() {
        File result = new File();
        result.setFileName("logo.jpeg");
        result.setMayPublicRead(true);
        result.setMimeType("image/jpeg");
        session.save(result);
        return result;
    }

    /**
     * Retrieves a random user from the database who can be promoted to tenant
     * admin.
     * 
     * @return
     * @throws RuntimeException
     *             if no user can be found.
     */
    public User getRandomAdmin() {
        // please note rand() is specific to MySQL!
        String hql = "from User u " + "where (u.disabledSince is null) and "
                + "(u.unavailableSince is null) and "
                + "(u.userProfile is not null) order by rand()";

        User user = (User) session.createQuery(hql).setMaxResults(1)
                .uniqueResult();

        if (user == null) {
            throw new RuntimeException(
                    "Cannot find a user who can be promoted to tenant admin.");
        }

        return user;
    }

    /**
     * Creates a random tenant name that meets the DecidR tenant name criteria.
     * 
     * @param id
     *            tenant "id" appended to the end of the tenant name
     * @return
     */
    private String getRandomTenantName(int id) {
        StringBuffer randomName = new StringBuffer(MAX_TENANT_NAME_LENTH);
        String idString = Integer.toString(id);

        int nameLength = MIN_TENANT_NAME_LENGTH
                + rnd.nextInt(MAX_TENANT_NAME_LENTH - MIN_TENANT_NAME_LENGTH
                        - idString.length());

        for (int i = 0; i < nameLength; i++) {
            char c = validTenantNameCharacters[rnd
                    .nextInt(validTenantNameCharacters.length)];

            randomName.append(rnd.nextBoolean() ? Character.toUpperCase(c) : c);
        }

        randomName.append(idString);

        return randomName.toString();
    }
}
