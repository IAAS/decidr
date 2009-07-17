package de.decidr.model.entities;

// Generated 17.07.2009 15:40:18 by Hibernate Tools 3.2.4.GA

/**
 * SystemSettings generated by hbm2java
 */
public class SystemSettings implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private User superAdmin;
    private boolean autoAcceptNewTenants;
    private String systemName;
    private String systemEmailAddress;
    private String logLevel;
    private int passwordResetRequestLifetimeSeconds;
    private int registrationRequestLifetimeSeconds;
    private int changeEmailRequestLifetimeSeconds;
    private int invitationLifetimeSeconds;
    private String mtaHostname;
    private int mtaPort;
    private boolean mtaUseTls;
    private String mtaUsername;
    private String mtaPassword;
    private long maxUploadFileSizeBytes;
    private int maxAttachmentsPerEmail;

    public SystemSettings() {
    }

    public SystemSettings(boolean autoAcceptNewTenants, String systemName,
            String systemEmailAddress, String logLevel,
            int passwordResetRequestLifetimeSeconds,
            int registrationRequestLifetimeSeconds,
            int changeEmailRequestLifetimeSeconds,
            int invitationLifetimeSeconds, String mtaHostname, int mtaPort,
            boolean mtaUseTls, String mtaUsername, String mtaPassword,
            long maxUploadFileSizeBytes, int maxAttachmentsPerEmail) {
        this.autoAcceptNewTenants = autoAcceptNewTenants;
        this.systemName = systemName;
        this.systemEmailAddress = systemEmailAddress;
        this.logLevel = logLevel;
        this.passwordResetRequestLifetimeSeconds = passwordResetRequestLifetimeSeconds;
        this.registrationRequestLifetimeSeconds = registrationRequestLifetimeSeconds;
        this.changeEmailRequestLifetimeSeconds = changeEmailRequestLifetimeSeconds;
        this.invitationLifetimeSeconds = invitationLifetimeSeconds;
        this.mtaHostname = mtaHostname;
        this.mtaPort = mtaPort;
        this.mtaUseTls = mtaUseTls;
        this.mtaUsername = mtaUsername;
        this.mtaPassword = mtaPassword;
        this.maxUploadFileSizeBytes = maxUploadFileSizeBytes;
        this.maxAttachmentsPerEmail = maxAttachmentsPerEmail;
    }

    public SystemSettings(User superAdmin, boolean autoAcceptNewTenants,
            String systemName, String systemEmailAddress, String logLevel,
            int passwordResetRequestLifetimeSeconds,
            int registrationRequestLifetimeSeconds,
            int changeEmailRequestLifetimeSeconds,
            int invitationLifetimeSeconds, String mtaHostname, int mtaPort,
            boolean mtaUseTls, String mtaUsername, String mtaPassword,
            long maxUploadFileSizeBytes, int maxAttachmentsPerEmail) {
        this.superAdmin = superAdmin;
        this.autoAcceptNewTenants = autoAcceptNewTenants;
        this.systemName = systemName;
        this.systemEmailAddress = systemEmailAddress;
        this.logLevel = logLevel;
        this.passwordResetRequestLifetimeSeconds = passwordResetRequestLifetimeSeconds;
        this.registrationRequestLifetimeSeconds = registrationRequestLifetimeSeconds;
        this.changeEmailRequestLifetimeSeconds = changeEmailRequestLifetimeSeconds;
        this.invitationLifetimeSeconds = invitationLifetimeSeconds;
        this.mtaHostname = mtaHostname;
        this.mtaPort = mtaPort;
        this.mtaUseTls = mtaUseTls;
        this.mtaUsername = mtaUsername;
        this.mtaPassword = mtaPassword;
        this.maxUploadFileSizeBytes = maxUploadFileSizeBytes;
        this.maxAttachmentsPerEmail = maxAttachmentsPerEmail;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSuperAdmin() {
        return this.superAdmin;
    }

    public void setSuperAdmin(User superAdmin) {
        this.superAdmin = superAdmin;
    }

    public boolean isAutoAcceptNewTenants() {
        return this.autoAcceptNewTenants;
    }

    public void setAutoAcceptNewTenants(boolean autoAcceptNewTenants) {
        this.autoAcceptNewTenants = autoAcceptNewTenants;
    }

    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemEmailAddress() {
        return this.systemEmailAddress;
    }

    public void setSystemEmailAddress(String systemEmailAddress) {
        this.systemEmailAddress = systemEmailAddress;
    }

    public String getLogLevel() {
        return this.logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public int getPasswordResetRequestLifetimeSeconds() {
        return this.passwordResetRequestLifetimeSeconds;
    }

    public void setPasswordResetRequestLifetimeSeconds(
            int passwordResetRequestLifetimeSeconds) {
        this.passwordResetRequestLifetimeSeconds = passwordResetRequestLifetimeSeconds;
    }

    public int getRegistrationRequestLifetimeSeconds() {
        return this.registrationRequestLifetimeSeconds;
    }

    public void setRegistrationRequestLifetimeSeconds(
            int registrationRequestLifetimeSeconds) {
        this.registrationRequestLifetimeSeconds = registrationRequestLifetimeSeconds;
    }

    public int getChangeEmailRequestLifetimeSeconds() {
        return this.changeEmailRequestLifetimeSeconds;
    }

    public void setChangeEmailRequestLifetimeSeconds(
            int changeEmailRequestLifetimeSeconds) {
        this.changeEmailRequestLifetimeSeconds = changeEmailRequestLifetimeSeconds;
    }

    public int getInvitationLifetimeSeconds() {
        return this.invitationLifetimeSeconds;
    }

    public void setInvitationLifetimeSeconds(int invitationLifetimeSeconds) {
        this.invitationLifetimeSeconds = invitationLifetimeSeconds;
    }

    public String getMtaHostname() {
        return this.mtaHostname;
    }

    public void setMtaHostname(String mtaHostname) {
        this.mtaHostname = mtaHostname;
    }

    public int getMtaPort() {
        return this.mtaPort;
    }

    public void setMtaPort(int mtaPort) {
        this.mtaPort = mtaPort;
    }

    public boolean isMtaUseTls() {
        return this.mtaUseTls;
    }

    public void setMtaUseTls(boolean mtaUseTls) {
        this.mtaUseTls = mtaUseTls;
    }

    public String getMtaUsername() {
        return this.mtaUsername;
    }

    public void setMtaUsername(String mtaUsername) {
        this.mtaUsername = mtaUsername;
    }

    public String getMtaPassword() {
        return this.mtaPassword;
    }

    public void setMtaPassword(String mtaPassword) {
        this.mtaPassword = mtaPassword;
    }

    public long getMaxUploadFileSizeBytes() {
        return this.maxUploadFileSizeBytes;
    }

    public void setMaxUploadFileSizeBytes(long maxUploadFileSizeBytes) {
        this.maxUploadFileSizeBytes = maxUploadFileSizeBytes;
    }

    public int getMaxAttachmentsPerEmail() {
        return this.maxAttachmentsPerEmail;
    }

    public void setMaxAttachmentsPerEmail(int maxAttachmentsPerEmail) {
        this.maxAttachmentsPerEmail = maxAttachmentsPerEmail;
    }

}
