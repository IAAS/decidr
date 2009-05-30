package de.decidr.ui.view;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;

public class SystemSetting extends CustomComponent {
    
    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = 3389525551936631625L;

    private static SystemSetting systemSetting = null;
    
    private VerticalLayout verticalLayout = null;
    
    private static final String[] logLevel = new String[] {
        "INFO", "TRACE", "DEBUG", "WARN", "ERROR", "FATAL"
    };
    
    private NativeSelect nativeSelect = null;
    
    private CheckBox checkBox = null;
    
    private Button saveButton = null;
    
    private SystemSetting(){
        init();
    }
    
    private void init(){
        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        
        nativeSelect = new NativeSelect("Log Level");
        for(int i = 0; i < logLevel.length; i++){
            nativeSelect.addItem(logLevel[i]);
        }
        nativeSelect.setNullSelectionAllowed(false);
        nativeSelect.setValue(logLevel[0]);
        
        checkBox = new CheckBox("Automatically approve all new tenants");
        
        saveButton = new Button("Save");
        
        setCompositionRoot(verticalLayout);
        setCaption("System Setting");
        
        verticalLayout.addComponent(nativeSelect);
        verticalLayout.setComponentAlignment(nativeSelect, Alignment.TOP_LEFT);
        verticalLayout.addComponent(checkBox);
        verticalLayout.setComponentAlignment(checkBox, Alignment.MIDDLE_LEFT);
        verticalLayout.addComponent(saveButton);
        verticalLayout.setComponentAlignment(saveButton, Alignment.BOTTOM_LEFT);
    }
    
    public static SystemSetting getInstance(){
        if(systemSetting == null){
            systemSetting = new SystemSetting();
        }
        return systemSetting;
    }

}
