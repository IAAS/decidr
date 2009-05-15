package de.decidr.model.permissions;

import de.decidr.model.entities.Tenant;

public class TenantPermission extends Permission {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Long tenantId;

	public TenantPermission(Long tenantId) {
		super(Tenant.class.getCanonicalName()+ "." + tenantId.toString());
		this.tenantId = tenantId;
	}

	public Long getTenantId() {
		return this.tenantId;
	}
}