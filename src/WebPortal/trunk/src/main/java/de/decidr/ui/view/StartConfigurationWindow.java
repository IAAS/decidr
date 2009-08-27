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


import java.util.Collection;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import de.decidr.model.workflowmodel.wsc.TAssignment;
import de.decidr.model.workflowmodel.wsc.TConfiguration;
import de.decidr.model.workflowmodel.wsc.TRole;

/**
 * This window represents the start configuration xml file. In this window
 * the user can add actors for roles and can enter his value for assignment 
 * variables. These values and actors will be stored in the xml file, so the 
 * user can start a workflow instance with the given configuration file.
 *
 * @author AT
 */
public class StartConfigurationWindow extends Window implements Handler, Property.ValueChangeListener{
    

    
    private HorizontalLayout horizontalLayout = null;
    
    private VerticalLayout verticalLayout = null;
    
    private SplitPanel splitPanel = null;
    
    private Tree rolesTree = null;
    
    private Form assignmentForm = null;
    
    private List<TRole> roles = null;
    
    private List<TAssignment> assignment = null;
    
    private TextField userIdTextField = null;
    
    private TextField usernameTextField = null;
    
    private TextField emailTextField = null;
    
    private Button applyButton = null;
    
    private static final Action ACTION_ADD = new Action("Add child item");
    private static final Action ACTION_DELETE = new Action("Delete");

    private static final Action[] ACTIONS = new Action[] { ACTION_ADD,
        ACTION_DELETE };

    /**
     * Default constructor with TConfiguration as parameter.
     *
     */
    public StartConfigurationWindow(TConfiguration tConfiguration) {
        init(tConfiguration);
    }
    
    /**
     * Initializes the components for the start configuration window. The 
     * Tconfiguration which represents the start configuration xml file is 
     * given as parametrer.
     *
     * @param tconfiguration
     */
    private void init(TConfiguration tconfiguration){
        splitPanel = new SplitPanel();
        horizontalLayout = new HorizontalLayout();
        verticalLayout = new VerticalLayout();
        rolesTree = new Tree("Rollen");
        assignmentForm = new Form();
        userIdTextField = new TextField("User ID: ");
        usernameTextField = new TextField("Username: ");
        emailTextField = new TextField("E-Mail: ");
        applyButton = new Button("Apply");
        
        rolesTree.setItemCaptionPropertyId("caption");
        rolesTree.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
        
        //The edited value should be taken and set as caption in the roles tree
        applyButton.addListener(new Button.ClickListener(){

            @Override
            public void buttonClick(ClickEvent event) {
                Item item = rolesTree.getItem(rolesTree.getValue());
                Property caption = item.getItemProperty("caption");
                if(!usernameTextField.getValue().equals("")){
                    caption.setValue(usernameTextField.getValue());
                    item.addItemProperty("username", usernameTextField);
                }
                if(!userIdTextField.getValue().equals("")){
                    caption.setValue(userIdTextField.getValue());
                    item.addItemProperty("userId", userIdTextField);
                }
                if(!emailTextField.getValue().equals("")){
                    caption.setValue(emailTextField.getValue());
                    item.addItemProperty("email", emailTextField);
                }
                if(emailTextField.getValue().equals("") && userIdTextField.getValue().equals("") && emailTextField.getValue().equals("")){
                    showNotification("Bitte geben Sie einer der drei Möglichkeiten an, bevor sie auf Apply drücken!");
                }
                
            }
            
        });
        
        this.setContent(splitPanel);
        
        this.setModal(true);
        
        horizontalLayout.setSpacing(true);
        horizontalLayout.addComponent(rolesTree);
        addRolesToTree(tconfiguration);
        
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(userIdTextField);
        verticalLayout.addComponent(usernameTextField);
        verticalLayout.addComponent(emailTextField);
        verticalLayout.setMargin(false, true, false, false);
        
        horizontalLayout.addComponent(verticalLayout);
        horizontalLayout.addComponent(applyButton);
        
        splitPanel.setFirstComponent(horizontalLayout);
        
        assignmentForm.setCaption("Liste der Konfigurationsvariablen");
        assignmentForm.setWriteThrough(false);
        assignmentForm.setInvalidCommitted(false);
        addAssignmentToForm(tconfiguration);
        
        splitPanel.setSecondComponent(assignmentForm);
    }
    
    /**
     * Adds a child to the tree which will be a parent node and which represents 
     * the roles which are stored in the start configuration xml file. Also the
     * nodes can contain children and they will be expanded.
     *
     * @param tConfiguration
     */
    private void addRolesToTree(TConfiguration tConfiguration){
        roles = tConfiguration.getRoles().getRole();
        for(int i = 0; i < roles.size(); i++){
            rolesTree.addItem(roles.get(i).getName());
            rolesTree.setChildrenAllowed(roles.get(i).getName(), true); 
            rolesTree.expandItem(roles.get(i).getName());
            
        }
        
    }
    
    /**
     * Adds a field for every assignment which is in the start configuration xml file.
     * So for every assignment the user can enter his desired value.
     *
     * @param tConfiguration
     */
    private void addAssignmentToForm(TConfiguration tConfiguration){
        assignment = tConfiguration.getAssignment();
        for(int i = 0; i< assignment.size(); i++){
            assignmentForm.addField(assignment.get(i).getKey(), new TextField(assignment.get(i).getKey()));
        }
    }

    /* (non-Javadoc)
     * @see com.vaadin.event.Action.Handler#getActions(java.lang.Object, java.lang.Object)
     */
    @Override
    public Action[] getActions(Object target, Object sender) {
        
        return ACTIONS;
    }

    /* (non-Javadoc)
     * @see com.vaadin.event.Action.Handler#handleAction(com.vaadin.event.Action, java.lang.Object, java.lang.Object)
     */
    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if(action == ACTION_ADD){
            Object itemId = rolesTree.addItem();
            //Setzt den Vaterknoten des neuhinzugefügten Kindes und erlaubt es dem Kind nicht,
            //weitere Kinder zu haben. Und setzt die Caption im Tree.
            rolesTree.setParent(itemId, target);
            rolesTree.setChildrenAllowed(itemId, false);   
          
            Item item = rolesTree.getItem(itemId);
            Property caption = item.getItemProperty("caption");
            caption.setValue("New Actor");    
        }else if(action == ACTION_DELETE){
           
            rolesTree.removeItem(target);
        }
        
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
     */
    @Override
    public void valueChange(ValueChangeEvent event) {
        //holt das ausgewählte item aus dem tree
        Item item = rolesTree.getItem(rolesTree.getValue());
        //holt die property ids aus dem ausgewählten item
        Collection collect = item.getItemPropertyIds();
        //Geht die property ids durch, schaut ob es property ids userId, username oder email
        //gibt. Wenn ja wird der Wert dieser Property, in diesem Fall ein TextField, ausgelesen,
        //sodass der Wert des TextField in das entsprechende TextField gesetzt werden kann, damit
        //der User die schon eingegeben Daten des Actors wieder einsehen kann.
        for(int i = 0; i < collect.size(); i++){
            if (collect.contains(item.getItemProperty("userId"))){
                TextField tf = (TextField)item.getItemProperty("userId").getValue();
                userIdTextField.setValue(tf.getValue());
            }else if(collect.contains(item.getItemProperty("username"))){
                TextField tf = (TextField)item.getItemProperty("username").getValue();
                usernameTextField.setValue(tf.getValue());
            }else if(collect.contains(item.getItemProperty("email"))){
                TextField tf = (TextField)item.getItemProperty("email").getValue();
                emailTextField.setValue(tf.getValue());
            }else{
                showNotification("Bitte geben Sie erst Werte ein, damit diese angezeigt werden können!");
            }
        }
    }

}
