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

package de.decidr.ui.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Label;
import com.vaadin.ui.AbstractSelect.NewItemHandler;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.facades.WorkItemFacade;
import de.decidr.model.permissions.UserRole;
import de.decidr.ui.view.Main;

/**
 * TODO: add comment
 *
 * @author AT
 */
public class WorkItemContainer extends Observable implements Container {
    
    private ApplicationContext ctx = Main.getCurrent().getContext();
    private WebApplicationContext webCtx = (WebApplicationContext)ctx;
    private HttpSession session = webCtx.getHttpSession();
    
    private Long userId = (Long)session.getAttribute("userId");
    
    private UserFacade userFacade = new UserFacade(new UserRole(userId));
    private WorkItemFacade workItemFacade = new WorkItemFacade(new UserRole(userId));
    
    private List<Item> workItemList = userFacade.getWorkItems(userId, null, null);
    
    private Item workItem = null;
       
    private ArrayList<Object> propertyIds = new ArrayList<Object>();
    private LinkedHashMap<Object, Object> items = new LinkedHashMap<Object, Object>();
    
    public WorkItemContainer(){
        setChanged();
        notifyObservers();
        for(Item item : workItemList){
           addItem(item);
        }
    }
    
    /* (non-Javadoc)
     * @see com.vaadin.data.Container#addContainerProperty(java.lang.Object, java.lang.Class, java.lang.Object)
     */
    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type,
            Object defaultValue) throws UnsupportedOperationException {
        if(propertyIds.contains(propertyId)){
            return false;
        }
        propertyIds.add(propertyId);
        return true;
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#addItem()
     */
    @Override
    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#addItem(java.lang.Object)
     */
    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        items.put(itemId, itemId);
        try{
            return workItemFacade.getWorkItem((Long)itemId);
        }catch(TransactionException exception){
            
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#containsId(java.lang.Object)
     */
    @Override
    public boolean containsId(Object itemId) {
        return items.containsKey(itemId);//item1.contains(itemId); 
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#getContainerProperty(java.lang.Object, java.lang.Object)
     */
    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        return ((Item)getItem(itemId)).getItemProperty(propertyId);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#getContainerPropertyIds()
     */
    @Override
    public Collection<?> getContainerPropertyIds() {
        return propertyIds;
        
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#getItem(java.lang.Object)
     */
    @Override
    public Item getItem(Object itemId) {
        Item item = (Item)items.get(itemId);
        if(item == null){
            Long id = workItemFacade.createWorkItem(userId, deployedWorkflowModelId, odePid, name, description, data, notifyUser);
            item = workItemFacade.getWorkItem(id);
        }
        return item;
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#getItemIds()
     */
    @Override
    public Collection<?> getItemIds() {
       return items.keySet();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#getType(java.lang.Object)
     */
    @Override
    public Class<?> getType(Object propertyId) {
        return String.class;
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#removeAllItems()
     */
    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        items.clear();
        return true;
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#removeContainerProperty(java.lang.Object)
     */
    @Override
    public boolean removeContainerProperty(Object propertyId)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#removeItem(java.lang.Object)
     */
    @Override
    public boolean removeItem(Object itemId)
            throws UnsupportedOperationException {
        items.remove(itemId);
        return true;
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#size()
     */
    @Override
    public int size() {
        return items.size();
    }

}