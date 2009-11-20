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
import java.util.HashSet;

import javax.activation.MimetypesFileTypeMap;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;
import com.vaadin.data.Item;

import de.decidr.model.acl.permissions.FilePermission;
import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.FileFacade;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TenantSettingsComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This class handles the css saving
 * 
 * @author AT
 */
public class CssHandler {

	private TenantSettingsComponent component = null;

	private Long fileId = null;

	private Long tenantId = null;

	private String tenant = "";//TODO: aus der tenantfacade den namen holen

	private String cssFilePath = "";

	/**
	 * The default constructor stores the given parameter.
	 * 
	 */
	public CssHandler(TenantSettingsComponent component) {
		tenantId = (Long) Main.getCurrent().getSession().getAttribute("tenantId");
		this.component = component;
		cssFilePath = +File.separatorChar + "themes" + File.separator + tenant
				+ File.separator + "styles.css";
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
			Item settings = tenantFacade.getTenantSettings(tenantId);
			Long colorSchemeId;
			File f;
			InputStream in;
			// Checkt ob advanced color scheme oder simple color scheme
			// ausgewählt ist und erzeugt dementsprechend
			// einen input stream. Entweder, dass was der User eingibt im
			// Advanced Modus oder das was er auswählt beim
			// simple.
			if (advanced) {
				in = getInputStream(component.getCssTextField().getValue()
						.toString());
			} else {
				String cssValue = saveSimpleCss();
				in = getInputStream(cssValue);
			}
			// Checkt ob die advancedColorSchemeId gesetzt ist und ob der User
			// eine Advanced CSS speichern möchte. Wenn ja,
			// dann holt er sich die Id, das dazugehörige File aus der
			// FileFacade, um die file Id zu bekommen. Als letztes wird
			// das vorhanden CSS file ersetzt mit der gleichen file id und dem
			// neuen input. Das gleiche auch für simple CSS.
			if ((settings.getItemProperty("advancedColorSchemeId") != null)
					&& advanced) {
				colorSchemeId = (Long) settings.getItemProperty(
						"advancedColorSchemeId").getValue();

				f = getFileFromInputStream(in);

				fileFacade.replaceFile(colorSchemeId, in, f.length(), f
						.getAbsolutePath(), new MimetypesFileTypeMap()
						.getContentType(f));
				tenantFacade.setCurrentColorScheme(tenantId, advanced);
			} else if (settings.getItemProperty("simpleColorSchemeId") != null) {
				colorSchemeId = (Long) settings.getItemProperty(
						"simpleColorSchemeId").getValue();

				f = getFileFromInputStream(in);

				fileFacade.replaceFile(colorSchemeId, in, f.length(), f
						.getAbsolutePath(), new MimetypesFileTypeMap()
						.getContentType(f));
				tenantFacade.setCurrentColorScheme(tenantId, advanced);
			} else {
				f = getFileFromInputStream(in);
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
					new TransactionErrorDialogComponent(exception));
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
			InputStream stream = new FileInputStream(new File(cssFilePath));
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
							.getBackgroundSelect().getValue().toString()
							+ ";");
				}
				css += rule;
			}
			stream.close();
			return css;
		} catch (FileNotFoundException e) {
			Main.getCurrent().getMainWindow()
					.showNotification("File not found");
			return "";
		} catch (IOException e) {
			Main.getCurrent().getMainWindow().showNotification("IO failure");
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
		File f = new File(cssFilePath);
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