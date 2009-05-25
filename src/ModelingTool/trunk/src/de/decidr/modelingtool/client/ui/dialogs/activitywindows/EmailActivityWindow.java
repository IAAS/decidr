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

package de.decidr.modelingtool.client.ui.dialogs.activitywindows;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.TextField;

/**
 * TODO: add comment
 * 
 * @author "Jonas Schlaak"
 */
public class EmailActivityWindow extends Dialog {

    private TextField<String> toField;
    private TextField<String> ccField;
    private TextField<String> bccField;
    private TextField<String> subjectField;
    private TextField<String> messageField;
    private FileUploadField attachmentField;
    //TODO: private ComboBox<VariableType> sent;

}
