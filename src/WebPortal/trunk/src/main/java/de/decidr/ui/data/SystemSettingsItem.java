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


/**
 * This item stores the system settings.
 *
 * @author Geoffrey-Alexeij Heinze
 */

import java.util.Collection;

import javax.servlet.http.HttpSession;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.facades.SystemFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

public class SystemSettingsItem implements Item {

    private HttpSession session = Main.getCurrent().getSession();
    
    private Long userId = (Long)session.getAttribute("userId");
    private SystemFacade systemFacade = new SystemFacade(new UserRole(userId));

	private Item items = null;
	
	public SystemSettingsItem(){
		try{
		//	items = systemFacade.getSettings();
			//TODO: remove later
			 items = new BeanItem(ProfileSettingsContainer.getInstance());
		} catch (Exception e){
			Main.getCurrent().addWindow(new TransactionErrorDialogComponent());
		}
	}

	/* (non-Javadoc)
	 * @see com.vaadin.data.Item#addItemProperty(java.lang.Object, com.vaadin.data.Property)
	 */
	@Override
	public boolean addItemProperty(Object id, Property property)
			throws UnsupportedOperationException {
		return items.addItemProperty(id, property);
	}

	/* (non-Javadoc)
	 * @see com.vaadin.data.Item#getItemProperty(java.lang.Object)
	 */
	@Override
	public Property getItemProperty(Object id) {
		return items.getItemProperty(id);
	}

	/* (non-Javadoc)
	 * @see com.vaadin.data.Item#getItemPropertyIds()
	 */
	@Override
	public Collection<?> getItemPropertyIds() {
		return items.getItemPropertyIds();
	}

	/* (non-Javadoc)
	 * @see com.vaadin.data.Item#removeItemProperty(java.lang.Object)
	 */
	@Override
	public boolean removeItemProperty(Object id)
			throws UnsupportedOperationException {
		return items.removeItemProperty(id);
	}

}
