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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;

import de.decidr.modelingtool.client.ModelingTool;
import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.activitywindows.EmailActivityWindow;

/**
 * TODO: add comment
 * 
 * @author JE
 */
public class EmailInvokeNode extends InvokeNode {

    // private EmailInvokeNodeModel model = null;

    // public EmailInvokeNodeModel getModel() {
    // return model;
    // }
    //
    // public void setModel(EmailInvokeNodeModel model) {
    // this.model = model;
    // }

    public EmailInvokeNode(HasChildren parentPanel) {
        super(parentPanel);

        FocusPanel graphic = new FocusPanel();
        graphic.addStyleName("node-graphic-std");
        graphic.setWidget(new Label("Email"));
        graphic.setWidget(new Button(ModelingTool.messages
                .changePropertyButton(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                DialogRegistry.getInstance().showDialog(
                        EmailActivityWindow.class.getName());
            }
        }));
        this.setGraphic(graphic);

        setInputPort(new InputPort());
        setOutputPort(new OutputPort());
    }

}
