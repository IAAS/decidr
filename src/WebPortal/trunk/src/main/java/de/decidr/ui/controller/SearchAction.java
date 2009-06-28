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

package de.decidr.ui.controller;

import com.vaadin.data.Container.Filterable;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;


/**
 * This action filters a table and shows only these entries,
 * which contain the search string (partially or complete)
 *
 * @author Geoffrey-Alexeij Heinze
 */
public class SearchAction implements ClickListener {

    private Table searchTable = null;
    private TextField searchField = null;
    
    /**
     * Constructor, requires the Table which should be filtered and
     * the TextField, which contains the search string
     *
     * @param table: Table to be searched
     * @param textField: TextField, which contains the search string
     */
    public SearchAction(Table table, TextField textField){
        searchTable = table;
        searchField = textField;
    }
    
    /* (non-Javadoc)
     * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        Filterable container = (Filterable)searchTable.getContainerDataSource();
        container.removeAllContainerFilters();
        container.addContainerFilter(container.getContainerPropertyIds(), searchField.getValue().toString(), true, false);
        //TODO: check refresh method
        searchTable.getParent().requestRepaint();
    }

}
