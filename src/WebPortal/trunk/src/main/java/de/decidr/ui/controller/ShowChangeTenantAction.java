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

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.decidr.ui.view.ChangeTenantComponent;
import de.decidr.ui.view.SiteFrame;

/**
 * TODO: add comment
 *
 * @author GH
 */
@SuppressWarnings("serial")
public class ShowChangeTenantAction  implements ClickListener {
    
    private UIDirector uiDirector = UIDirector.getInstance();
    private SiteFrame siteFrame = uiDirector.getTemplateView();


    @Override
    public void buttonClick(ClickEvent event) {
        siteFrame.setContent(ChangeTenantComponent.getInstance());

    }
    
}