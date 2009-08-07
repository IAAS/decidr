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

package de.decidr.modelingtool.server;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vaadin.data.Item;

import de.decidr.model.acl.roles.UserRole;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.facades.WorkflowModelFacade;
import de.decidr.modelingtool.client.exception.LoadDWDLException;
import de.decidr.modelingtool.client.exception.SaveDWDLException;
import de.decidr.modelingtool.client.io.DWDLIOServiceAsync;

/**
 * TODO: add comment
 * 
 * @author Modood Alvi, Johannes Engelhardt
 * @version 0.1
 */
public class DWDLIOServiceAsyncImpl implements DWDLIOServiceAsync {

    @Override
    public String load(long workflowModelId, AsyncCallback<String> callback) {
        // return "<workflow id=\"123\" name=\"test\" \\>";

        // TODO: get user role from session data

        // create facade from model
        WorkflowModelFacade facade = new WorkflowModelFacade(new UserRole());

        try {
            Item workflowModel = facade.getWorkflowModel(workflowModelId);
            Object byteDWDL = workflowModel.getItemProperty("dwdl").getValue();

            // check if value is a byte array
            if (byteDWDL instanceof byte[]) {
                // convert byte array into string
                String dwdl = new String((byte[]) byteDWDL);

                // success callback
                callback.onSuccess("DWDL successfully loaded.");
                return dwdl;
            } else {
                // failure callback
                callback.onFailure(new LoadDWDLException("Couldn't load DWDL."));
                return null;
            }
        } catch (TransactionException e) {
            callback.onFailure(new LoadDWDLException("Couldn't load DWDL."));
            return null;
        }
    }

    @Override
    public void save(long workflowModelId, String name, String description,
            String dwdl, AsyncCallback<String> callback) {
        // TODO: get user role from session data

        // create facade from model
        WorkflowModelFacade facade = new WorkflowModelFacade(new UserRole());

        // convert dwdl string into byte array
        byte[] byteDWDL = dwdl.getBytes();

        try {
            facade.saveWorkflowModel(workflowModelId, name, description,
                    byteDWDL);
            callback.onSuccess("DWDL successfully saved.");
        } catch (TransactionException e) {
            callback.onFailure(new SaveDWDLException("Couldn't save DWDL."));
        }
    }

}
