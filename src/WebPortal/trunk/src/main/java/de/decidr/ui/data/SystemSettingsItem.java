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

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.facades.SystemFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

public class SystemSettingsItem implements Item {

    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");
    private SystemFacade systemFacade = new SystemFacade(new UserRole(userId));

    private Item item = null;

    public SystemSettingsItem() {
        try {
            item = systemFacade.getSettings();
            // Aleks, GH: remove later
            // item = new BeanItem(ProfileSettingsContainer.getInstance());
        } catch (Exception e) {
            Main.getCurrent().addWindow(new TransactionErrorDialogComponent(e));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Item#addItemProperty(java.lang.Object,
     * com.vaadin.data.Property)
     */
    @Override
    public boolean addItemProperty(Object id, Property property)
            throws UnsupportedOperationException {
        return item.addItemProperty(id, property);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Item#getItemProperty(java.lang.Object)
     */
    @Override
    public Property getItemProperty(Object id) {
        return item.getItemProperty(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Item#getItemPropertyIds()
     */
    @Override
    public Collection<?> getItemPropertyIds() {
        return item.getItemPropertyIds();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.data.Item#removeItemProperty(java.lang.Object)
     */
    @Override
    public boolean removeItemProperty(Object id)
            throws UnsupportedOperationException {
        return item.removeItemProperty(id);
    }

}
