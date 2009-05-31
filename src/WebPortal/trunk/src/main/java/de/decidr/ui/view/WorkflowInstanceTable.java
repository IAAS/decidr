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

import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

/**
 * This class represents the workflow instance ui component. 
 * It will be connected with data from the database.
 *
 * @author AT
 */
public class WorkflowInstanceTable extends Table implements Observer{

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -4395559171091884350L;

    /**
     * Default constructor.
     *
     */
    public WorkflowInstanceTable() {
        init();
    }

    /**
     * This method initializes the components for the workflow instance table.
     *
     */
    private void init() {
        setSizeFull();
        addContainerProperty("Name", String.class, null);
        addContainerProperty("Create", Button.class, null);
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        
    }

}
