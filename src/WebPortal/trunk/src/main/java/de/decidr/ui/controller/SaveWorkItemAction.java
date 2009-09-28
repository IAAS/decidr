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

import java.util.LinkedList;
import java.util.List;

import com.vaadin.ui.Form;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.model.workflowmodel.humantask.THumanTaskData;
import de.decidr.model.workflowmodel.humantask.TTaskItem;
import de.decidr.model.workflowmodel.humantask.dataitem.TTaskDataItem;

/**
 * This action saves the entered information from the user in his work item.
 *
 * @author AT
 */
public class SaveWorkItemAction implements ClickListener {
    
    private Form form = null;
    
    private THumanTaskData tHumanTaskData = null;
    
    private List<TTaskDataItem> taskDataItemList = new LinkedList<TTaskDataItem>();
    
    /**
     * Constructor which has a form and a tHumanTaskData as parameter so the
     * entered information can be saved in the task items.
     *
     */
    public SaveWorkItemAction(Form form, THumanTaskData tHumanTaskData) {
        this.form = form;
        this.tHumanTaskData = tHumanTaskData;
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        for(int i = 0; i < this.tHumanTaskData.getTaskItemOrInformation().size(); i++){
            TTaskItem taskItem = (TTaskItem)this.tHumanTaskData.getTaskItemOrInformation().get(i);
            TTaskDataItem tTaskDataItem = new TTaskDataItem();
            tTaskDataItem.setName(taskItem.getName());
            tTaskDataItem.setValue(taskItem.getValue().toString());
            tTaskDataItem.setType(taskItem.getType().toString());
            taskDataItemList.add(tTaskDataItem);
        }
        //TODO: in datenbank abspeichern
        new HideDialogWindowAction();
        
    }

}
