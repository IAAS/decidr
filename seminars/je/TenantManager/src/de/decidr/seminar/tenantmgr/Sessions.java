package de.decidr.seminar.tenantmgr;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Class for storing active login sessions. Each session id is mapped to a
 * tenant.
 * @author Johannes Engelhardt
 *
 */
public class Sessions {
	
	/** the sessions */
	private Map<String, Tenant> sessions = new TreeMap<String, Tenant>();
	
	/**
	 * Creates a new session with a new unique id
	 * @param tenant	The tenant mapped by the new session
	 * @return			The session id
	 */
	public String createSession(Tenant tenant) {
		String sid = UUID.randomUUID().toString();
		sessions.put(sid, tenant);
		return sid;
	}
	
	/**
	 * Deletes the entry with the given session id
	 * @param sid		The session id
	 */
	public void deleteSession(String sid) {
		if (sessions.containsKey(sid)) {
			sessions.remove(sid);
		}
	}
	
	/**
	 * Returns the tenant mapped by the session id
	 * @param sid		The session id
	 * @return			The tenant
	 * @throws TenantManagerException	if the sid is not found
	 */
	public Tenant getTenant(String sid) throws TenantManagerException {
		if (!sessions.containsKey(sid)) {
			throw new TenantManagerException("SID not found.");
		} else {
			return sessions.get(sid);
		}
	}
	
}
