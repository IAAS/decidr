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

package de.decidr.modelingtool.client.ui.dialogs.humantask;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;

/**
 * EventHandler for {@link TaskItemFieldSet}. Handles the mouse click on the
 * remove button (Remove Taskitem) by removing the whole widget from the
 * {@link FlexTable} of the task item editor.
 * 
 * @author Jonas Schlaak
 */
public class TaskItemClickHandler implements ClickHandler {

    private FlexTable table;
    private TaskItemFieldSet widget;
    private List<TaskItemFieldSet> fieldssets;

    /**
     * Default constructor for this event handler.
     * 
     * @param table
     *            the table which container the value field widget
     * @param widget
     *            the widget itself
     * @param fieldsets
     *            the list which holds references to all value text fields
     */
    public TaskItemClickHandler(FlexTable table, TaskItemFieldSet widget,
            List<TaskItemFieldSet> fieldsets) {
        this.table = table;
        this.widget = widget;
        this.fieldssets = fieldsets;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event
     * .dom.client.ClickEvent)
     */
    @Override
    public void onClick(ClickEvent event) {
        fieldssets.remove(widget);
        table.remove(widget);
    }

}
