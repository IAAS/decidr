package de.decidr.model.entities;

// Generated 09.10.2009 11:43:53 by Hibernate Tools 3.2.4.GA

import java.util.HashSet;
import java.util.Set;

/**
 * File generated by hbm2java
 */
public class File implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private String fileName;
    private String mimeType;
    private boolean mayPublicRead;
    private boolean mayPublicReplace;
    private boolean mayPublicDelete;
    private long fileSizeBytes;
    private byte[] data;
    private Set<Tenant> tenantsForAdvancedColorSchemeId = new HashSet<Tenant>(0);
    private Set<Tenant> tenantsForSimpleColorSchemeId = new HashSet<Tenant>(0);
    private Set<Tenant> tenantsForLogoId = new HashSet<Tenant>(0);
    private Set<Tenant> tenantsForCurrentColorSchemeId = new HashSet<Tenant>(0);
    private Set<UserHasFileAccess> userHasFileAccess = new HashSet<UserHasFileAccess>(
            0);

    public File() {
    }

    public File(String fileName, String mimeType, boolean mayPublicRead,
            boolean mayPublicReplace, boolean mayPublicDelete,
            long fileSizeBytes) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.mayPublicRead = mayPublicRead;
        this.mayPublicReplace = mayPublicReplace;
        this.mayPublicDelete = mayPublicDelete;
        this.fileSizeBytes = fileSizeBytes;
    }

    public File(String fileName, String mimeType, boolean mayPublicRead,
            boolean mayPublicReplace, boolean mayPublicDelete,
            long fileSizeBytes, byte[] data,
            Set<Tenant> tenantsForAdvancedColorSchemeId,
            Set<Tenant> tenantsForSimpleColorSchemeId,
            Set<Tenant> tenantsForLogoId,
            Set<Tenant> tenantsForCurrentColorSchemeId,
            Set<UserHasFileAccess> userHasFileAccess) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.mayPublicRead = mayPublicRead;
        this.mayPublicReplace = mayPublicReplace;
        this.mayPublicDelete = mayPublicDelete;
        this.fileSizeBytes = fileSizeBytes;
        this.data = data;
        this.tenantsForAdvancedColorSchemeId = tenantsForAdvancedColorSchemeId;
        this.tenantsForSimpleColorSchemeId = tenantsForSimpleColorSchemeId;
        this.tenantsForLogoId = tenantsForLogoId;
        this.tenantsForCurrentColorSchemeId = tenantsForCurrentColorSchemeId;
        this.userHasFileAccess = userHasFileAccess;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public boolean isMayPublicRead() {
        return this.mayPublicRead;
    }

    public void setMayPublicRead(boolean mayPublicRead) {
        this.mayPublicRead = mayPublicRead;
    }

    public boolean isMayPublicReplace() {
        return this.mayPublicReplace;
    }

    public void setMayPublicReplace(boolean mayPublicReplace) {
        this.mayPublicReplace = mayPublicReplace;
    }

    public boolean isMayPublicDelete() {
        return this.mayPublicDelete;
    }

    public void setMayPublicDelete(boolean mayPublicDelete) {
        this.mayPublicDelete = mayPublicDelete;
    }

    public long getFileSizeBytes() {
        return this.fileSizeBytes;
    }

    public void setFileSizeBytes(long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Set<Tenant> getTenantsForAdvancedColorSchemeId() {
        return this.tenantsForAdvancedColorSchemeId;
    }

    public void setTenantsForAdvancedColorSchemeId(
            Set<Tenant> tenantsForAdvancedColorSchemeId) {
        this.tenantsForAdvancedColorSchemeId = tenantsForAdvancedColorSchemeId;
    }

    public Set<Tenant> getTenantsForSimpleColorSchemeId() {
        return this.tenantsForSimpleColorSchemeId;
    }

    public void setTenantsForSimpleColorSchemeId(
            Set<Tenant> tenantsForSimpleColorSchemeId) {
        this.tenantsForSimpleColorSchemeId = tenantsForSimpleColorSchemeId;
    }

    public Set<Tenant> getTenantsForLogoId() {
        return this.tenantsForLogoId;
    }

    public void setTenantsForLogoId(Set<Tenant> tenantsForLogoId) {
        this.tenantsForLogoId = tenantsForLogoId;
    }

    public Set<Tenant> getTenantsForCurrentColorSchemeId() {
        return this.tenantsForCurrentColorSchemeId;
    }

    public void setTenantsForCurrentColorSchemeId(
            Set<Tenant> tenantsForCurrentColorSchemeId) {
        this.tenantsForCurrentColorSchemeId = tenantsForCurrentColorSchemeId;
    }

    public Set<UserHasFileAccess> getUserHasFileAccess() {
        return this.userHasFileAccess;
    }

    public void setUserHasFileAccess(Set<UserHasFileAccess> userHasFileAccess) {
        this.userHasFileAccess = userHasFileAccess;
    }

}
