package de.decidr.seminar.tenantmgr;

import java.util.Map;

public class TenantManagerImpl implements TenantManager {
	
	private Sessions sessions = new Sessions();
	private TenantList tenantlist = new TenantList();

	@Override
	public void addTenant(String name) throws Exception {
		tenantlist.addTenant(new Tenant(name));
	}
	
	@Override
	public String login(String name) throws Exception {
		Tenant tenant = tenantlist.getTenant(name);
		return sessions.createSession(tenant);
	}

	@Override
	public void logout(String sid) throws Exception {
		sessions.deleteSession(sid);

	}
	
	@Override
	public void addCustomField(String sid, String fieldName, String value)
			throws Exception {
		Tenant tenant = sessions.getTenant(sid);
		tenant.addCustomField(fieldName, value);
	}
	
	@Override
	public void setValue(String sid, String fieldName, String value)
			throws Exception {
		Tenant tenant = sessions.getTenant(sid);
		tenant.setValue(fieldName, value);
	}

	@Override
	public String getValue(String sid, String fieldName) throws Exception {
		Tenant tenant = sessions.getTenant(sid);
		return tenant.getValue(fieldName);
	}

	@Override
	public Map<String, String> getValues(String sid) throws Exception {
		Tenant tenant = sessions.getTenant(sid);
		return tenant.getValues();
	}

}
