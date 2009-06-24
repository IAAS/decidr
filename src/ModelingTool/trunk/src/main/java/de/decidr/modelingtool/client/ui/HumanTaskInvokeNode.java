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

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * TODO: add comment
 *
 * @author JE
 */
public class HumanTaskInvokeNode extends InvokeNode {

//    private HumanTaskInvokeNodeModel model;
//    
//    public HumanTaskInvokeNodeModel getModel() {
//        return model;
//    }
//
//    public void setModel(HumanTaskInvokeNodeModel model) {
//        this.model = model;
//    }

    public HumanTaskInvokeNode(HasChildren parentPanel) {
        super(parentPanel);
        
        FocusPanel graphic = new FocusPanel();
        graphic.addStyleName("node-graphic-std");
        graphic.setWidget(new Label("HT"));
        this.setGraphic(graphic);
        
        setInputPort(new InputPort());
        setOutputPort(new OutputPort());
    }
    
}
