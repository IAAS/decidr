package de.decidr.model.permissions;

import de.decidr.model.entities.Tenant;

/**
 * Represents the permission to a tenant.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class TenantPermission extends EntityPermission {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param tenantId
     */
    public TenantPermission(Long tenantId) {
        super(Tenant.class.getCanonicalName(), tenantId);
    }

}