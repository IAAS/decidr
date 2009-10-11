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
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.KeywordFilter;
import de.decidr.model.filters.Paginator;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This container holds the tenants. The tenants are represented as items in a
 * table.
 * 
 * @author AT
 */
public class TenantContainer extends Observable implements Container,
        Container.Filterable, Container.Ordered {

    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");

    private TenantFacade tenantFacade = new TenantFacade(new UserRole(userId));

    List<Item> tenantList = null;

    private ArrayList<Object> propertyIds = new ArrayList<Object>();
    private LinkedHashMap<Object, Object> items = new LinkedHashMap<Object, Object>();

    private KeywordFilter filter = new KeywordFilter();

    private List<Filter> filterList = new LinkedList<Filter>();

    private Paginator paginator = new Paginator();

    /**
     * Default constructor. The tenant items are added to the container.
     * 
     */
    public TenantContainer() {
        setChanged();
        notifyObservers();
        filterList.add(filter);
        try {
            tenantList = tenantFacade.getAllTenants(filterList, paginator);
            for (Item item : tenantList) {
                addItem(item);
            }
        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent());
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.data.Container.Filterable#addContainerFilter(java.lang.Object,
     * java.lang.String, boolean, boolean)
     */
    @Override
    public void addContainerFilter(Object propertyId, String filterString,
            boolean ignoreCase, boolean onlyMatchPrefix) {
        filter.setKeyword(filterString);
        filter.getProperties().add((String) propertyId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#addContainerProperty(java.lang.Object,
     * java.lang.Class, java.lang.Object)
     */
    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type,
            Object defaultValue) throws UnsupportedOperationException {
        if (propertyIds.contains(propertyId)) {
            propertyIds.add(propertyId);
            return false;

        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#addItem()
     */
    @Override
    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#addItem(java.lang.Object)
     */
    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        items.put(itemId, itemId);
        return getItem(itemId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container.Ordered#addItemAfter(java.lang.Object)
     */
    @Override
    public Object addItemAfter(Object previousItemId)
            throws UnsupportedOperationException {
        new UnsupportedOperationException();
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container.Ordered#addItemAfter(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public Item addItemAfter(Object previousItemId, Object newItemId)
            throws UnsupportedOperationException {
        new UnsupportedOperationException();
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#containsId(java.lang.Object)
     */
    @Override
    public boolean containsId(Object itemId) {
        return items.containsKey(itemId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container.Ordered#firstItemId()
     */
    @Override
    public Object firstItemId() {
        Object[] itemsArray = getItemIds().toArray();
        return itemsArray[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#getContainerProperty(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        return getItem(itemId).getItemProperty(propertyId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#getContainerPropertyIds()
     */
    @Override
    public Collection<?> getContainerPropertyIds() {

        return propertyIds;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#getItem(java.lang.Object)
     */
    @Override
    public Item getItem(Object itemId) {
        Item item = (Item) items.get(itemId);
        return item;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#getItemIds()
     */
    @Override
    public Collection<?> getItemIds() {

        return items.keySet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#getType(java.lang.Object)
     */
    @Override
    public Class<?> getType(Object propertyId) {
        if (getContainerPropertyIds().contains(propertyId)) {
            if (propertyId.equals("adminFirstName")
                    || propertyId.equals("adminLastName")
                    || propertyId.equals("tenantName")) {
                return String.class;
            } else if (propertyId.equals("numDeployedWorkflowModels")
                    || propertyId.equals("numMembers")
                    || propertyId.equals("numWorkflowInstance")
                    || propertyId.equals("id")) {
                return Long.class;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container.Ordered#isFirstId(java.lang.Object)
     */
    @Override
    public boolean isFirstId(Object itemId) {
        if (firstItemId().equals(itemId)) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container.Ordered#isLastId(java.lang.Object)
     */
    @Override
    public boolean isLastId(Object itemId) {
        if (lastItemId().equals(itemId)) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container.Ordered#lastItemId()
     */
    @Override
    public Object lastItemId() {
        Object[] itemsArray = getItemIds().toArray();
        return itemsArray[getItemIds().size()];
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container.Ordered#nextItemId(java.lang.Object)
     */
    @Override
    public Object nextItemId(Object itemId) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container.Ordered#prevItemId(java.lang.Object)
     */
    @Override
    public Object prevItemId(Object itemId) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container.Filterable#removeAllContainerFilters()
     */
    @Override
    public void removeAllContainerFilters() {
        filter.setKeyword("");

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#removeAllItems()
     */
    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        items.clear();
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.data.Container.Filterable#removeContainerFilters(java.lang
     * .Object)
     */
    @Override
    public void removeContainerFilters(Object propertyId) {
        if (filter.getProperties().contains(propertyId)) {
            filter.setKeyword("");
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#removeContainerProperty(java.lang.Object)
     */
    @Override
    public boolean removeContainerProperty(Object propertyId)
            throws UnsupportedOperationException {
        getContainerPropertyIds().remove(propertyId);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#removeItem(java.lang.Object)
     */
    @Override
    public boolean removeItem(Object itemId)
            throws UnsupportedOperationException {
        items.remove(itemId);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container#size()
     */
    @Override
    public int size() {
        return items.size();
    }

}
