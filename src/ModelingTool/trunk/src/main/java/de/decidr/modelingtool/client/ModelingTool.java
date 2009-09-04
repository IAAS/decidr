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
package de.decidr.modelingtool.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>. Only needed for test
 * purposes.
 */
public class ModelingTool implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        RootPanel.get("panel").add(ModelingToolWidget.getInstance());
        ModelingToolWidget.getInstance().init();
        // TODO, JS: This is test data
        ModelingToolWidget
                .getInstance()
                .setUsers(
                        "<userlist><user id=\"23\" name=\"Clark Kent\"/><user id=\"42\" name=\"Bruce Wayne\"/><user id=\"113\" name=\"Peter Parker\"/></userlist>");

    }

}
