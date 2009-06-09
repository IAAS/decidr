package de.decidr.model.entities;

// Generated 06.06.2009 16:49:44 by Hibernate Tools 3.2.4.GA

import java.util.Date;

/**
 * ApprovedTenantsView generated by hbm2java
 */
public class ApprovedTenantsView implements java.io.Serializable {

    private long id;
    private String name;
    private String description;
    private Long logoId;
    private Long simpleColorSchemeId;
    private Long advancedColorSchemeId;
    private Long currentColorSchemeId;
    private Date approvedSince;
    private long adminId;

    public ApprovedTenantsView() {
    }

    public ApprovedTenantsView(long id, String name, String description,
            long adminId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.adminId = adminId;
    }

    public ApprovedTenantsView(long id, String name, String description,
            Long logoId, Long simpleColorSchemeId, Long advancedColorSchemeId,
            Long currentColorSchemeId, Date approvedSince, long adminId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.logoId = logoId;
        this.simpleColorSchemeId = simpleColorSchemeId;
        this.advancedColorSchemeId = advancedColorSchemeId;
        this.currentColorSchemeId = currentColorSchemeId;
        this.approvedSince = approvedSince;
        this.adminId = adminId;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLogoId() {
        return this.logoId;
    }

    public void setLogoId(Long logoId) {
        this.logoId = logoId;
    }

    public Long getSimpleColorSchemeId() {
        return this.simpleColorSchemeId;
    }

    public void setSimpleColorSchemeId(Long simpleColorSchemeId) {
        this.simpleColorSchemeId = simpleColorSchemeId;
    }

    public Long getAdvancedColorSchemeId() {
        return this.advancedColorSchemeId;
    }

    public void setAdvancedColorSchemeId(Long advancedColorSchemeId) {
        this.advancedColorSchemeId = advancedColorSchemeId;
    }

    public Long getCurrentColorSchemeId() {
        return this.currentColorSchemeId;
    }

    public void setCurrentColorSchemeId(Long currentColorSchemeId) {
        this.currentColorSchemeId = currentColorSchemeId;
    }

    public Date getApprovedSince() {
        return this.approvedSince;
    }

    public void setApprovedSince(Date approvedSince) {
        this.approvedSince = approvedSince;
    }

    public long getAdminId() {
        return this.adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    // The following is extra code specified in the hbm.xml files
    private static final long serialVersionUID = 1L;
    // end of extra code specified in the hbm.xml files

}