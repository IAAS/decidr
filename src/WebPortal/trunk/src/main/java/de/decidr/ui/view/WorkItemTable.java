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
import com.vaadin.ui.Table;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.KeywordFilter;
import de.decidr.model.filters.Paginator;
import de.decidr.ui.data.WorkItemContainer;

public class WorkItemTable extends Table implements Observer {

    /**
     * The table represents the work items as items.
     * 
     * @author AT
     */
    private static final long serialVersionUID = 24861377458898625L;

    private Container workItemContainer = null;
    private Observable observable = null;
    
    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");

    private UserFacade userFacade = new UserFacade(new UserRole(userId));

    private List<Item> workItemList = null;
    
    private KeywordFilter filter = new KeywordFilter();

    private List<Filter> filterList = new LinkedList<Filter>();

    private Paginator paginator = new Paginator();

    /**
     * Default constructor. The table is added as an observer to the container
     * which notifies the table if the data has changed.
     * 
     * @param observable
     * @param container
     */
    public WorkItemTable(Observable observable, Container container) {
        this.workItemContainer = container;
        this.observable = observable;
        observable.addObserver(this);
        init(container);
    }

    /**
     * Initializes the table and sets the container data source.
     * 
     * @param observable
     * @param container
     */
    private void init(Container container) {

        // workItemContainer = new WorkItemContainer();
        setSizeFull();
        setContainerDataSource(container);

        addContainerProperty("Name", String.class, null);
        addContainerProperty("Tenant", String.class, null);
        addContainerProperty("Data received", String.class, null);
        addContainerProperty("Status", String.class, null);
        // setVisibleColumns(workItemContainer.getContainerPropertyIds().toArray());

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof WorkItemContainer) {
        	filterList.add(filter);
            try {
                workItemList = userFacade.getWorkItems(userId, filterList,
                        paginator);
                for (Item item : workItemList) {
                    addItem(item);
                }
                this.requestRepaint();
            } catch (TransactionException exception) {
                Main.getCurrent().getMainWindow().addWindow(
                        new TransactionErrorDialogComponent());
            }
        }

    }

}
