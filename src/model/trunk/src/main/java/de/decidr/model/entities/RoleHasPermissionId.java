package de.decidr.model.entities;

// Generated 07.12.2009 17:47:57 by Hibernate Tools 3.2.4.GA

/**
 * RoleHasPermissionId generated by hbm2java
 */
public class RoleHasPermissionId implements java.io.Serializable {

    private long roleId;
    private long permissionId;

    public RoleHasPermissionId() {
        //default empty JavaBean constructor
    }

    public RoleHasPermissionId(long roleId, long permissionId) {
        //generated full constructor
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    public long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getPermissionId() {
        return this.permissionId;
    }

    public void setPermissionId(long permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof RoleHasPermissionId))
            return false;
        RoleHasPermissionId castOther = (RoleHasPermissionId) other;

        return (this.getRoleId() == castOther.getRoleId())
                && (this.getPermissionId() == castOther.getPermissionId());
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + (int) this.getRoleId();
        result = 37 * result + (int) this.getPermissionId();
        return result;
    }

}
