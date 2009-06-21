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

import de.decidr.modelingtool.client.ui.EmailInvokeNode;

/**
 * Command for creating an email invoke node.
 * 
 * @author JE
 */
public class CreateEmailInvokeNodeCommand implements UndoableCommand {

    /**
     * The graphical node.
     */
    EmailInvokeNode node = null;

    /**
     * The model of the node.
     */
    // EmailInvokeNodeModel model = null;

    public CreateEmailInvokeNodeCommand(EmailInvokeNode node) {
        this.node = node;

        // TODO: create model
        // TODO: execute
    }

//    public CreateEmailInvokeNodeCommand(EmailInvokeNodeModel model,
//            int NodeLeft, int NodeTop) {
//        this.model = model;
//
//        // TODO: create node and set to position
//        // TODO: execute
//    }

    @Override
    public void undo() {
        // TODO: remove node and model to workflow

    }

    @Override
    public void execute() {
        // TODO: add node and model to workflow

    }

}
