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
import de.decidr.model.entities.User;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;

import de.decidr.ui.beans.UserBean;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * The container holds the users. The users are represented as {@link Item
 * items} in a table.<br>
 * Aleks: update comment: not an Item
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2499", currentReviewState = State.PassedWithComments)
public class UserListContainer extends BeanItemContainer<UserBean> {

    private static final long serialVersionUID = 1L;

    private HttpSession session = Main.getCurrent().getSession();

    private Role role = (Role) session.getAttribute("role");

    UserFacade userFacade = new UserFacade(role);

    List<User> userList = null;

    public static final Object[] NAT_COL_ORDER = new Object[] { "username",
            "firstName", "lastName", "email", "disabledSince",
            "unavailableSince" };

    public static final String[] COL_HEADERS = new String[] { "Username",
            "First Name", "Surname", "Email",
            // Aleks: shouldn't this be "Disabled"?
            "Deactivated", "Unavailable" };

    /**
     * The user {@link Item items} are added to the container.<br>
     * Aleks: update comment: not an Item
     */
    public UserListContainer() {
        super(UserBean.class);
        try {
            userList = userFacade.getAllUsers(null, null);
            UserBean userBean;
            for (User user : userList) {
                userBean = new UserBean(user);
                addBean(userBean);
            }
        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(exception));
        }
    }
}
