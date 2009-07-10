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

package de.decidr.ui.gwt.client;

import com.vaadin.terminal.gwt.client.DefaultWidgetSet;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

import de.decidr.ui.gwt.client.ui.VModelingTool;

/**
 * TODO: add comment
 *
 * @author AT
 */
public class ModelingToolWidgetSet extends DefaultWidgetSet {
    
    /* (non-Javadoc)
     * @see com.vaadin.terminal.gwt.client.DefaultWidgetSet#resolveWidgetType(com.vaadin.terminal.gwt.client.UIDL)
     */
    @Override
    protected Class resolveWidgetType(UIDL uidl) {
        final String tag = uidl.getTag();
        if("modelingtool".equals(tag)){
            return VModelingTool.class;
        }
        return super.resolveWidgetType(uidl);
    }
    
    /* (non-Javadoc)
     * @see com.vaadin.terminal.gwt.client.DefaultWidgetSet#createWidget(com.vaadin.terminal.gwt.client.UIDL)
     */
    @Override
    public Paintable createWidget(UIDL uidl) {
        final Class type = resolveWidgetType(uidl);
        if(VModelingTool.class == type){
            VModelingTool vModelingTool = new VModelingTool();
            vModelingTool.init(0L);
            return vModelingTool;
        }
        return super.createWidget(uidl);
    }

}
