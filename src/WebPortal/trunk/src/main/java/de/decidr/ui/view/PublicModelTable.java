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

import com.vaadin.ui.Table;

/**
 * TODO: add comment
 *
 * @author AT
 */
public class PublicModelTable extends Table implements Observer{

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -8901605163680575150L;
    
    /**
     * Default constructor
     *
     */
    public PublicModelTable() {
        setSizeFull();
        addContainerProperty("Name", String.class, null);
        addContainerProperty("Creation Date", String.class, null);
        addContainerProperty("Tenant Name", String.class, null);
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        // TODO Auto-generated method stub
        
    }


    
}
