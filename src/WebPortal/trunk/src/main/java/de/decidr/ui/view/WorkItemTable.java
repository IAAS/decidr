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

import java.util.Observable;
import java.util.Observer;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;
import de.decidr.ui.data.WorkItemContainer;

public class WorkItemTable extends Table implements Observer{

    /**
     * The table represents the work items as items.
     * 
     * @author AT
     */
    private static final long serialVersionUID = 24861377458898625L;
    
   private Container workItemContainer = null;
   private Observable observable = null;

    /**
     * Default constructor. The table is added as an observer to 
     * the container which notifies the table if the data has changed.
     *
     * @param observable
     * @param container
     */
    public WorkItemTable(Observable observable, Container container){
        this.workItemContainer = container;
        this.observable = observable;
        observable.addObserver(this);
        init(observable, container);
    }
    
    /**
     * Initializes the table and sets the container data source.
     *
     * @param observable
     * @param container
     */
    private void init(Observable observable, Container container){
       
        //workItemContainer = new WorkItemContainer();
        setSizeFull();
        setContainerDataSource(container);
       
        workItemContainer.addContainerProperty("Name", String.class, null);
        workItemContainer.addContainerProperty("Tenant", String.class, null);
        workItemContainer.addContainerProperty("Data received", String.class, null);
        workItemContainer.addContainerProperty("Status", String.class, null);
        //setVisibleColumns(workItemContainer.getContainerPropertyIds().toArray());
        
        
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof WorkItemContainer)
        refreshCurrentPage();
    }

}
