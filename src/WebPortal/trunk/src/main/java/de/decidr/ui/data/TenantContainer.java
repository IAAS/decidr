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

package de.decidr.ui.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.TenantSummaryView;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.filters.EqualsFilter;
import de.decidr.model.filters.Filter;

import de.decidr.ui.beans.TenantSummaryViewBean;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This container holds tenants. The tenants are represented by a list of
 * {@link Item items}.<br>
 * Aleks: update comment: not an Item
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2499", currentReviewState = State.PassedWithComments)
public class TenantContainer extends BeanItemContainer<TenantSummaryViewBean> {

    private static final long serialVersionUID = 1L;

    private HttpSession session = Main.getCurrent().getSession();

    private Role role = (Role) session.getAttribute("role");

    private TenantFacade tenantFacade = new TenantFacade(role);

    List<TenantSummaryView> tenantList = null;

    private List<Filter> filterList = null;

    public static final Object[] NAT_COL_ORDER = new Object[] {
            "adminFirstName", "adminLastName", "tenantName",
            "numDeployedWorkflowModels", "numMembers", "numWorkflowInstances" };

    public static final String[] COL_HEADERS = new String[] { "First name",
            "Surname", "Tenant name", "#Deployed Models", "#Members",
            "#Instances" };

    /**
     * The tenant {@link Item items} are added to the container.<br>
     * Aleks: update comment: not an Item
     */
    public TenantContainer() {
        super(TenantSummaryViewBean.class);
        try {
            filterList = new ArrayList<Filter>();
            filterList.add(new EqualsFilter(true, "approvedSince", new Date()));
            tenantList = tenantFacade.getAllTenants(null, null);
            TenantSummaryViewBean tenantSummaryViewBean;
            for (TenantSummaryView tenantSummaryView : tenantList) {
                tenantSummaryViewBean = new TenantSummaryViewBean(
                        tenantSummaryView);
                addBean(tenantSummaryViewBean);
            }
        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(exception));
        }
    }
}
