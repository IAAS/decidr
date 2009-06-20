package de.decidr.model.entities;

// Generated 20.06.2009 12:22:41 by Hibernate Tools 3.2.4.GA

/**
 * UserIsMemberOfTenantId generated by hbm2java
 */
public class UserIsMemberOfTenantId implements java.io.Serializable {

    private long userId;
    private long tenantId;

    public UserIsMemberOfTenantId() {
    }

    public UserIsMemberOfTenantId(long userId, long tenantId) {
        this.userId = userId;
        this.tenantId = tenantId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof UserIsMemberOfTenantId))
            return false;
        UserIsMemberOfTenantId castOther = (UserIsMemberOfTenantId) other;

        return (this.getUserId() == castOther.getUserId())
                && (this.getTenantId() == castOther.getTenantId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (int) this.getUserId();
        result = 37 * result + (int) this.getTenantId();
        return result;
    }

}
