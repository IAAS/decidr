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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.IOUtils;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;
import com.vaadin.data.Item;
import com.vaadin.ui.Window;

import de.decidr.model.acl.permissions.FilePermission;
import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.FileFacade;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TenantSettingsComponent;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This class handles the CSS saving.
 * 
 * @author AT
 * @reviewed ~tk, ~dh
 */
public class CssHandler {

	private TenantSettingsComponent component = null;

	private Long fileId = null;

	private Long tenantId = null;
	// AT: tenantName muss auch rein. ~tk, ~dh

	private String tenant = "";

	/**
	 * The default constructor stores the given parameter.
	 */
	public CssHandler(TenantSettingsComponent component) {

		tenantId = (Long) Main.getCurrent().getSession().getAttribute(
				"tenantId");
		this.component = component;
	}

	/**
	 * Saves the css file
	 * 
	 * 
	 * @throws TransactionException
	 *             , IOException
	 */
	public void saveCss(TenantFacade tenantFacade, boolean advanced,
			FileFacade fileFacade) throws TransactionException {
		try {
			Item settings = tenantFacade.getTenantSettings(tenantId);
			tenant = settings.getItemProperty("name").getValue().toString();
			Long colorSchemeId;
			File f;
			InputStream in = null;
			// Checkt ob advanced color scheme oder simple color scheme
			// ausgewählt ist und erzeugt dementsprechend
			// einen input stream. Entweder, dass was der User eingibt im
			// Advanced Modus oder das was er auswählt beim
			// simple.
			try {
				if (advanced) {
					in = getInputStream(component.getCssTextField().getValue()
							.toString());
				} else {
					String cssValue = saveSimpleCss();
					in = getInputStream(cssValue);
					
				}
				// Checkt ob die advancedColorSchemeId gesetzt ist und ob der
				// User
				// eine Advanced CSS speichern möchte. Wenn ja,
				// dann holt er sich die Id, das dazugehörige File aus der
				// FileFacade, um die file Id zu bekommen. Als letztes wird
				// das vorhanden CSS file ersetzt mit der gleichen file id und
				// dem
				// neuen input. Das gleiche auch für simple CSS.
				if ((settings.getItemProperty("advancedColorSchemeId")
						.getValue() != null)
						&& advanced) {
					colorSchemeId = (Long) settings.getItemProperty(
							"advancedColorSchemeId").getValue();

					f = getFileFromInputStream(in);
					in.reset();
					// AT: was ist wenn der MIME Typ nicht erkannt wird? ~dh,tk
					fileFacade.replaceFile(colorSchemeId, in, f.length(), f
							.getAbsolutePath(), new MimetypesFileTypeMap()
							.getContentType(f));
				} else if (settings.getItemProperty("simpleColorSchemeId")
						.getValue() != null) {
					colorSchemeId = (Long) settings.getItemProperty(
							"simpleColorSchemeId").getValue();

					f = getFileFromInputStream(in);
					in.reset();
					fileFacade.replaceFile(colorSchemeId, in, f.length(), f
							.getAbsolutePath(), new MimetypesFileTypeMap()
							.getContentType(f));
				} else {
					// AT getFileFromInputStream can return null, but null value
					// is not handled! ~dh
					f = getFileFromInputStream(in);
					if (f == null){
					    Main.getCurrent().getMainWindow().showNotification("input file is null", Window.Notification.TYPE_ERROR_MESSAGE);
					}
					in.reset();
					HashSet<Class<? extends FilePermission>> filePermission = new HashSet<Class<? extends FilePermission>>();
					filePermission.add(FileReadPermission.class);
					if (fileFacade == null){
                                            Main.getCurrent().getMainWindow().showNotification("fileFacade is null", Window.Notification.TYPE_ERROR_MESSAGE);
                                        }
					if (in == null){
                                            Main.getCurrent().getMainWindow().showNotification("input stream is null", Window.Notification.TYPE_ERROR_MESSAGE);
                                        }
					fileId = fileFacade.createFile(in, f.length(), f
							.getAbsolutePath(), new MimetypesFileTypeMap()
							.getContentType(f), false, filePermission);
					tenantFacade.setColorScheme(tenantId, fileId, advanced);

				}
			} finally {
				if (in != null) {
					tenantFacade.setCurrentColorScheme(tenantId, advanced);
					in.close();
				}
			}
		} catch (TransactionException exception) {
			Main.getCurrent().getMainWindow().addWindow(
					new TransactionErrorDialogComponent(exception));
		} catch (IOException e) {
			Main.getCurrent().getMainWindow().addWindow(
					new InformationDialogComponent("Failed to load the file",
							"Failure"));
		}
	}

	/**
	 * Saves the simple CSS and returns the string representation
	 * 
	 * @return css - The string representation of the simple CSS file
	 */
	private String saveSimpleCss() {
		InputSource source = new InputSource();
		String css = "";
		try {
			InputStream stream = new FileInputStream(new File(Main.getCurrent().getContext().getBaseDirectory().getPath() + File.separator + "VAADIN" + File.separator + "themes"
					+ File.separator + "decidr" + File.separator + "styles.css"));
			source.setByteStream(stream);

			CSSOMParser p = new CSSOMParser();

			CSSStyleSheet sheet = p.parseStyleSheet(source, null, "");

			CSSRuleList list = sheet.getCssRules();

			for (int i = 0; i < list.getLength(); i++) {
				CSSRule cssRule = list.item(i);
				String rule = cssRule.getCssText();
				String substring;
				int index;
				if (cssRule.getCssText()
						.contains("*.v-generated-body, *.v-app")) {
					// Foreground setzen
					rule = rule.replace("}", "; color: "
							+ component.getForegroundSelect().getValue()
									.toString() + " }");

					// Font-family setzen
					index = rule.indexOf("font-family:");
					substring = rule.substring(index);
					index = substring.indexOf(";");
					substring = substring.substring(13, index);
					rule = rule.replace(substring, component.getFontSelect()
							.getValue().toString());

					// Font-size setzen
					index = rule.indexOf("font-size:");
					substring = rule.substring(index);
					index = substring.indexOf(";");
					substring = substring.substring(11, index);
					rule = rule.replace(substring, component
							.getFontSizeSelect().getValue().toString()
							+ "px");
				} else if (cssRule.getCssText().contains("*.v-generated-body")) {
					// Background setzen
					index = rule.indexOf("background:");
					substring = rule.substring(index);
					index = substring.indexOf("}");
					substring = substring.substring(12, index);
					rule = rule.replace(substring, component
							.getBackgroundSelect().getValue().toString());
				}
				css += rule;
			}
			stream.close();
			return css;
		} catch (FileNotFoundException e) {
			Main.getCurrent().getMainWindow()
					.showNotification("saveSimpleCSS: File not found", Window.Notification.TYPE_ERROR_MESSAGE);
			return "";
		} catch (IOException e) {
			Main.getCurrent().getMainWindow().showNotification("saveSimpleCSS: IO failure", Window.Notification.TYPE_ERROR_MESSAGE);
			return "";
		}
	}

	/**
	 * Gets the input stream from the css string the user has entered in the
	 * component
	 * 
	 * @return input
	 */
	private InputStream getInputStream(String cssValue) {
		InputStream input;

		byte[] cssValueBytes;
		try {
			cssValueBytes = cssValue.getBytes("UTF-8");
			input = new ByteArrayInputStream(cssValueBytes);

			return input;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Fatal: UTF-8 not supported");
		}
	}

	/**
	 * Returns a file object from the input stream of the entered css string
	 * 
	 * 
	 * @return File f
	 */
	private File getFileFromInputStream(InputStream in) {
		File f = new File(Main.getCurrent().getContext().getBaseDirectory().getPath() + File.separator + "VAADIN" + File.separator + "themes" + File.separator + tenant + File.separator
				+ "styles.css");
		
		if (!f.exists()){
	            try {
	                File dir = new File(Main.getCurrent().getContext().getBaseDirectory().getPath() + File.separator + "VAADIN" + File.separator + "themes" + File.separator + tenant);
	                dir.mkdirs();
                        f.createNewFile();
                    } catch (IOException e) {
                        Main.getCurrent().getMainWindow().showNotification(f.getAbsolutePath() + "does not exist, creating failed", Window.Notification.TYPE_ERROR_MESSAGE);
                    }
	                    
		}
		//Main.getCurrent().getMainWindow().showNotification("file exists: " + f.exists(), Window.Notification.TYPE_ERROR_MESSAGE);
		//Main.getCurrent().getMainWindow().showNotification("file path: " + f.getAbsolutePath(), Window.Notification.TYPE_ERROR_MESSAGE);
		
		OutputStream output = null;
		try {
			
				output = new FileOutputStream(f);
				IOUtils.copy(in, output);
				output.close();
				return f;
			
		} catch (FileNotFoundException e) {
		        
		        Main.getCurrent().getMainWindow().showNotification("FileNotFound:" + e.getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
			return null;
		} catch (IOException e) {
		        Main.getCurrent().getMainWindow().showNotification("IO Exception:" + e.getMessage(), Window.Notification.TYPE_ERROR_MESSAGE);
			return null;
		}
	}

}