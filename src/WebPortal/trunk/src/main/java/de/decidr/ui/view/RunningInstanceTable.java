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
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

import de.decidr.ui.data.RunningInstanceContainer;

public class RunningInstanceTable extends Table implements Observer {

    /**
     * This table holds the running workflow instances as items.
     * 
     * @author AT
     */
    private static final long serialVersionUID = 49258596599726066L;

    private Observable observable = null;
    private Container runningInstanceContainer = null;

    /**
     * Default construtctor. The table is added as an observer to the container
     * which notifies the table if the data has changed.
     * 
     * @param observable
     * @param container
     */
    public RunningInstanceTable(Observable observable, Container container) {
        this.observable = observable;
        runningInstanceContainer = container;
        observable.addObserver(this);
        init(container);
    }

    /**
     * Initializes the table and sets the container.
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
        addContainerProperty("Status", String.class, null);
        addContainerProperty("Terminate", Button.class, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof RunningInstanceContainer) {
            this.requestRepaint();
            refreshCurrentPage();
        }

    }
}
