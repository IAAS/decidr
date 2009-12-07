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
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This container holds the workflow models. The models are represented as items
 * in a table
 * 
 * @author AT
 */
public class WorkflowModelContainer implements Container, Container.Ordered, Container.Filterable {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;

	private HttpSession session = Main.getCurrent().getSession();
	
	private Role role = (Role)session.getAttribute("role");

	TenantFacade tenantFacade = new TenantFacade(role);

	List<Item> workflowModelList = null;

	private ArrayList<Object> propertyIds = new ArrayList<Object>();
	private Map<Object, Object> items = new LinkedHashMap<Object, Object>();

	private List<Object> itemIdList = null;

	/**
	 * Default constructor. The workflow model items are added to the container
	 * 
	 */
	public WorkflowModelContainer() {

		try {
			Long tenantId = (Long) Main.getCurrent().getSession().getAttribute(
					"tenantId");
			workflowModelList = tenantFacade.getWorkflowModels(tenantId, null,
					null);
			for (Item item : workflowModelList) {
				addItem(item);
			}
		} catch (TransactionException e) {
			Main.getCurrent().getMainWindow().addWindow(
					new TransactionErrorDialogComponent(e));
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
	 * @see com.vaadin.data.Container#containsId(java.lang.Object)
	 */
	@Override
	public boolean containsId(Object itemId) {
		return items.containsKey(itemId);
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
			if (propertyId.equals("name")) {
				return String.class;
			} else if (propertyId.equals("id")) {
				return Long.class;
			} else if (propertyId.equals("creationDate")) {
				return Date.class;
			} else if (propertyId.equals("published")
					|| propertyId.equals("executable")) {
				return Boolean.class;
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
		if (items.containsKey(itemId)) {
			items.remove(itemId);
			return true;
		} else {
			return false;
		}

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

	/* (non-Javadoc)
	 * @see com.vaadin.data.Container.Filterable#addContainerFilter(java.lang.Object, java.lang.String, boolean, boolean)
	 */
	@Override
	public void addContainerFilter(Object propertyId, String filterString,
			boolean ignoreCase, boolean onlyMatchPrefix) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.vaadin.data.Container.Filterable#removeAllContainerFilters()
	 */
	@Override
	public void removeAllContainerFilters() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.vaadin.data.Container.Filterable#removeContainerFilters(java.lang.Object)
	 */
	@Override
	public void removeContainerFilters(Object propertyId) {
		// TODO Auto-generated method stub
		
	}

}
