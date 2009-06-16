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

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.ui.CheckBox;

import de.decidr.modelingtool.client.ui.dialogs.Dialog;

/**
 * TODO: add comment
 * 
 * @author JS
 */
public class HumanTaskActivityWindow extends Dialog {

    private TextField<String> userField;
    private TextField<String> descriptionField;
    private TextField<String> filesProvidedField;
    private CheckBox notify;
    
    @Override
    public void initialize() {
    	// TODO Auto-generated method stub
    	
    }
    
    @Override
    public void reset() {
    	// TODO Auto-generated method stub
    	
    }
    
    @Override
    public void refresh() {
    	// TODO Auto-generated method stub
    	
    }

}
