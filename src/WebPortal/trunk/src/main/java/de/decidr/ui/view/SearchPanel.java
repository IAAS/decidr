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


import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

import de.decidr.ui.controller.SearchAction;


public class SearchPanel extends Panel {
    
    /**
     * This is a panel containing a text field and a button. 
     * This should represent a search widget.
     * 
     * @author AT
     */
    private static final long serialVersionUID = 8703352734047305920L;

    private HorizontalLayout searchHorizontalLayout = null;
    
    private Label keywordLabel = null;
    
    private TextField searchTextField = null;
    
    private Button searchButton = null;
    
    
    /**
     * Constructor requires a table to know in which table the keyword
     * should be searched.
     *
     * @param table
     */
    public SearchPanel(Table table){
        init(table);
    }
    
    /**
     * This method initializes the components of the search panel component
     *
     */
    private void init(Table table){
        searchHorizontalLayout = new HorizontalLayout();
        
        keywordLabel = new Label("Keyword: ");
        
        searchTextField = new TextField();
        
        searchButton = new Button("Search", new SearchAction(table ,searchTextField));
        
        addComponent(searchHorizontalLayout);
        
        searchHorizontalLayout.setSpacing(true);
        searchHorizontalLayout.addComponent(keywordLabel);
        searchHorizontalLayout.addComponent(searchTextField);
        searchHorizontalLayout.addComponent(searchButton);
    }

    /**
     * Returns the horizontal layout.
     *
     * @return searchHorizontalLayout
     */
    public HorizontalLayout getSearchHorizontalLayout() {
        return searchHorizontalLayout;
    }

    

}
