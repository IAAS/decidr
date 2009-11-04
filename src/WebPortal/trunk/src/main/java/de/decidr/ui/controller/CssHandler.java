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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.FileUtils;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS2;
import com.vaadin.data.Item;

import de.decidr.model.acl.permissions.FilePermission;
import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.FileFacade;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TenantSettingsComponent;
import de.decidr.ui.view.TransactionErrorDialogComponent;

/**
 * This class handles the css saving
 * 
 * @author AT
 */
public class CssHandler {

	private TenantSettingsComponent component = null;
	
	private File file = null;

	private Long fileId = null;

	private Long tenantId = null;

	private String tenant = "";
	
	private String cssFile = "";

	/**
	 * The default constructor stores the given parameter.
	 * 
	 */
	public CssHandler(TenantSettingsComponent component) {
		tenant = (String) Main.getCurrent()
		.getSession().getAttribute("tenant");
		this.component = component;
		file = new File("../../../../webapp/VAADIN/themes/" + tenant + "/styles.css");
		
		try {
			cssFile = FileUtils.readFileToString(file);
		} catch (IOException e) {
			Main.getCurrent().getMainWindow().showNotification("File not found");
		}
	}

	/**
	 * Saves the css file and returns the file id
	 * 
	 * @return fileId
	 * @throws TransactionException
	 */
	public void saveCss(TenantFacade tenantFacade, boolean advanced,
			FileFacade fileFacade) throws TransactionException {
		try {
			tenantId = tenantFacade.getTenantId(tenant);
			InputStream in = getInputStream();
			if ((tenantFacade.getTenantSettings(tenantId).getItemProperty(
					"advancedColorSchemeId") != null) && advanced) {
				Item settings = tenantFacade.getTenantSettings(tenantId);
				Long colorSchemeId = (Long) settings.getItemProperty(
						"advancedColorSchemeId").getValue();

				de.decidr.model.entities.File file = fileFacade
						.getFileInfo(colorSchemeId);

				File f = getFileFromInputStream(in);
				fileFacade.replaceFile(file.getId(), in, f.length(), f
						.getAbsolutePath(), new MimetypesFileTypeMap()
						.getContentType(f));
				tenantFacade.setCurrentColorScheme(tenantId, advanced);
			}else if(tenantFacade.getTenantSettings(tenantId).getItemProperty(
					"simpleColorSchemeId") != null) {
				
			}
			else {
				File f = getFileFromInputStream(in);
				HashSet<Class<? extends FilePermission>> filePermission = new HashSet<Class<? extends FilePermission>>();
				filePermission.add(FileReadPermission.class);
				fileId = fileFacade.createFile(in, f.length(), f
						.getAbsolutePath(), new MimetypesFileTypeMap()
						.getContentType(f), false, filePermission);
				tenantFacade.setColorScheme(tenantId, fileId, advanced);
				tenantFacade.setCurrentColorScheme(tenantId, advanced);
			}
		} catch (TransactionException exception) {
			Main.getCurrent().getMainWindow().addWindow(
					new TransactionErrorDialogComponent());
		}
	}
	
	private void saveSimpleCss(){
		
	}


	/**
	 * Gets the input stream from the css string the user has entered in the
	 * component
	 * 
	 * @return input
	 */
	private InputStream getInputStream() {
		InputStream input;

		String cssValue = component.getCssTextField().getValue().toString();
		byte[] cssValueBytes = cssValue.getBytes();
		input = new ByteArrayInputStream(cssValueBytes);

		return input;

	}

	/**
	 * Returns a file object from the input stream of the entered css string
	 * 
	 * @return File f
	 */
	private File getFileFromInputStream(InputStream in) {
		File f = new File("../../../../webapp/VAADIN/themes/" + tenant
				+ "/styles.css");
		InputStream input = in;
		try {
			OutputStream output = new FileOutputStream(f);
			byte[] buf = new byte[1024];
			int len;
			while ((len = input.read(buf)) > 0) {
				output.write(buf, 0, len);

			}
			output.close();
			input.close();
			return f;
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

}