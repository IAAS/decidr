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

package de.decidr.modelingtool.client.io;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import de.decidr.modelingtool.client.model.WorkflowModel;

/**
 * TODO: add comment
 *
 * @author Modood Alvi
 * @version 0.1
 */
public class WorkflowIOImpl implements WorkflowIO {

    private DWDLIOServiceAsync service=null;
    private SaveCallback saveCallback = null;
    private LoadCallback loadCallback = null;
    
    public WorkflowIOImpl (){
        saveCallback = new SaveCallback();
        loadCallback = new LoadCallback();
        service = (DWDLIOServiceAsync) GWT.create(DWDLIOService.class);
        ((ServiceDefTarget) service).setServiceEntryPoint( GWT.getModuleBaseURL() +
        "/bla/bla");
    }
    
    /* (non-Javadoc)
     * @see de.decidr.modelingtool.client.io.WorkflowIO#loadWorkflow(java.lang.String)
     */
    @Override
    public WorkflowModel loadWorkflow() {
        DWDLParser dwdlParser = new DWDLParserImpl();
        String xmldwdl = service.load(loadCallback);
        return dwdlParser.parse(xmldwdl);
    }

    /* (non-Javadoc)
     * @see de.decidr.modelingtool.client.io.WorkflowIO#saveWorkflow(de.decidr.modelingtool.client.model.WorkflowModel)
     */
    @Override
    public void saveWorkflow(WorkflowModel model) {
        WorkflowParser workflowParser = new WorkflowParserImpl();
        String xmldwdl = workflowParser.parse(model);
        service.save(xmldwdl, saveCallback);
    }
}
