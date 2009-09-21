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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Observable;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This container holds the running worfklow instances. The runnin instances 
 * are represented as items in a table.
 *
 * @author AT
 */
public class RunningInstanceContainer extends Observable implements Container, Container.Ordered {
    
    private HttpSession session = Main.getCurrent().getSession();
    
    private Long userId = (Long)session.getAttribute("userId");
    
    UserFacade userFacade = new UserFacade(new UserRole(userId));
    
    List<Item> runningInstanceList = null;
    
    private ArrayList<Object> propertyIds = new ArrayList<Object>();
    private LinkedHashMap<Object, Object> items = new LinkedHashMap<Object, Object>();
    
    /**
     * Default constructor.
     *
     */
    public RunningInstanceContainer() {
        setChanged();
        notifyObservers();
       try{
           runningInstanceList = userFacade.getAdminstratedWorkflowInstances(userId);
           for(Item item : runningInstanceList){
               if(item.getItemProperty("completed") == null ){
                   addItem(item);
               }
           }
       }catch(TransactionException exception){
           Main.getCurrent().getMainWindow().addWindow(new TransactionErrorDialogComponent());
       }
    }


    /* (non-Javadoc)
     * @see com.vaadin.data.Container#addContainerProperty(java.lang.Object, java.lang.Class, java.lang.Object)
     */
    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type,
            Object defaultValue) throws UnsupportedOperationException {
        if(propertyIds.contains(propertyId)){
            propertyIds.add(propertyId);
            return false;
            
        }
        
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
        return getItem(itemId);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#containsId(java.lang.Object)
     */
    @Override
    public boolean containsId(Object itemId) {
        return items.containsKey(itemId);
    }

    /* (non-Javadoc)
     * @see com.vaadin.data.Container#getContainerProperty(java.lang.Object, java.lang.Object)
     */
    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        return getItem(itemId).getItemProperty(propertyId);
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


    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Ordered#addItemAfter(java.lang.Object)
     */
    @Override
    public Object addItemAfter(Object previousItemId)
            throws UnsupportedOperationException {
        // TODO Auto-generated method stub
        return null;
    }


    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Ordered#addItemAfter(java.lang.Object, java.lang.Object)
     */
    @Override
    public Item addItemAfter(Object previousItemId, Object newItemId)
            throws UnsupportedOperationException {
        // TODO Auto-generated method stub
        return null;
    }


    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Ordered#firstItemId()
     */
    @Override
    public Object firstItemId() {
        // TODO Auto-generated method stub
        return null;
    }


    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Ordered#isFirstId(java.lang.Object)
     */
    @Override
    public boolean isFirstId(Object itemId) {
        // TODO Auto-generated method stub
        return false;
    }


    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Ordered#isLastId(java.lang.Object)
     */
    @Override
    public boolean isLastId(Object itemId) {
        // TODO Auto-generated method stub
        return false;
    }


    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Ordered#lastItemId()
     */
    @Override
    public Object lastItemId() {
        // TODO Auto-generated method stub
        return null;
    }


    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Ordered#nextItemId(java.lang.Object)
     */
    @Override
    public Object nextItemId(Object itemId) {
        // TODO Auto-generated method stub
        return null;
    }


    /* (non-Javadoc)
     * @see com.vaadin.data.Container.Ordered#prevItemId(java.lang.Object)
     */
    @Override
    public Object prevItemId(Object itemId) {
        // TODO Auto-generated method stub
        return null;
    }

}