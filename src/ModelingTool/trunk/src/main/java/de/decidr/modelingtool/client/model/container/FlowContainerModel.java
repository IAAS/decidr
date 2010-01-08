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

package de.decidr.modelingtool.client.model.container;

/**
 * Model class for the {@link FlowContainerModel}. Currently, this class holds
 * no properties that can or need to be modeled.
 * 
 * @author Johannes Engelhardt
 */
public class FlowContainerModel extends ContainerModel {

    /**
     * Default constructor invoking the super constructor.
     * 
     * @param parentModel
     *            the mode of the parent node
     */
    public FlowContainerModel(HasChildModels parentModel) {
        super(parentModel);
        this.name = this.getClass().getName();
    }

}
