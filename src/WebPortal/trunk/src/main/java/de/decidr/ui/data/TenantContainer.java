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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.filters.EqualsFilter;
import de.decidr.model.filters.Filter;

import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This container holds tenants. The tenants are represented by a list of
 * {@link Item items}.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "RR" }, lastRevision = "2366", currentReviewState = State.Passed)
public class TenantContainer implements Container, Container.Ordered {

    private static final long serialVersionUID = 1L;

    private HttpSession session = Main.getCurrent().getSession();

    private Role role = (Role) session.getAttribute("role");

    private TenantFacade tenantFacade = new TenantFacade(role);

    List<Item> tenantList = null;

    private ArrayList<Object> propertyIds = new ArrayList<Object>();
    private LinkedHashMap<Object, Object> items = new LinkedHashMap<Object, Object>();
    private List<Object> itemIdList = null;

    private List<Filter> filterList = null;

    /**
     * The tenant {@link Item items} are added to the container.
     */
    public TenantContainer() {
        try {
            filterList = new ArrayList<Filter>();
            filterList.add(new EqualsFilter(true, "approvedSince", new Date()));
            tenantList = tenantFacade.getAllTenants(null, null);
            for (Item item : tenantList) {
                addItem(item);
            }
        } catch (TransactionException exception) {
            Main.getCurrent().getMainWindow().addWindow(
                    new TransactionErrorDialogComponent(exception));
        }
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
            return false;
        }
        propertyIds.add(propertyId);
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
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
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
        itemIdList = new ArrayList<Object>(items.keySet());
        return itemIdList.get(0);
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
                    || propertyId.equals("numWorkflowInstances")
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
        itemIdList = new ArrayList<Object>(items.keySet());
        Object object = firstItemId();
        if (itemId.equals(object)) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container.Ordered#isLastId(java.lang.Object)
     */
    @Override
    public boolean isLastId(Object itemId) {
        itemIdList = new ArrayList<Object>(items.keySet());
        Object object = lastItemId();
        if (itemId.equals(object)) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container.Ordered#lastItemId()
     */
    @Override
    public Object lastItemId() {
        itemIdList = new ArrayList<Object>(items.keySet());
        return itemIdList.get(itemIdList.size() - 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container.Ordered#nextItemId(java.lang.Object)
     */
    @Override
    public Object nextItemId(Object itemId) {
        itemIdList = new ArrayList<Object>(items.keySet());
        int index = itemIdList.indexOf(itemId);
        if (index == itemIdList.size() - 1) {
            return itemIdList.get(index);
        } else {
            return itemIdList.get(index + 1);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Container.Ordered#prevItemId(java.lang.Object)
     */
    @Override
    public Object prevItemId(Object itemId) {
        itemIdList = new ArrayList<Object>(items.keySet());
        int index = itemIdList.indexOf(itemId);
        return itemIdList.get(index - 1);
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
