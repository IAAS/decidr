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

import java.util.List;

import javax.servlet.http.HttpSession;

import com.vaadin.data.util.BeanItemContainer;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.TenantWithAdminView;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.beans.TenantWithAdminViewBean;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This container holds the tenants to approve. The tenants are represented as a
 * list of {@link TenantWithAdminView TenantWithAdminViews}.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2499", currentReviewState = State.Passed)
public class ApproveTenantContainer extends
        BeanItemContainer<TenantWithAdminViewBean> {

    private static final long serialVersionUID = 1L;

    private HttpSession session = Main.getCurrent().getSession();

    private Role role = (Role) session.getAttribute("role");

    private TenantFacade tenantFacade = new TenantFacade(role);

    private List<TenantWithAdminView> approveTenantsList = null;

    public static final Object[] NAT_COL_ORDER = new Object[] { "name",
            "adminFirstName", "adminLastName", "adminId" };

    public static final String[] COL_HEADERS = new String[] { "Name",
            "First name", "Surname", "Admin ID" };

    public ApproveTenantContainer() {
        super(TenantWithAdminViewBean.class);
        try {
            approveTenantsList = tenantFacade.getTenantsToApprove(null, null);
            TenantWithAdminViewBean tenantWithAdminViewBean;
            for (TenantWithAdminView tenantWithAdminView : approveTenantsList) {
                tenantWithAdminViewBean = new TenantWithAdminViewBean(
                        tenantWithAdminView);
                addBean(tenantWithAdminViewBean);
            }
        } catch (TransactionException e) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(e));
        }
    }
}
