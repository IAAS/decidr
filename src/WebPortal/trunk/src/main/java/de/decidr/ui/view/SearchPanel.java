package de.decidr.ui.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

public class SearchPanel extends Panel {
    
    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = 8703352734047305920L;

    private HorizontalLayout searchHorizontalLayout = null;
    
    private Label keywordLabel = null;
    
    private TextField searchTextField = null;
    
    private Button searchButton = null;
    
    public SearchPanel(){
        init();
    }
    
    private void init(){
        searchHorizontalLayout = new HorizontalLayout();
        
        keywordLabel = new Label("Keyword: ");
        
        searchTextField = new TextField();
        
        searchButton = new Button("Search");
        
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
