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

package de.decidr.ui.view;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.KeywordFilter;
import de.decidr.model.filters.Paginator;
import de.decidr.ui.data.UserListContainer;

/**
 * This class represents the user list's ui component. It will be connected with
 * data from the database.
 * 
 * @author AT
 */
public class UserListTable extends Table implements Observer {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -4772118786130924736L;

    private Observable observable = null;
    private Container userListContainer = null;
    
    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");

    UserFacade userFacade = new UserFacade(new UserRole(userId));

    List<Item> userList = null;
    
    private KeywordFilter filter = new KeywordFilter();

    private List<Filter> filterList = new LinkedList<Filter>();

    private Paginator paginator = new Paginator();

    /**
     * Default constructor
     * 
     */
    public UserListTable(Observable observable, Container container) {
        this.observable = observable;
        userListContainer = container;
        observable.addObserver(this);
        init(container);
    }

    /**
     * This method initializes the components for the user list table
     * 
     */
    private void init(Container container) {
        setSizeFull();
        setContainerDataSource(container);
        addContainerProperty("Username", String.class, null);
        addContainerProperty("Read Name", String.class, null);
        addContainerProperty("E-Mail address", String.class, null);
        addContainerProperty("Edit", Button.class, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable arg0, Object arg1) {
        if (arg0 instanceof UserListContainer) {
        	filterList.add(filter);
            try {
                userList = userFacade.getAllUsers(filterList, paginator);
                for (Item item : userList) {
                    addItem(item);
                }
                this.requestRepaint();
            } catch (TransactionException exception) {
                Main.getCurrent().getMainWindow().addWindow(
                        new TransactionErrorDialogComponent(exception));
            }
        }

    }

}
