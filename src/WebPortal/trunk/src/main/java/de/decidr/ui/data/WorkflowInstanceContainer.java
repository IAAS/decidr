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

import javax.servlet.http.HttpSession;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.TenantFacade;
import de.decidr.model.facades.UserFacade;
import de.decidr.model.filters.EqualsFilter;
import de.decidr.model.filters.Filter;
import de.decidr.model.filters.StartableWorkflowModelFilter;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * The container holds the workflow instances. The instances are represented as
 * items in a table.
 * 
 * @author AT
 */
public class WorkflowInstanceContainer implements Container {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;

	private HttpSession session = Main.getCurrent().getSession();

	private Long userId = (Long) session.getAttribute("userId");
	private Role role = (Role) session.getAttribute("role");

	TenantFacade tenantFacade = new TenantFacade(role);
	UserFacade userFacade = new UserFacade(role);
	Long tenantId = (Long) Main.getCurrent().getSession().getAttribute(
			"tenantId");

	List<Item> workflowModelList = null;

	private ArrayList<Object> propertyIds = new ArrayList<Object>();
	private LinkedHashMap<Object, Object> items = new LinkedHashMap<Object, Object>();

	private List<Filter> filterList;

	/**
	 * Default constructor. The instance items are added to the container.
	 * 
	 */
	public WorkflowInstanceContainer() {
		try {
			// Aleks Please review my changes: ~dh
			filterList = new ArrayList<Filter>();
			filterList.add(new StartableWorkflowModelFilter());
			filterList.add(new EqualsFilter(true, "tenant.id", tenantId));

			workflowModelList = userFacade.getAdministratedWorkflowModels(
					userId, null, null);
			for (Item item : workflowModelList) {
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
			if (propertyId.equals("id")) {
				return Long.class;
			} else if (propertyId.equals("name")
					|| propertyId.equals("description")) {
				return String.class;
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
