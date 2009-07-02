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

import java.util.Iterator;
import java.util.Map;

import com.vaadin.terminal.ParameterHandler;

/**
 * TODO: add comment
 *
 * @author AT
 */
public class MyParamterHandler implements ParameterHandler {
    
    String key = null;
    String value = null;

    /* (non-Javadoc)
     * @see com.vaadin.terminal.ParameterHandler#handleParameters(java.util.Map)
     */
    @Override
    public void handleParameters(Map parameters) {
        for (Iterator it = parameters.keySet().iterator(); it.hasNext();) {
            key   = (String) it.next();
            value = ((String[]) parameters.get(key))[0];
            
        }



    }

    /**
     * TODO: add comment
     *
     * @return
     */
    public String getKey() {
        return key;
    }

}
