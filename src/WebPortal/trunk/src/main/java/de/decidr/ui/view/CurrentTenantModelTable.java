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
import de.decidr.ui.data.CurrentTenantContainer;

/**
 * The table holds the current tenant items. It is integrated in a component.
 * 
 * @author AT
 */
public class CurrentTenantModelTable extends Table implements Observer {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -3378507042364075268L;
    private Observable observable = null;
    private Container currentTenantContainer = null;
    
    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");

    UserFacade userFacade = new UserFacade(new UserRole(userId));

    List<Item> currentTenantList = null;

    /**
     * Default Constructor. Adds this table as an observer to the depending
     * container.
     * 
     */
    public CurrentTenantModelTable(Observable observable, Container container) {
        this.observable = observable;
        currentTenantContainer = container;
        observable.addObserver(this);
        init(container);
    }

    /**
     * Initializes the table and sets the container.
     * 
     */
    private void init(Container container) {
        setSizeFull();
        setContainerDataSource(container);
        addContainerProperty("Name", String.class, null);
        addContainerProperty("Creation Date", String.class, null);
        addContainerProperty("Published", String.class, null);
        addContainerProperty("Edit", Button.class, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof CurrentTenantContainer) {
        	try {
                currentTenantList = userFacade.getJoinedTenants(userId);
                for (Item item : currentTenantList) {
                    addItem(item);
                }
                this.requestRepaint();
            } catch (TransactionException exception) {
                Main.getCurrent().addWindow(new TransactionErrorDialogComponent(exception));
            } 
        }

    }

}
