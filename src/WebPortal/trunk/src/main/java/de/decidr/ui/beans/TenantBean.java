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
import de.decidr.model.entities.File;
import de.decidr.model.entities.Tenant;

/**
 * This bean represents the tenant from the model in the GUI. The properties are
 * reachable through getters and setters. The bean has the following properties:
 * <ul>
 * <li>id : Long</li>
 * <li>logo : File</li>
 * <li>simpleColorScheme : File</li>
 * <li>advancedColorScheme : File</li>
 * <li>currentColorScheme : File</li>
 * <li>name : String</li>
 * <li>description : String</li>
 * <li>approvedSince : Date</li>
 * </ul>
 * 
 * @author AT
 */
@Reviewed(currentReviewState = State.Passed, lastRevision = "2498", reviewers = { "RR" })
public class TenantBean implements Serializable {

    /**
     * Serial version uid
     */
    private static final long serialVersionUID = -779554821373778177L;
    private Long id;
    private File logo;
    private File simpleColorScheme;
    private File advancedColorScheme;
    private File currentColorScheme;
    private String name;
    private String description;
    private Date approvedSince;

    /**
     * Constructor which initializes the properties from the given tenant.
     * 
     * @param tenant
     *            - The tenant from the model.
     */
    public TenantBean(Tenant tenant) {
        this.id = tenant.getId();
        this.logo = tenant.getLogo();
        this.simpleColorScheme = tenant.getSimpleColorScheme();
        this.advancedColorScheme = tenant.getAdvancedColorScheme();
        this.currentColorScheme = tenant.getCurrentColorScheme();
        this.name = tenant.getName();
        this.description = tenant.getDescription();
        this.approvedSince = tenant.getApprovedSince();
    }

    public File getAdvancedColorScheme() {
        return advancedColorScheme;
    }

    public Date getApprovedSince() {
        return approvedSince;
    }

    public File getCurrentColorScheme() {
        return currentColorScheme;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public File getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }

    public File getSimpleColorScheme() {
        return simpleColorScheme;
    }

    public void setAdvancedColorScheme(File advancedColorScheme) {
        this.advancedColorScheme = advancedColorScheme;
    }

    public void setApprovedSince(Date approvedSince) {
        this.approvedSince = approvedSince;
    }

    public void setCurrentColorScheme(File currentColorScheme) {
        this.currentColorScheme = currentColorScheme;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogo(File logo) {
        this.logo = logo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSimpleColorScheme(File simpleColorScheme) {
        this.simpleColorScheme = simpleColorScheme;
    }
}
