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

package de.decidr.modelingtool.client.ui.dialogs.valueeditor;

import java.util.List;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;

import de.decidr.modelingtool.client.ModelingToolWidget;

/**
 * Widget for a single value of a variable. Holds the {@link TextField} where
 * the value itself can be edited and a remove button for removing the value
 * from the variable.
 * 
 * @author Jonas Schlaak
 */
public class ValueFieldWidget extends HorizontalPanel {

    private TextField<String> textField;
    private PushButton button;

    /**
     * Default constructor.
     * 
     * @param table
     *            the table where the widget is to be displayed
     * @param text
     *            the text field for the variable value
     * @param fields
     *            the list of references to all value text fields
     * 
     */
    public ValueFieldWidget(FlexTable table, TextField<String> text,
            List<TextField<String>> fields) {
        this.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        this.textField = text;
        this.add(this.textField);

        ValueFieldClickHandler handler = new ValueFieldClickHandler(table,
                this, fields);
        this.button = new PushButton(ModelingToolWidget.getMessages()
                .delValue(), handler);
        this.add(this.button);
    }

    /**
     * Returns the text field of this widget.
     * 
     * @return the textField
     */
    public TextField<String> getTextField() {
        return textField;
    }

}
