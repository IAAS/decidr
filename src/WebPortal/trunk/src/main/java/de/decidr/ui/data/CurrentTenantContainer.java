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

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.beans.TenantBean;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This container holds all tenants which belong to the current user. The
 * tenants will be represented as {@link Item items} in a table.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2353", currentReviewState = State.NeedsReview)
public class CurrentTenantContainer extends BeanItemContainer<TenantBean> {

    private static final long serialVersionUID = 1L;

    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");

    private Role role = (Role) session.getAttribute("role");

    UserFacade userFacade = new UserFacade(role);

    List<Tenant> currentTenantList = null;
    
    public static final Object[] NAT_COL_ORDER = new Object[] { "id", "name" };

    public static final String[] COL_HEADERS = new String[] { "ID", "Name" };



    /**
     * Adds tenant items to the container.
     */
    public CurrentTenantContainer() {
        super(TenantBean.class);
        try {
            currentTenantList = userFacade.getJoinedTenants(userId);
            TenantBean tenantBean;
            for (Tenant tenant : currentTenantList) {
                tenantBean = new TenantBean(tenant);
                addBean(tenantBean);
            }
        } catch (TransactionException exception) {
            Main.getCurrent().addWindow(
                    new TransactionErrorDialogComponent(exception));
        }
    }
}
