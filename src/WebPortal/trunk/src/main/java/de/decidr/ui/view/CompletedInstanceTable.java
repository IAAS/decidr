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

import de.decidr.ui.data.CompletedInstancesContainer;

public class CompletedInstanceTable extends Table implements Observer {

    /**
     * This table represents the completed workflow instances. The instances are
     * stored in items. The table is connected to the
     * {@link CompletedInstancesContainer}.
     * 
     * @author AT
     */
    private static final long serialVersionUID = -4341477724807479177L;

    private Observable observable = null;
    private Container workflowInstanceContainer = null;

    /**
     * The table is added as an observer to the container.
     * 
     * @param observable
     * @param container
     */
    public CompletedInstanceTable(Observable observable, Container container) {
        this.observable = observable;
        workflowInstanceContainer = container;
        observable.addObserver(this);
        init(container);
    }

    /**
     * Initializes the properties for the table like the data source and the
     * container property.
     * 
     * @param observable
     * @param container
     */
    private void init(Container container) {
        setSizeFull();
        setContainerDataSource(container);
        addContainerProperty("Name", String.class, null);
        addContainerProperty("Model", String.class, null);
        addContainerProperty("Start date", String.class, null);
        addContainerProperty("Completion date", String.class, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof CompletedInstancesContainer) {
            this.requestRepaint();
            refreshCurrentPage();
        }

    }

}
