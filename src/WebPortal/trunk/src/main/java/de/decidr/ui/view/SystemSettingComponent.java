package de.decidr.ui.view;

import java.util.Arrays;

import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;

import de.decidr.ui.controller.SaveSystemSettingsAction;
import de.decidr.ui.data.SystemSettingsItem;

public class SystemSettingComponent extends CustomComponent {
    
    /**
     * TODO: add comment
     */
    private static final long serialVersionUID = 3389525551936631625L;
    
    private SystemSettingsItem settingsItem = new SystemSettingsItem();
    
    private VerticalLayout verticalLayout = null;
    
    private Form settingsForm = null;
    
    private static final String[] logLevel = new String[] {
        "INFO", "TRACE", "DEBUG", "WARN", "ERROR", "FATAL"
    };
    
    private NativeSelect nativeSelect = null;
    
    private CheckBox checkBox = null;
    
    private Button saveButton = null;
    
    /**
     * Default constructor
     *
     */
    public SystemSettingComponent(){
        init();
    }
    
    public void saveSettingsItem(){
    	try{
            settingsForm.commit();
            
        } catch (Exception e) {
            Main.getCurrent().getMainWindow().showNotification(e.getMessage());
        }
    }
    
    public Item getSettingsItem(){
    	return settingsItem;
    }
    
    /**
     * This method initializes the components of the system settings component
     *
     */
    private void init(){
    	settingsForm = new Form();
        settingsForm.setWriteThrough(false);
        settingsForm.setItemDataSource(settingsItem);
        settingsForm.setVisibleItemProperties(Arrays.asList(new String[] {
                "logLevel", "autoAcceptNewTenants" }));
        
        verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        
        nativeSelect = new NativeSelect("Log Level");
        for(int i = 0; i < logLevel.length; i++){
            nativeSelect.addItem(logLevel[i]);
        }
        nativeSelect.setNullSelectionAllowed(false);
        nativeSelect.setValue(logLevel[0]);
        
        checkBox = new CheckBox("Automatically approve all new tenants");
        
        saveButton = new Button("Save", new SaveSystemSettingsAction());
        
        setCompositionRoot(verticalLayout);
        setCaption("System Setting");
        
        settingsForm.addField("logLevel", nativeSelect);
        settingsForm.addField("autoAcceptNewTenants", checkBox);
        
        verticalLayout.addComponent(settingsForm);
        verticalLayout.addComponent(saveButton);
        verticalLayout.setComponentAlignment(saveButton, Alignment.BOTTOM_LEFT);
    }
 
}
