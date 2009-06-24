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

package de.decidr.modelingtool.client.model;


/**
 * TODO: add comment
 *
 * @author JE
 */
public class NodeModel extends AbstractModel {

    private HasChildModels parentModel = null;
    
    private ConnectionModel input = null;
    
    private ConnectionModel output = null;
    
    public NodeModel(HasChildModels parentModel) {
        this.parentModel = parentModel;
    }

    public ConnectionModel getInput() {
        return input;
    }

    public void setInput(ConnectionModel input) {
        this.input = input;
    }

    public ConnectionModel getOutput() {
        return output;
    }

    public void setOutput(ConnectionModel output) {
        this.output = output;
    }

    public HasChildModels getParentModel() {
        return parentModel;
    }

    public void setParentModel(HasChildModels parentModel) {
        this.parentModel = parentModel;
    }
    
}
