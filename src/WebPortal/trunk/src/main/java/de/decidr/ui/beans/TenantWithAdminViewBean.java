/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.decidr.ui.beans;

import java.io.Serializable;
import java.util.Date;

import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.TenantWithAdminView;

/**
 * TODO: add comment
 * 
 * @author AT
 */
@Reviewed(currentReviewState = State.PassedWithComments, lastRevision = "2498", reviewers = { "RR" })
public class TenantWithAdminViewBean implements Serializable {

    private long id;
    private String name;
    private String description;
    private Long logoId;
    private Long simpleColorSchemeId;
    private Long advancedColorSchemeId;
    private Long currentColorSchemeId;
    private Date approvedSince;
    private long adminId;
    private String adminUsername;
    private String adminFirstName;
    private String adminLastName;

    /**
     * TODO: add comment
     * 
     */
    public TenantWithAdminViewBean(TenantWithAdminView tenantWithAdminView) {
        this.name = tenantWithAdminView.getName();
        this.adminFirstName = tenantWithAdminView.getAdminFirstName();
        this.adminLastName = tenantWithAdminView.getAdminLastName();
        this.adminId = tenantWithAdminView.getAdminId();
        this.id = tenantWithAdminView.getId();
        this.logoId = tenantWithAdminView.getLogoId();
        this.simpleColorSchemeId = tenantWithAdminView.getSimpleColorSchemeId();
        this.advancedColorSchemeId = tenantWithAdminView
                .getAdvancedColorSchemeId();
        this.currentColorSchemeId = tenantWithAdminView
                .getCurrentColorSchemeId();
        this.approvedSince = tenantWithAdminView.getApprovedSince();
        this.adminUsername = tenantWithAdminView.getAdminUsername();
        this.description = tenantWithAdminView.getDescription();
    }

    /**
     * TODO: add comment
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * TODO: add comment
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * TODO: add comment
     * 
     * @return the adminFirstName
     */
    public String getAdminFirstName() {
        return adminFirstName;
    }

    /**
     * TODO: add comment
     * 
     * @param adminFirstName
     *            the adminFirstName to set
     */
    public void setAdminFirstName(String adminFirstName) {
        this.adminFirstName = adminFirstName;
    }

    /**
     * TODO: add comment
     * 
     * @return the adminLastName
     */
    public String getAdminLastName() {
        return adminLastName;
    }

    /**
     * TODO: add comment
     * 
     * @param adminLastName
     *            the adminLastName to set
     */
    public void setAdminLastName(String adminLastName) {
        this.adminLastName = adminLastName;
    }

    /**
     * TODO: add comment
     * 
     * @return the adminId
     */
    public Long getAdminId() {
        return adminId;
    }

    /**
     * TODO: add comment
     * 
     * @param adminId
     *            the adminId to set
     */
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    /**
     * TODO: add comment
     * 
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * TODO: add comment
     * 
     * @param id
     *            the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * TODO: add comment
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * TODO: add comment
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * TODO: add comment
     * 
     * @return the logoId
     */
    public Long getLogoId() {
        return logoId;
    }

    /**
     * TODO: add comment
     * 
     * @param logoId
     *            the logoId to set
     */
    public void setLogoId(Long logoId) {
        this.logoId = logoId;
    }

    /**
     * TODO: add comment
     * 
     * @return the simpleColorSchemeId
     */
    public Long getSimpleColorSchemeId() {
        return simpleColorSchemeId;
    }

    /**
     * TODO: add comment
     * 
     * @param simpleColorSchemeId
     *            the simpleColorSchemeId to set
     */
    public void setSimpleColorSchemeId(Long simpleColorSchemeId) {
        this.simpleColorSchemeId = simpleColorSchemeId;
    }

    /**
     * TODO: add comment
     * 
     * @return the advancedColorSchemeId
     */
    public Long getAdvancedColorSchemeId() {
        return advancedColorSchemeId;
    }

    /**
     * TODO: add comment
     * 
     * @param advancedColorSchemeId
     *            the advancedColorSchemeId to set
     */
    public void setAdvancedColorSchemeId(Long advancedColorSchemeId) {
        this.advancedColorSchemeId = advancedColorSchemeId;
    }

    /**
     * TODO: add comment
     * 
     * @return the currentColorSchemeId
     */
    public Long getCurrentColorSchemeId() {
        return currentColorSchemeId;
    }

    /**
     * TODO: add comment
     * 
     * @param currentColorSchemeId
     *            the currentColorSchemeId to set
     */
    public void setCurrentColorSchemeId(Long currentColorSchemeId) {
        this.currentColorSchemeId = currentColorSchemeId;
    }

    /**
     * TODO: add comment
     * 
     * @return the approvedSince
     */
    public Date getApprovedSince() {
        return approvedSince;
    }

    /**
     * TODO: add comment
     * 
     * @param approvedSince
     *            the approvedSince to set
     */
    public void setApprovedSince(Date approvedSince) {
        this.approvedSince = approvedSince;
    }

    /**
     * TODO: add comment
     * 
     * @return the adminUsername
     */
    public String getAdminUsername() {
        return adminUsername;
    }

    /**
     * TODO: add comment
     * 
     * @param adminUsername
     *            the adminUsername to set
     */
    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    /**
     * TODO: add comment
     * 
     * @param adminId
     *            the adminId to set
     */
    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }
}
