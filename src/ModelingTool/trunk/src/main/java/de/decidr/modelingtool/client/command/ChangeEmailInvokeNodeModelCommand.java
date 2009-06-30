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

package de.decidr.modelingtool.client.command;

import de.decidr.modelingtool.client.model.EmailInvokeNodeModel;
import de.decidr.modelingtool.client.ui.EmailInvokeNode;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class ChangeEmailInvokeNodeModelCommand implements UndoableCommand {

    private EmailInvokeNode node;
    private EmailInvokeNodeModel oldModel = new EmailInvokeNodeModel();
    private EmailInvokeNodeModel newmodel = new EmailInvokeNodeModel();

    /**
     * TODO: add comment
     * 
     */
    public ChangeEmailInvokeNodeModelCommand(EmailInvokeNode node,
            EmailInvokeNodeModel newModel) {
        this.node = node;
        this.newmodel = newModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        oldModel = (EmailInvokeNodeModel) node.getModel();
        node.setModel(newmodel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.command.UndoableCommand#undo()
     */
    @Override
    public void undo() {
        node.setModel(oldModel);
    }

}
