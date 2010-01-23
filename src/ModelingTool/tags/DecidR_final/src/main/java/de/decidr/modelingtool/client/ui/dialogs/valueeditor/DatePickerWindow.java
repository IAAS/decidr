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

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.DatePicker;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.tips.Tip;

import de.decidr.modelingtool.client.ui.resources.DateFormatter;

/**
 * A simple windows without any heading or buttons. The window only holds a
 * {@link DatePicker} widget.
 * 
 * @author Jonas Schlaak
 */
public class DatePickerWindow extends Tip {

    /**
     * Creates a date picker window. After the date was selected from the
     * {@link DatePicker} widget the value is written into a {@link TextField}
     * in a format as defined in {@link DateFormatter}.
     * 
     * @param text
     *            the {@link TextField} into which the selected date shall be
     *            written
     */
    public DatePickerWindow(final TextField<String> text) {
        final DatePicker datePicker = new DatePicker();
        datePicker.addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                String date = DateFormatter.format(datePicker.getValue());
                text.setValue(date);
                DatePickerWindow.this.hide();
            }

        });
        add(datePicker);
    }

}
