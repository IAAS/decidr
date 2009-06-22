package de.decidr.ui.view;

import javax.servlet.http.HttpSession;

import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;

import de.decidr.ui.data.WorkItemContainer;

public class SearchPanel extends Panel {
    
    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = 8703352734047305920L;

    private HorizontalLayout searchHorizontalLayout = null;
    
    private Label keywordLabel = null;
    
    private TextField searchTextField = null;
    
    private Button searchButton = null;
    
    /**
     * Default constructor
     *
     */
    public SearchPanel(){
        init();
    }
    
    /**
     * This method initializes the components of the search panel component
     *
     */
    private void init(){
        searchHorizontalLayout = new HorizontalLayout();
        
        keywordLabel = new Label("Keyword: ");
        
        searchTextField = new TextField();
        
        searchButton = new Button("Search");
        searchButton.addListener(new Button.ClickListener(){
//just testing
            @Override
            public void buttonClick(ClickEvent event) {
                
                
            }
            
        });
        
        addComponent(searchHorizontalLayout);
        
        searchHorizontalLayout.setSpacing(true);
        searchHorizontalLayout.addComponent(keywordLabel);
        searchHorizontalLayout.addComponent(searchTextField);
        searchHorizontalLayout.addComponent(searchButton);
    }

    public HorizontalLayout getSearchHorizontalLayout() {
        return searchHorizontalLayout;
    }

    public void setSearchHorizontalLayout(HorizontalLayout searchHorizontalLayout) {
        this.searchHorizontalLayout = searchHorizontalLayout;
    }

}
