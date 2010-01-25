/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.decidr.test.database.factories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.File;
import de.decidr.model.entities.Tenant;
import de.decidr.model.entities.User;
import de.decidr.model.entities.UserIsMemberOfTenant;
import de.decidr.model.entities.UserIsMemberOfTenantId;
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
     * Makes up to numMembers users that are not already an admin of the given
     * tenant and makes them members of the given tenant.
     * 
     * @param tenant
     * @param numMembers
     */
    @SuppressWarnings("unchecked")
    private void associateUsersWithTenant(Tenant tenant, int numMembers) {
        String hql = "from User u where not exists(from Tenant t where t.admin = u and "
                + "t.id = :tenantId) order by rand()";
        List<User> members = session.createQuery(hql).setMaxResults(numMembers)
                .setLong("tenantId", tenant.getId()).list();

        for (User member : members) {
            UserIsMemberOfTenant rel = new UserIsMemberOfTenant();
            rel.setTenant(tenant);
            rel.setUser(member);
            rel
                    .setId(new UserIsMemberOfTenantId(member.getId(), tenant
                            .getId()));
            session.save(rel);
        }
    }

    /**
     * Creates a new persisted file reference that can be used as a tenant color
     * scheme.
     * 
     * @return persisted file entity
     */
    public File createDefaultColorScheme() {
        File result = new File();
        result.setFileName("styles1.css");
        result.setMayPublicRead(true);
        result.setMayPublicDelete(false);
        result.setMayPublicReplace(false);
        result.setTemporary(false);
        result.setCreationDate(DecidrGlobals.getTime().getTime());
        result.setMimeType("text/css");
        result.setData(getFileBytes("files/styles1.css"));
        result.setFileSizeBytes(result.getData().length);
        session.save(result);
        return result;
    }

    /**
     * Creates a new persisted file reference that can be used as a tenant logo.
     * 
     * @return persisted file entity
     */
    public File createDefaultLogo() {
        File result = new File();
        result.setFileName("logo.png");
        result.setMimeType("image/png");
        result.setMayPublicRead(true);
        result.setMayPublicDelete(false);
        result.setMayPublicReplace(false);
        result.setTemporary(false);
        result.setCreationDate(DecidrGlobals.getTime().getTime());
        result.setData(getFileBytes("files/logo"
                + Integer.toString(rnd.nextInt(2) + 1) + ".png"));
        result.setFileSizeBytes(result.getData().length);
        session.save(result);
        return result;
    }

    /**
     * Creates the default tenant.
     */
    public void createDefaultTenant() {
        Tenant result = new Tenant();
        result.setAdmin(DecidrGlobals.getSettings().getSuperAdmin());
        result.setName("DefaultTenant");
        result.setDescription("This is the default tenant description");
        result.setAdvancedColorScheme(createDefaultColorScheme());
        result.setSimpleColorScheme(createDefaultColorScheme());
        result.setCurrentColorScheme(result.getAdvancedColorScheme());
        result.setApprovedSince(DecidrGlobals.getTime().getTime());
        result.setLogo(createDefaultLogo());

        session.save(result);

        // workaround for Hibernate ignoring setId()
        session.createQuery(
                "update Tenant set id = :defaultId where id = :currentId")
                .setLong("defaultId", DecidrGlobals.DEFAULT_TENANT_ID).setLong(
                        "currentId", result.getId()).executeUpdate();
    }

    /**
     * Creates numTenants random persisted tenants.
     * 
     * @param numTenants
     * @return persisted tenant entities
     */
    public List<Tenant> createRandomTenants(int numTenants,
            int maxMembersPerTenant) {
        maxMembersPerTenant = Math.abs(maxMembersPerTenant);

        ArrayList<Tenant> result = new ArrayList<Tenant>(numTenants);

        Date now = DecidrGlobals.getTime().getTime();

        createDefaultTenant();

        for (int i = 0; i < numTenants; i++) {
            Tenant tenant = new Tenant();

            User admin = getRandomAdmin();

            tenant.setAdmin(admin);
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

            admin.setCurrentTenant(tenant);
            session.update(admin);

            result.add(tenant);

            int numTenantMembers = maxMembersPerTenant == 0 ? 0 : rnd
                    .nextInt(maxMembersPerTenant) + 1;
            associateUsersWithTenant(tenant, numTenantMembers);

            fireProgressEvent(numTenants, i + 1);
        }

        return result;
    }

    /**
     * Retrieves a random user from the database who can be promoted to tenant
     * admin.
     * 
     * @return suitable user entity
     * @throws RuntimeException
     *             if no user can be found.
     */
    public User getRandomAdmin() {
        // please note rand() is specific to MySQL!
        String hql = "from User u where (u.disabledSince is null) and "
                + "(u.unavailableSince is null) and "
                + "exists(from UserProfile p where p.id = u.id) order by rand()";

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
     *            tenant "ID" appended to the end of the tenant name
     * @return random tenant name
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