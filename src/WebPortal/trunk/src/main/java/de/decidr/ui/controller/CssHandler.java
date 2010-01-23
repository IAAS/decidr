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
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

import de.decidr.model.acl.permissions.FilePermission;
import de.decidr.model.acl.permissions.FileReadPermission;
import de.decidr.model.annotations.Reviewed;
import de.decidr.model.annotations.Reviewed.State;
import de.decidr.model.entities.Tenant;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.FileFacade;
import de.decidr.model.facades.TenantFacade;
import de.decidr.ui.view.Main;
import de.decidr.ui.view.TenantSettingsComponent;
import de.decidr.ui.view.windows.InformationDialogComponent;
import de.decidr.ui.view.windows.TransactionErrorDialogComponent;

/**
 * This class handles CSS saving.
 * 
 * @author AT
 */
@Reviewed(reviewers = { "TK", "DH", "RR" }, lastRevision = "2358", currentReviewState = State.Rejected)
public class CssHandler {

    private TenantSettingsComponent component = null;

    private Long fileId = null;

    private Long tenantId = null;
    // TODO: tenantName muss auch rein.

    private String tenantName = "";

    /**
     * The constructor of the CSSHandler requires a TenantSettingsComponent. The
     * component is stored and later used to access the required input fields.
     */
    public CssHandler(TenantSettingsComponent component) {

        tenantId = (Long) Main.getCurrent().getSession().getAttribute(
                "tenantId");
        this.component = component;
    }

    /**
     * Returns a file object from the input stream of the entered CSS string.
     * 
     * @return File File representation of the given input stream
     */
    private File getFileFromInputStream(InputStream in) {
        File f = new File(Main.getCurrent().getContext().getBaseDirectory()
                .getPath()
                + File.separator
                + "VAADIN"
                + File.separator
                + "themes"
                + File.separator + tenantName + File.separator + "styles.css");

        if (!f.exists()) {
            try {
                File dir = new File(Main.getCurrent().getContext()
                        .getBaseDirectory().getPath()
                        + File.separator
                        + "VAADIN"
                        + File.separator
                        + "themes"
                        + File.separator + tenantName);
                dir.mkdirs();
                f.createNewFile();
            } catch (IOException e) {
                Main.getCurrent().getMainWindow()
                        .showNotification(
                                f.getAbsolutePath()
                                        + "does not exist, creation failed",
                                Window.Notification.TYPE_ERROR_MESSAGE);
            }

        }
        // Main.getCurrent().getMainWindow().showNotification("file exists: " +
        // f.exists(), Window.Notification.TYPE_ERROR_MESSAGE);
        // Main.getCurrent().getMainWindow().showNotification("file path: " +
        // f.getAbsolutePath(), Window.Notification.TYPE_ERROR_MESSAGE);

        OutputStream output = null;
        try {

            output = new FileOutputStream(f);
            IOUtils.copy(in, output);
            output.close();
            return f;

        } catch (FileNotFoundException e) {

            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Gets the {@link InputStream} from the CSS string the user has entered in
     * the {@link Component}.
     * 
     * @return input TODO document
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
     * Saves the CSS file.
     * 
     * @throws TransactionException
     *             TODO document
     */
    @SuppressWarnings("null")
    public void saveCss(TenantFacade tenantFacade, boolean advanced,
            FileFacade fileFacade) throws TransactionException {
        try {
            Tenant tenant = tenantFacade.getTenant(tenantId);
            tenantName = tenant.getName();
            Long colorSchemeId;
            File f;
            InputStream in = null;
            // check whether the advanced or the simple color scheme
            // is selected and create either a input stream with the
            // content the user entered into the field for advanced
            // css settings or with the selected settings from simple
            // scheme
            try {
                if (advanced) {
                    in = getInputStream(component.getCssTextField().getValue()
                            .toString());
                } else {
                    String cssValue = saveSimpleCss();
                    in = getInputStream(cssValue);

                }

                // if a advancedColorSchemeId is set and the user wants to
                // save a advanced color scheme then get the
                // advancedColorSchemeId
                // and replace the current css file with the new one.
                // For the simple scheme it's basically the same
                if ((tenant.getAdvancedColorScheme() != null) && advanced) {
                    colorSchemeId = tenant.getAdvancedColorScheme().getId();

                    f = getFileFromInputStream(in);
                    in.reset();
                    // TODO: was ist wenn der MIME Typ nicht erkannt wird?
                    fileFacade.replaceFile(colorSchemeId, in, f.length(), f
                            .getAbsolutePath(), new MimetypesFileTypeMap()
                            .getContentType(f));
                } else if (tenant.getSimpleColorScheme() != null) {
                    colorSchemeId = tenant.getSimpleColorScheme().getId();

                    f = getFileFromInputStream(in);
                    in.reset();
                    fileFacade.replaceFile(colorSchemeId, in, f.length(), f
                            .getAbsolutePath(), new MimetypesFileTypeMap()
                            .getContentType(f));
                } else {
                    // TODO: getFileFromInputStream can return null, but null
                    // value is not handled!
                    f = getFileFromInputStream(in);
                    if (f == null) {
                        Main.getCurrent().getMainWindow().showNotification(
                                "input file is null",
                                Window.Notification.TYPE_ERROR_MESSAGE);
                    }
                    in.reset();
                    HashSet<Class<? extends FilePermission>> filePermission = new HashSet<Class<? extends FilePermission>>();
                    filePermission.add(FileReadPermission.class);
                    if (fileFacade == null) {
                        Main.getCurrent().getMainWindow().showNotification(
                                "fileFacade is null",
                                Window.Notification.TYPE_ERROR_MESSAGE);
                    }
                    if (in == null) {
                        Main.getCurrent().getMainWindow().showNotification(
                                "input stream is null",
                                Window.Notification.TYPE_ERROR_MESSAGE);
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
                    new InformationDialogComponent(
                            "Failed to load the CSS file!", "Failure"));
        }
    }

    /**
     * Saves the simple CSS and returns the string representation.
     * 
     * @return css - The string representation of the simple CSS file.
     */
    private String saveSimpleCss() {
        InputSource source = new InputSource();
        String css = "";
        try {
            InputStream stream = new FileInputStream(
                    new File(Main.getCurrent().getContext().getBaseDirectory()
                            .getPath()
                            + File.separator
                            + "VAADIN"
                            + File.separator
                            + "themes"
                            + File.separator
                            + "decidr"
                            + File.separator + "styles.css"));
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
                    // set foreground color
                    rule = rule.replace("}", "; color: "
                            + component.getForegroundSelect().getValue()
                                    .toString() + " }");

                    // set font family
                    index = rule.indexOf("font-family:");
                    substring = rule.substring(index);
                    index = substring.indexOf(";");
                    substring = substring.substring(13, index);
                    rule = rule.replace(substring, component.getFontSelect()
                            .getValue().toString());

                    // set font size
                    index = rule.indexOf("font-size:");
                    substring = rule.substring(index);
                    index = substring.indexOf(";");
                    substring = substring.substring(11, index);
                    rule = rule.replace(substring, component
                            .getFontSizeSelect().getValue().toString()
                            + "px");
                } else if (cssRule.getCssText().contains("*.v-generated-body")) {
                    // set background color
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
            Main.getCurrent().getMainWindow().showNotification(
                    "saveSimpleCSS: File not found",
                    Window.Notification.TYPE_ERROR_MESSAGE);
            return "";
        } catch (IOException e) {
            Main.getCurrent().getMainWindow().showNotification(
                    "saveSimpleCSS: IO failure",
                    Window.Notification.TYPE_ERROR_MESSAGE);
            return "";
        }
    }
}