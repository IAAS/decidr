package de.decidr.seminar.tenantmgr;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * Implements the TenantManager interface.
 * @author Johannes Engelhardt
 *
 */
public class TenantManagerImpl implements TenantManager {
	
	/** the login sessions */
	private Sessions sessions = new Sessions();
	
	/** entity manager for the JPA */
	private EntityManager em;
	
	public TenantManagerImpl() {
		// create entity manager
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("TenantManager");
		em = factory.createEntityManager();
	}
	
	protected void finalize() {
		// close entity manager
		em.close();
	}

	@Override
	public void addTenant(String name) throws Exception {
		// check if tenant name already exists
		if (getTenantFromDB(name) == null) {
			// add new tenant to database
			em.getTransaction().begin();
			em.persist(new Tenant(name));
			em.getTransaction().commit();
			
		} else {
			throw new TenantManagerException("Tentant name already exists.");
		}
	}
	
	@Override
	public String login(String name) throws Exception {
		// get tenant from database
		Tenant tenant = getTenantFromDB(name);
		if (tenant != null) {
			return sessions.createSession(tenant);
			
		} else {
			throw new TenantManagerException("Tenant not found.");
		}
	}

	@Override
	public void logout(String sid) throws Exception {
		sessions.deleteSession(sid);
	}
	
	@Override
	public void addCustomField(String sid, String fieldName, String value)
			throws Exception {
		String name = sessions.getTenant(sid).getName();
		
		// get tenant from database
		Tenant tenant = getTenantFromDB(name);
		
		// check if tenant exists
		if (tenant != null) {
			
			// add new custom field
			tenant.addCustomField(fieldName, value);
			
			// persist tenant
			em.getTransaction().begin();
			em.persist(tenant);
			em.getTransaction().commit();
			
		} else {
			throw new TenantManagerException("Tenant not found.");
		}
	}
	
	@Override
	public void setValue(String sid, String fieldName, String value)
			throws Exception {
		String name = sessions.getTenant(sid).getName();

		// get tenant from database
		Tenant tenant = getTenantFromDB(name);
		
		// check if tenant exists
		if (tenant != null) {
			
			// set custom field value
			tenant.setValue(fieldName, value);
			
			// persist tenant
			em.getTransaction().begin();
			em.persist(tenant);
			em.getTransaction().commit();
			
		} else {
			throw new TenantManagerException("Tenant not found.");
		}
	}

	@Override
	public String getValue(String sid, String fieldName) throws Exception {
		String name = sessions.getTenant(sid).getName();

		// get tenant from database
		Tenant tenant = getTenantFromDB(name);
		
		// check if tenant exists
		if (tenant != null) {		
			// get custom field value
			return tenant.getValue(fieldName);
			
		} else {
			throw new TenantManagerException("Tenant not found.");
		}
	}

	@Override
	public Map<String, String> getValues(String sid) throws Exception {
		String name = sessions.getTenant(sid).getName();

		// get tenant from database
		Tenant tenant = getTenantFromDB(name);
		
		// check if tenant exists
		if (tenant != null) {		
			// get custom field value
			return tenant.getValues();
			
		} else {
			throw new TenantManagerException("Tenant not found.");
		}
	}
	
	/**
	 * Gets the tenant with the given unique name from the database
	 * @param name	the name of the tenant
	 * @return	the tenant. Null, if the tenant is not found.
	 */
	private Tenant getTenantFromDB(String name) {
		Query q = em.createQuery("SELECT t FROM tenants t WHERE t.name = '"
				+ name + "'");
		if (q.getResultList().size() > 0) {
			return (Tenant)q.getSingleResult();
		} else {
			return null;
		}
	}

}
