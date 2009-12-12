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

import com.vaadin.ui.Button.ClickEvent;

import de.decidr.model.acl.roles.Role;
import de.decidr.model.entities.File;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.FileFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.windows.InformationDialogComponent;

/**
 * TODO: add comment
 * 
 * @author AT
 */
public class HideWindowAndDeleteFileAction extends HideDialogWindowAction {

	FileFacade fileFacade = new FileFacade((Role) Main.getCurrent()
			.getSession().getAttribute("role"));

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.decidr.ui.controller.HideDialogWindowAction#buttonClick(com.vaadin
	 * .ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		
		super.buttonClick(event);
		Long fileId = (Long) Main.getCurrent().getMainWindow().getData();
		try {
			File file = fileFacade.getFileInfo(fileId);
			if (file.isTemporary()) {
				fileFacade.deleteFile(fileId);
			}
		} catch (TransactionException e) {
			Main.getCurrent().getMainWindow().addWindow(
					new InformationDialogComponent("File couldn't be deleted",
							"File not found to delete"));
		}
	}

}
