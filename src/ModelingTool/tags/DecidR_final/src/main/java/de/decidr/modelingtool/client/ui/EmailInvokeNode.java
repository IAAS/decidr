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

package de.decidr.modelingtool.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;

import de.decidr.modelingtool.client.menu.MenuImageBundle;
import de.decidr.modelingtool.client.ui.dialogs.email.EmailActivityWindowInvoker;

/**
 * This node represents an email activity.
 * 
 * @author Johannes Engelhardt
 */
public class EmailInvokeNode extends InvokeNode {

    public EmailInvokeNode(HasChildren parentPanel) {
        super(parentPanel);

        FocusPanel graphic = new FocusPanel();
        graphic.addStyleName("node-graphic-std");

        MenuImageBundle imgBundle = GWT.create(MenuImageBundle.class);
        String html = imgBundle.email().getHTML() + "<br/>Email";
        graphic.setWidget(new HTML(html));

        this.setGraphic(graphic);

        setInputPort(new InputPort());
        setOutputPort(new OutputPort());
    }

    @Override
    public void showPropertyWindow() {
        EmailActivityWindowInvoker.invoke(EmailInvokeNode.this);
    }

}
