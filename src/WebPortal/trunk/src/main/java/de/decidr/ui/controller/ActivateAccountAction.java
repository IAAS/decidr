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

package de.decidr.ui.controller;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.UserFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This action enables a list of user accounts.
 * 
 * @author Geoffrey-Alexeij Heinze
 */
public class ActivateAccountAction implements ClickListener {

    private HttpSession session = Main.getCurrent().getSession();

    private Long userId = (Long) session.getAttribute("userId");
    private UserFacade userFacade = new UserFacade(new UserRole(userId));

    private Table table = null;

    /**
     * Contructor, requires the table which contains the data
     * 
     * @param table
     *            : requires Table with data
     */
    public ActivateAccountAction(Table table) {
        this.table = table;
    }

    /*
     * (non-Javadoc)
     * 
     * @seecom.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        Set<?> value = (Set<?>) table.getValue();
        if ((value != null) && (value.size() != 0)) {
            for (Iterator<?> iter = value.iterator(); iter.hasNext();) {
                try {
                    userFacade.setUnavailableSince(
                            (Long) table
                                    .getContainerProperty(iter.next(), "id")
                                    .getValue(), null);
                } catch (TransactionException e) {
                    Main.getCurrent().getMainWindow().addWindow(
                            new TransactionErrorDialogComponent());
                }
            }
        }
    }
}