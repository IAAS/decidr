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
package de.decidr.webservices.humantask;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import de.decidr.model.soap.types.ItemList;
import de.decidr.model.soap.types.TaskIdentifier;

/**
 * This is a temporary interface for the BEPL callback method web service
 * interface. It will be replaced by whatever the BEPL libraries offer as soon
 * as they do.
 * 
 * @author Reinhold
 */
@WebService(targetNamespace = BasicProcessInterface.TARGET_NAMESPACE, name = "basicProcessPT")
public interface BasicProcessInterface {

    /**
     * The namespace of the ODE callback.
     */
    public static final String TARGET_NAMESPACE = "http://decidr.de/wsdl/basicProcess";

    /**
     * The callback method for when a human task is finished.
     * 
     * @param taskID
     *            Information needed to identify the target ODE &amp; workflow
     *            instances.
     * @param dataList
     *            The data produced by the work item.
     */
    @WebMethod(action = TARGET_NAMESPACE + "/taskCompleted", operationName = "taskCompleted")
    public void taskCompleted(@WebParam(name = "taskID") TaskIdentifier taskID,
            @WebParam(name = "dataList") ItemList dataList);
}
