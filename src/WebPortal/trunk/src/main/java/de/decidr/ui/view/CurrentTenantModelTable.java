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

import de.decidr.ui.data.CurrentTenantContainer;

/**
 * TODO: add comment
 *
 * @author AT
 */
public class CurrentTenantModelTable extends Table implements Observer{

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -3378507042364075268L;
    private Observable observable = null;
    private Container currentTenantContainer = null;
    
    /**
     * Default Constructor
     *
     */
    public CurrentTenantModelTable(Observable observable, Container container) {
        this.observable = observable;
        currentTenantContainer = container;
        observable.addObserver(this);
        init(observable, container);
    }

    
    /**
     * TODO: add comment
     *
     */
    private void init(Observable observable, Container container) {
        setSizeFull();
        addContainerProperty("Name", String.class, null);
        addContainerProperty("Creation Date", String.class, null);
        addContainerProperty("Published", String.class, null);
        addContainerProperty("Edit", Button.class, null);
        
    }


    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof CurrentTenantContainer){
            refreshCurrentPage();
        }
        
    }

}
