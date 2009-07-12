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

package de.decidr.ui.view;

import java.util.Map;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;

/**
 * TODO: add comment
 *
 * @author AT
 */
public class ModelingTool extends AbstractComponent {
    
    /**
     * TODO: add comment
     *
     */
    public ModelingTool() {
       super();
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.AbstractComponent#getTag()
     */
    @Override
    public String getTag() {
         return "modelingtool";
    }

    /* (non-Javadoc)
     * @see com.vaadin.ui.AbstractField#paintContent(com.vaadin.terminal.PaintTarget)
     */
    @Override
    public void paintContent(PaintTarget target) throws PaintException {
       
        super.paintContent(target);
        
    }
    
    /* (non-Javadoc)
     * @see com.vaadin.ui.AbstractField#changeVariables(java.lang.Object, java.util.Map)
     */
    @Override
    public void changeVariables(Object source, Map variables) {
        
        super.changeVariables(source, variables);
    }

}
