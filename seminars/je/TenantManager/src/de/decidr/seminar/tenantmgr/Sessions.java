package de.decidr.seminar.tenantmgr;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class Sessions {
	
	private Map<String, Tenant> sessions = new TreeMap<String, Tenant>();
	
	public String createSession(Tenant tenant) {
		String sid = UUID.randomUUID().toString();
		sessions.put(sid, tenant);
		return sid;
	}
	
	public void deleteSession(String sid) {
		if (sessions.containsKey(sid)) {
			sessions.remove(sid);
		}
	}
	
	public Tenant getTenant(String sid) throws TenantManagerException {
		if (!sessions.containsKey(sid)) {
			throw new TenantManagerException("SID not found.");
		} else {
			return sessions.get(sid);
		}
	}
	
}
